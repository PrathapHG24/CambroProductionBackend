package com.demo.service;


import com.demo.model.User;
import com.demo.model.userRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class userServiceImpl implements UserService {

    @Autowired
    private com.demo.repository.userRepository userRepository;

    @Autowired
    private com.demo.repository.roleRepository roleRepository;
    @Override
    public User createUser(User user, Set<userRole> userRoles) throws Exception {

        User local =this.userRepository.findByuserName(user.getUserName());

        if(local !=null)
        {
            System.out.println("User already present");
            throw  new Exception("User already present");
        }
        else
        {
            for(userRole ur:userRoles)
            {
                roleRepository.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);
            local=this.userRepository.save(user);
        }
        return local;
    }

    @Override
    public User getUser(String uname) {
        return this.userRepository.findByuserName(uname);
    }

    @Override
    @Transactional
    public void deleteUserByUserName(String username) {
        // Step 1: Fetch the user by username
        User user = userRepository.findByuserName(username);
        if (user != null) {
            // Step 2: Get the user ID
            Long userId = user.getId();

            // Step 3: Delete the user by ID
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
    }
}

