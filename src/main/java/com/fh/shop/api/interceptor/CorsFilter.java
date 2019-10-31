package com.fh.shop.api.interceptor;


import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response2 = (HttpServletResponse) response;
        response2.setHeader("Access-Control-Allow-Origin", "*");
        response2.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE ,PUT");
        response2.setHeader("Access-Control-Max-Age", "3600");
        response2.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization,x-auth");
        response2.setHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
