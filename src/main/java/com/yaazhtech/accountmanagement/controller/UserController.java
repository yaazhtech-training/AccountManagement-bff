//package com.yaazhtech.accountmanagement.controller;
//
//import com.yaazhtech.accountmanagement.data.User;
//import com.yaazhtech.accountmanagement.model.request.UserRequest;
//import com.yaazhtech.accountmanagement.model.response.ApiResponse;
//import com.yaazhtech.accountmanagement.service.UserService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class UserController {
//    @Autowired
//    private UserService userService;
//
//    /**
//     * Create a new user
//     */
//    @PostMapping("/create")
//    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserRequest userRequest) throws IOException {
//        User createdUser = userService.createUser(userRequest);
//        ApiResponse response = new ApiResponse("User created successfully", createdUser);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    /**
//     * Get all users
//     */
//    @GetMapping
//    public ResponseEntity<ApiResponse> getAllUsers() {
//        Iterable<User> users = userService.getAllUsers();
//        return ResponseEntity.ok(new ApiResponse("All users fetched", (User) users));
//    }
//
//    /**
//     * Get user by ID
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse> getUserById(@PathVariable String id) {
//        User user = userService.getUserById(id);
//        return ResponseEntity.ok(new ApiResponse("User fetched successfully", user));
//    }
//
//    /**
//     * Delete user by ID
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String id) {
//        userService.deleteUser(id);
//        return ResponseEntity.ok(new ApiResponse("User deleted successfully", null));
//    }
//}
