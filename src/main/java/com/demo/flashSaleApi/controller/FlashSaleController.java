package com.demo.flashSaleApi.controller;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.demo.flashSaleApi.model.AllUser;
import com.demo.flashSaleApi.repository.AllUserRepository;
import com.demo.flashSaleApi.service.MailService;


@RestController
@RequestMapping(value = "/api")

public class FlashSaleController {
	
	@Autowired
	AllUserRepository allUserRepository;
	
	
	@Autowired
	MailService mailService;
	
	@Autowired
	private AllUser allUser;
	
	
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<Object> createPatient(@RequestBody AllUser allUser) {
		AllUser savedAllUser = allUserRepository.save(allUser);
		return new ResponseEntity<Object>(allUser, HttpStatus.CREATED);

	}
	@RequestMapping(value = "/user", params = { "page", "limit" }, method = RequestMethod.GET)
	public List<AllUser> getAllUser(@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "limit", required = true) int limit) {
		Page<AllUser> allUser = allUserRepository.findAll(PageRequest.of(page, limit));
		return allUser.getContent();

	}
	
	@RequestMapping(value = "/send-mail")
	public String sendWithAttachment() throws MessagingException{
		try {
			mailService.sendEmailWithAttachment(allUser);
			
		} catch (MailException mailException) {
			System.out.println(mailException);
		}
		return "congratulations your mail has been send to the user for flashSale";
	}

}

