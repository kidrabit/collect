package com.pcn.manager.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(2)
@ControllerAdvice(annotations = Controller.class)
public class ExceptionController {
	
	Logger logger = LogManager.getLogger(getClass());
	
	@ExceptionHandler(Exception.class)
	public void errorLogWrite(Exception e) throws Exception {
		
		System.out.println("ExceptionControllerExceptionControllerExceptionController================================");
		
		ByteArrayOutputStream byteOut = null;
		String error = "";
		
		try {
			byteOut = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(byteOut));
			byteOut.flush();
			error = new String(byteOut.toByteArray());
			byteOut.close();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		} finally {
			if(byteOut != null)
				try {
					byteOut.close();
				} catch (IOException ioe) {
					// TODO Auto-generated catch block
					ioe.printStackTrace();
				}
		}
		
		logger.error(error);
		throw e;
	}
}
