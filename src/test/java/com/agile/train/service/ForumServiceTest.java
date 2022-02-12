package com.agile.train.service;

import com.agile.train.dto.CommentAddDTO;
import com.agile.train.dto.ParticipationDTO;
import com.agile.train.dto.QuestionAddDTO;
import com.agile.train.dto.QuestionAndCommentDTO;
import com.agile.train.entity.CommentAndUser;
import com.agile.train.entity.Question;
import com.agile.train.repo.ForumRepository;
import com.agile.train.repo.QuestionRepository;
import com.agile.train.repo.ReadedRepository;
import com.agile.train.security.CustomAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.testng.annotations.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mengting Lu
 * @date 2022/2/11 13:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class ForumServiceTest {
    @Autowired
    ForumService forumService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ForumRepository forumRepository;
    @Autowired
    CustomAuthenticationFilter customAuthenticationFilter;
    @Autowired
    ReadedRepository readedRepository;

    @BeforeEach
    void before(){
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("111222333@qq.com",new String[]{"111222333","ROLE_STUDENT"});
        Authentication authentication=customAuthenticationFilter.getAuthenticationManager().authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void addQuestion() {
        QuestionAddDTO questionAddDTO=new QuestionAddDTO("HAHAHAA","AA");
        Question question = forumService.addQuestion(questionAddDTO);
        assertNotNull(question);
        questionRepository.deleteByQuestionTitle("HAHAHAA");
    }

    @Test
    void addComment() {
        QuestionAddDTO questionAddDTO=new QuestionAddDTO("HAHAHAA","AA");
        Question question = forumService.addQuestion(questionAddDTO);
        CommentAddDTO commentAddDTO=new CommentAddDTO("comment",null,question.getId(),"qqq");
        CommentAndUser commentAndUser=forumService.addComment(commentAddDTO);
        assertNotNull(commentAndUser);
        questionRepository.deleteByQuestionTitle("HAHAHAA");
        forumRepository.deleteById(commentAndUser.getId());
        readedRepository.deleteByUserLoginNameAndQuestionId("qqq",question.getId());
        readedRepository.deleteByUserLoginNameAndQuestionId("111",question.getId());
    }

    @Test
    void getAllComment() {
        QuestionAddDTO questionAddDTO=new QuestionAddDTO("HAHAHAA","AA");
        Question question = forumService.addQuestion(questionAddDTO);
        CommentAddDTO commentAddDTO=new CommentAddDTO("comment",null,question.getId(),"qqq");
        CommentAndUser commentAndUser=forumService.addComment(commentAddDTO);
        QuestionAndCommentDTO list=forumService.getAllComment(question.getId());
        assertNotNull(list);
        questionRepository.deleteByQuestionTitle("HAHAHAA");
        forumRepository.deleteById(commentAndUser.getId());
        readedRepository.deleteByUserLoginNameAndQuestionId("qqq",question.getId());
        readedRepository.deleteByUserLoginNameAndQuestionId("111",question.getId());
    }

    @Test
    void getQuestionByKeyword() {
        QuestionAddDTO questionAddDTO=new QuestionAddDTO("HAHAHAA","AA");
        Question question = forumService.addQuestion(questionAddDTO);
        assertNotNull(forumService.getQuestionByKeyword("AA","desc",null));
        questionRepository.deleteByQuestionTitle("HAHAHAA");
    }

    @Test
    void getParticipation() {
        ParticipationDTO participationDTO= forumService.getParticipation();
        assertNotNull(participationDTO);
    }

    @Test
    void getUnreadedMsgQuestionList() {
        QuestionAddDTO questionAddDTO=new QuestionAddDTO("HAHAHAA","AA");
        Question question = forumService.addQuestion(questionAddDTO);
        CommentAddDTO commentAddDTO=new CommentAddDTO("comment",null,question.getId(),"111");
        CommentAndUser commentAndUser=forumService.addComment(commentAddDTO);
        assertNotNull(forumService.getUnreadedMsgQuestionList());
        questionRepository.deleteByQuestionTitle("HAHAHAA");
        readedRepository.deleteByUserLoginNameAndQuestionId("111",question.getId());
        forumRepository.deleteById(commentAndUser.getId());
    }
}