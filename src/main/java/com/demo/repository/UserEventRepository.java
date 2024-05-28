package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.model.UserEvent;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    UserEvent findFirstByOrderByLoginTimeDesc();
    // You can add more custom query methods if needed
}
