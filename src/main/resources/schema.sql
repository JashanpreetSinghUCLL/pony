DROP TABLE IF EXISTS stables;
DROP TABLE IF EXISTS animals;

CREATE TABLE stables (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    max_number_of_animals INT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE animals (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    stable_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (stable_id) references stables(id)
);

