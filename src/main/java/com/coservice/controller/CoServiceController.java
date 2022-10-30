package com.coservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coservice.service.CoTriggerService;

@RestController
public class CoServiceController {
    
	@Autowired
	private CoTriggerService coservice;
	
	@GetMapping("/correspondece")
	public ResponseEntity<String> completeCoService() throws Exception{
		String cotrigger = coservice.cotriggerOperations();
		if(cotrigger != null)
		   return new ResponseEntity<String>(cotrigger, HttpStatus.OK);
		return new ResponseEntity<String>("Operation failed", HttpStatus.BAD_REQUEST);
	} 
}
