DROP SEQUENCE IF EXISTS customers_id_sequence;
DROP TABLE IF EXISTS customers;
CREATE SEQUENCE customers_id_sequence
    INCREMENT 1
    START WITH 1
    MINVALUE 1
;
CREATE table customers (id INT DEFAULT nextval('customers_id_sequence') NOT NULL, name VARCHAR, age INT NOT NULL);
