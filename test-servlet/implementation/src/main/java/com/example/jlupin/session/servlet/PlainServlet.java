package com.example.jlupin.session.servlet;

import org.springframework.security.web.csrf.CsrfToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

/**
 * @author Piotr Heilman
 */
@WebServlet(urlPatterns = "/plainServlet")
public class PlainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession();

        final CsrfToken csrfToken = (CsrfToken) session.getAttribute("org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN");

        resp.setContentType("text/html");
        final PrintWriter writer = resp.getWriter();

        writer.println("<!DOCTYPE html>");
        writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:th=\"http://www.thymeleaf.org\"");
        writer.println("      xmlns:sec=\"http://www.thymeleaf.org/thymeleaf-extras-springsecurity3\">");
        writer.println("<head>");
        writer.println("    <title>Session Repository</title>");
        writer.println("");
        writer.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />");
        writer.println("    <link rel=\"icon\" type=\"image/x-icon\" href=\"favicon.ico\" />");
        writer.println("");
        writer.println("    <link rel=\"stylesheet\" href=\"/test-servlet/css/bootstrap.min.css\" crossorigin=\"anonymous\" />");
        writer.println("    <link rel=\"stylesheet\" href=\"/test-servlet/css/application.css\" crossorigin=\"anonymous\" />");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("    <nav class=\"navbar navbar-expand-md navbar-dark bg-dark\">");
        writer.println("        <a class=\"navbar-brand\" href=\"#\">Session Repository</a>");
        writer.println("        <div class=\"collapse navbar-collapse\" id=\"navbarsExampleDefault\">");
        writer.println("            <ul class=\"navbar-nav mr-auto\">");
        writer.println("                <li class=\"nav-item\">");
        writer.println("                    <a class=\"nav-link\" href=\"/test-servlet/\">Home</a>");
        writer.println("                </li>");
        writer.println("                <li class=\"nav-item active\">");
        writer.println("                    <a class=\"nav-link\" href=\"#\">Plain servlet <span class=\"sr-only\">(current)</span></a>");
        writer.println("                </li>");
        writer.println("            </ul>");
        writer.println("            <form action=\"/test-servlet/logout\" class=\"form-inline my-2 my-lg-0\" method=\"POST\">");
        writer.println("                <input type=\"hidden\" name=\"_csrf\" value=\"" + csrfToken.getToken() + "\"/>");
        writer.println("                <button class=\"btn btn-outline-success my-2 my-sm-0\" type=\"submit\">Logout</button>");
        writer.println("            </form>");
        writer.println("        </div>");
        writer.println("    </nav>");
        writer.println("");
        writer.println("    <div class=\"container\">");
        writer.println("        <div class=\"row\">");
        writer.println("            <h1>Session attributes</h1>");
        writer.println("        </div>");
        writer.println("        <div class=\"row\">");
        writer.println("            <form action=\"/test-servlet/plainServlet\" method=\"POST\">");
        writer.println("                <div class=\"form-row\">");
        writer.println("                <input type=\"hidden\" name=\"_csrf\" value=\"" + csrfToken.getToken() + "\"/>");
        writer.println("                    <div class=\"col\">");
        writer.println("                        <input type=\"text\" class=\"form-control\" id=\"key\" name=\"key\" placeholder=\"Key\"/>");
        writer.println("                    </div>");
        writer.println("                    <div class=\"col\">");
        writer.println("                        <input type=\"text\" class=\"form-control\" id=\"value\" name=\"value\" placeholder=\"Value\"/>");
        writer.println("                    </div>");
        writer.println("                    <div class=\"col\">");
        writer.println("                        <button class=\"btn btn-primary\" type=\"submit\">Send</button>");
        writer.println("                    </div>");
        writer.println("                </div>");
        writer.println("            </form>");
        writer.println("        </div>");
        writer.println("        <div class=\"row\">");
        writer.println("            <table class=\"table table-striped\">");

        for (String attr : Collections.list(session.getAttributeNames())) {
            writer.println("                <tr>");
            writer.println("                    <td>" + attr + "</td>");
            writer.println("                    <td>" + session.getAttribute(attr) + "</td>");
            writer.println("                </tr>");
        }

        writer.println("            </table>");
        writer.println("        </div>");
        writer.println("    </div>");
        writer.println("");
        writer.println("    <script src=\"/test-servlet/js/jquery-3.2.1.slim.min.js\" crossorigin=\"anonymous\"></script>");
        writer.println("    <script src=\"/test-servlet/js/popper.min.js\" crossorigin=\"anonymous\"></script>");
        writer.println("    <script src=\"/test-servlet/js/tether.min.js\" crossorigin=\"anonymous\"></script>");
        writer.println("    <script src=\"/test-servlet/js/bootstrap.min.js\" crossorigin=\"anonymous\"></script>");
        writer.println("    <script src=\"/test-servlet/js/ie10-viewport-bug-workaround.js\" crossorigin=\"anonymous\"></script>");
        writer.println("</body>");
        writer.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession();

        final String key = req.getParameter("key");
        final String value = req.getParameter("value");

        if (key != null && !key.isEmpty()) {
            if (value != null && !value.isEmpty()) {
                session.setAttribute(key, value);
            } else {
                session.removeAttribute(key);
            }
        }

        resp.sendRedirect("/test-servlet/plainServlet");
    }
}
