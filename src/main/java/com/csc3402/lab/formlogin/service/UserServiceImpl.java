package com.csc3402.lab.formlogin.service;

import com.csc3402.lab.formlogin.model.User;
import com.csc3402.lab.formlogin.dto.UserDto;
import com.csc3402.lab.formlogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User((userDto.getFname()), (userDto.getLname()), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateUser(User user) {
        User existingUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        // Only allow setting totamount if it is currently null
        if (existingUser.getTotamount() == null) {
            existingUser.setTotamount(user.getTotamount());
        }

        // Update fields
        existingUser.setFname(user.getFname());
        existingUser.setLname(user.getLname());
        existingUser.setPhone(user.getPhone());
        existingUser.setEmail(user.getEmail());

        // Encode the new password only if it's provided
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            existingUser.setPassword(encodedPassword);
        }
        userRepository.save(existingUser);
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }
}
