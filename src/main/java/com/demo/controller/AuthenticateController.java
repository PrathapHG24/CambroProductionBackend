package com.demo.controller;


import com.demo.config.javaUtils;
import com.demo.model.JWTRequest;
import com.demo.model.JWTResponse;
import com.demo.service.UserEventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin("*")
public class AuthenticateController {


    @Autowired
    private AuthenticationManager authenticationManager;
   
    @Autowired
    private UserEventService userEventService; // Inject UserEventService

    @Autowired
    private javaUtils jwtutils;

    @Autowired
    private com.demo.service.userDetailServiceImpl userDetailServiceImpl;


    
    @PostMapping("/generate-token")
    public ResponseEntity<?> generateToken(@RequestBody JWTRequest jwtRequest) throws Exception {
        try {
            authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());

            // Get user details
            UserDetails userDetails = this.userDetailServiceImpl.loadUserByUsername(jwtRequest.getUsername());

            // Check if user does not have ADMIN role, then save login event
            if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                userEventService.saveLoginEvent(jwtRequest.getUsername());
            }

            // Generate token
            String token = this.jwtutils.generateToken(userDetails);
            return ResponseEntity.ok(new JWTResponse(token));
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("User disabled");
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid Credentials" + e.getMessage());
        }
    }


    @GetMapping("/current-user")
    public UserDetails getCurrentUser(Principal principal)
    {
        return this.userDetailServiceImpl.loadUserByUsername(principal.getName());
    }

}

