package com.agile.train.controller;

import com.agile.train.dto.ResultVM;
import com.agile.train.entity.Courseware;
import com.agile.train.service.CoursewareService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Mengting Lu
 * @date 2022/1/30 14:11
 */
@RestController
@RequestMapping("/train/courseware")
@Slf4j
public class CoursewareController {
    @Autowired
    CoursewareService coursewareService;

    @PostMapping(value = "")
    @ApiOperation(value = "上传课件", notes = "只有TEACHER有权限调用此接口上传新课件")
    @ApiImplicitParam(name = "file", value = "文件")
    public ResultVM<Courseware> uploadFile(MultipartFile file) {
        return coursewareService.uploadFile(file);
    }

    @GetMapping(value="")
    @ApiOperation(value = "下载课件",notes = "TEACHER和STUDENT有权限调用此接口")
    @ApiImplicitParam(name="coursewareId", value="课件id")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String coursewareId){
        return coursewareService.downloadFile(coursewareId);
    }

    @GetMapping(value="download_cnt")
    @ApiOperation(value = "增加课件下载量(+1)",notes = "TEACHER和STUDENT有权限调用此接口")
    @ApiImplicitParam(name="coursewareId", value="课件id")
    public ResultVM<String> addDownloadFileCnt(@RequestParam String coursewareId){
        return new ResultVM<String>().success().data(coursewareService.addDownloadFileCnt(coursewareId));
    }

    @DeleteMapping(value = "")
    @ApiOperation(value = "删除课件", notes = "只有TEACHER有权限调用此接口删除课件")
    @ApiImplicitParam(name = "coursewareId", value = "课件id")
    public void deleteFile(@RequestParam String coursewareId) throws IOException {
        coursewareService.deleteFile(coursewareId);
    }

    @GetMapping(value = "/user_downloads")
    @ApiOperation(value="获得学生自己的学习进度",notes = "STUDENT有权调用")
    public ResultVM<Double> getUserDownloads(){
        return coursewareService.getUserDownloads();
    }

    @GetMapping(value = "/all_downloads")
    @ApiOperation(value = "获得总体学习进度", notes = "TEACHER有权调用")
    public ResultVM<Double> getAllDownloads(){
        return coursewareService.getAllDownloads();
    }

    @GetMapping(value = "/list_all")
    @ApiOperation(value="获得所有课件列表",notes = "TEACHER和STUDENT有权调用")
    public ResultVM<List<Courseware>> getAllCoursewares(){
        return coursewareService.getAllCoursewares();
    }
}
