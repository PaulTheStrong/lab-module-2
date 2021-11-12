package com.epam.esm.repository.api;

import com.epam.esm.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findRoleByName(String name);

}
