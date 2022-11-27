package com.mosscorp.userservice.dao;

import com.mosscorp.userservice.model.User;

import java.util.List;

public interface UserDao {
    User getUser(String id);

    void createUser(User user);

    List<User> listUsers();
}
