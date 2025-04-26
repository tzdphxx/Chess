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
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");


        String uri = request.getRequestURI();

        if(uri.contains("/login.html") || uri.contains("/user/login") ||
                uri.contains("/css/") || uri.contains("/js/") || uri.contains(("/register.html"))
                || uri.contains("/user/register") || uri.contains("/picture/")){
            filterChain.doFilter(request, response);
            return;
        }


        HttpSession session = request.getSession(false);
        if (session == null||session.getAttribute("user")==null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            System.out.println("no login");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
