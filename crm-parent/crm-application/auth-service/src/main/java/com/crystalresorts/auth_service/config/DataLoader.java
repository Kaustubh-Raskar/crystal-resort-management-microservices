package com.crystalresorts.auth_service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.crystalresorts.auth_service.entity.Role;
import com.crystalresorts.auth_service.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author Kaustubh Raskar
 * Created: July 2025
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner{

    private final RoleRepository roleRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByRoleName("USER").isEmpty()) {
            Role guestRole = new Role();
            guestRole.setRoleName("USER");
            roleRepository.save(guestRole);
        }
    }


}
