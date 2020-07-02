-- UTNPHONES DATABASE --

-- INIT --
drop database if exists utnphones;
create database utnphones;
use utnphones;
SET GLOBAL TIME_ZONE= '-3:00'; -- zona horaria BsAs/Arg
SET GLOBAL event_scheduler = ON; -- Necesario para crear eventos automaticos.
SET SQL_SAFE_UPDATES = 0;

-- TABLE GENERATOR --
CREATE TABLE IF NOT EXISTS `provinces` (
  `id_province` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) UNIQUE NULL,
  PRIMARY KEY (`id_province`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `cities` (
 `id_city` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) UNIQUE,
  `prefix` VARCHAR(5) UNIQUE,
  `id_province` INT,
  PRIMARY KEY (`id_city`),
  CONSTRAINT `fk_id_province`
    FOREIGN KEY (`id_province`)
    REFERENCES `provinces` (`id_province`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

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

CREATE TABLE IF NOT EXISTS `users` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `lastname` VARCHAR(45) NULL,
  `dni` VARCHAR(45) UNIQUE NOT NULL,
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `id_city` INT NULL,
  `type` ENUM('client', 'employee', 'infrastructure') NOT NULL,
  `status` ENUM('active', 'disabled') NOT NULL default 'active',
  PRIMARY KEY (`id_user`),
  CONSTRAINT `fk_id_city`
    FOREIGN KEY (`id_city`)
    REFERENCES `cities` (`id_city`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `phone_lines` (
  `id_phone_line` INT NOT NULL AUTO_INCREMENT,
  `line_number` VARCHAR(15) NULL,
  `id_user` INT NULL,
  `id_city` INT NULL,
  `type` ENUM('mobile', 'resindential') NOT NULL,
  `status` ENUM('active', 'disabled', 'suspended') NOT NULL,
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

CREATE TABLE IF NOT EXISTS `invoices` (
  `id_invoice` INT NULL AUTO_INCREMENT,
  `number_of_calls` INT NULL,
  `price_cost` DOUBLE NULL,
  `total_price` DOUBLE NULL,
  `invoice_date` DATE NULL,
  `expiration_date` DATE NULL,
  `id_phone_line` INT NULL,
  `paid` BOOLEAN NOT NULL,
  PRIMARY KEY (`id_invoice`),
  CONSTRAINT `fk_id_phone_line`
    FOREIGN KEY (`id_phone_line`)
    REFERENCES `phone_lines` (`id_phone_line`)
    ON DELETE SET NULL
    ON UPDATE SET NULL)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `calls` (
  `id_call` INT NOT NULL AUTO_INCREMENT,
  `duration` INT NULL,
  `id_invoice` INT NULL,
  `origin_number` VARCHAR(15) NULL,
  `destiny_number` VARCHAR(15) NULL,
  `tariff_price` DOUBLE NULL,
  `tariff_cost` DOUBLE NULL,
  `total_price` DOUBLE NULL,
  `call_date` DATETIME NULL,
  PRIMARY KEY (`id_call`),
  CONSTRAINT `fk_id_invoice`
    FOREIGN KEY (`id_invoice`)
    REFERENCES `invoices` (`id_invoice`)
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- ADDING SAMPLE DATA --

INSERT INTO `provinces` (name) VALUES ('Buenos Aires'),('Catamarca'),('Chaco')
,('Chubut'),('Cordoba'),('Corrientes'),('Entre Rios'),('Formosa'),('Jujuy'),('La Pampa'),('La Rioja')
,('Mendoza'),('Misiones'),('Neuquen'),('Rio Negro'),('Salta'),('San Juan'),('San Luis'),('Santa Cruz')
,('Santa Fe'),('Santiago del Estero'),('Tierra del Fuego'),('Tucuman');

INSERT INTO cities (name, prefix, id_province) VALUES ('Mar del Plata', '223', 1),('Rio Cuarto', '358', 2);

INSERT INTO `tariffs` (id_origin_city, id_destiny_city, price, cost) VALUES (1,2,0.3,0.1), (2,1,0.3,0.1), (1,1,0.15,0.05), (2,2,0.2,0.1);

INSERT INTO `users` (name, lastname, dni, email, password, id_city, type) VALUES ('Juan', 'Perez', '12345678', 'juan@gmail.com','psw',1,'client'),
('Martin', 'Lopez','1234', 'martin@gmail.com','psw',2,'client'),
('admin', 'admin','4567', 'admin@gmail.com','admin',2,'employee'),
('infrastructure', 'infrastructure','7891', 'infrastructure@gmail.com','infrastructure',1,'infrastructure'),
('Osvaldo', 'Larreta', '11990555', 'osvaldo@gmail.com','123',1,'client');

INSERT INTO `phone_lines` (line_number, id_user, id_city, type, status) VALUES ('2234545456', 1, 1, 'mobile','active'),
('3582545456', 2, 2, 'mobile','active'), ('2234444444', 1, 1, 'mobile','active');

insert into invoices (number_of_calls,price_cost,total_price,invoice_date,expiration_date,id_phone_line,paid) values (4,53.25,106.5,"2020-01-06","2020-01-13",1,false);

INSERT INTO `calls` (duration, id_invoice, origin_number, destiny_number, tariff_price, tariff_cost, total_price, call_date) VALUES
(60,1,'2234545456','3582545456',0.3,0.1,10,"2020-01-01 10:10:10"),
(120,1,'2234545456','3582545456',0.3,0.1,10,"2020-01-02 10:10:10"),
(160,1,'2234545456','3582545456',0.3,0.1,10,"2020-01-03 10:10:10"),
(30,null,'3582545456','2234545456',0.3,0.1,10,"2020-01-04 10:10:10"),
(30,null,'2234444444','2234545456',0.3,0.1,10,"2020-01-04 10:10:10"),
(30,null,'2234444444','2234545456',0.3,0.1,10,"2020-01-04 10:10:10"),
(30,null,'2234444444','2234545456',0.3,0.1,10,"2020-01-04 10:10:10"),
(30,1,'2234545456','2234475475',0.15,0.1,10,"2020-01-05 10:10:10");

-- TRIGGERS --

/*Previene invoices duplicados*/
DELIMITER $$
CREATE trigger trigger_generate_invoice before insert on invoices for each row
begin
	if exists (select * from invoices where number_of_calls = new.number_of_calls
    and total_price = new.total_price and id_phone_line = new.id_phone_line)
    then
	signal sqlstate '45000'
	set message_text = 'Error, esta factura ya fue generada';
	end if;
end$$


/*Previene calls duplicados*/
DELIMITER $$
CREATE trigger trigger_generate_call before insert on calls for each row
begin
	if exists (select * from calls where duration = new.duration
    and origin_number = new.origin_number and destiny_number = new.destiny_number
    and total_price = new.total_price and MINUTE(call_date) = MINUTE(new.call_date))
    then
	signal sqlstate '45000'
	set message_text = 'Call already created';
	end if;
end$$
-- drop trigger trigger_generate_call;

/*Previene usuarios duplicados*/
DELIMITER $$
CREATE trigger trigger_generate_user before insert on users for each row
begin
	if exists (select * from users where email = new.email)
    then
	signal sqlstate '45000'
	set message_text = 'Username (email) already exists';
	end if;
end$$

/*Previene phone_lines duplicados*/
DELIMITER $$
CREATE trigger trigger_generate_phone_line before insert on phone_lines for each row
begin
	if exists (select * from phone_lines where line_number = new.line_number
    and id_city = new.id_city )
    then
	signal sqlstate '45000'
	set message_text = 'Error, esta linea ya fue generada';
    end if;
    if (LENGTH(new.line_number) != 10 )
    then
	signal sqlstate '45000'
	set message_text = 'Error, el número de la linea no cumple los requisitos ';
    end if;
end$$

-- STORED PROCEDURES --

DELIMITER //
CREATE FUNCTION fn_id_city_from_prefix(origin_number VARCHAR(15)) RETURNS INTEGER-- working
BEGIN
declare city_id int;
select id_city into city_id from cities where origin_number
like concat(prefix,'%') order by LENGTH(prefix) DESC LIMIT 1;
return city_id;
END//

DELIMITER $$
CREATE  PROCEDURE `sp_generate_invoices`(IN `sp_phone_line` VARCHAR(20))
begin

	START TRANSACTION; -- necesaria ya que si se crea la tabla temp y ocurre algo
    -- en el medio de la procedure nunca se va a borrar y va a impedir que se vuelva a ejecutar

	/*Creo una tabla temporaria para tratar las calls*/
	create temporary table noInvoiced
	select id_call from calls c where c.origin_number= sp_phone_line and id_invoice is null;

	/*Inserto los invoices y agrupo las llamadas por la phone_line */
	insert into invoices (number_of_calls,price_cost,total_price,invoice_date,expiration_date,id_phone_line,paid)
	select count(pl.id_phone_line) as "number_of_calls",ROUND(sum(c.tariff_cost* c.duration),2),sum(c.total_price),now(), (CURDATE() + INTERVAL 15 DAY),pl.id_phone_line, false
	from calls c
		inner join noInvoiced noInv on c.id_call = noInv.id_call
		inner join phone_lines pl on c.origin_number = pl.line_number
	where pl.line_number = sp_phone_line and c.id_invoice is null
	group by pl.id_phone_line ;

	/*Updateo las calls con su respectivo invoice*/
	update calls inner join noInvoiced noInv on calls.id_call = noInv.id_call
	set calls.id_invoice = LAST_INSERT_ID();
	drop table noInvoiced;

    COMMIT;
end$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `sp_verify_calls_and_generate_invoices`()
begin

   DECLARE vFinish int DEFAULT 0;
   DECLARE phone_number VARCHAR (15);

   DECLARE phonelines_cursor CURSOR
   FOR
   SELECT line_number FROM phone_lines;
   DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinish= 1;
   OPEN phonelines_cursor;
   REPEAT
      FETCH phonelines_cursor INTO phone_number;
   		call sp_generate_invoices(phone_number);
   UNTIL vFinish END REPEAT;

   CLOSE phonelines_cursor;

end$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `sp_add_call`(p_origin_phone VARCHAR(20),
	p_destiny_phone VARCHAR(20), p_duration int)-- working
BEGIN
DECLARE origin_city_id int;
DECLARE destiny_city_id int;
DECLARE tariff_price double;
DECLARE tariff_cost double;
DECLARE total_price double;
DECLARE date_call datetime;
set origin_city_id= fn_id_city_from_prefix(p_origin_phone);
set destiny_city_id= fn_id_city_from_prefix(p_destiny_phone);
select price into tariff_price from tariffs where id_origin_city= origin_city_id and id_destiny_city= destiny_city_id;
select cost into tariff_cost from tariffs where id_origin_city= origin_city_id and id_destiny_city= destiny_city_id;
set total_price =  (p_duration * tariff_price);
set date_call = (select now());
	if (origin_city_id IS NULL or destiny_city_id IS NULL)
		then signal sqlstate '01000' set MESSAGE_TEXT = 'Numero/s de telefono/s incompatible/s', MYSQL_ERRNO =  1000;
    elseif (LENGTH(p_destiny_phone) != 10 )
		then signal sqlstate '01000' set MESSAGE_TEXT = 'El número de destino no cumple los requisitos', MYSQL_ERRNO =  1000;
	elseif( (select line_number from phone_lines where line_number = p_origin_phone) IS NULL )
		then signal sqlstate '01000' set MESSAGE_TEXT = 'El número de origen no pertenece a ninguna linea telefónica', MYSQL_ERRNO =  1000;
    else
    insert into calls (duration, id_invoice, origin_number, destiny_number, tariff_price, tariff_cost, total_price, call_date)
	values(p_duration, null, p_origin_phone, p_destiny_phone,tariff_price,
	tariff_cost, ROUND(total_price,2), date_call);
	select LAST_INSERT_ID() as 'id_call';
	end if;
END $$

DELIMITER $$
CREATE PROCEDURE `sp_callsByUserAndDates`( IN `id_user` INT,IN `date_from` DATE,IN `date_to` DATE)-- working
BEGIN
select
    	c.id_call as "callId",
		pl.line_number as "originNumber",
        ct.name as "originCity",
		pl2.line_number as "destinyNumber",
        ct2.name as "destinyCity",
        c.duration as "duration",
        c.total_price as "totalPrice",
		c.call_date as "date"

	from calls c
		inner join phone_lines pl on c.origin_number = pl.line_number
		inner join phone_lines pl2 on c.destiny_number = pl2.line_number
		inner join cities ct on pl.id_city = ct.id_city
		inner join cities ct2 on pl2.id_city = ct2.id_city
	where pl.id_user = id_user and call_date between date_from and date_to + interval 1 day;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `sp_invoicesByUser`( IN `id_user` INT)
BEGIN
select
    	i.id_invoice as "invoiceId",
		i.number_of_calls as "numberOfCalls",
		pl.line_number as "lineNumber",
        pl.type as "lineType",
        i.total_price as "totalPrice",
        i.paid as "paid",
		i.invoice_date as "date",
        i.expiration_date as "expirationDate"

	from invoices i
		inner join phone_lines pl on i.id_phone_line = pl.id_phone_line
	where pl.id_user = id_user;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `sp_invoicesByUserAndDates`( IN `id_user` INT,IN `date_from` DATE,IN `date_to` DATE)
BEGIN
select
    	i.id_invoice as "invoiceId",
		i.number_of_calls as "numberOfCalls",
		pl.line_number as "lineNumber",
        pl.type as "lineType",
        i.total_price as "totalPrice",
        i.paid as "paid",
		i.invoice_date as "date",
        i.expiration_date as "expirationDate"

	from invoices i
		inner join phone_lines pl on i.id_phone_line = pl.id_phone_line
	where pl.id_user = id_user and invoice_date between date_from and date_to +  interval 1 day;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `sp_mostDestination`(IN `id_user` INT, IN `size` INT)
begin
select
	pl2.line_number as "NumberPhone",
	ct.name as "Destination"
from calls c
	inner join phone_lines pl on c.origin_number = pl.line_number
	inner join phone_lines pl2 on c.destiny_number = pl2.line_number
	inner join cities ct on pl2.id_city = ct.id_city
where pl.id_user = id_user
group by destiny_number
order by count(destiny_number) desc
LIMIT size;
end$$
DELIMITER ;

-- AUTOMATED PROCESS --
-- Hay un proceso automático que todos los 1º de cada mes, corre automáticamente
-- y verifica las llamadas no facturadas y genera una nueva factura con un vencimiento
-- a 15 días. Debemos guardar solo el estado de si la factura esta paga o no
DROP EVENT IF EXISTS `e_check_calls`;
DELIMITER $$
CREATE EVENT `e_check_calls`
  ON SCHEDULE EVERY 1 MINUTE STARTS '2020-06-24 16:26:01'
  ON COMPLETION PRESERVE
DO BEGIN
  call sp_verify_calls_and_generate_invoices();
END$$
DELIMITER ;

-- USERS --
/*repair table mysql.db use_frm;*/
DROP USER if exists 'utnphones_admin'@'localhost';
DROP USER if exists 'backoffice'@'localhost';
DROP USER if exists 'clients'@'localhost';
DROP USER if exists 'infrastructure'@'localhost';

CREATE USER 'utnphones_admin'@'localhost' IDENTIFIED BY 'admin'; -- El unico en la parte de prog, * privileges
CREATE USER 'backoffice'@'localhost' IDENTIFIED BY 'psw';
CREATE USER 'clients'@'localhost' IDENTIFIED BY 'psw';
CREATE USER 'infrastructure'@'localhost' IDENTIFIED BY 'psw';
-- CREATE USER 'invoices'@'localhost' IDENTIFIED BY 'psw';
-- NO LO VAMOS A USAR PORQUE CONTROLARIA UN EVENTO AUTOMATICO

GRANT ALL ON utnphones.* TO 'utnphones_admin'@'localhost';

GRANT ALL ON utnphones.users TO 'backoffice'@'localhost';
GRANT ALL ON utnphones.tariffs TO 'backoffice'@'localhost';
GRANT ALL ON utnphones.phone_lines TO 'backoffice'@'localhost';
GRANT TRIGGER ON utnphones.* TO 'backoffice'@'localhost';
GRANT EXECUTE ON PROCEDURE sp_callsByUserAndDates TO 'backoffice'@'localhost';
GRANT EXECUTE ON PROCEDURE sp_invoicesByUserAndDates TO 'backoffice'@'localhost';

GRANT SELECT ON utnphones.calls TO 'clients'@'localhost';
GRANT SELECT ON utnphones.invoices TO 'clients'@'localhost';
GRANT EXECUTE ON PROCEDURE sp_callsByUserAndDates TO 'clients'@'localhost';
GRANT EXECUTE ON PROCEDURE sp_invoicesByUserAndDates TO 'clients'@'localhost';

GRANT INSERT ON utnphones.calls TO 'infrastructure'@'localhost';
GRANT TRIGGER ON utnphones.* TO 'infrastructure'@'localhost';
GRANT EXECUTE ON PROCEDURE sp_add_call TO 'infrastructure'@'localhost';
GRANT EXECUTE ON FUNCTION fn_id_city_from_prefix TO 'infrastructure'@'localhost';

-- INDICES --
CREATE INDEX index_calls_dates_user ON calls(origin_number, call_date) using btree;
CREATE INDEX index_invoices_dates_user ON invoices(id_phone_line, invoice_date) using btree;

/*FLUSH PRIVILEGES;*/