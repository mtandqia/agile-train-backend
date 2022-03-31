package com.agile.train.service;

import com.agile.train.constant.AuthoritiesConstants;
import com.agile.train.dto.ResultVM;
import com.agile.train.dto.UserDTO;
import com.agile.train.dto.UserModifyDTO;
import com.agile.train.entity.User;
import com.agile.train.exception.EmailAlreadyUsedException;
import com.agile.train.exception.LoginAlreadyUsedException;
import com.agile.train.exception.NullParameterException;
import com.agile.train.repo.UserRepository;
import com.agile.train.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }

    public List<User> getAllStudents() {
        return userRepository.findAllByAuthorities(AuthoritiesConstants.STUDENT);
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
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        newUser.setCreatedDate(LocalDateTime.now());
        newUser.setLastModifiedDate(LocalDateTime.now());

       String authorities = "";
        //在这里可以再加上管理员、教师、学生的角色
        if(role.equals(AuthoritiesConstants.STUDENT)||role.equals(AuthoritiesConstants.TEACHER)||role.equals(AuthoritiesConstants.ADMIN)){
            authorities=role;
        }
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public List<UserDTO> getAccountListByRole(String role, String keyword, Pageable pageable) {
        if(role==null){
            throw new NullParameterException();
        }
        Page<User> userList=userRepository.findByRoleAndKeyword(role,keyword,pageable);
        List<UserDTO> userDTOList=new ArrayList<>();
        for(User u:userList){
            userDTOList.add(new UserDTO(u));
        }
        return userDTOList;
    }

    public ResultVM<String> updateUser(UserModifyDTO dto) {
        if(dto==null||dto.getId()==null){
            throw new NullParameterException();
        }
        boolean updated= false;
        Optional<User> opt=userRepository.findById(dto.getId());
        if(opt.isPresent()){
            User user=opt.get();
            if(dto.getLogin()!=null&&!dto.getLogin().equals(user.getLogin())){
                userRepository.findOneByLogin(dto.getLogin().toLowerCase()).ifPresent(existingUser -> {
                    throw new LoginAlreadyUsedException();
                });
                user.setLogin(dto.getLogin());
            }
            if(dto.getEmail()!=null&&!dto.getEmail().equals(user.getEmail())){
                userRepository.findOneByEmailIgnoreCase(dto.getEmail()).ifPresent(existingUser -> {
                    throw new EmailAlreadyUsedException();
                });
                user.setEmail(dto.getEmail().toLowerCase());
            }
            BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
            if(dto.getPassword()!=null){user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));}
            userRepository.save(user);
            log.debug("Changed Information for User: {}", user);
            updated=true;
        }
        if(updated){
            return new ResultVM<String>().success().data("Changed information for user: "+dto.getId());
        }else{
            return new ResultVM<String>().fail().data("Fail to change information for user: "+dto.getId());
        }
    }

    public ResultVM<String> deleteUser(String login) {
        if(login==null){
            throw new NullParameterException();
        }
        Optional<User> optional=userRepository.findOneByLogin(login);
        if(optional.isPresent()) {
            User user=optional.get();
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
            return new ResultVM<String>().success().data("Deleted User: "+login);
        }
        return new ResultVM<String>().fail().data("Delete failed, there is no user named "+login);
    }
}
