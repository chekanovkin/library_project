package com.project.library_project.service;

import com.project.library_project.entity.Role;
import com.project.library_project.entity.User;
import com.project.library_project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepo;

    public UserService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByLogin(username);
        if(user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user;
    }

    public boolean save(User user) {
        User userFromDb = userRepo.findByLogin(user.getUsername());
        if (userFromDb != null) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return true;
    }
}
