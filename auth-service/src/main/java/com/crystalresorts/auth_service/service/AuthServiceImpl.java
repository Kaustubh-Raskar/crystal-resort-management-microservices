package com.crystalresorts.auth_service.service;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.crystalresorts.auth_service.dto.JwtRequest;
import com.crystalresorts.auth_service.dto.JwtResponse;
import com.crystalresorts.auth_service.dto.UserDto;
import com.crystalresorts.auth_service.entity.Role;
import com.crystalresorts.auth_service.entity.UserEntity;
import com.crystalresorts.auth_service.repository.RoleRepository;
import com.crystalresorts.auth_service.repository.UserRepository;
import com.crystalresorts.auth_service.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Override
    public JwtResponse login(JwtRequest request) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
                                  .orElseThrow(() -> new RuntimeException("User not found"));
             
        if(!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())){
            throw new RuntimeException("Invalid Credentials");
        }                          
        Set<String> roleNames = userEntity.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        String token = jwtUtil.generateToken(userEntity.getUsername(), roleNames);

        return JwtResponse.builder()
                          .username(userEntity.getUsername())
                          .fullName(userEntity.getFullName())
                          .token(token)
                          .roles(roleNames)
                          .build();
    }


    @Override
    public UserDto register(UserDto userDto) {

        if(userRepository.findByUsername(userDto.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }

        Role defaultRole = roleRepository.findByRoleName("ROLE_GUEST")
                                         .orElseThrow(() -> new RuntimeException("Default role not found."));

        UserEntity user = UserEntity.builder()
                              .username(userDto.getUsername())
                              .fullName(userDto.getFullName())
                              .password(passwordEncoder.encode(userDto.getPassword()))
                              .enabled(true)
                              .roles(Set.of(defaultRole))
                              .build();

        userRepository.save(user);

        return UserDto.builder()
                      .username(user.getUsername())
                      .fullName(user.getFullName())
                      .roleNames(Set.of(defaultRole.getRoleName()))
                      .build();
    }

}
