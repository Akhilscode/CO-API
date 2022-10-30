package com.coservice.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coservice.entity.CoTrigger;

public interface CoTriggerRepository extends JpaRepository<CoTrigger, Serializable> {
     
}
