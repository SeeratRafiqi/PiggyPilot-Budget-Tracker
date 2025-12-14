package com.csc3402.lab.formlogin.service;

import com.csc3402.lab.formlogin.model.User;
import com.csc3402.lab.formlogin.dto.UserDto;

import java.util.Optional;

public interface UserService {

    void saveUser(UserDto userDto);
    User findUserByEmail(String email);

    void updateUser(User user);

    Optional<User> findUserById(Long userId);
}
