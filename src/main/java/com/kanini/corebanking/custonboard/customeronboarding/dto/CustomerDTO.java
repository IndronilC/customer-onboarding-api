package com.kanini.corebanking.custonboard.customeronboarding.dto;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CustomerDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dob;
    private String aadharNo;

}
