-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: maintenance_db
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('ADMIN','USER','CUSTOMER') COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contact_info` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'admin','$2a$10$exampleHashForAdmin','ADMIN','Quản trị viên',NULL,NULL,NULL),(2,'employee','$2a$10$exampleHashForUser','USER','Nhân viên bảo trì',NULL,NULL,NULL),(3,'cust001','$2a$10$exampleHashForCustomer','CUSTOMER','Công ty ABC','abc@example.com, 0901234567','123 Đường Láng, Hà Nội',NULL),(4,'cust002','$2a$10$exampleHashForCustomer','CUSTOMER','Công ty XYZ','xyz@example.com, 0912345678','456 Lê Lợi, TP.HCM',NULL),(5,'admin_user','password123','USER','Nguyễn Văn A','customer@example.com','123 Đường ABC, Hà Nội','admin_avatar.jpg'),(6,'customer1','password456','CUSTOMER','Lê Thị B','customer1@example.com','456 Đường DEF, Hà Nội','customer1_avatar.jpg'),(7,'customer2','password456','CUSTOMER','Trần Minh C','customer2@example.com','789 Đường GHI, Hà Nội','customer2_avatar.jpg'),(8,'customer3','password456','CUSTOMER','Phạm Ngọc D','customer3@example.com','101 Đường JKL, Hà Nội','customer3_avatar.jpg'),(9,'customer4','password456','CUSTOMER','Vũ Hoàng E','customer4@example.com','202 Đường MNO, Hà Nội','customer4_avatar.jpg'),(10,'customer5','password456','CUSTOMER','Đỗ Thùy F','customer5@example.com','303 Đường PQR, Hà Nội','customer5_avatar.jpg');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (7,'Bảo trì'),(5,'Đóng gói'),(2,'Gia công'),(1,'In ấn'),(10,'Khác'),(4,'Kiểm tra'),(3,'Lắp ráp'),(9,'Lưu trữ'),(8,'Quản lý'),(6,'Vận chuyển');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `manufacturer` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `purchase_date` date DEFAULT NULL,
  `location` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `account_id` bigint DEFAULT NULL,
  `type_id` bigint DEFAULT NULL,
  `avatar` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `account_id` (`account_id`),
  KEY `equipment_ibfk_2` (`type_id`),
  KEY `idx_equipment_status_id` (`status_id`),
  CONSTRAINT `equipment_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE SET NULL,
  CONSTRAINT `equipment_ibfk_2` FOREIGN KEY (`type_id`) REFERENCES `category` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
INSERT INTO `equipment` VALUES (1,'Máy in','PRINTER001','HP','2023-01-15','Phòng IT',3,1,'printer.jpg',1),(2,'Máy CNC','CNC002','Haas','2022-06-10','Xưởng sản xuất',4,2,'cnc.jpg',2),(3,'Máy lắp ráp tự động','AUTO001','Mitsubishi','2022-03-20','Xưởng lắp ráp',2,3,'auto1.jpg',1),(4,'Máy kiểm tra điện','TEST001','Fluke','2021-11-01','Phòng kiểm tra',3,4,'test1.jpg',2),(5,'Máy đóng gói','PACK001','Bosch','2023-02-15','Khu đóng gói',5,5,'pack1.jpg',1),(6,'Xe nâng','FORK001','Toyota','2020-12-12','Kho vận chuyển',2,6,'forklift.jpg',3),(7,'Máy hàn','WELD001','Lincoln','2022-09-30','Xưởng gia công',4,2,'weld1.jpg',1),(8,'Máy scan 3D','SCAN001','Artec','2023-05-05','Phòng thiết kế',1,4,'scan3d.jpg',1),(9,'Tủ lưu trữ','STOR001','Samsung','2021-04-18','Phòng lưu trữ',3,9,'storage.jpg',1),(10,'Máy in tem','LABEL001','Zebra','2023-08-22','Khu đóng gói',5,1,'label.jpg',2);
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forum_message`
--

DROP TABLE IF EXISTS `forum_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `forum_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL,
  `message` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `forum_message_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_message`
--

LOCK TABLES `forum_message` WRITE;
/*!40000 ALTER TABLE `forum_message` DISABLE KEYS */;
INSERT INTO `forum_message` VALUES (1,1,'Có ai biết cách xử lý lỗi máy in kẹt giấy không?','2025-04-29 09:00:00'),(2,3,'Máy in của chúng tôi hay bị kẹt giấy, cần hỗ trợ!','2025-04-29 09:30:00'),(3,2,'Kiểm tra khay giấy và vệ sinh con lăn nhé!','2025-04-29 10:00:00'),(4,4,'Máy CNC của chúng tôi cần bảo trì gấp!','2025-04-29 11:00:00');
/*!40000 ALTER TABLE `forum_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issue`
--

DROP TABLE IF EXISTS `issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `issue` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `equipment_id` bigint NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `date` datetime NOT NULL,
  `severity_id` bigint NOT NULL,
  `is_resolved` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `equipment_id` (`equipment_id`),
  KEY `fk_issue_severity` (`severity_id`),
  CONSTRAINT `fk_issue_severity` FOREIGN KEY (`severity_id`) REFERENCES `severity_level` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `issue_ibfk_1` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issue`
--

LOCK TABLES `issue` WRITE;
/*!40000 ALTER TABLE `issue` DISABLE KEYS */;
INSERT INTO `issue` VALUES (1,1,'Máy in bị kẹt giấy','2025-05-01 00:00:00',1,0),(2,2,'Máy chiếu không hiển thị','2025-05-02 00:00:00',2,1),(3,3,'Lỗi bàn phím laptop','2025-05-03 00:00:00',3,0),(4,4,'Máy quạt quá nhiệt','2025-05-04 00:00:00',1,1),(5,5,'Máy lạnh không làm mát','2025-05-05 00:00:00',2,0);
/*!40000 ALTER TABLE `issue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance_frequency`
--

DROP TABLE IF EXISTS `maintenance_frequency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance_frequency` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance_frequency`
--

LOCK TABLES `maintenance_frequency` WRITE;
/*!40000 ALTER TABLE `maintenance_frequency` DISABLE KEYS */;
INSERT INTO `maintenance_frequency` VALUES (3,'Hàng năm'),(2,'Hàng quý'),(1,'Hàng tháng'),(4,'Theo sự cố');
/*!40000 ALTER TABLE `maintenance_frequency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance_schedule`
--

DROP TABLE IF EXISTS `maintenance_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance_schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `equipment_id` bigint NOT NULL,
  `frequency_id` bigint NOT NULL,
  `type_id` bigint NOT NULL,
  `date` date NOT NULL,
  `next_date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `equipment_id` (`equipment_id`),
  KEY `frequency_id` (`frequency_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `maintenance_schedule_ibfk_1` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`id`) ON DELETE CASCADE,
  CONSTRAINT `maintenance_schedule_ibfk_2` FOREIGN KEY (`frequency_id`) REFERENCES `maintenance_frequency` (`id`) ON DELETE CASCADE,
  CONSTRAINT `maintenance_schedule_ibfk_3` FOREIGN KEY (`type_id`) REFERENCES `maintenance_type` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance_schedule`
--

LOCK TABLES `maintenance_schedule` WRITE;
/*!40000 ALTER TABLE `maintenance_schedule` DISABLE KEYS */;
INSERT INTO `maintenance_schedule` VALUES (1,1,1,1,'2025-05-01','2025-06-01'),(2,2,2,2,'2025-04-01','2025-07-01'),(3,3,3,3,'2025-01-15','2026-01-15'),(4,4,4,4,'2025-05-03','2025-05-10');
/*!40000 ALTER TABLE `maintenance_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance_type`
--

DROP TABLE IF EXISTS `maintenance_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance_type`
--

LOCK TABLES `maintenance_type` WRITE;
/*!40000 ALTER TABLE `maintenance_type` DISABLE KEYS */;
INSERT INTO `maintenance_type` VALUES (2,'Bảo trì toàn diện'),(1,'Kiểm tra định kỳ'),(4,'Sửa chữa khẩn cấp'),(3,'Sửa chữa lớn');
/*!40000 ALTER TABLE `maintenance_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair`
--

DROP TABLE IF EXISTS `repair`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repair` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `equipment_id` bigint NOT NULL,
  `date` datetime NOT NULL,
  `type_id` bigint NOT NULL,
  `cost` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `equipment_id` (`equipment_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `repair_ibfk_1` FOREIGN KEY (`equipment_id`) REFERENCES `equipment` (`id`) ON DELETE CASCADE,
  CONSTRAINT `repair_ibfk_2` FOREIGN KEY (`type_id`) REFERENCES `repair_type` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair`
--

LOCK TABLES `repair` WRITE;
/*!40000 ALTER TABLE `repair` DISABLE KEYS */;
INSERT INTO `repair` VALUES (1,1,'2025-05-01 10:00:00',1,1500000.00),(2,2,'2025-04-15 14:30:00',2,3000000.00),(3,3,'2025-03-20 09:00:00',1,500000.00),(4,4,'2025-05-02 11:00:00',2,4500000.00);
/*!40000 ALTER TABLE `repair` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair_type`
--

DROP TABLE IF EXISTS `repair_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repair_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair_type`
--

LOCK TABLES `repair_type` WRITE;
/*!40000 ALTER TABLE `repair_type` DISABLE KEYS */;
INSERT INTO `repair_type` VALUES (2,'Bảo trì'),(1,'Sửa chữa');
/*!40000 ALTER TABLE `repair_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `severity_level`
--

DROP TABLE IF EXISTS `severity_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `severity_level` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `severity_level`
--

LOCK TABLES `severity_level` WRITE;
/*!40000 ALTER TABLE `severity_level` DISABLE KEYS */;
INSERT INTO `severity_level` VALUES (3,'HIGH'),(1,'LOW'),(2,'MEDIUM');
/*!40000 ALTER TABLE `severity_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (2,'Bảo trì'),(1,'Hoạt động'),(3,'Ngưng sử dụng');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `support_ticket`
--

DROP TABLE IF EXISTS `support_ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `support_ticket` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL,
  `issue` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `timestamp` datetime NOT NULL,
  `is_open` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'True nếu ticket đang mở hoặc xử lý, False nếu đã đóng',
  PRIMARY KEY (`id`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `support_ticket_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `support_ticket`
--

LOCK TABLES `support_ticket` WRITE;
/*!40000 ALTER TABLE `support_ticket` DISABLE KEYS */;
INSERT INTO `support_ticket` VALUES (1,3,'Yêu cầu hỗ trợ cài đặt phần mềm cho máy in','2025-04-28 09:00:00',1),(2,2,'Cần hỗ trợ kiểm tra máy CNC','2025-04-28 10:00:00',1);
/*!40000 ALTER TABLE `support_ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'maintenance_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-06 10:40:19
