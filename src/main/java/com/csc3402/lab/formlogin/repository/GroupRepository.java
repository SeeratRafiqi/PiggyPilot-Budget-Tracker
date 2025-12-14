package com.csc3402.lab.formlogin.repository;

import com.csc3402.lab.formlogin.model.Group;
import com.csc3402.lab.formlogin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByUsersUserId(Long userId);
    List<Group> findByUsers(User user);
}

