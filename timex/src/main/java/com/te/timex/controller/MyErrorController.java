
package com.te.timex.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

//https://www.baeldung.com/custom-error-page-spring-mvc

@Controller
public class MyErrorController implements ErrorController {

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	    
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	            return "error/404";
	        }
	        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	            return "error/500";
	        }
	    }
	    return "error/error";
	}
	
//	
//    @RequestMapping(value = "errors", method = RequestMethod.GET)
//    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
//        
//        ModelAndView errorPage = new ModelAndView("errorPage");
//        String errorMsg = "";
//        int httpErrorCode = getErrorCode(httpRequest);
//
//        switch (httpErrorCode) {
//            case 400: {
//                errorMsg = "Http Error Code: 400. Bad Request";
//                break;
//            }
//            case 401: {
//                errorMsg = "Http Error Code: 401. Unauthorized";
//                break;
//            }
//            case 404: {
//                errorMsg = "Http Error Code: 404. Resource not found";
//                break;
//            }
//            case 500: {
//                errorMsg = "Http Error Code: 500. Internal Server Error";
//                break;
//            }
//        }
//        errorPage.addObject("errorMsg", errorMsg);
//        return errorPage;
//    }
//    
//    private int getErrorCode(HttpServletRequest httpRequest) {
//        return (Integer) httpRequest
//          .getAttribute("javax.servlet.error.status_code");
//    }
}