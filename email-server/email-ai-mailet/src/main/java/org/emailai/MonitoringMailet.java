package org.emailai;

import java.io.IOException;
import java.util.Iterator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;

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
        String old_subject = message.getSubject();
	String new_subject = old_subject + " Email-AI appended subject line bar bar bar";

	message.setSubject(new_subject);
		
        logger.info("Email-AI MonitoringMailet: Log via slf4j with INFO level with Subject Line: " + new_subject);

	// The following call is untested, and so for now  have commented it out
	//addKeywordsToMail(mail);
    }


    protected String getOpenAIKeywords(Mail mail)
    {
	// Hardwired example of adding in keywords for now
	//
	// The future step up here would be to access the text of
	// the email message, send it to OpenAI via a URL call to the web-server-api.js
	// and the set the 'keywords' variable to be what was returned
	
	String keywords = "Keywords: Important, Urgent";

	return keywords;
    }

    protected void addKeywordsToMail(Mail mail){
	
	logger.info("Email-AI MonitoringMailet: Adding Keywords to start of email body");

	try {
	    
	    MimeMessage message = mail.getMessage();
	    
	    // Get the existing content of the message
	    Object content = message.getContent();
	    
	    // Create a new MimeMultipart to hold the modified content
	    MimeMultipart modifiedContent = new MimeMultipart();
	    
	    // Create a MimeBodyPart for the keywords
	    MimeBodyPart keywordPart = new MimeBodyPart();
	    String keywords = getOpenAIKeywords(mail);
	    keywordPart.setText("Keywords: " + keywords);
	    
	    // Add the keywords part to the modified content
	    modifiedContent.addBodyPart(keywordPart);
	    
	    // Add the existing content to the modified content
	    MimeBodyPart existingContentPart = new MimeBodyPart();
	    existingContentPart.setContent(content, message.getContentType());
	    modifiedContent.addBodyPart(existingContentPart);
	    
	    // Set the modified content as the new content of the message
	    message.setContent(modifiedContent);
	    
	    // Update the Mail object with the modified message
	    mail.setMessage(message);
	}
	catch (Exception e) {
	    logger.error("Email-AI MonitoringMailet: An exception was through in addKeywordsToMail");		
	    e.printStackTrace();
	}
    }
}


