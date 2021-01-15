CREATE SCHEMA `taxi` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `taxi`.`manufacturer`
(
    `manufacturer_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
    `manufacturer_name` VARCHAR(45) NOT NULL,
    `manufacturer_country` VARCHAR(45) NOT NULL,
    `manufacturer_deleted` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`manufacturer_id`)
);
