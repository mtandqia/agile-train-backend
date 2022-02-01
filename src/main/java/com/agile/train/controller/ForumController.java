package com.agile.train.controller;

import com.agile.train.dto.CommentAddDTO;
import com.agile.train.dto.QuestionAddDTO;
import com.agile.train.dto.ResultVM;
import com.agile.train.entity.CommentAndUser;
import com.agile.train.entity.Question;
import com.agile.train.service.ForumService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PostMapping("/comment")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "回复",notes="TEACHER和STUDENT有权限调用此接口")
    public ResultVM<CommentAndUser> addComment(@Valid @RequestBody CommentAddDTO commentDTO) {
        return new ResultVM<CommentAndUser>().success().data(forumService.addComment(commentDTO));
    }
}
