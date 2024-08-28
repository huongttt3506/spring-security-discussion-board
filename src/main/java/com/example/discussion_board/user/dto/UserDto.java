package com.example.discussion_board.user.dto;

import com.example.discussion_board.user.entity.UserEntity;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String username;
    private String password;
    private String nickname;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String profileImagePath;

    public static UserDto fromEntity(UserEntity entity) {
        return new UserDto(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getNickname(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddress(),
                entity.getRole().name(),
                entity.getProfileImagePath()
        );
    }
}
