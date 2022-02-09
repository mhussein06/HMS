-- UCHeckIn Database Creation Script
-- Super Awesome Dev Squad
-- Max Christiansen
-- Version: 2.0

-- -------------------------------- --
-- -------------------------------- --
-- I. Create and Select uCheckin DB --
-- -------------------------------- --
-- -------------------------------- --
CREATE DATABASE IF NOT EXISTS uCheckIn;
USE uCheckIn;


-- ------------------------------------ --
-- ------------------------------------ --
-- II. Drop existing tables from the DB --
-- ------------------------------------ --
-- ------------------------------------ --
DROP TABLE IF EXISTS `Customer`;
DROP TABLE IF EXISTS `RoomType`;
DROP TABLE IF EXISTS `RequestType`;
DROP TABLE IF EXISTS `Room`;
DROP TABLE IF EXISTS `Booking`;
DROP TABLE IF EXISTS `Items`;
DROP TABLE IF EXISTS `Employee`;
DROP TABLE IF EXISTS `Request`;
DROP TABLE IF EXISTS `RequestItems`;


-- ---------------------------------- --
-- ---------------------------------- --
-- III. Create the appropriate tables --
-- ---------------------------------- --
-- ---------------------------------- --

-- --------------------------------------- --
-- 1. Table structure for table `Customer` --
-- --------------------------------------- --
CREATE TABLE Customer(
	cust_ID int primary key auto_increment,
    cust_Fname varchar(20),
    cust_Lname varchar(20),
    cust_Phone varchar(11),
    cust_Email varchar(100),
    KEY cust_Email (cust_Email)
);


-- --------------------------------------- --
-- 2. Table structure for table `RoomType` --
-- --------------------------------------- --
CREATE TABLE RoomType(
	roomType_ID varchar(4) primary key,
    king int,
    queen int,
    full int,
    pull_Out int,
    suite boolean,
    rate float
);


-- ------------------------------------------ --
-- 3. Table structure for table `RequestType` --
-- ------------------------------------------ --
CREATE TABLE RequestType(
	reqType_ID varchar(20) primary key,
    reqType_Description varchar(250)
);


-- ----------------------------------- --
-- 4. Table structure for table `Room` --
-- ----------------------------------- --
CREATE TABLE Room(
	room_num int primary key,
    roomType_ID varchar(4),
    room_status boolean,
    room_active boolean,
    floor int,
    KEY roomType_ID (roomType_ID),
    FOREIGN KEY(roomType_ID) REFERENCES RoomType(roomType_ID) ON DELETE CASCADE
);


-- -------------------------------------- --
-- 5. Table structure for table `Booking` --
-- -------------------------------------- --
CREATE TABLE Booking(
	conf_ID int primary key,
    cust_email varchar(100),
    room_num int,
    stay_length int,
    check_in datetime,
    check_out datetime,
	cctoken int,
    checkin_status boolean,
    KEY cust_email (cust_email),
    KEY room_num (room_num),
    FOREIGN KEY(cust_email) REFERENCES Customer(cust_email) ON DELETE CASCADE,
    FOREIGN KEY(room_num) REFERENCES Room(room_num) ON DELETE CASCADE
);


-- ------------------------------------ --
-- 6. Table structure for table `Items` --
-- ------------------------------------ --
CREATE TABLE Items(
    item_ID int auto_increment,
    item_Name varchar(100),
    item_price float,
    PRIMARY KEY(item_ID)
);


-- --------------------------------------- --
-- 7. Table structure for table `Employee` --
-- --------------------------------------- --
CREATE TABLE Employee(
    emp_ID int auto_increment,
    emp_Fname varchar(20),
    emp_Lname varchar(20),
    admin boolean,
    username varchar(25),
    password varchar(25),
    PRIMARY KEY(emp_ID)
);


-- -------------------------------------- --
-- 8. Table structure for table `Request` --
-- -------------------------------------- --
CREATE TABLE Request(
    req_ID int primary key,
    room_num int,
    req_Type varchar(25),
    req_DateTime datetime,
    req_FullfillDateTime date,
    fulfilled boolean,
    conf_ID int,
    item_ID int,
	FOREIGN KEY(conf_ID) REFERENCES Booking(conf_ID),
    FOREIGN KEY(item_ID) REFERENCES Items(item_ID),
    FOREIGN KEY(room_num) REFERENCES ROOM(room_num)
);


-- ------------------------------------------- --
-- 9. Table structure for table `RequestItems` --
-- ------------------------------------------- --
CREATE TABLE RequestItems(
	reqItem_ID int primary key,
    req_ID int, 
    item_ID int,
    fulfilled boolean,
    KEY item_ID (item_ID),
    KEY req_ID (req_ID),
    FOREIGN KEY(item_ID) REFERENCES Items(item_ID) ON UPDATE RESTRICT,
    FOREIGN KEY(req_ID) REFERENCES Request(req_ID)
);


-- ---------------------------------------------- --
-- ---------------------------------------------- --
-- IV. Insert sample data into the created tables --
-- ---------------------------------------------- --
-- ---------------------------------------------- --

-- ------------------------------ --
-- 1. Insert data into `Customer` --
-- ------------------------------ --
INSERT INTO Customer(cust_Fname, cust_Lname, cust_Phone, cust_email)
VALUES ("Jerry", "Seinfeld", 6515550123, "jseinfeld1@seinfeld.com"),
	("Linus", "Sebastian", 6125553210, "linus@ltt.com"),
    ("Jeff", "Johnson", 6125558289, "jeff@imjeff.com"),
    ("Max", "Christiansen", 6515550124, "max@max.com"),
    ("David", "Qual", 6515550125, "David@limosales.limo"),
    ("Chuck", "Gustner", 6515550216, "Chuck@limosales.limo"),
    ("Mahad", "Hussein", 6515550217, "Mahad@limosales.limo"),
    ("Jennifer", "Aniston", 6125551234, "janiston@jenan.net"),
    ("Free", "Willy", 6125554321, "Free.Will@flippers.rock"),
    ("Alex", "Trebek", 6125558985, "ATrebek@gameshowcommittee.org");

-- ------------------------------ --
-- 2. Insert data into `RoomType` --
-- ------------------------------ --
INSERT INTO RoomType
VALUES ('Single King', 1, 0, 0, 0, 0, 100),
	   ('Double Queen', 0, 2, 0, 0, 0, 110),
       ('Single King Suite', 1, 0, 0, 1, 1, 175),
       ('Double Queen Suite', 0, 2, 0, 1, 1, 200);

-- --------------------------------- --
-- 3. Insert data into `RequestType` --
-- --------------------------------- --    
INSERT INTO RequestType
VALUES (1, "Food Service"),
	(2, "Hotel Amenities");

-- -------------------------- --
-- 4. Insert data into `Room` --
-- -------------------------- --   
INSERT INTO Room
VALUES (100, 'SK', 1, 1, 1),
	   (101, 'DQ', 1, 1, 1),
       (200, 'DQ', 0, 1, 2),
       (220, 'SKS', 0, 0, 2),
       (310, 'DQS', 1, 1, 3);
       
-- ----------------------------- --
-- 5. Insert data into `Booking` --
-- ----------------------------- --  
INSERT INTO Booking
VALUES (0, "ATrebek@gameshowcommittee.org", 100, 7, "2021-12-25", "2022-01-01", 983047, 0),
	(1, "linus@ltt.com", 100, 3, "2022-01-01", "2022-01-04", 853935, 0),
    (2, "max@max.com", 100, 5, "2022-06-13", "2022-06-17", 843011, 0),
    (3, "Chuck@limosales.limo", 101, 5, "2022-06-13", "2022-06-17", 818023, 0),
    (4, "David@limosales.limo", 200, 5, "2022-06-13", "2022-06-17", 695011, 0),
    (5, "Mahad@limosales.limo", 220, 5, "2022-06-13", "2022-06-17", 385609, 0),
    (6, "Free.Will@flippers.rock", 220, 1, "2022-08-03", "2022-08-04", 353433, 0),
    (7, "ATrebek@gameshowcommittee.org", 310, 7, "2023-12-25", "2024-01-01", 58230, 0),
    (8, "janiston@jenan.net", 310, 7, "2021-12-25", "2022-01-01", 29816, 0);

-- --------------------------- --
-- 6. Insert data into `Items` --
-- --------------------------- --
INSERT INTO Items
VALUES (11, "Chicken Kiev", 13.99),
		(12, "Caesar Salad", 8.99),
        (13, "16oz New York Strip Steak", 16.99),
        (14, "Pork Ribs", 17.99),
        (15, "Talapia", 6.99),
        (16, "Shrimp Scampi", 9.99),
        (17, "Pepperoni Pizza", 12.99),
        (18, "Chocolate Cake", 6.99),
        (19, "New York Cheese Cake", 9.99),
        (20, "French Silk Pie", 8.99),
        (21, "Toiletries", 0.00),
        (22, "Personal Care Items", 0.00),
        (23, "Coffee Kit", 0.00),
        (24, "Tissue Box", 0.00),
        (25, "Bathrobe and Slippers", 0.00),
        (26, "Fresh Pillows", 0.00);
        
-- ------------------------------ --
-- 7. Insert data into `Employee` --
-- ------------------------------ --        
INSERT INTO Employee(emp_Fname, emp_Lname, admin, username, password)
VALUES ("Chuck", "Gustner", 1, "cgustner", ""),
	   ("Mahad", "Hussein", 0, "mhussein", "");
       
       
-- ----------------------------- --
-- 8. Insert data into `Request` --
-- ----------------------------- --
 INSERT INTO Request
 VALUES (0, 100, "Food Service", "2021-12-04 18:03:03", NULL, 0, 00000, 14),
	(1, 310, "Hotel Amenities", "2021-12-04 16:53:22", NULL, 0, 00001, 25),
    (2, 101, "Hotel Amenities", "2021-10-12 11:28:35", NULL, 0, 00002, 21);

-- ---------------------------------- --
-- 9. Insert data into `RequestItems` --
-- ---------------------------------- --       
