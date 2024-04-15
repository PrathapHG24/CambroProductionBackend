package com.demo.service;

import com.demo.model.User;
import com.demo.model.userRole;

import java.util.Set;

public interface UserService {
    public User createUser(User user, Set<userRole> userRoles) throws Exception;
    public User getUser(String uname);
    public void deleteUser(Long userId);
}
