package com.agile.train.service;

import com.agile.train.dto.*;
import com.agile.train.entity.CommentAndUser;
import com.agile.train.entity.Question;
import com.agile.train.entity.Readed;
import com.agile.train.entity.User;
import com.agile.train.exception.NullParameterException;
import com.agile.train.repo.ForumRepository;
import com.agile.train.repo.QuestionRepository;
import com.agile.train.repo.ReadedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    ReadedRepository readedRepository;

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
                if(!opt.isPresent()){
                    throw new NullPointerException("there is no such parentId.");
                }
            }
            Readed replied=new Readed(
                    null,
                    commentDTO.getReplyUserLoginName(),
                    commentDTO.getQuestionId(),
                    false,
                    LocalDateTime.now().toString());
            readedRepository.save(replied);

            Optional<Question> question1=questionRepository.findById(commentDTO.getQuestionId());
            if(question1.isPresent()){
                Readed poster=new Readed(
                        null,
                        question1.get().getLoginName(),
                        commentDTO.getQuestionId(),
                        false,
                        LocalDateTime.now().toString());
                readedRepository.save(poster);
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

    public QuestionAndCommentDTO getAllComment(String questionId){
        setReadedMsg(questionId);
        if(questionId==null){
            throw new NullParameterException();
        }
        //allList就是对这个提问的第一层回复
        List<CommentAndUser> allList = forumRepository.findAllByQuestionId(questionId);
        Map<String, CommentAndUser> map = new HashMap<>();
        List<CommentAndUser> commentAndUserArrayList = new ArrayList<>();
        for (CommentAndUser c : allList) {
            if (c.getParentId()==null) {
                commentAndUserArrayList.add(c);
            }
            map.put(c.getId(), c);
        }
        for (CommentAndUser c : allList) {
            if (c.getParentId()!=null) {
                CommentAndUser parent = map.get(c.getParentId());
                if (parent == null||parent.getId().equals(c.getId())) { continue; }
                if (parent.getChildList() == null) {
                    parent.setChildList(new ArrayList<>());
                }
                parent.getChildList().add(c);
            }
        }
        Optional<Question> questionOptional=questionRepository.findById(questionId);
        String questionContent="";
        String questionTitle="";
        if(questionOptional.isPresent()){
            questionContent=questionOptional.get().getQuestionContent();
            questionTitle=questionOptional.get().getQuestionTitle();
        }
        return new QuestionAndCommentDTO(
                questionId,
                questionTitle,
                questionContent,
                commentAndUserArrayList
        );
    }

    private void setReadedMsg(String questionId) {
        Optional<User> optional=userService.getUserWithAuthorities();
        if(optional.isPresent()){
            User user=optional.get();
            List<Readed> readed=readedRepository.findByUserLoginNameAndQuestionId(user.getLogin(),questionId);
            for(Readed r:readed) {
                if (!r.isReader()) {
                    r.setReader(true);
                    readedRepository.save(r);
                }
            }
        }
    }

    public List<QuestionDTO> getQuestionByKeyword(String keyword,String order, Pageable pageable) {
        Page<Question> questionPage;
        if("desc".equals(order)){
            questionPage=questionRepository.findByQuestionTitleLikeOrQuestionContentLikeOrderByModifyTimeDesc(keyword,keyword,pageable);
        }else{
            questionPage=questionRepository.findByQuestionTitleLikeOrQuestionContentLikeOrderByModifyTimeAsc(keyword, keyword, pageable);
        }
        List<QuestionDTO> questionDTOList=new ArrayList<>();
        for(Question q:questionPage){
            questionDTOList.add(new QuestionDTO(q,forumRepository.countByQuestionId(q.getId())));
        }
        return questionDTOList;
    }

    public ParticipationDTO getParticipation() {
        Optional<User> opt=userService.getUserWithAuthorities();
        if(opt.isPresent()) {
            String loginName = opt.get().getLogin();
            Set<String> questionIdList = new HashSet<>(forumRepository.findQuestionIdList(loginName));
            int involvedQuestionNum = questionIdList.size();
            int repliedNum = forumRepository.countByReplyUserLoginName(loginName);
            return new ParticipationDTO(involvedQuestionNum, repliedNum);
        }
        return null;
    }

    public Set<QuestionSimpleDTO> getUnreadedMsgQuestionList() {
        Optional<User> opt=userService.getUserWithAuthorities();
        Set<QuestionSimpleDTO> questionIdList=new HashSet<>();
        if(opt.isPresent()) {
            String loginName=opt.get().getLogin();
            List<Readed> unreadedList=readedRepository.findByUserLoginNameAndReader(loginName,false);
            for(Readed r:unreadedList){
                Optional<Question> questionOptional=questionRepository.findById(r.getQuestionId());
                questionOptional.ifPresent(question ->
                        questionIdList.add(new QuestionSimpleDTO(r.getQuestionId(), question.getQuestionTitle())));
            }
        }
        return questionIdList;
    }
}
