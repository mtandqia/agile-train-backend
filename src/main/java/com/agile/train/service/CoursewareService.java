package com.agile.train.service;

import com.agile.train.dto.ResultVM;
import com.agile.train.constant.PathConstants;
import com.agile.train.entity.Courseware;
import com.agile.train.entity.User;
import com.agile.train.entity.UserDownload;
import com.agile.train.exception.CoursewareAlreadyExistException;
import com.agile.train.exception.CoursewareNotFoundException;
import com.agile.train.exception.NullParameterException;
import com.agile.train.repo.CoursewareRepository;
import com.agile.train.repo.UserDownloadRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Mengting Lu
 * @date 2022/1/30 14:12
 */
@Service
@Slf4j
public class CoursewareService {
    @Autowired
    UserService userService;
    @Autowired
    CoursewareRepository coursewareRepository;
    @Autowired
    UserDownloadRepository userDownloadRepository;

    public ResultVM<Courseware> uploadFile(MultipartFile multipartFile) {
        try {
            String fileName=multipartFile.getOriginalFilename();
            //查重
            Optional<Courseware> optional=coursewareRepository.findOneByCoursewareName(fileName);
            if(optional.isPresent()){
                throw new CoursewareAlreadyExistException();
            }
            // 保存图片
            String filePath= PathConstants.PATH + fileName;
            File file = new File(filePath);
            multipartFile.transferTo(file);

            String nowTime= LocalDateTime.now().toString().replace("T"," ").substring(0,19);
            Courseware courseware=coursewareRepository.save(
                    new Courseware(null,PathConstants.URL + fileName,fileName,nowTime));

            return new ResultVM<Courseware>().success().data(courseware);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e){
            e.printStackTrace();
            throw new NullParameterException();
        }
    }

    public ResponseEntity<byte[]> downloadFile(String id) throws IOException {
        if(id==null){
            throw new NullParameterException();
        }
        Optional<Courseware> optional=coursewareRepository.findOneById(id);
        if(!optional.isPresent()){
            throw new CoursewareNotFoundException();
        }
        String fileName=optional.get().getCoursewareName();
        File file=new File(PathConstants.PATH+fileName);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = fis.read(b)) != -1)
        {
            bos.write(b, 0, n);
        }
        fis.close();
        bos.close();
        HttpHeaders httpHeaders = new HttpHeaders();
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        log.info(fileName);
        httpHeaders.setContentDispositionFormData("attachment", fileName);
        //指定以流的形式下载文件
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        //更新下载量 TODO: move
        Optional<User> opt=userService.getUserWithAuthorities();
        String userId=opt.orElseThrow().getId();
        UserDownload origin=userDownloadRepository.findOneByCoursewareIdAndUserId(id,userId);
        if(origin==null) {
            UserDownload userDownload=new UserDownload(null,userId,id, 1,LocalDateTime.now().toString());
            userDownloadRepository.save(userDownload);
        }else{
            origin.setDownloads(origin.getDownloads()+1);
            origin.setModifyTime(LocalDateTime.now().toString());
            userDownloadRepository.save(origin);
        }

        return new ResponseEntity<>(bos.toByteArray(), httpHeaders, HttpStatus.CREATED);
    }

    public ResultVM<Double> getUserDownloads() {
        long coursewareCnt=coursewareRepository.count();
        Optional<User> opt=userService.getUserWithAuthorities();
        long userDownloadCnt=userDownloadRepository.countByUserId(opt.orElseThrow().getId());
        return new ResultVM<Double>().success().data(
                (double)Math.round((double)userDownloadCnt/coursewareCnt*1000)/1000);
    }
}
