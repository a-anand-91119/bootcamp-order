package dev.notyouraverage.bootcamp_order.kickoff.controllers;

import dev.notyouraverage.base.dtos.response.wrapper.ResponseWrapper;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.UserDTO;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.request.CreateUserRequest;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.request.UpdateUserRequest;
import dev.notyouraverage.bootcamp_order.kickoff.services.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<UserDTO>> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(userService.createUser(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<UserDTO>> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(ResponseWrapper.success(userService.getUserById(id)));
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<UserDTO>>> getAllUsers() {
        return ResponseEntity.ok(ResponseWrapper.success(userService.getAllUsers()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<UserDTO>> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(ResponseWrapper.success(userService.updateUser(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ResponseWrapper.success(null));
    }
}
