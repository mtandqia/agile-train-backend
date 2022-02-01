package com.agile.train.service;

import com.agile.train.dto.CommentAddDTO;
import com.agile.train.dto.QuestionAddDTO;
import com.agile.train.entity.CommentAndUser;
import com.agile.train.entity.Question;
import com.agile.train.entity.User;
import com.agile.train.exception.NullParameterException;
import com.agile.train.repo.ForumRepository;
import com.agile.train.repo.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:13
 */
@Service
@Slf4j
public class ForumService {
    @Autowired
    ForumRepository forumRepository;
    @Autowired
    UserService userService;
    @Autowired
    QuestionRepository questionRepository;

    public Question addQuestion(QuestionAddDTO questionAddDTO) {
        if(questionAddDTO==null){
            throw new NullParameterException();
        }
        Optional<User> optional=userService.getUserWithAuthorities();
        if(optional.isPresent()) {
            String loginName = optional.get().getLogin();
            String nowTime= LocalDateTime.now().toString().replace("T"," ").substring(0,19);
            return questionRepository.save(new Question(
                    null,
                    loginName,
                    questionAddDTO.getQuestionTitle(),
                    questionAddDTO.getQuestionContent(),
                    nowTime,
                    nowTime
            ));
        }else{
            return null;
        }
    }

    public CommentAndUser addComment(CommentAddDTO commentDTO){
        if(commentDTO==null){
            throw new NullParameterException();
        }
        Optional<User> optional=userService.getUserWithAuthorities();
        if(optional.isPresent()) {
            String loginName = optional.get().getLogin();
            String nowTime=LocalDateTime.now().toString().replace("T"," ").substring(0,19);

            Optional<Question> question=questionRepository.findById(commentDTO.getQuestionId());
            if(question.isPresent()){
                Question value=question.get();
                value.setModifyTime(nowTime);
                questionRepository.save(value);
            }
            else{
                throw new NullParameterException("there is no such question.");
            }
            if(commentDTO.getParentId()!=null) {
                Optional<CommentAndUser> opt = forumRepository.findById(commentDTO.getParentId());
                opt.orElseThrow(() -> new NullPointerException("there is no such parentId."));
            }

            return forumRepository.save(new CommentAndUser(
                    null,
                    commentDTO.getParentId(),
                    commentDTO.getQuestionId(),
                    loginName,
                    commentDTO.getReplyUserLoginName(),
                    commentDTO.getCommentContent(),
                    nowTime,
                    null
            ));
        }else{
            return null;
        }
    }
}
