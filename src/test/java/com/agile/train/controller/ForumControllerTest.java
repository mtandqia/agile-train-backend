package com.agile.train.controller;

import com.agile.train.dto.CommentAddDTO;
import com.agile.train.dto.QuestionAddDTO;
import com.agile.train.entity.CommentAndUser;
import com.agile.train.entity.Question;
import com.agile.train.repo.ForumRepository;
import com.agile.train.repo.QuestionRepository;
import com.agile.train.repo.ReadedRepository;
import com.agile.train.security.CustomAuthenticationFilter;
import com.agile.train.service.ForumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mengting Lu
 * @date 2022/2/11 13:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class ForumControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mvc;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    CustomAuthenticationFilter customAuthenticationFilter;
    @Autowired
    ForumService forumService;
    @Autowired
    ReadedRepository readedRepository;
    @Autowired
    ForumRepository forumRepository;

    MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);

    @BeforeEach
    void setupMockMvc(){
        mvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc对象

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("333222111@qq.com",new String[]{"333222111","ROLE_TEACHER"});
        Authentication authentication=customAuthenticationFilter.getAuthenticationManager().authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    @Test
    void addQuestion() throws Exception {
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
    void getQuestion() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/forum/question?keyword=t&order=asc");
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void addComment() throws Exception {
        QuestionAddDTO questionAddDTO=new QuestionAddDTO("HAHAHAA","AA");
        Question question = forumService.addQuestion(questionAddDTO);

        String json="{\"commentContent\":\"HAHAHAA\",\"questionId\":\""
                + question.getId()+"\",\"replyUserLoginName\":\"aaa\"}";
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/train/forum/comment");
        request.content(json);
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());

        questionRepository.deleteByQuestionTitle("HAHAHAA");
        forumRepository.deleteByCommentContent("HAHAHAA");
        readedRepository.deleteByUserLoginNameAndQuestionId("aaa",question.getId());
        readedRepository.deleteByUserLoginNameAndQuestionId("333",question.getId());
    }

    @Test
    void getReply() throws Exception {
        QuestionAddDTO questionAddDTO=new QuestionAddDTO("HAHAHAA","AA");
        Question question = forumService.addQuestion(questionAddDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/forum/question_comment?questionId="+question.getId());
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
        questionRepository.deleteByQuestionTitle("HAHAHAA");
    }

    @Test
    void getParticipation() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/forum/participation");
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getUnreadedMsgQuestionList() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/forum/unreaded_msg_question_list");
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void allQuestions() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/forum/all_questions");
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void allComments() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/forum/all_comments");
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }
}