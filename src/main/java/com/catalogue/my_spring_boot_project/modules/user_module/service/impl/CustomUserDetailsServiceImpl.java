package com.catalogue.my_spring_boot_project.modules.user_module.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.catalogue.my_spring_boot_project.modules.common.entity.UserEntity;
import com.catalogue.my_spring_boot_project.modules.common.utils.Log;
import com.catalogue.my_spring_boot_project.modules.common.utils.ThreadLocalUtil;
import com.catalogue.my_spring_boot_project.modules.user_module.mapper.UserMapper;
import com.catalogue.my_spring_boot_project.security.CustomUserDetail;


@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    public CustomUserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Value("${file.img.avatarPath}")
    private String AVATARPATH;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Log.info(getClass(), "用户：[" + username + "]正在登录");
        UserEntity user = userMapper.selectUserWithRoles(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<GrantedAuthority> roles = user.getRoles() != null ? user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
                : List.of(new SimpleGrantedAuthority("ROLE_USER"));
        Log.info(getClass(), "用户：[" + username + "]的权限是：{}", roles.toString());
        Log.info(getClass(), "将用户id添加到ThreadLocal");
        ThreadLocalUtil.set(user.getId());
        CustomUserDetail customUserDetail = new CustomUserDetail();
        customUserDetail.setId(user.getId());
        customUserDetail.setUsername(username);

        if (user.getAvatarUrl() == null || user.getAvatarUrl().isEmpty()
                || user.getAvatarUrl().trim().isEmpty()) {
            customUserDetail.setAvatarUrl(AVATARPATH + "default.txt");
        } else {
            customUserDetail.setAvatarUrl(user.getAvatarUrl());
        }
        Log.info(getClass(), "用户：[" + username + "]的头像是：{}", user.getAvatarUrl());
        customUserDetail.setPassword(user.getPassword());
        customUserDetail.setAuthorities(roles);
        return customUserDetail;
    }
}
