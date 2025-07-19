package dev.notyouraverage.bootcamp_order.kickoff.services;

import dev.notyouraverage.bootcamp_order.kickoff.dtos.UserDTO;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.request.CreateUserRequest;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.request.UpdateUserRequest;
import java.util.List;

public interface UserService {

    UserDTO createUser(CreateUserRequest request);

    UserDTO getUserById(String id);

    List<UserDTO> getAllUsers();

    UserDTO updateUser(String id, UpdateUserRequest request);

    void deleteUser(String id);
}
