package com.demo.repository;

import com.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface roleRepository extends JpaRepository<Role,Long> {

}