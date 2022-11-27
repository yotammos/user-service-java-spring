package com.mosscorp.mooer.userservice.controller;

import com.mosscorp.mooer.userservice.dao.UserDao;
import com.mosscorp.mooer.userservice.dao.impl.DynamoDBUserDao;
import com.mosscorp.mooer.userservice.exception.UserNotFoundException;
import com.mosscorp.mooer.userservice.model.CreateUserRequest;
import com.mosscorp.mooer.userservice.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    private final UserDao userDao;

    public UserController(final UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping("/hello/{name}")
    ResponseEntity<String> helloWorld(@PathVariable final String name) {
        return ResponseEntity.ok("Hello " + name + "!\n");
    }

    @PostMapping("/user")
    ResponseEntity<String> createUser(@RequestBody final CreateUserRequest request) {
        final String id = UUID.randomUUID().toString();
        userDao.createUser(new User(id, request.name()));
        return ResponseEntity.ok(id);
    }

    @GetMapping("/user/{id}")
    ResponseEntity<User> getUser(@PathVariable final String id) {
        final User user = userDao.getUser(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user")
    ResponseEntity<List<User>> listUsers() {
        final List<User> users = userDao.listUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/authenticate/{id}")
    ResponseEntity<Boolean> authenticate(@PathVariable final String id) {
        try {
            getUser(id);
        } catch (ResourceNotFoundException | UserNotFoundException exception) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }
}
