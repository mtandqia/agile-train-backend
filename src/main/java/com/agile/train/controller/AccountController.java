package com.agile.train.controller;

import com.agile.train.dto.ManagedUserVM;
import com.agile.train.dto.ResultVM;
import com.agile.train.entity.User;
import com.agile.train.exception.InvalidPasswordException;
import com.agile.train.repo.UserRepository;
import com.agile.train.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Mengting Lu
 * @date 2022/2/2 19:32
 */
@RestController
@RequestMapping("/train/admin")
@Slf4j
public class AccountController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "用户注册",notes = "只有ADMIN有权限调用此接口注册新用户")
    public ResultVM<User> registerAccount(@Valid @RequestBody ManagedUserVM managedUserVm) {
        if (!checkPasswordLength(managedUserVm.getPassword())) {
            throw new InvalidPasswordException();
        }
        if (managedUserVm.getLangKey() == null || managedUserVm.getLangKey().isEmpty()) {
            managedUserVm.setLangKey("zh_cn");
        }
        return new ResultVM<User>().success().data(userService.registerUser(managedUserVm, managedUserVm.getPassword(),managedUserVm.getRole()));
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
                password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
                password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
