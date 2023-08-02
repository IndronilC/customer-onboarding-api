package com.kanini.corebanking.custonboard.customeronboarding.data.model.repositories;

import com.kanini.corebanking.custonboard.customeronboarding.data.model.entities.CustomerIdentificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerIdentificationRepository extends JpaRepository<CustomerIdentificationEntity, UUID> {
}
