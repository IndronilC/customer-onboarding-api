package com.kanini.corebanking.custonboard.customeronboarding.data.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"customerEntity"})
@Builder
@Table(name="customer_identification")
public class CustomerIdentificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="proof_id", nullable = false)
    private UUID proofId;
    @Column(name="aadhar_no", nullable = false)
    private BigInteger aadharNo;
    @Column(name="created_At", nullable = false)
    private OffsetDateTime createdAt;
    @Column(name="updated_At", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToOne(mappedBy = "customerIdentificationEntity")
    private CustomerEntity customerEntity;



}
