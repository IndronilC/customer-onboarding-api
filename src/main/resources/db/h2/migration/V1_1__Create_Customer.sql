DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
  id bigint NOT NULL AUTO_INCREMENT,
   first_name varchar(255) NOT NULL,
   last_name varchar(255) NOT NULL,
   aadhar_no bigint NOT NULL,
   date_of_birth date NOT NULL,
   created_At timestamp NOT NULL,
   updated_At varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);