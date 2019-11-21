package com.pcn.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

//@Controller
public class ErrorPageController implements ErrorController {

    private static final String ERROR_PATH = "/error/error";

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

    @RequestMapping("/error/error")
    public String handleError(HttpServletRequest request, Model model) {
        return "error404";
    }
}
