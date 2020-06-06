/*drop database utnphones;*/
-- MySQL Script generated by MySQL Workbench
-- Wed May 13 16:29:29 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering
SET GLOBAL TIME_ZONE= '-3:00'; -- zona horaria BsAs/Arg
SET GLOBAL event_scheduler = ON; -- Necesario para crear eventos automaticos.
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema utnphones
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `utnphonesSchema` DEFAULT CHARACTER SET utf8 ;
create database if not exists utnphones;
USE utnphones;

-- -----------------------------------------------------
-- Table `provinces`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `provinces` (
  `id_province` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NULL,
  PRIMARY KEY (`id_province`))
ENGINE = InnoDB;

INSERT INTO `provinces` (name) VALUES ('Buenos Aires'), ('Cordoba');
-- -----------------------------------------------------
-- Table `cities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cities` (
 `id_city` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45),
  `prefix` VARCHAR(5),
  `id_province` INT,
  PRIMARY KEY (`id_city`),
  CONSTRAINT `fk_id_province`
    FOREIGN KEY (`id_province`)
    REFERENCES `provinces` (`id_province`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO cities (name, prefix, id_province) VALUES ('Mar del Plata', '223', 1),('Rio Cuarto', '358', 2);

-- -----------------------------------------------------
-- Table `tariffs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `tariffs` (
  `id_tariff` INT NOT NULL AUTO_INCREMENT,
  `id_origin_city` INT NULL,
  `id_destiny_city` INT NULL,
  `price` DOUBLE NULL,
  `cost` DOUBLE NULL,
  PRIMARY KEY (`id_tariff`),
  CONSTRAINT `fk_id_origin_city`
    FOREIGN KEY (`id_origin_city`)
    REFERENCES `cities` (`id_city`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_id_destiny_city`
    FOREIGN KEY (`id_destiny_city`)
    REFERENCES `cities` (`id_city`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO `tariffs` (id_origin_city, id_destiny_city, price, cost) VALUES (1,2,0.1,0.3), (2,1,0.1,0.3), (1,1,0.05,0.15);
-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `lastname` VARCHAR(45) NULL,
  `dni` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `id_city` INT NULL,
  `type` ENUM('client', 'employee') NOT NULL,
  PRIMARY KEY (`id_user`),
  CONSTRAINT `fk_id_city`
    FOREIGN KEY (`id_city`)
    REFERENCES `cities` (`id_city`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO `users` (name, lastname, dni, email, password, id_city, type) VALUES ('Juan', 'Perez', '12345678', 'juan@gmail.com','psw',1,'client'),
('Martin', 'Lopez','12345678', 'martin@gmail.com','psw',2,'client'),
('Osvaldo', 'Larreta', '11990555', 'osvaldo@gmail.com','123',1,'client');
-- -----------------------------------------------------
-- Table `phone_lines`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `phone_lines` (
  `id_phone_line` INT NOT NULL AUTO_INCREMENT,
  `line_number` VARCHAR(15) NULL,
  `id_user` INT NULL,
  `id_city` INT NULL,
  `type` ENUM('mobile', 'resindential') NOT NULL,
  `status` ENUM('active', 'disabled') NOT NULL,
  PRIMARY KEY (`id_phone_line`),
  CONSTRAINT `fk_id_city_pl`
    FOREIGN KEY (`id_city`)
    REFERENCES `cities` (`id_city`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_id_user`
    FOREIGN KEY (`id_user`)
    REFERENCES `users` (`id_user`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO `phone_lines` (line_number, id_user, id_city, type, status) VALUES ('2234545456', 1, 1, 'mobile','active'),
('3582545456', 2, 2, 'mobile','active'),('2234475475', 3, 1, 'mobile','active');
-- -----------------------------------------------------
-- Table `invoices`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `invoices` (
  `id_invoice` INT NOT NULL AUTO_INCREMENT,
  `number_of_calls` INT NULL,
  `price_cost` DOUBLE NULL,
  `total_price` DOUBLE NULL,
  `invoice_date` DATE NULL,
  `expiration_date` DATE NULL,
  `id_phone_line` INT NULL,
  `paid` BOOLEAN NULL,
  PRIMARY KEY (`id_invoice`),
  CONSTRAINT `fk_id_phone_line`
    FOREIGN KEY (`id_phone_line`)
    REFERENCES `phone_lines` (`id_phone_line`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

insert into invoices (number_of_calls,price_cost,total_price,invoice_date,expiration_date,id_phone_line,paid) values (4,53.25,106.5,"2020-01-06","2020-01-13",1,false);

-- -----------------------------------------------------
-- Table `calls`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `calls` (
  `id_call` INT NOT NULL AUTO_INCREMENT,
  `duration` INT NULL,
  `id_invoice` INT NULL,
  `origin_number` VARCHAR(15) NULL,
  `destiny_number` VARCHAR(15) NULL,
  `tariff_price` DOUBLE NULL,
  `call_date` DATETIME NULL,
  PRIMARY KEY (`id_call`),
  CONSTRAINT `fk_id_bill`
    FOREIGN KEY (`id_invoice`)
    REFERENCES `invoices` (`id_invoice`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO `calls` (duration, id_invoice, origin_number, destiny_number, tariff_price, call_date) VALUES
(60,1,'2234545456','3582545456',0.3,"2020-01-01 10:10:10"),
(120,1,'2234545456','3582545456',0.3,"2020-01-02 10:10:10"),
(160,1,'2234545456','3582545456',0.3,"2020-01-03 10:10:10"),
(30,2,'3582545456','2234545456',0.3,"2020-01-04 10:10:10"),
(30,1,'2234545456','2234475475',0.15,"2020-01-05 10:10:10");

