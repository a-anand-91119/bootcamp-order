package dev.notyouraverage.bootcamp_order.kickoff.services.impl;

import dev.notyouraverage.base.dtos.response.wrapper.ErrorResponse;
import dev.notyouraverage.base.exceptions.RestException;
import dev.notyouraverage.bootcamp_order.enums.AppErrorCode;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.UserDTO;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.request.CreateUserRequest;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.request.UpdateUserRequest;
import dev.notyouraverage.bootcamp_order.kickoff.models.postgres.User;
import dev.notyouraverage.bootcamp_order.kickoff.repositories.UserRepository;
import dev.notyouraverage.bootcamp_order.kickoff.services.UserService;
import dev.notyouraverage.bootcamp_order.kickoff.transformers.UserTransformer;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserTransformer userTransformer;

    @Override
    public UserDTO createUser(CreateUserRequest request) {
        User user = userTransformer.toEntity(request);
        return userTransformer.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUserById(String id) {
        return userRepository.findById(UUID.fromString(id))
                .map(userTransformer::toDTO)
                .orElseThrow(
                        () -> new RestException(HttpStatus.NOT_FOUND, ErrorResponse.from(AppErrorCode.USER_NOT_FOUND))
                );
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userTransformer.toDTOList(users);
    }

    @Override
    public UserDTO updateUser(String id, UpdateUserRequest request) {
        UUID uuid = UUID.fromString(id);
        User existingUser = userRepository.findById(uuid)
                .orElseThrow(
                        () -> new RestException(HttpStatus.NOT_FOUND, ErrorResponse.from(AppErrorCode.USER_NOT_FOUND))
                );

        User updatedUser = userTransformer.updateEntity(existingUser, request);
        User savedUser = userRepository.save(updatedUser);
        return userTransformer.toDTO(savedUser);
    }

    @Override
    public void deleteUser(String id) {
        UUID uuid = UUID.fromString(id);
        if (!userRepository.existsById(uuid)) {
            throw new RestException(HttpStatus.NOT_FOUND, ErrorResponse.from(AppErrorCode.USER_NOT_FOUND));
        }
        userRepository.deleteById(uuid);
    }
}
