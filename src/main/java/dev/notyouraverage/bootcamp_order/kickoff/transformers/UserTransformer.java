package dev.notyouraverage.bootcamp_order.kickoff.transformers;

import dev.notyouraverage.base.annotations.Transformer;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.UserDTO;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.request.CreateUserRequest;
import dev.notyouraverage.bootcamp_order.kickoff.dtos.request.UpdateUserRequest;
import dev.notyouraverage.bootcamp_order.kickoff.models.postgres.User;
import java.util.List;

@Transformer
public class UserTransformer {

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .toList();
    }

    public User toEntity(CreateUserRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();
    }

    public User updateEntity(User existingUser, UpdateUserRequest request) {
        if (request.getFirstName() != null) {
            existingUser.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            existingUser.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            existingUser.setEmail(request.getEmail());
        }
        return existingUser;
    }
}
