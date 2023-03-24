package com.tiCloudServer.systemContact.filter;

import com.tiCloudServer.systemContact.util.RequestWrapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@Component
@WebFilter(urlPatterns = "/test")
public class TestFilter  implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain){
        ServletRequest requestWrapper = null;
        if(servletRequest instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
        }
        if(requestWrapper == null) {
            //防止流读取一次就没有了,将流传递下去
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }
    @Override
    public void destroy() {
    }

}
