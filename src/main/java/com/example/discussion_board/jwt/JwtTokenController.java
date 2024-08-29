package com.example.discussion_board.jwt;

import com.example.discussion_board.jwt.dto.JwtRequestDto;
import com.example.discussion_board.jwt.dto.JwtResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping ("/auth")
public class JwtTokenController {
    private final JwtTokenUtil tokenUtil;
    private final UserDetailsService userService;
    private final PasswordEncoder passwordEncoder;

    public JwtTokenController(
            JwtTokenUtil tokenUtil,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        this.tokenUtil = tokenUtil;
        this.userService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/issue")
    public JwtResponseDto createAuthenticationToken(
            @RequestBody
            JwtRequestDto requestDto
    ){
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(requestDto.getUsername());
        } catch (UsernameNotFoundException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!passwordEncoder.matches(requestDto.getPassword(), userDetails.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        String jwt = tokenUtil.generateToken(userDetails);
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        return response;
    }

}

