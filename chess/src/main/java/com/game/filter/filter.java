package com.game.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


public class filter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        response.setContentType("text/html;charset=UTF-8");

        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.setHeader("X-Content-Type-Options", "nosniff");


        String uri = request.getRequestURI();

        if (uri.endsWith(".html")
                || uri.startsWith(request.getContextPath() + "/user/login")
                || uri.startsWith(request.getContextPath() + "/user/register")
                || uri.startsWith(request.getContextPath() + "/css/")
                || uri.startsWith(request.getContextPath() + "/js/")
                || uri.startsWith(request.getContextPath() + "/picture/")) {
            filterChain.doFilter(request, response);
            return;
        }


        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            System.out.println("no login");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
