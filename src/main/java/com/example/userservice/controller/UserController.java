package com.example.userservice.controller;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<EntityModel<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse user = userService.createUser(userRequest);

        EntityModel<UserResponse> resource = EntityModel.of(user);
        resource.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).updateUser(user.getId(), userRequest)).withRel("update"));
        resource.add(linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete"));
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<EntityModel<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);

        EntityModel<UserResponse> resource = EntityModel.of(user);
        resource.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update"));
        resource.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
        resource.add(linkTo(methodOn(UserController.class).createUser(null)).withRel("create-user"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<CollectionModel<EntityModel<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();

        List<EntityModel<UserResponse>> userResources = users.stream()
                .map(user -> {
                    EntityModel<UserResponse> resource = EntityModel.of(user);
                    resource.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
                    return resource;
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UserResponse>> resources = CollectionModel.of(userResources);
        resources.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        resources.add(linkTo(methodOn(UserController.class).createUser(null)).withRel("create-user"));

        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by ID")
    public ResponseEntity<EntityModel<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {
        UserResponse user = userService.updateUser(id, userRequest);

        EntityModel<UserResponse> resource = EntityModel.of(user);
        resource.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).updateUser(id, userRequest)).withRel("update"));
        resource.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));

        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}