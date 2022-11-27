package com.mosscorp.mooer.userservice.dao;

import com.mosscorp.mooer.userservice.model.User;

import java.util.List;

public interface UserDao {
    User getUser(String id);

    void createUser(User user);

    List<User> listUsers();
}
