package com.coservice.utils;



import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailsUtils {
	
	Logger logger = LoggerFactory.getLogger(EmailsUtils.class);

	@Autowired
	private JavaMailSender mailSender;

	public boolean sendEmail(String to, String subject, String body) {
		boolean flag = false;
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage, true);
			msgHelper.setTo(to);
			msgHelper.setSubject(subject);
			msgHelper.setText(body, true);
			FileSystemResource file = new FileSystemResource("F:\\JRTP-21\\CO-Api\\notice.pdf");
			msgHelper.addAttachment("notice.pdf", file);
			mailSender.send(mimeMessage);
			flag = true;
		} catch (Exception e) {
			logger.error("error message is  ", e);
		}
		return flag;
	}
}
