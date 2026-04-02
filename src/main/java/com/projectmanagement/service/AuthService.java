package com.projectmanagement.service;

import com.projectmanagement.dto.request.LoginRequest;
import com.projectmanagement.dto.request.RegisterRequest;
import com.projectmanagement.dto.response.AuthResponse;
import com.projectmanagement.entity.User;
import com.projectmanagement.enums.Role;
import com.projectmanagement.repository.UserRepository;
import com.projectmanagement.security.JwtUtils;
import com.projectmanagement.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

//    public AuthResponse register(RegisterRequest request) {
//        // Check if email already exists
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("Email already in use!");
//        }
//
//        // Create new user
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole(Role.MEMBER); // Default role
//
//        User savedUser = userRepository.save(user);
//
//        // Generate token
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//        );
//
//        String jwt = jwtUtils.generateJwtToken(authentication);
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//        return new AuthResponse(jwt, userDetails.getId(), userDetails.getName(),
//                userDetails.getEmail(), userDetails.getAuthorities().toString());
//    }

    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use!");
        }

        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Make first user ADMIN, rest MEMBER
        if (userRepository.count() == 0) {
            user.setRole(Role.ADMIN);  // First user is ADMIN
        } else {
            user.setRole(Role.MEMBER);  // Subsequent users are MEMBER
        }

        User savedUser = userRepository.save(user);

        // Generate token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new AuthResponse(jwt, userDetails.getId(), userDetails.getName(),
                userDetails.getEmail(), userDetails.getAuthorities().toString());
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new AuthResponse(jwt, userDetails.getId(), userDetails.getName(),
                userDetails.getEmail(), userDetails.getAuthorities().toString());
    }
}