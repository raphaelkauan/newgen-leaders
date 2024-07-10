package com.newgenleaders.modules.role.controller;

import com.newgenleaders.modules.role.dto.RoleRequestDto;
import com.newgenleaders.modules.role.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/role")
    @PreAuthorize("hasAuthority('SCOPE_adminSUPER')")
    public ResponseEntity<?> createRole(@RequestBody @Valid RoleRequestDto roleRequestDto,JwtAuthenticationToken jwtAuthenticationToken) {
        return roleService.createRole(roleRequestDto, jwtAuthenticationToken);
    }
}
