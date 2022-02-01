package com.agile.train.security;

import com.agile.train.exception.EmailNotFoundException;
import com.agile.train.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:46
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 授权的时候是对角色授权，而认证的时候应该基于资源，而不是角色，因为资源是不变的，而用户的角色是会变的
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.agile.train.entity.User> user = userRepository.findOneByLogin(username);
        user.orElseThrow(EmailNotFoundException::new);

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String authority : user.get().getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(user.get().getLogin(), user.get().getPassword(), authorities);
    }
}
