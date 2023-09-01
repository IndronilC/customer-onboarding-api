ALTER TABLE Customer DROP COLUMN aadhar_no;

DROP TABLE IF EXISTS `customer_identification`;

CREATE TABLE `customer_identification` (
    proof_id bigint NOT NULL AUTO_INCREMENT,
   aadhar_no bigint NOT NULL,
   created_At timestamp NOT NULL,
   updated_At timestamp NOT NULL,
  PRIMARY KEY (`proof_id`)
);

ALTER TABLE Customer ALTER COLUMN updated_At timestamp NOT NULL;
ALTER TABLE Customer ADD COLUMN customer_proof_id bigint;

ALTER TABLE Customer ADD CONSTRAINT FK_CUSTOMER_PROOF_ID FOREIGN KEY(customer_proof_id) REFERENCES customer_identification(proof_id);