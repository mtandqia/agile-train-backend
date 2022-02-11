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
import java.util.List;
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
            log.info("context", e);
            return null;
        } catch (NullPointerException e){
            log.info("context", e);
            throw new NullParameterException();
        }
    }

    public ResponseEntity<byte[]> downloadFile(String id) {
        if(id==null){
            throw new NullParameterException();
        }
        Optional<Courseware> optional=coursewareRepository.findOneById(id);
        if(!optional.isPresent()){
            throw new CoursewareNotFoundException();
        }
        String fileName=optional.get().getCoursewareName();
        File file=new File(PathConstants.PATH+fileName);
        byte[] bytes=null;
        try(
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream()
        ) {
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            bytes=bos.toByteArray();
        }catch (IOException e){
            log.info("context", e);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        log.info(fileName);
        httpHeaders.setContentDispositionFormData("attachment", fileName);
        //指定以流的形式下载文件
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.CREATED);
    }

    public ResultVM<Double> getUserDownloads() {
        long coursewareCnt=coursewareRepository.count();
        Optional<User> opt=userService.getUserWithAuthorities();
        if(opt.isPresent()) {
            long userDownloadCnt = userDownloadRepository.countByUserId(opt.get().getId());
            return new ResultVM<Double>().success().data(
                    (double) Math.round((double) userDownloadCnt / coursewareCnt * 1000) / 1000);
        }
        return null;
    }

    public ResultVM<Double> getAllDownloads(){
        long coursewareCnt=coursewareRepository.count();
        List<User> students = userService.getAllStudents();
        long downloadCnt = 0;
        for(int i = 0; i < students.size(); i++){
            User st = students.get(i);
            long userDownloadCnt = userDownloadRepository.countByUserId(st.getId());
            downloadCnt += userDownloadCnt;
        }
        return new ResultVM<Double>().success().data((double)Math.round((double)downloadCnt / coursewareCnt / students.size() * 1000) / 1000);
    }

    public ResultVM<List<Courseware>> getAllCoursewares(){
        List<Courseware> coursewares = coursewareRepository.findAll();
        return new ResultVM<List<Courseware>>().success().data(coursewares);
    }

    public void deleteFile(String id) {
        coursewareRepository.deleteById(id);
    }

    public String addDownloadFileCnt(String id) {
        Optional<User> opt=userService.getUserWithAuthorities();
        if(opt.isPresent()) {
            int cnt=0;
            String userId = opt.get().getId();
            UserDownload origin = userDownloadRepository.findOneByCoursewareIdAndUserId(id, userId);
            if (origin == null) {
                cnt=1;
                UserDownload userDownload = new UserDownload(null, userId, id, 1, LocalDateTime.now().toString());
                userDownloadRepository.save(userDownload);
            } else {
                origin.setDownloads(origin.getDownloads() + 1);
                cnt=origin.getDownloads();
                origin.setModifyTime(LocalDateTime.now().toString());
                userDownloadRepository.save(origin);
            }
            return "Succeed adding download counts, now is "+cnt+".";
        }
        return "";
    }

    public List<UserDownload> getOneFileDownloadsCount(String coursewareId){
        return userDownloadRepository.findAllByCoursewareId(coursewareId);
    }

    public List<UserDownload> getAllFileDownloadsCount(){
        return userDownloadRepository.findAll();
    }
}
