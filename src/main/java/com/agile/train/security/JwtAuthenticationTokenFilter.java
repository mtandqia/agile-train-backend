package com.agile.train.security;

import com.agile.train.constant.AuthoritiesConstants;
import com.agile.train.exception.EmailNotFoundException;
import com.agile.train.util.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:34
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        //先从url中取token
        String authToken = request.getParameter("token");
        String tokenHeader = "Authorization";
        String authHeader = request.getHeader(tokenHeader);
        String url=request.getRequestURI();
        String method=request.getMethod();
        String tokenHead = "Bearer ";
        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith(tokenHead)) {
            //如果header中存在token，则覆盖掉url中的token  // "Bearer "之后的内容
            authToken = authHeader.substring(tokenHead.length());
        }
        if (StringUtils.isNotBlank(authToken)) {
            String username=null;
            try{
                username = TokenUtils.getUsernameFromToken(authToken);
            }catch (Exception e){
                logger.error(e.getMessage());
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //从已有的user缓存中取了出user信息
                UserDetails user=null;
                boolean hasToken=true;
                boolean valid = false;
                try {
                    user = userDetailsService.loadUserByUsername(username);
                }catch (EmailNotFoundException e){
                    hasToken=false;
                    logger.error(e.getMessage());
                }

                if(hasToken) {
                    valid=true;
                    String authorities = String.valueOf(user.getAuthorities());

                    //teacher & student
                    if (Pattern.matches(".*forum.*|.*courseware/.*", url)) {
                        valid = authorities.contains(AuthoritiesConstants.STUDENT) ||
                                authorities.contains(AuthoritiesConstants.TEACHER);
                    }
                    //teacher, only teachers can upload and delete files
                    if (Pattern.matches(".*courseware.*", url)&&
                            ("POST".equals(method)||"DELETE".equals(method))) {
                        valid = authorities.contains(AuthoritiesConstants.TEACHER);
                    }
                    //student

                    //admin
                    else if (Pattern.matches(".*admin.*", url)) {
                        valid = authorities.contains(AuthoritiesConstants.ADMIN);
                    }

                    //查看自身账户信息跳过权限验证
                    if (url.contains("account_own")) {
                        valid = true;
                    }

                    logger.info("valid:" + valid);
                }
                //检查token是否有效
                if (!TokenUtils.isTokenExpired(authToken)&&valid) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user
                            , null, user.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //设置用户登录状态
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }

}
