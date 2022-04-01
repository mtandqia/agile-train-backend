package com.agile.train.service;


import com.agile.train.dto.UserProgressDTO;
import com.agile.train.entity.Courseware;
import com.agile.train.repo.CoursewareRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
/**
 * @author wqy
 * @date 2022/2/14 10:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CoursewareServiceTest {
    @Autowired
    CoursewareService coursewareService;

    @Autowired
    CoursewareRepository coursewareRepository;

    @Test
    void uploadFile() throws Exception{
        String filename = "test.pdf";
        InputStream inStream = getClass().getClassLoader().getResourceAsStream(filename);
        MultipartFile file = new MockMultipartFile("file", "test.pdf", "pdf", inStream);
        coursewareService.uploadFile(file);
    }

    @Test
    void downloadFile() {
        List<Courseware> coursewareList = coursewareRepository.findAll();
        Courseware courseware = coursewareList.get(0);
        assertNotNull(courseware);
        String id = courseware.getId();
        assertNotNull(coursewareService.downloadFile(id));
    }

    @Test
    void getUserDownloads() {
        assertNotNull(coursewareService.getUserDownloads());
    }

    @Test
    void getAllDownloads() {
        assertNotNull(coursewareService.getAllDownloads());
    }

    @Test
    void getAllCoursewares() {
        assertNotNull(coursewareService.getAllCoursewares());
    }

    @Test
    void deleteFile() {
        coursewareService.deleteFile("aaa");
    }

    @Test
    void addDownloadFileCnt() {
        List<Courseware> coursewareList = coursewareRepository.findAll();
        Courseware courseware = coursewareList.get(0);
        assertNotNull(courseware);
        String id = courseware.getId();
        assertNotNull(coursewareService.addDownloadFileCnt(id));
    }

    @Test
    void getUserProgressDTOList() {
        List<UserProgressDTO> list=coursewareService.getUserProgressDTOList("",null);
        assertNotNull(list);
    }

    @Test
    void getOneFileDownloadsCount() {
        List<Courseware> coursewareList = coursewareRepository.findAll();
        Courseware courseware = coursewareList.get(0);
        assertNotNull(courseware);
        String id = courseware.getId();
        assertNotNull(coursewareService.getOneFileDownloadsCount(id));
    }

    @Test
    void getAllFileDownloadsCount() {
        assertNotNull(coursewareService.getAllFileDownloadsCount());
    }
}
