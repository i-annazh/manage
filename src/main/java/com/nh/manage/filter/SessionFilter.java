package com.nh.manage.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component("SessionFilter")
public class SessionFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		Map<String,String> map  = new HashMap<String, String>();
		Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = ((HttpServletRequest)request).getCookies();
        if(null!=cookies){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        
        if(cookieMap.containsKey("tel")){
            Cookie cookie = (Cookie)cookieMap.get("tel");
            String tel = cookie.getValue();
            System.out.println("Filter print------"+tel);
            request.setAttribute("cookieTel", tel);

        }

		
		
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
