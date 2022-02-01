package com.agile.train.security;

import com.agile.train.util.TokenUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("===========登陆成功================");

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");

        PrintWriter printWriter = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("access_token", TokenUtils.token((String)authentication.getPrincipal()));
        printWriter.write(jsonObject.toJSONString());
        printWriter.flush();
        printWriter.close();
    }
}
