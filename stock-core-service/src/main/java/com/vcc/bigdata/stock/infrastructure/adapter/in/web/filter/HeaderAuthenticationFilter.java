package com.vcc.bigdata.stock.infrastructure.adapter.in.web.filter;

import com.vcc.bigdata.stock.application.context.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * class insert current user id (on request's header) to thread local
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    private static final String USER_ID_HEADER = "X-User-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String userIdHeader = request.getHeader(USER_ID_HEADER);

        if (StringUtils.hasText(userIdHeader)) {
            try {
                Long currentUserId = Long.valueOf(userIdHeader);
                UserContext.setCurrentUserId(currentUserId);
            } catch (NumberFormatException ex){
                log.error("Value of header {} do not invalid: {}", USER_ID_HEADER, userIdHeader);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "X-User-Id must be a valid Long");
                return;
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
