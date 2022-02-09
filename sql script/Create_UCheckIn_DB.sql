-- Database Creation Script

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
	`cust_ID` int NOT NULL AUTO_INCREMENT,
  `cust_Fname` varchar(20) DEFAULT NULL,
  `cust_Lname` varchar(20) DEFAULT NULL,
  `cust_Phone` varchar(10) DEFAULT NULL,
  `cust_Email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`cust_ID`),
  KEY `cust_Email` (`cust_Email`)
);

--
-- Insert data for table `Customer`
--
INSERT INTO Customer (cust_Fname, cust_Lname, cust_Phone, cust_email) VALUES (
	("Jerry", "Seinfeld", 6515550123, "jseinfeld1@seinfeld.com"),
	("Linus", "Sebastian", 6125553210, "linus@ltt.com"),
    ("Jeff", "Johnson", 6125558289, "jeff@imjeff.com"),
    ("Nefarias", "Bredd", 6515550217, "Mahad@limosales.limo"),
    ("Jennifer", "Aniston", 6125551234, "janiston@jenan.net"),
    ("Free", "Willy", 6125554321, "Free.Will@flippers.rock"),
    ("Alex", "Trebek", 6125558985, "ATrebek@gameshowcommittee.org")
    );


-- --------------------------------------- --
-- 2. Table structure for table `RoomType` --
-- --------------------------------------- --
CREATE TABLE RoomType(
  `roomType_ID` varchar(20) NOT NULL,
  `king` int DEFAULT NULL,
  `queen` int DEFAULT NULL,
  `full` int DEFAULT NULL,
  `pull_Out` int DEFAULT NULL,
  `suite` tinyint(1) DEFAULT NULL,
  `rate` float DEFAULT NULL,
  PRIMARY KEY (`roomType_ID`)
);

--
-- Insert data for table `RoomType`
--

INSERT INTO `RoomType` VALUES( 
('Double Queen',0,2,0,0,0,110),
('Double Queen Suite',0,2,0,1,1,200),
('Single King',1,0,0,0,0,100),
('Single King Suite',1,0,0,1,1,175)
);


-- ------------------------------------------ --
-- 3. Table structure for table `RequestType` --
-- ------------------------------------------ --
CREATE TABLE RequestType(
	`reqType_ID` int NOT NULL,
  `reqType_Description` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`reqType_ID`)
);

--
-- Dumping data for table `RequestType`
--

INSERT INTO `RequestType` VALUES (
(1,'Food Service'),
(2,'Hotel Amenities')
);


-- ----------------------------------- --
-- 4. Table structure for table `Room` --
-- ----------------------------------- --
CREATE TABLE Room(
	`room_num` int NOT NULL,
  `roomType_ID` varchar(20) DEFAULT NULL,
  `room_status` tinyint(1) DEFAULT NULL,
  `room_active` tinyint(1) DEFAULT NULL,
  `floor` int DEFAULT NULL,
  PRIMARY KEY (`room_num`),
  KEY `room_ibfk_1` (`roomType_ID`),
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`roomType_ID`) REFERENCES `RoomType` (`roomType_ID`) ON DELETE CASCADE
);

--
-- Insert data for table `Room`
--

INSERT INTO `Room` VALUES (
(100,'Single King',0,1,1),
(101,'Double Queen',0,1,1),
(200,'Double Queen',0,1,2),
(220,'Single King Suite',1,1,2),
(310,'Double Queen Suite',0,1,3)
);


-- -------------------------------------- --
-- 5. Table structure for table `Booking` --
-- -------------------------------------- --
CREATE TABLE Booking(
	`conf_ID` int NOT NULL,
  `cust_email` varchar(100) DEFAULT NULL,
  `room_num` int DEFAULT NULL,
  `stay_length` int DEFAULT NULL,
  `check_in` date DEFAULT NULL,
  `check_out` date DEFAULT NULL,
  `cctoken` int DEFAULT NULL,
  `checkin_status` tinyint DEFAULT NULL,
  PRIMARY KEY (`conf_ID`),
  KEY `cust_email` (`cust_email`),
  KEY `room_num` (`room_num`),
  CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`cust_email`) REFERENCES `Customer` (`cust_Email`) ON DELETE CASCADE,
  CONSTRAINT `booking_ibfk_2` FOREIGN KEY (`room_num`) REFERENCES `Room` (`room_num`) ON DELETE CASCADE
);


-- ------------------------------------ --
-- 6. Table structure for table `Items` --
-- ------------------------------------ --
CREATE TABLE Items(
    `item_ID` int NOT NULL AUTO_INCREMENT,
  `item_Name` varchar(30) DEFAULT NULL,
  `item_price` float DEFAULT NULL,
  PRIMARY KEY (`item_ID`)
);

--
-- Inserting data for table `Items`
--

INSERT INTO `Items` VALUES (
(11,'Chicken Kiev',13.99),
(12,'Caesar Salad',8.99),
(13,'New York Strip Steak',16.99),
(14,'Pork Ribs',17.99),
(15,'Talapia',6.99),
(16,'Shrimp Scampi',9.99),
(17,'Pepperoni Pizza',12.99),
(18,'Chocolate Cake',6.99),
(19,'New York Cheese Cake',9.99),
(20,'French Silk Pie',8.99),
(21,'Toiletries',0),
(22,'Personal Care Items',0),
(23,'Coffee Kit',0),
(24,'Tissue Box',0),
(25,'Bathroom Robe and Slippers',0),
(26,'Fresh Pillows',0)
);

-- --------------------------------------- --
-- 7. Table structure for table `Employee` --
-- --------------------------------------- --
CREATE TABLE Employee(
    `emp_ID` int NOT NULL AUTO_INCREMENT,
  `emp_Fname` varchar(20) DEFAULT NULL,
  `emp_Lname` varchar(20) DEFAULT NULL,
  `admin` tinyint(1) DEFAULT NULL,
  `username` varchar(25) DEFAULT NULL,
  `password` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`emp_ID`)
);

--
-- Inserting data for table `employee`
--

INSERT INTO `employee` VALUES (
(1,'fname','lname',1,'Admin','$2a$10$V/PNTLwcH5zmBzJVD5W8LOPiDZABpEZuEpcph77vjL7e99LnGFNOy')
);


-- -------------------------------------- --
-- 8. Table structure for table `Request` --
-- -------------------------------------- --
CREATE TABLE Request(
    `req_ID` int NOT NULL AUTO_INCREMENT,
  `req_DateTime` datetime DEFAULT NULL,
  `req_FullfillDateTime` datetime DEFAULT NULL,
  `fulfilled` tinyint(1) DEFAULT NULL,
  `conf_ID` int DEFAULT NULL,
  PRIMARY KEY (`req_ID`),
  KEY `conf_ID` (`conf_ID`),
  CONSTRAINT `request_ibfk_1` FOREIGN KEY (`conf_ID`) REFERENCES `Booking` (`conf_ID`)
);


-- ------------------------------------------- --
-- 9. Table structure for table `RequestItems` --
-- ------------------------------------------- --
CREATE TABLE RequestItems(
	`reqitem_ID` int NOT NULL AUTO_INCREMENT,
  `req_ID` int DEFAULT NULL,
  `item_ID` int DEFAULT NULL,
  `fulfilled` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`reqitem_ID`),
  KEY `item_ID_idx` (`item_ID`),
  KEY `reqID_idx` (`req_ID`),
  CONSTRAINT `item_ID` FOREIGN KEY (`item_ID`) REFERENCES `Items` (`item_ID`) ON UPDATE RESTRICT,
  CONSTRAINT `reqID` FOREIGN KEY (`req_ID`) REFERENCES `Request` (`req_ID`)
);


