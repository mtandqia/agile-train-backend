package com.agile.train.service;

import com.agile.train.constant.AuthoritiesConstants;
import com.agile.train.dto.UserDTO;
import com.agile.train.entity.User;
import com.agile.train.exception.EmailAlreadyUsedException;
import com.agile.train.exception.LoginAlreadyUsedException;
import com.agile.train.exception.NullParameterException;
import com.agile.train.repo.UserRepository;
import com.agile.train.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:23
 */
@Service
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }

    public User registerUser(UserDTO userDTO, String password, String role) {
        if(userDTO==null){
            throw new NullParameterException();
        }
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            throw new LoginAlreadyUsedException();
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            throw new EmailAlreadyUsedException();
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());

        List<String> authorities = new ArrayList<>();
        //在这里可以再加上管理员、教师、学生的角色
        if(role.equals(AuthoritiesConstants.STUDENT)||role.equals(AuthoritiesConstants.TEACHER)||role.equals(AuthoritiesConstants.ADMIN)){
            authorities.add(role);
        }
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
}
