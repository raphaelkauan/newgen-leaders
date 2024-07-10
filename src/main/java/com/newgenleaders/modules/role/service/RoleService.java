package com.newgenleaders.modules.role.service;

import com.newgenleaders.common.exception.RoleConflictException;
import com.newgenleaders.common.exception.UserConflictException;
import com.newgenleaders.modules.role.dto.RoleRequestDto;
import com.newgenleaders.modules.role.entity.RoleEntity;
import com.newgenleaders.modules.role.repository.RoleRepository;
import com.newgenleaders.modules.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<?> createRole(@RequestBody @Valid RoleRequestDto roleRequestDto, JwtAuthenticationToken jwtAuthenticationToken) {
        if(roleRepository.existsByName(roleRequestDto.name())) {
            throw new RoleConflictException("Essa role já existe!");
        }

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(roleRequestDto.name());

        roleRepository.save(roleEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(roleEntity.getName() + " Cadastrado com sucesso!");
    }
}
