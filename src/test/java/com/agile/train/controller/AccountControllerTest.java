package com.agile.train.controller;

import com.agile.train.dto.UserDTO;
import com.agile.train.entity.User;
import com.agile.train.security.CustomAuthenticationFilter;
import com.agile.train.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mengting Lu
 * @date 2022/2/10 15:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mvc;
    @Autowired
    UserService userService;
    @Autowired
    CustomAuthenticationFilter customAuthenticationFilter;

    MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);

    @Before
    public void setupMockMvc(){
        mvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("934808050@qq.com",new String[]{"934808050","ROLE_ADMIN"});
        Authentication authentication=customAuthenticationFilter.getAuthenticationManager().authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Test
    public void registerAccount() throws Exception {
        String json="{\"login\":\"HAHAHAA\",\"password\":\"Spring\",\"email\":\"999888771@qq.ccom\",\"role\":\"ROLE_STUDENT\"}";
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/train/admin/register");
        request.content(json);
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());
        userService.deleteUser("hahahaa");
    }

    @Test
    public void getAccount() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/admin/account_own");
        //request.content(json);
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getAccountByRole() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/admin/account_list?role=ROLE_ADMIN");
        //request.content(json);
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAccount() throws Exception{
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail("aaabbbddd@qq.com");
        userDTO.setLogin("abd");
        User user= userService.registerUser(userDTO,"password","ROLE_STUDENT");

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/train/admin/account?login=abd");
        //request.content(json);
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void updateAccount() throws Exception {
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail("aaabbbddd@qq.com");
        userDTO.setLogin("abd");
        User user= userService.registerUser(userDTO,"password","ROLE_STUDENT");
        String json="{\"id\":\""+user.getId()+"\",\"login\":\"abdabd\",\"email\":\""+user.getEmail()+"\",\"password\":\"password\"}";
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/train/admin/account");
        request.content(json);
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
        userService.deleteUser("abdabd");
    }
}