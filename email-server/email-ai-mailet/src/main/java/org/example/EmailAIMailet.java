package org.example;

import java.io.IOException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailAIMailet extends GenericMailet{

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailAIMailet.class);

    private static final String HTML_BR_TAG = "<br />";
    private static final String CARRIAGE_RETURN = "\r\n";
    private static final Pattern BODY_OPENING_TAG = Pattern.compile("((?i:<body>))");
    private String keywordsText = null;


    @Override
    public String getMailetInfo() {
        return "Add keywords Mailet";
    }

    @Override
    public void service(Mail mail) throws MessagingException {
        try {
            MimeMessage message = mail.getMessage();
            EmailTextReader reader = new EmailTextReader();
            String text = reader.getTextFromMessage(message);
            LOGGER.info("text was " + text);
            AIAPI aiapi = new AIAPI();
            keywordsText = aiapi.getKeywords(text);
            LOGGER.info("Keywords were " + keywordsText);
            if (attachKeywords(message)) {
                message.saveChanges();
            } else {
                LOGGER.info("Unable to add keywords to mail {}", mail.getName());
            }
        } catch (IOException ioe) {
            throw new MessagingException("Could not read message", ioe);
        }
        catch (Exception e){
            throw new MessagingException("API call failed", e);
        }
    }

    private boolean attachKeywords(MimePart part) throws IOException, MessagingException {
        String contentType = part.getContentType();

       if (part.getContent() instanceof String) {
            Optional<String> content = attachKeywordsToTextPart(part);
            if (content.isPresent()) {
                part.setContent(content.get(), contentType);
                part.setHeader(RFC2822Headers.CONTENT_TYPE, contentType);
                return true;
            }

        }
        if (part.isMimeType("multipart/mixed")
                || part.isMimeType("multipart/related")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            boolean added = attachKeywordsToFirstPart(multipart);
            if (added) {
                part.setContent(multipart);
            }
            return added;

        } else if (part.isMimeType("multipart/alternative")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            boolean added = attachKeywordsToAllSubparts(multipart);
            if (added) {
                part.setContent(multipart);
            }
            return added;
        }
        //Give up... we won't attach the keywords to this MimePart
        return false;

    }
    private Optional<String> attachKeywordsToTextPart(MimePart part) throws MessagingException, IOException {
        String content = (String) part.getContent();
        if (part.isMimeType("text/plain")) {
            return Optional.of(attachKeywordsToText(content));
        }
        else if (part.isMimeType("text/html")){
             return Optional.of(attachKeywordsToHTML(content));
        }
        return Optional.empty();
    }

    private String attachKeywordsToText(String content) throws MessagingException,
            IOException {
        StringBuilder builder = new StringBuilder(getKeywordsText());
        builder.append(CARRIAGE_RETURN);
        builder.append(CARRIAGE_RETURN);
        builder.append("TL;DR:");
        builder.append(CARRIAGE_RETURN);
        builder.append(content);
        return builder.toString();
    }

    private String attachKeywordsToHTML(String content) throws MessagingException,
            IOException {

        /* This HTML part may have a opening <BODY> tag.  If so, we
         * want to insert out keywords immediately after that tag.
         */
        Matcher matcher = BODY_OPENING_TAG.matcher(content);
        if (!matcher.find()) {
            return  "Keywords: " + getKeywordsHTML() + content;
        }
        int insertionIndex = matcher.start(matcher.groupCount() - 1);
        return new StringBuilder()
                .append(content, 0, insertionIndex)
                .append(getKeywordsHTML())
                .append(content.substring(insertionIndex))
                .toString();
    }

    private boolean attachKeywordsToFirstPart(MimeMultipart multipart) throws MessagingException, IOException {
        MimeBodyPart firstPart = (MimeBodyPart) multipart.getBodyPart(0);
        return attachKeywords(firstPart);
    }

    private boolean attachKeywordsToAllSubparts(MimeMultipart multipart) throws MessagingException, IOException {
        int count = multipart.getCount();
        boolean isKeywordsAttached = false;
        for (int index = 0; index < count; index++) {
            MimeBodyPart mimeBodyPart = (MimeBodyPart) multipart.getBodyPart(index);
            isKeywordsAttached |= attachKeywords(mimeBodyPart);
        }
        return isKeywordsAttached;
    }


    private String getKeywordsText() {
        return keywordsText;
    }

    private String getKeywordsHTML() {
        String text = getKeywordsText();
        return text.replaceAll(CARRIAGE_RETURN, HTML_BR_TAG) + HTML_BR_TAG;
    }


}
