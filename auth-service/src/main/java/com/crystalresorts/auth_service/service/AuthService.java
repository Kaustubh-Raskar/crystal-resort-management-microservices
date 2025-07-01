package com.crystalresorts.auth_service.service;

import com.crystalresorts.auth_service.dto.JwtRequest;
import com.crystalresorts.auth_service.dto.JwtResponse;
import com.crystalresorts.auth_service.dto.UserDto;

public interface AuthService {

    JwtResponse login(JwtRequest request);
    UserDto register(UserDto userDto);
}
