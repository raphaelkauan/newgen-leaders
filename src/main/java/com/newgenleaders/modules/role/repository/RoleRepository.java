package com.newgenleaders.modules.role.repository;

import com.newgenleaders.modules.role.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String name);
    boolean existsByName(String name);
}
