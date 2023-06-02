package org.example;

import java.io.IOException;
import java.util.Optional;
import javax.mail.Message;
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

import static com.ibm.icu.lang.UCharacter.LineBreak.CARRIAGE_RETURN;

public class EmailMailet extends GenericMailet{
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailMailet.class);

    @Override
    public void service(Mail mail) throws MessagingException {
        LOGGER.debug("SERVICE RAN");
        try {
            MimeMessage message = mail.getMessage();

            if (attachTLDR(message)) {
                message.saveChanges();
            } else {
                LOGGER.info("Unable to add tldr to mail {}", mail.getName());
            }
        } catch (IOException ioe) {
            throw new MessagingException("Could not read message", ioe);
        }
    }

    private boolean attachTLDR(MimePart part) throws IOException, MessagingException {
        LOGGER.debug("attachTLDR ran");
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
        LOGGER.debug("attachTLDRToTextPart ran");
        String content = (String) part.getContent();
        if (part.isMimeType("text/plain")) {
            return Optional.of(attachTLDRToText(content));
        }
        else{
            LOGGER.info("not plain text! "+ part.getContentType());
        }
        return Optional.empty();
    }

    private String attachTLDRToText(String content) throws MessagingException,
            IOException {
        LOGGER.debug("attachTLDRToText RAN");
        StringBuilder builder = new StringBuilder("hi from emma");
        builder.append(CARRIAGE_RETURN);
        builder.append(CARRIAGE_RETURN);
        builder.append(content);
        return builder.toString();
    }


    private boolean attachTLDRToFirstPart(MimeMultipart multipart) throws MessagingException, IOException {
        LOGGER.debug("attachTLDRToFirstPart RAN");
        MimeBodyPart firstPart = (MimeBodyPart) multipart.getBodyPart(0);
        return attachTLDR(firstPart);
    }

    private boolean attachTLDRToAllSubparts(MimeMultipart multipart) throws MessagingException, IOException {
        LOGGER.debug("attachTLDRToAllSubparts RAN");
        int count = multipart.getCount();
        boolean isTLDRAttached = false;
        for (int index = 0; index < count; index++) {
            MimeBodyPart mimeBodyPart = (MimeBodyPart) multipart.getBodyPart(index);
            isTLDRAttached |= attachTLDR(mimeBodyPart);
        }
        return isTLDRAttached;
    }


}
