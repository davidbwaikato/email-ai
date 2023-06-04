package org.example;

import java.io.IOException;
import java.io.Reader;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;

import org.apache.mailet.Mail;
import org.apache.mailet.base.GenericMailet;
import org.apache.mailet.base.RFC2822Headers;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailAIMailet extends GenericMailet{

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailAIMailet.class);

    private static final String HTML_BR_TAG = "<br />";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final Pattern BODY_OPENING_TAG = Pattern.compile("((?i:<body>))");
    private String tldrText = null;


    @Override
    public String getMailetInfo() {
        return "Add TLDR Mailet";
    }

    @Override
    public void service(Mail mail) throws MessagingException {
        try {
            MimeMessage message = mail.getMessage();
            EmailTextReader reader = new EmailTextReader();
            String text = reader.getTextFromMessage(message);
            LOGGER.info("text was " + text);
            AIAPI aiapi = new AIAPI();
            tldrText = aiapi.getTLDR(text);
            LOGGER.info("TDLR was " + tldrText);
            if (attachTLDR(message)) {
                message.saveChanges();
            } else {
                LOGGER.info("Unable to add tldr to mail {}", mail.getName());
            }
        } catch (IOException ioe) {
            throw new MessagingException("Could not read message", ioe);
        }
        catch (Exception e){
            throw new MessagingException("API call failed", e);
        }
    }

    private boolean attachTLDR(MimePart part) throws IOException, MessagingException {
        String contentType = part.getContentType();

       if (part.getContent() instanceof String) {
            Optional<String> content = attachTLDRToTextPart(part);
            if (content.isPresent()) {
                part.setContent(content.get(), contentType);
                part.setHeader(RFC2822Headers.CONTENT_TYPE, contentType);
                return true;
            }

        }
        if (part.isMimeType("multipart/mixed")
                || part.isMimeType("multipart/related")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            boolean added = attachTLDRToFirstPart(multipart);
            if (added) {
                part.setContent(multipart);
            }
            return added;

        } else if (part.isMimeType("multipart/alternative")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            boolean added = attachTLDRToAllSubparts(multipart);
            if (added) {
                part.setContent(multipart);
            }
            return added;
        }
        //Give up... we won't attach the tldr to this MimePart
        return false;

    }
    private Optional<String> attachTLDRToTextPart(MimePart part) throws MessagingException, IOException {
        String content = (String) part.getContent();
        if (part.isMimeType("text/plain")) {
            return Optional.of(attachTLDRToText(content));
        }
        else if (part.isMimeType("text/html")){
             return Optional.of(attachTLDRToHTML(content));
        }
        return Optional.empty();
    }

    private String attachTLDRToText(String content) throws MessagingException,
            IOException {
        StringBuilder builder = new StringBuilder(getTLDRText());
        builder.append(CARRIAGE_RETURN);
        builder.append(CARRIAGE_RETURN);
        builder.append("TLDR:")
        builder.append(content);
        return builder.toString();
    }

    private String attachTLDRToHTML(String content) throws MessagingException,
            IOException {

        /* This HTML part may have a opening <BODY> tag.  If so, we
         * want to insert out TLDR immediately after that tag.
         */
        Matcher matcher = BODY_OPENING_TAG.matcher(content);
        if (!matcher.find()) {
            return  getTLDRHTML() + content;
        }
        int insertionIndex = matcher.start(matcher.groupCount() - 1);
        return new StringBuilder()
                .append(content, 0, insertionIndex)
                .append(getTLDRHTML())
                .append(content.substring(insertionIndex))
                .toString();
    }

    private boolean attachTLDRToFirstPart(MimeMultipart multipart) throws MessagingException, IOException {
        MimeBodyPart firstPart = (MimeBodyPart) multipart.getBodyPart(0);
        return attachTLDR(firstPart);
    }

    private boolean attachTLDRToAllSubparts(MimeMultipart multipart) throws MessagingException, IOException {
        int count = multipart.getCount();
        boolean isTLDRAttached = false;
        for (int index = 0; index < count; index++) {
            MimeBodyPart mimeBodyPart = (MimeBodyPart) multipart.getBodyPart(index);
            isTLDRAttached |= attachTLDR(mimeBodyPart);
        }
        return isTLDRAttached;
    }


    private String getTLDRText() {
        return tldrText;
    }

    private String getTLDRHTML() {
        String text = getTLDRText();
        return text.replaceAll(CARRIAGE_RETURN, HTML_BR_TAG) + HTML_BR_TAG;
    }


}
