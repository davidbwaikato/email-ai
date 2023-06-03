package org.emailai;

import java.util.Iterator;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.mailet.Mail;
import org.apache.mailet.base.GenericMailet;
import org.apache.james.core.MailAddress;

    
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitoringMailet extends GenericMailet
{
    protected static final Logger logger = LoggerFactory.getLogger(MonitoringMailet.class);

    @Override
    public void service(Mail mail) throws MessagingException {

	MimeMessage message = mail.getMessage();
        String subject = message.getSubject();

        logger.info("Email-AI MonitoringMailet: Log via slf4j with INFO level with Subject Line: " + subject);


	message.setSubject(subject + " bar bar bar");
	
    }

}


