package com.example.jlupin.session.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Piotr Heilman
 */
@Controller
public class IndexController {
    @GetMapping("/")
    public String get() {
        return "index";
    }

    @PostMapping("/")
    public String post(@Autowired HttpSession httpSession, @Autowired HttpServletRequest httpServletRequest) {
        final String key = httpServletRequest.getParameter("key");
        final String value = httpServletRequest.getParameter("value");

        if (key != null && !key.isEmpty()) {
            if (value != null && !value.isEmpty()) {
                httpSession.setAttribute(key, value);
            } else {
                httpSession.removeAttribute(key);
            }
        }

        return "redirect:/";
    }
}
