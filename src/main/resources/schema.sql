DROP TABLE IF EXISTS toys;
DROP TABLE IF EXISTS addresses;
DROP TABLE IF EXISTS stables;
DROP TABLE IF EXISTS animals;
DROP TABLE IF EXISTS animals_toys;
DROP TABLE IF EXISTS medical_records;

CREATE TABLE toys (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE addresses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(500) NOT NULL,
    number INT NOT NULL,
    place VARCHAR(500) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE stables (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    max_number_of_animals INT NOT NULL,
    owner VARCHAR(255) NOT NULL,
    address_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (address_id) REFERENCES addresses(id)
);

CREATE TABLE animals (
    id BIGINT NOT NULL AUTO_INCREMENT,
    animal_type VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    lays_eggs BOOLEAN DEFAULT FALSE,
    stable_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (stable_id) references stables(id)
);

CREATE TABLE animals_toys (
    animal_id BIGINT,
    toy_id BIGINT,
    PRIMARY KEY (animal_id, toy_id),
    FOREIGN KEY (animal_id) references animals(id),
    FOREIGN KEY (toy_id) references toys(id)
);

CREATE TABLE medical_records (
    id BIGINT NOT NULL AUTO_INCREMENT,
    registrationDate DATE NOT NULL,
    closingDate DATE,
    description VARCHAR(1000) NOT NULL,
    animal_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (animal_id) REFERENCES animals(id)
);

