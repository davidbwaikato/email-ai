package org.example;

import javax.mail.MessagingException;

import org.apache.mailet.Mail;
import org.apache.mailet.base.GenericMailet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailMailet extends GenericMailet{
    private static final Logger logger = LoggerFactory.getLogger(EmailMailet.class);
    @Override
    public void service(Mail mail) throws MessagingException {
        log("Email-AI barbarbar: log via mailet logger with simple log INFO level");
        logger.info("Email-AI barbarbar: Log via slf4j with INFO level !!! Add log4j.logger.com.test=INFO, CONS, FILE in the log4j.properties");
        logger.debug("Email-AI barbarbar: Log via slf4j with DEBUG level !!! Add log4j.logger.com.test=DEBUG, CONS, FILE in the log4j.properties");
	logger.error("Email-AI foofoofoo: Log via slf4j with ERROR level !!! Add log4j.logger.com.test=DEBUG, CONS, FILE in the log4j.properties");
    }

}
