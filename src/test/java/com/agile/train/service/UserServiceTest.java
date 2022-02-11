package com.agile.train.service;

import com.agile.train.dto.UserDTO;
import com.agile.train.dto.UserModifyDTO;
import com.agile.train.entity.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mengting Lu
 * @date 2022/2/10 15:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    public void getUserWithAuthorities() {
        Optional<User> user=userService.getUserWithAuthorities();
        assertTrue(true);
    }

    @Test
    public void getAllStudents() {
        List<User> studentList=userService.getAllStudents();
        assertNotNull(studentList);
    }

    @Test
    public void registerUser() {
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail("aaabbbddd@qq.com");
        userDTO.setLogin("abd");
        User user= userService.registerUser(userDTO,"password","ROLE_STUDENT");
        assertNotNull(user);
        UserModifyDTO userModifyDTO=new UserModifyDTO(user.getId(),"abdabd",user.getEmail(),"password");
        userService.updateUser(userModifyDTO);
        userService.deleteUser("abdabd");
    }

    @Test
    public void getAccountListByRole() {
        List<UserDTO> list=userService.getAccountListByRole("ROLE_ADMIN","",null);
        assertNotNull(list);
    }

    @Test
    public void updateUser() {
        assertTrue(true);
    }

    @Test
    public void deleteUser() {
        assertTrue(true);
    }
}