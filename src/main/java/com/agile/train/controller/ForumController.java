package com.agile.train.controller;

import com.agile.train.dto.*;
import com.agile.train.entity.CommentAndUser;
import com.agile.train.entity.Question;
import com.agile.train.service.ForumService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:11
 */
@RestController
@RequestMapping("/train/forum")
@Slf4j
public class ForumController {
    @Autowired
    ForumService forumService;

    @PostMapping("/question")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "新增提问", notes = "TEACHER和STUDENT有权限调用此接口")
    public ResultVM<Question> addQuestion(@RequestBody QuestionAddDTO questionAddDTO) {
        return new ResultVM<Question>().success().data(forumService.addQuestion(questionAddDTO));
    }

    @GetMapping("/question")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "按页返回提问列表，默认0，10，可以加搜索关键词", notes="TEACHER和STUDENT有权限调用此接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value="页码（从0开始）",name = "page",defaultValue = "0"),
            @ApiImplicitParam(value="页的大小",name = "size",defaultValue = "10"),
            @ApiImplicitParam(value="搜索关键词",name = "keyword"),
            @ApiImplicitParam(value="顺序",name = "order",defaultValue = "desc")
    })
    public ResultVM<List<QuestionDTO>> getQuestion(@RequestParam(defaultValue = "",required = false) String keyword,
                                                   @RequestParam(defaultValue = "desc",required = false) String order,
                                                   Pageable pageable){
        return new ResultVM<List<QuestionDTO>>().success().data(
                forumService.getQuestionByKeyword(keyword,order,pageable));
    }

    @PostMapping("/comment")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "回复",notes="TEACHER和STUDENT有权限调用此接口")
    public ResultVM<CommentAndUser> addComment(@Valid @RequestBody CommentAddDTO commentDTO) {
        return new ResultVM<CommentAndUser>().success().data(forumService.addComment(commentDTO));
    }

    @GetMapping("/question_comment")
    @ApiOperation(value="按questionId来获取回复",notes="TEACHER和STUDENT有权限调用此接口")
    @ApiImplicitParam(value="问题id",name = "questionId")
    public ResultVM<QuestionAndCommentDTO> getReply(@RequestParam String questionId){
        return new ResultVM<QuestionAndCommentDTO>().success().data(forumService.getAllComment(questionId));
    }
}
