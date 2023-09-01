package com.kanini.corebanking.custonboard.api.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;

/**
 * CustomerRequest
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-09-01T16:34:22.332638700+05:30[Asia/Calcutta]")

public class CustomerRequest  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("firstName")
  private String firstName;

  @JsonProperty("middleName")
  private String middleName;

  @JsonProperty("lastName")
  private String lastName;

  @JsonProperty("dob")
  private LocalDate dob;

  @JsonProperty("aadharNo")
  private String aadharNo;

  public CustomerRequest firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
  */
  @ApiModelProperty(value = "")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public CustomerRequest middleName(String middleName) {
    this.middleName = middleName;
    return this;
  }

  /**
   * Get middleName
   * @return middleName
  */
  @ApiModelProperty(value = "")
  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public CustomerRequest lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
  */
  @ApiModelProperty(value = "")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public CustomerRequest dob(LocalDate dob) {
    this.dob = dob;
    return this;
  }

  /**
   * Date of Birth
   * @return dob
  */
  @ApiModelProperty(example = "Sat Jan 30 05:30:00 IST 2021", value = "Date of Birth")
  public LocalDate getDob() {
    return dob;
  }

  public void setDob(LocalDate dob) {
    this.dob = dob;
  }

  public CustomerRequest aadharNo(String aadharNo) {
    this.aadharNo = aadharNo;
    return this;
  }

  /**
   * Get aadharNo
   * @return aadharNo
  */
  @ApiModelProperty(value = "")
  public String getAadharNo() {
    return aadharNo;
  }

  public void setAadharNo(String aadharNo) {
    this.aadharNo = aadharNo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomerRequest customerRequest = (CustomerRequest) o;
    return Objects.equals(this.firstName, customerRequest.firstName) &&
        Objects.equals(this.middleName, customerRequest.middleName) &&
        Objects.equals(this.lastName, customerRequest.lastName) &&
        Objects.equals(this.dob, customerRequest.dob) &&
        Objects.equals(this.aadharNo, customerRequest.aadharNo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, middleName, lastName, dob, aadharNo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomerRequest {\n");
    
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    dob: ").append(toIndentedString(dob)).append("\n");
    sb.append("    aadharNo: ").append(toIndentedString(aadharNo)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

