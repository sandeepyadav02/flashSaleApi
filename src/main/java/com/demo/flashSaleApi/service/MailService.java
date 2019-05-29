package com.demo.flashSaleApi.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.demo.flashSaleApi.model.AllUser;


@Component
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	
	@Autowired
	public MailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void sendEmailWithAttachment(AllUser  allUser) throws MailException, MessagingException{
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
		
		helper.setTo(allUser.getEmail());
		helper.setSubject("FlashSale");
		helper.setText("please find the attached link below");
		javaMailSender.send(mimeMessage);
	}
}
