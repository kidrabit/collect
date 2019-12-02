package com.pcn.manager.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.UrlPathHelper;

/**
 * @class ErrorController
 * 	com.pcn.manager.controller
 * @section 클래스작성정보
 *    |    항  목        	|       	내  용       			|
 *    | :--------: 	| -----------------------------	|
 *    | Company 	| PCN
 *    | Author 		| rnd
 *    | Date 		| 2019. 9. 5.
 *    | 작업자 		| rnd, Others...
 * @section 상세설명
 * - 클래스의 업무내용에 대해 기술...
*/

@Controller
public class ErrorPageController implements ErrorController {
	
	Logger logger = LogManager.getLogger(getClass());

    private static final String ERROR_PATH = "/error";
    /**
     * @fn
     * @remark
     * - 오버라이드 함수의 상세 설명
     * @see org.springframework.boot.web.servlet.error.ErrorController#getErrorPath()
    */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping(ERROR_PATH)
    public String handleError(HttpServletRequest req, HttpServletResponse res,@RequestHeader HttpHeaders headers, Exception e) throws Exception {
    	
    	this.errorLogWrite(e);
    	
    	String str_path = "";
        Object statusCode = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        UrlPathHelper urlPathHelper = new UrlPathHelper(); 
        String originalURL = urlPathHelper.getOriginatingRequestUri(req);
        str_path = originalURL;
        
        if("404".equals(statusCode.toString())) {
        	str_path = "error404";
        }else {
        	str_path = "error500";
        	throw e;
        }
        return str_path;
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
