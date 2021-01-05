package com.pcn.manager.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

//@Order(1)
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionRestController {
	
	Logger logger = LogManager.getLogger(getClass());
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNotFoundError(HttpServletRequest req, HttpServletResponse res, NoHandlerFoundException e) {
		
		this.errorLogWrite(e);
        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("timestamp", sdf.format(new Date()));
        responseBody.put("path", req.getRequestURI());
        responseBody.put("status", HttpStatus.NOT_FOUND.value());
        responseBody.put("message", "The URL you have reached is not in service at this time");
        return new ResponseEntity<Object>(responseBody,HttpStatus.NOT_FOUND);
    }
	
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> errorLogWrite(HttpServletRequest req, HttpServletResponse res, Exception e) {
		
    	this.errorLogWrite(e);
		Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("timestamp", sdf.format(new Date()));
        responseBody.put("path", req.getRequestURI());
		responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		responseBody.put("message", "Internal Server Error");
		return new ResponseEntity<Object>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
	}
    
	private void errorLogWrite(Exception e) {
		
		ByteArrayOutputStream byteOut = null;
		String error = "";
		
		try {
			byteOut = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(byteOut));
			byteOut.flush();
			error = new String(byteOut.toByteArray());
			byteOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if(byteOut != null)
				try {
					byteOut.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		}
		logger.error(error);
	}
}
