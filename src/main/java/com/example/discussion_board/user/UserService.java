package com.example.discussion_board.user;

import com.example.discussion_board.user.dto.UserDto;
import com.example.discussion_board.user.entity.CustomUserDetails;
import com.example.discussion_board.user.entity.UserEntity;
import com.example.discussion_board.user.entity.UserPermission;
import com.example.discussion_board.user.entity.UserRole;
import com.example.discussion_board.user.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;

        createDefaultUsers();
    }

    private void createDefaultUsers() {
        createUserIfNotExists(
                UserDto.builder()
                        .username("user1")
                        .password(passwordEncoder.encode("12345"))
                        .email("user1@gmail.com")
                        .phone("010-567-4578")
                        .role(UserRole.ROLE_USER.name())
                        .build()
        );

        createUserIfNotExists(
                UserDto.builder()
                        .username("author1")
                        .password(passwordEncoder.encode("12345"))
                        .email("author1@gmail.com")
                        .phone("010-567-4555")
                        .role(UserRole.ROLE_AUTHOR.name())
                        .build()
        );

        createUserIfNotExists(
                UserDto.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("12345"))
                        .email("admin@gmail.com")
                        .phone("010-567-5679")
                        .role(UserRole.ROLE_ADMIN.name())
                        .build()
        );

        log.info("Default users created successfully");
    }

    private void createUserIfNotExists(UserDto userDto) {

        boolean userExists = repository.existsByUsername(userDto.getUsername())
                || repository.existsByEmail(userDto.getEmail());

        if (!userExists) {
            createUserFromDto(userDto);
        } else {
            log.info("User with username {} or email {} already exists", userDto.getUsername(), userDto.getEmail());
        }
    }

    private void createUserFromDto(UserDto dto) {
        UserEntity userEntity = UserEntity.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .profileImagePath(dto.getProfileImagePath())
                .role(UserRole.valueOf(dto.getRole())) // covert from String to enum type
                .build();
        repository.save(userEntity);
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        log.info("loadUserByUsername in UserService by me!");
        Optional<UserEntity> optionalUser = repository.findByUsername(username);
        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException("User not found with username: " + username);
        UserEntity user = optionalUser.get();
        UserRole userRole = user.getRole();

        // Convert UserRole to roles array
        String[] roles = userRole.getPermissions().stream()
                .map(UserPermission::name)
                .toArray(String[]::new);


        return User.builder()
                .username(username)
                .password(user.getPassword())
                .roles(roles)
                .build();
    }

    // CREATE USER
    public void createUser(
            String username,
            String password,
            String passCheck
    ) {
        if (repository.existsByUsername(username) || !password.equals(passCheck))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        UserDto newUser = UserDto.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        createUserFromDto(newUser);
    }
}
