package org.example;

import java.io.IOException;
import java.util.Optional;
import javax.mail.Message;
import javax.mail.MessagingException;
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
        try {
            MimeMessage message = mail.getMessage();

            if (attachTLDR(message)) {
                message.saveChanges();
            } else {
                LOGGER.info("Unable to add footer to mail {}", mail.getName());
            }
        } catch (IOException ioe) {
            throw new MessagingException("Could not read message", ioe);
        }
    }

    private boolean attachTLDR(MimePart part) throws IOException, MessagingException {
        String contentType = part.getContentType();

        if (!(part.getContent() instanceof String)) {
            LOGGER.info("was not string! bad! " + contentType);
            return false;
        }
        if (part.getContent() instanceof String) {
            Optional<String> content = attachTLDRToTextPart(part);
            if (content.isPresent()) {
                part.setContent(content.get(), contentType);
                part.setHeader(RFC2822Headers.CONTENT_TYPE, contentType);
                return true;
            }

        }
        //Give up... we won't attach the footer to this MimePart
        return false;

    }
    private Optional<String> attachTLDRToTextPart(MimePart part) throws MessagingException, IOException {
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
        StringBuilder builder = new StringBuilder("hi from emma");
        builder.append(CARRIAGE_RETURN);
        builder.append(CARRIAGE_RETURN);
        builder.append(content);
        return builder.toString();
    }


}
