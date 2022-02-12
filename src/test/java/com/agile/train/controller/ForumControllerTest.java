package com.agile.train.controller;

import com.agile.train.repo.QuestionRepository;
import com.agile.train.security.CustomAuthenticationFilter;
import com.agile.train.service.ForumService;
import com.agile.train.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
//import org.testng.annotations.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mengting Lu
 * @date 2022/2/11 13:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ForumControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mvc;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    CustomAuthenticationFilter customAuthenticationFilter;

    MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);

    @Before
    public void setupMockMvc(){
        mvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("111222333@qq.com",new String[]{"111222333","ROLE_STUDENT"});
        Authentication authentication=customAuthenticationFilter.getAuthenticationManager().authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    @Test
    public void addQuestion() throws Exception {
        String json="{\"questionTitle\":\"HAHAHAA\",\"questionContent\":\"Spring\"}";
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/train/forum/question");
        request.content(json);
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());
        questionRepository.deleteByQuestionTitle("HAHAHAA");
    }

    @Test
    public void getQuestion() {
    }

    @Test
    public void addComment() {

    }

    @Test
    public void getReply() {
    }

    @Test
    public void getParticipation() {
    }

    @Test
    public void getUnreadedMsgQuestionList() {
    }
}