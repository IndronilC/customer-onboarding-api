ALTER TABLE Customer DROP FOREIGN KEY customer_ibfk_1;
ALTER TABLE customer_identification MODIFY COLUMN proof_id binary(16) NOT NULL;
ALTER TABLE Customer MODIFY COLUMN customer_proof_id binary(16) NOT NULL;
ALTER TABLE Customer ADD FOREIGN KEY(customer_proof_id) REFERENCES customer_identification(proof_id);
ALTER TABLE Customer DROP FOREIGN KEY customer_ibfk_1;