package org.ruananta.parser.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class ParserErrorController implements ErrorController {

    private ErrorAttributes errorAttributes;

    @Autowired
    public void setErrorAttributes(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }


    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request, WebRequest webRequest) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == 404) {
                return new ModelAndView("404");
            }
        }
        return new ModelAndView("error"); //
    }

    @RequestMapping("/error-details")
    public Map<String, Object> getErrorDetails(WebRequest webRequest) {
        return this.errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(Include.MESSAGE));
    }
}
