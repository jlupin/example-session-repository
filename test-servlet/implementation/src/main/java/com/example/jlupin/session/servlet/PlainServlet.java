package com.example.jlupin.session.servlet;

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
    private final static String KEY = "TEST_KEY";
    private final static String FORM_FIELD_VALUE = "KEY_VALUE";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession();
        final Object attribute = session.getAttribute(KEY);

        final String value;
        if (attribute == null) {
            value = "KEY is null value.";
        } else {
            if (attribute instanceof String) {
                value = (String) attribute;
            } else {
                value = "KEY is not a string.";
            }
        }

        resp.setContentType("text/html");
        final PrintWriter writer = resp.getWriter();

        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>Plain servlet</title>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<h1>Current value:</h1>");
        writer.println("<p>" + value + "</p>");
        writer.println("<h1>Set value:</h1>");
        writer.println("<form method=\"POST\">");
        writer.println("<input name=\"" + FORM_FIELD_VALUE + "\" type=\"text\" />");
        writer.println("<input type=\"submit\" />");
        writer.println("</form>");
        writer.println("</body>");
        writer.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession();
        final Object parameter = req.getParameter(FORM_FIELD_VALUE);
        if (parameter == null) {
            session.removeAttribute(KEY);
        } else {
            session.setAttribute(KEY, parameter);
        }

        resp.sendRedirect("/plainServlet");
    }
}
