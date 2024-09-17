package com.yaroslav.dragontmsbackend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class HttpsEnforcementFilter extends HttpFilter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Инициализация фильтра (если необходимо)
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String requestURI = request.getRequestURI();

        // Проверка, начинается ли URL с /api
        if (requestURI.startsWith("/api") && !request.isSecure()) {
            // Перенаправление на HTTPS
            String redirectUrl = "https://" + request.getServerName() + request.getRequestURI();
            if (request.getQueryString() != null) {
                redirectUrl += "?" + request.getQueryString();
            }
            response.sendRedirect(redirectUrl);
            return;
        }

        // Для остальных URL-адресов продолжаем цепочку фильтров
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Очистка фильтра (если необходимо)
    }
}
