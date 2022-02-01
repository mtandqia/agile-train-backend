package com.agile.train.security;

import java.util.*;


import com.agile.train.entity.User;
import com.agile.train.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        String credentials=Arrays.toString((String[]) authentication.getCredentials());
        String password = credentials.split(", ")[0].substring(1);
        String second=credentials.split(", ")[1];
        String role = second.substring(0,second.length()-1);
        System.out.println("para:" + username+" "+ password +" "+role);
        Optional<User> user = userRepository.findOneByEmailIgnoreCase(username);
        if(!user.isPresent()) {
            throw new UsernameNotFoundException("Email address not registered");
        }
        if(!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        if("".equals(role)||!user.get().getAuthorities().contains(role)){
            throw new BadCredentialsException("Inappropriate role or lack of role");
        }


        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user.get().getLogin(), password,
                listUserGrantedAuthorities(user.get().getAuthorities()));
        result.setDetails(authentication.getDetails());

        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private Set<GrantedAuthority> listUserGrantedAuthorities(List<String> authoritySet) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String authority : authoritySet) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

}
