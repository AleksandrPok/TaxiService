CREATE SCHEMA `taxi` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `taxi`.`manufacturer` (
    `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    `country` VARCHAR(45) NOT NULL,
    `deleted` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);

CREATE TABLE `taxi`.`drivers` (
    `id` BIGINT(11) NOT NULL,
    `name` VARCHAR(45) NOT NULL,
    `license_number` VARCHAR(45) NOT NULL,
    `login` VARCHAR(45) NOT NULL,
     `password` VARCHAR(45) NOT NULL,
     `deleted` TINYINT NOT NULL DEFAULT 0,
     PRIMARY KEY (`id`),
     UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE
);

CREATE TABLE `taxi`.`cars` (
    `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
    `manufacturer_id` BIGINT(11) NOT NULL,
    `model` VARCHAR(45) NOT NULL,
    `deleted` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    INDEX `manufacturer_id_idx` (`manufacturer_id` ASC) VISIBLE,
    CONSTRAINT `manufacturer_id_fk`
        FOREIGN KEY (`manufacturer_id`)
            REFERENCES `taxi`.`manufacturer` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE TABLE `taxi`.`cars_drivers` (
    `car_id` BIGINT(11) NOT NULL,
    `driver_id` BIGINT(11) NOT NULL,
    INDEX `car_id_fk_idx` (`car_id` ASC) VISIBLE,
    INDEX `driver_id_fk_idx` (`driver_id` ASC) VISIBLE,
    CONSTRAINT `car_id_fk`
        FOREIGN KEY (`car_id`)
            REFERENCES `taxi`.`cars` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `driver_id_fk`
        FOREIGN KEY (`driver_id`)
            REFERENCES `taxi`.`drivers` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

