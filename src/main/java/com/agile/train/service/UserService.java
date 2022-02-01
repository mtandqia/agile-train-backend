package com.agile.train.service;

import com.agile.train.entity.User;
import com.agile.train.repo.UserRepository;
import com.agile.train.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:23
 */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }
}
