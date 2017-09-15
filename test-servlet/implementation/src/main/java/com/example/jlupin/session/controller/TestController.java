package com.example.jlupin.session.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Piotr Heilman
 */
@RestController
public class TestController {
    private final static String KEY = "TEST_KEY";

    @CrossOrigin
    @PostMapping("/set")
    public String postTest(@RequestBody String text, @Autowired HttpSession httpSession, @Autowired HttpServletRequest httpServletRequest) {
//        httpSession.setAttribute(KEY, text);
        final HttpSession session = httpServletRequest.getSession();
        session.setAttribute(KEY, text);
        return text;
    }

    @CrossOrigin
    @GetMapping("/get")
    public String getTest(@Autowired HttpSession httpSession) {
        final Object attribute = httpSession.getAttribute(KEY);
        if (attribute == null) {
            return "KEY is null value.";
        }

        if (attribute instanceof String) {
            return (String) attribute;
        } else {
            return "KEY is not a string.";
        }
    }
}
