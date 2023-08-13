package com.codecool.seasonalproductdiscounter.service.users;

import com.codecool.seasonalproductdiscounter.model.users.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();
    boolean addUser(User user);
}
