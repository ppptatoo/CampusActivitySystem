package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 全局 UTF-8 编码过滤器，防止中文乱码。
 */
public class EncodingFilter implements Filter {

    private static final String ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) {
        // 无需初始化参数
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding(ENCODING);
        response.setCharacterEncoding(ENCODING);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 无需释放资源
    }
}
