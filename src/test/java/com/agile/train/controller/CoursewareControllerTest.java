package com.agile.train.controller;

import com.agile.train.security.CustomAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author wqy
 * @date 2022/2/14 10:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CoursewareControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mvc;

    @Autowired
    CustomAuthenticationFilter customAuthenticationFilter;

    MediaType MEDIA_TYPE_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);

    @BeforeEach
    void setupMockMvc(){
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("333222111@qq.com",new String[]{"333222111","ROLE_TEACHER"});
        Authentication authentication=customAuthenticationFilter.getAuthenticationManager().authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void uploadFile() throws Exception{
        String filename = "test.pdf";
        InputStream inStream = getClass().getClassLoader().getResourceAsStream(filename);
        MockMultipartFile mfile = new MockMultipartFile("file", "test.pdf", "pdf", inStream);
        MockMultipartHttpServletRequestBuilder file = MockMvcRequestBuilders.multipart("/train/courseware").file(mfile);
        file.locale(Locale.CHINESE);
        file.accept(MEDIA_TYPE_JSON_UTF8);
        file.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(file)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void downloadFile() throws Exception{
//        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/courseware?coursewareId=61f63ea3295eda0408ffafb2");
//        request.locale(Locale.CHINESE);
//        request.accept(MEDIA_TYPE_JSON_UTF8);
//        request.contentType(MEDIA_TYPE_JSON_UTF8);
//        mvc.perform(request)
//                .andDo(print())
//                .andExpect(status().isOk());
    }

    @Test
    void addDownloadFileCnt() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/courseware/download_cnt?coursewareId=6203d03244641b60f0da8960");
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getOneFileCounts() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/courseware/one_file_count?coursewareId=6203d03244641b60f0da8960");
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getFileCounts() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/courseware/all_file_count");
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteFile() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/train/courseware?coursewareId=aaa");
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getUserProgressList() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/courseware/user_list_progress");
        //request.content(json);
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getUserDownloads() throws Exception{
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("111222333@qq.com",new String[]{"111222333","ROLE_STUDENT"});
        Authentication authentication=customAuthenticationFilter.getAuthenticationManager().authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/courseware/user_downloads");
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllDownloads() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/courseware/all_downloads");
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllCoursewares() throws Exception{
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/train/courseware/list_all");
        request.locale(Locale.CHINESE);
        request.accept(MEDIA_TYPE_JSON_UTF8);
        request.contentType(MEDIA_TYPE_JSON_UTF8);
        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

}
