package com.kanini.corebanking.custonboard.customeronboarding.data.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"customerIdentificationEntity"})
@Table(name="customer")
public class CustomerEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name="first_name", nullable = false)
    private String firstName;
    @Column(name="middle_name", nullable = false)
    private String middleName;
    @Column(name="last_name", nullable = false)
    private String lastName;
    @Column(name="date_of_birth", nullable = false)
    private LocalDate dob;
    @Column(name="created_At", nullable = false)
    private OffsetDateTime createdAt;
    @Column(name="updated_At", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_proof_id", referencedColumnName = "proof_id")
    private CustomerIdentificationEntity customerIdentificationEntity;


}
