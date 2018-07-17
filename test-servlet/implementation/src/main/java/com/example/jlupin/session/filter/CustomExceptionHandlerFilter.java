package com.example.jlupin.session.filter;

import com.example.jlupin.session.exception.CoreSystemUnavailableException;
import com.jlupin.impl.microservice.partofjlupin.httpsessionrepository.error.HttpSessionRepositoryUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Piotr Heilman
 */
public class CustomExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (HttpSessionRepositoryUnavailableException | CoreSystemUnavailableException e) {
            // Put exception into request scope (perhaps of use to a view)
            request.setAttribute("javax.servlet.error.exception", e);
            request.setAttribute("javax.servlet.error.status_code", HttpStatus.SERVICE_UNAVAILABLE.value());

            // forward to error page.
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error");
            dispatcher.forward(request, response);
        }
    }
}
