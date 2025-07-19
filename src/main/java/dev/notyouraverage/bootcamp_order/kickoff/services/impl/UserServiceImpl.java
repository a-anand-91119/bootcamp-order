package dev.notyouraverage.bootcamp_order.kickoff.services.impl;

import dev.notyouraverage.bootcamp_order.kickoff.repositories.UserRepository;
import dev.notyouraverage.bootcamp_order.kickoff.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
}
