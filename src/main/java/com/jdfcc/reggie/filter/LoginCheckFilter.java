package com.jdfcc.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.jdfcc.reggie.common.BaseContext;
import com.jdfcc.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录检查
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher matcher = new AntPathMatcher();//用于路径匹配。支持通配符

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String[] urls = new String[]{
                "/employee/login",
                "employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",//移动端登录
                "/user/login",//移动端发送短信
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"

        };
        String requestURI = request.getRequestURI();
        log.info("get request: {} ", requestURI);
        Boolean check = check(urls, requestURI);

        if (check) {//判断页面是否处于放行页面内
            log.info("This request does not need to be processed: {} ", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getSession().getAttribute("employee") != null) {//判断后台用户是否登录
            log.info("User logged in: {} ", requestURI);
            Long id = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getSession().getAttribute("user") != null) {//判断前台用户是否登录
            log.info("User logged in: {} ", requestURI);
            Long id = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(id);
            filterChain.doFilter(request, response);
            return;
        }


        log.info("User not logged in: {} ", requestURI);
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }

    /**
     * 判断本次请求是否需要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            Boolean match = matcher.match(url, requestURI);
            if (match)
                return true;
        }
        return false;
    }

}
