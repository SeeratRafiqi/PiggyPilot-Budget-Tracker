package com.csc3402.lab.formlogin.service;

import com.csc3402.lab.formlogin.model.User;
import com.csc3402.lab.formlogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(usernameOrEmail);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    (user.getEmail()),
                    user.getPassword(),
                    new ArrayList<>());// Empty list of authorities
        } else {
            throw new UsernameNotFoundException("Invalid email or password");
        }
    }
}
