package com.demo.controller;


import com.demo.model.Role;
import com.demo.model.User;
import com.demo.model.userRole;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class userController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    private com.demo.repository.userRepository userRepository;
    @PostMapping("/")
    @CrossOrigin("*")
    public User createUser(@RequestBody User user) throws Exception {
       
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Role role = new Role();
        role.setRoleName("NORMAL");
        Set<userRole> userRoleSet=new HashSet<>();
        userRole userRole=new userRole();
        userRole.setRole(role);
        userRole.setUser(user);
        userRoleSet.add(userRole);
        return  this.userService.createUser(user,userRoleSet);
    }

    @GetMapping("/{userName}")
    @CrossOrigin("*")
    public User getUser(@PathVariable("userName") String uname)
    
    {
        
        System.out.println(uname);

        return this.userService.getUser(uname);
    }

    @DeleteMapping("/{userId}")
    @CrossOrigin("*")
    public void deleteUser(@PathVariable("userId") Long uid)
    {
                System.out.println(uid);
        this.userService.deleteUser(uid);
    }

}

