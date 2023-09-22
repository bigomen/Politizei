package com.api.politizei.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {

    final Environment env;

    public JWTAuthenticationFilter(Environment env)
    {
        super();
        this.env = env;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        TokenAuthenticationService tks = new TokenAuthenticationService(env);

        Authentication authentication = null;

        if(isRefreshToken(req))
        {
            authentication = tks.getRefreshAuthentication((HttpServletRequest)req, (HttpServletResponse)resp);
        } else {
            authentication = tks.getAuthentication((HttpServletRequest)req);
        }

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(req, resp);
        } else {
            if (isRefreshToken(req)) {
                ((HttpServletResponse)resp).setStatus(403);
                resp.flushBuffer();
            } else if (!isPublic(req)) {
                ((HttpServletResponse)resp).setStatus(401);
                resp.flushBuffer();
            } else {
                filterChain.doFilter(req, resp);
            }
        }

    }

    private boolean isRefreshToken(ServletRequest request)
    {
        if (request instanceof HttpServletRequest) {
            return ((HttpServletRequest)request).getRequestURI().contains("/refresh");
        }
        return false;
    }

    private boolean isPublic(ServletRequest request)
    {
        return request instanceof HttpServletRequest && (
                ((HttpServletRequest) request).getRequestURI().indexOf("/publico") == 0
                        || ((HttpServletRequest) request).getRequestURI().indexOf("/forgot") == 0
                        || ((HttpServletRequest) request).getRequestURI().indexOf("/reset") == 0
                        || ((HttpServletRequest) request).getRequestURI().indexOf("/error") == 0
                        || ((HttpServletRequest) request).getRequestURI().indexOf("/login") == 0
                        || ((HttpServletRequest) request).getRequestURI().indexOf("/swagger-ui") == 0
                        || ((HttpServletRequest) request).getRequestURI().indexOf("/swagger-resources") == 0
                        || ((HttpServletRequest) request).getRequestURI().indexOf("/swagger-ui.html") == 0
                        || ((HttpServletRequest) request).getRequestURI().indexOf("/v3/api-docs") == 0
                        || ((HttpServletRequest) request).getRequestURI().indexOf("/webjars") == 0
        );
    }
}
