package com.agile.train.service;

import com.agile.train.dto.CoursewareWithDownloadInfo;
import com.agile.train.dto.ResultVM;
import com.agile.train.constant.PathConstants;
import com.agile.train.dto.UserDTO;
import com.agile.train.dto.UserProgressDTO;
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
import org.springframework.data.domain.Pageable;
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
import java.util.ArrayList;
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
            return null;
        } catch (NullPointerException e){
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
        Optional<User> opt=userService.getUserWithAuthorities();
        if(opt.isPresent()) {
            List<Courseware> coursewares = coursewareRepository.findAll();
            int count = 0;
            int countd = 0;
            for (Courseware courseware : coursewares) {
                UserDownload userDownload = userDownloadRepository.findOneByCoursewareIdAndUserId(courseware.getId(), opt.get().getId());
                count++;
                if(userDownload != null){
                    countd++;
                }
            }
            return new ResultVM<Double>().success().data((double) Math.round((double) countd / count * 1000) / 1000);
        }
        return null;
    }

    public Double getUserDownloadsById(String id) {
        List<Courseware> coursewares = coursewareRepository.findAll();
        int count = 0;
        int countd = 0;
        for (Courseware courseware : coursewares) {
            UserDownload userDownload = userDownloadRepository.findOneByCoursewareIdAndUserId(courseware.getId(), id);
            count++;
            if(userDownload != null){
                countd++;
            }
        }
        return (double) Math.round((double) countd / count * 1000) / 1000;
    }

    public ResultVM<Double> getAllDownloads(){
        List<User> students = userService.getAllStudents();
        Double sum = 0.0;
        for(int i = 0; i < students.size(); i++){
            User st = students.get(i);
            sum += getUserDownloadsById(st.getId());
        }
        return new ResultVM<Double>().success().data((double)Math.round(sum / students.size() * 1000) / 1000);
    }

    public ResultVM<List<CoursewareWithDownloadInfo>> getAllCoursewares(){
        Optional<User> opt=userService.getUserWithAuthorities();
        if(opt.isPresent()) {
            List<Courseware> coursewares = coursewareRepository.findAll();
            List<CoursewareWithDownloadInfo> res = new ArrayList<>();
            for (Courseware courseware : coursewares) {
                UserDownload userDownload = userDownloadRepository.findOneByCoursewareIdAndUserId(courseware.getId(), opt.get().getId());
                res.add(new CoursewareWithDownloadInfo(courseware, userDownload != null));
            }
            return new ResultVM<List<CoursewareWithDownloadInfo>>().success().data(res);
        }
        return null;
    }

    public void deleteFile(String id) {
        coursewareRepository.deleteById(id);
        userDownloadRepository.deleteByCoursewareId(id);
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

    public List<UserProgressDTO> getUserProgressDTOList(String keyword, Pageable pageable){
        List<UserDTO> userDTOList = userService.getAccountListByRole("ROLE_STUDENT",keyword,pageable);
        List<UserProgressDTO> userProgressDTOList = new ArrayList<>();
        for (UserDTO userDTO : userDTOList) {
            UserProgressDTO userProgressDTO = new UserProgressDTO(userDTO, getUserDownloadsById(userDTO.getId()));
            userProgressDTOList.add(userProgressDTO);
        }
        return userProgressDTOList;
    }

    public List<UserDownload> getOneFileDownloadsCount(String coursewareId){
        return userDownloadRepository.findAllByCoursewareId(coursewareId);
    }

    public List<UserDownload> getAllFileDownloadsCount(){
        return userDownloadRepository.findAll();
    }
}
