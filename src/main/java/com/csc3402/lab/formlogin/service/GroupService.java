package com.csc3402.lab.formlogin.service;

import com.csc3402.lab.formlogin.model.Group;
import com.csc3402.lab.formlogin.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GroupService {
    // listing all the categories in the list
    List<Group> listAllGroups();

    //displaying the list based on their user_id
    List<Group> listGroupsByUserId(Long userId);

    //adding new category
    Group addNewGroup(Group group);

    //updating category
    void updateGroup(Long id, Group updatedGroup, User user);

    //finding category based on user id
    Optional<Group> findGroupById(Long budgetId);

    //deleting category
    void deleteGroup(Long budgetId);

    List<Group> findByUser(User user);
}
