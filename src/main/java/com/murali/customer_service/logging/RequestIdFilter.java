package com.murali.customer_service.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestIdFilter implements Filter {

    public static final String REQUEST_ID = "requestId";

    @Override
    public void init(FilterConfig filterConfig) { }

    @Override
    public void destroy() { }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        try {
            String requestId = UUID.randomUUID().toString();
            MDC.put(REQUEST_ID, requestId);
            if (req instanceof HttpServletRequest) {
                HttpServletRequest r = (HttpServletRequest) req;
                // optional: if client passed X-Request-Id, use it:
                String incoming = r.getHeader("X-Request-Id");
                if (incoming != null && !incoming.isEmpty()) {
                    MDC.put(REQUEST_ID, incoming);
                }
            }
            chain.doFilter(req, res);
        } finally {
            MDC.remove(REQUEST_ID);
        }
    }
}
