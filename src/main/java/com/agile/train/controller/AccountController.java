package com.agile.train.controller;

import com.agile.train.dto.ManagedUserVM;
import com.agile.train.dto.ResultVM;
import com.agile.train.dto.UserDTO;
import com.agile.train.entity.User;
import com.agile.train.exception.InternalServerErrorException;
import com.agile.train.exception.InvalidPasswordException;
import com.agile.train.repo.UserRepository;
import com.agile.train.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    /**
     * GET  /account : get the current user.
     *
     * @return the current user
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account_own")
    @ApiOperation(value = "获取用户自身的账户信息",notes = "所有用户都有权限调用")
    public ResultVM<UserDTO> getAccount() {
        return new ResultVM<UserDTO>().success().data(
                new UserDTO(userService.getUserWithAuthorities().orElseThrow(() ->
                        new InternalServerErrorException("User could not be found"))));
    }

    @GetMapping("/account_list")
    @ApiOperation(value = "获取特定角色的用户账户信息列表",notes = "只有ADMIN有权限调用此接口查看用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value="角色",name = "role",required = true),
            @ApiImplicitParam(value="页码（从0开始）",name = "page",defaultValue = "0"),
            @ApiImplicitParam(value="页的大小",name = "size",defaultValue = "10"),
            @ApiImplicitParam(value="搜索关键词（用户名）",name = "keyword")
    })
    public ResultVM<List<UserDTO>> getAccountByRole(@RequestParam String role,
                                                    @RequestParam(defaultValue = "",required = false) String keyword,
                                                    @PageableDefault(sort = {"last_modified_time"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResultVM<List<UserDTO>>().success().data(userService.getAccountListByRole(role,keyword,pageable));
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
                password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
                password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
