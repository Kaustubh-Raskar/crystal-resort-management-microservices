package com.crystalresorts.auth_service.service;

import com.crystalresorts.auth_service.dto.JwtRequest;
import com.crystalresorts.auth_service.dto.JwtResponse;
import com.crystalresorts.auth_service.dto.UserDto;
/**
 * @author Kaustubh Raskar
 * Created: July 2025
 */
public interface AuthService {

    JwtResponse login(JwtRequest request);
    UserDto register(UserDto userDto);
    JwtResponse refreshToken(String refreshToken);
    void logout(String accessToken, String refreshToken);
    void promoteToAdmin(String username);
}
