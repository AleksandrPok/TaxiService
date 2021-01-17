CREATE SCHEMA `taxi` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `taxi`.`manufacturer` (
    `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    `country` VARCHAR(45) NOT NULL,
    `deleted` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);
