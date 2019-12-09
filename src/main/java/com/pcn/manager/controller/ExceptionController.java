package com.pcn.manager.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

//@Order(2)
//@ControllerAdvice(annotations = Controller.class)
public class ExceptionController {
	
	Logger logger = LogManager.getLogger(getClass());
	
    @ExceptionHandler(NoHandlerFoundException.class)
    public void handleNotFoundError(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException e) throws Exception {
    	
		this.errorLogWrite(e);
		throw e;
    }
	
	@ExceptionHandler(Exception.class)
    public void handlerServerError(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException e) throws Exception {
		
		this.errorLogWrite(e);
		throw e;
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
