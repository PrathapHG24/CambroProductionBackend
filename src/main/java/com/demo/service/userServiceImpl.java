package com.demo.service;


import com.demo.model.User;
import com.demo.model.userRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }

}

