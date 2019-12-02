package com.pcn.manager.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 

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
    public String handleError(HttpServletRequest req, HttpServletResponse res, @RequestHeader HttpHeaders headers, Model model, Exception e) {
    	
    	Exception exception = (Exception) req.getAttribute("javax.servlet.error.exception");
    	
    	if(exception != null) {
    		e = exception;
    	}
    	
    	this.errorLogWrite(e);
    	
    	String str_path = "";
        Object statusCode = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        UrlPathHelper urlPathHelper = new UrlPathHelper(); 
        String originalURL = urlPathHelper.getOriginatingRequestUri(req);
        str_path = originalURL;
        
        //String contentType = req.getHeader("Content-Type");
        
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(statusCode.toString()));
        model.addAttribute("code", statusCode.toString());
        model.addAttribute("msg", httpStatus.getReasonPhrase());
        //model.addAttribute("timestamp", sdf.format(new Date()));
        
        //ajax일 경우
        if(str_path.lastIndexOf(".ajax") > -1) {
        //if(MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            str_path = "ajax_error";
        }else {
        	str_path = "error";
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
