package com.agile.train.service;

import com.agile.train.VO.ResultVM;
import com.agile.train.config.PathConstants;
import com.agile.train.entity.Courseware;
import com.agile.train.exception.CoursewareAlreadyExistException;
import com.agile.train.exception.NullParameterException;
import com.agile.train.repo.CoursewareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Mengting Lu
 * @date 2022/1/30 14:12
 */
@Service
public class CoursewareService {
    @Autowired
    CoursewareRepository coursewareRepository;

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

}
