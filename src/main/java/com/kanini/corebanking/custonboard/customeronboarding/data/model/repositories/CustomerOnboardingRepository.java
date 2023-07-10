package com.kanini.corebanking.custonboard.customeronboarding.data.model.repositories;

import com.kanini.corebanking.custonboard.customeronboarding.data.model.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerOnboardingRepository extends JpaRepository<CustomerEntity, UUID> {
}
