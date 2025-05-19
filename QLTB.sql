-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: maintsysdb
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
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `base_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `base_id` (`base_id`),
  CONSTRAINT `account_ibfk_1` FOREIGN KEY (`base_id`) REFERENCES `base` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'admin','$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO','Nguyễn Văn A','a@example.com','ROLE_ADMIN','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747398195/ujxi2ngvtkszpj5nvjvx.png',1),(2,'trung','$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO','Lư Hiếu Trung','b@example.com','ROLE_USER','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747398195/ujxi2ngvtkszpj5nvjvx.png',2),(3,'duc','$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO','Nguyễn Văn Đức','c@example.com','ROLE_USER','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747398195/ujxi2ngvtkszpj5nvjvx.png',3),(4,'user','$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO','Phạm Thị D','d@example.com','ROLE_USER','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747398195/ujxi2ngvtkszpj5nvjvx.png',4),(5,'Duc Nguyen','$2a$10$5X9k5N1sTc1/CjVH5XJoje3QMYijH3ETpgkox00R0MdPaJPPrf7wO','Đức Nguyễn','duw3210@gmail.com','ROLE_USER','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747398195/ujxi2ngvtkszpj5nvjvx.png',1);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `base`
--

DROP TABLE IF EXISTS `base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base`
--

LOCK TABLES `base` WRITE;
/*!40000 ALTER TABLE `base` DISABLE KEYS */;
INSERT INTO `base` VALUES (1,'Trụ sở chính','123 Đường A, Quận 1, TP.HCM'),(2,'Chi nhánh Hà Nội','456 Đường B, Quận Ba Đình, Hà Nội'),(3,'Chi nhánh Đà Nẵng','789 Đường C, Đà Nẵng'),(4,'Chi nhánh TpHCM','321 Đường D, Quận 12, TP.HCM');
/*!40000 ALTER TABLE `base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Điện lạnh'),(2,'Thiết bị văn phòng'),(3,'Thiết bị mạng'),(4,'Thiết bị gia dụng');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `date` date DEFAULT NULL,
  `manufacturer` varchar(100) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `base_id` int DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `status_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `base_id` (`base_id`),
  KEY `category_id` (`category_id`),
  KEY `status_id` (`status_id`),
  CONSTRAINT `device_ibfk_1` FOREIGN KEY (`base_id`) REFERENCES `base` (`id`),
  CONSTRAINT `device_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `device_ibfk_3` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device`
--

LOCK TABLES `device` WRITE;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
INSERT INTO `device` VALUES (1,'Máy lạnh Daikin 1HP 3','2022-05-01','Daikin','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747558890/download_xvrpni.jpg',1,1,1),(2,'Tủ lạnh Panasonic 200L','2023-02-10','Panasonic','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747558931/download_cyj5uc.jpg',2,1,1),(3,'Máy giặt LG Inverter','2021-07-18','LG','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747565387/download_ye3ogn.jpg',3,4,2),(4,'Máy in HP LaserJet Pro M404','2022-09-05','HP','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747565413/download_yqp5rs.jpg',4,2,1),(8,'macbook','2025-05-17','HP','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747558873/macbookairm120207-1_us3vcp.jpg',1,3,1),(9,'iphone11','2025-05-17','apple','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747558807/download_bripso.jpg',3,1,1),(10,'iphone12','2025-05-17','apple','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747558807/download_bripso.jpg',4,1,1),(11,'Máy photocopy Canon','2024-03-10','Canon','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747565453/download_nt92i1.jpg',2,2,1),(12,'Switch Cisco 24 port','2023-12-01','Cisco','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747565480/download_kicrxu.jpg',3,3,1),(13,'Router MikroTik','2022-10-20','MikroTik','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747566298/download_npf7nk.jpg',1,3,2),(14,'Máy lạnh Samsung 2HP','2023-06-15','Samsung','',4,1,1),(15,'Tủ lạnh Toshiba 150L','2021-11-05','Toshiba','',2,1,3),(16,'Máy in Brother HL-1201','2023-04-28','Brother','',3,2,2),(17,'Điều hòa Panasonic','2023-08-10','Panasonic','',1,1,1),(18,'Máy quét Canon LIDE 300','2022-09-14','Canon','',1,2,1),(19,'Bộ phát WiFi TP-Link','2023-01-12','TP-Link','',4,3,1),(20,'Laptop Dell Vostro','2022-02-25','Dell','',3,2,2),(21,'Máy tính để bàn Lenovo','2023-07-19','Lenovo','',2,2,1),(22,'Bàn phím Logitech','2024-01-09','Logitech','',1,2,1),(23,'Màn hình Samsung 24\"','2023-03-03','Samsung','',1,2,1),(24,'Máy in màu Epson L3150','2023-05-14','Epson','',3,2,1),(25,'Ổ cứng NAS Synology','2022-12-22','Synology','',4,3,1),(26,'Camera IP Dahua','2023-06-30','Dahua','',1,3,1),(27,'Thiết bị phát WiFi Mesh Tenda','2024-02-17','Tenda','',2,3,1),(28,'Máy chiếu ViewSonic','2023-11-11','ViewSonic','',3,2,1),(29,'Điều hòa LG Inverter','2023-07-01','LG','',1,1,1),(30,'Tủ đông Alaska','2024-01-15','Alaska','',4,1,2),(31,'máy tính','2025-05-17','Dell','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747498658/ookdco65kajmbu9ukhqe.jpg',2,2,1),(33,'laptop','2025-05-17','Dell','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747499064/zp51pireoqodayuaof6y.jpg',2,2,1),(35,'b','2025-05-19','Daikin','https://res.cloudinary.com/dqpoa9ukn/image/upload/v1747593700/jc5r48htxuvfymo64jrm.png',3,2,1);
/*!40000 ALTER TABLE `device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `frequency`
--

DROP TABLE IF EXISTS `frequency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `frequency` (
  `id` int NOT NULL AUTO_INCREMENT,
  `frequency` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `frequency`
--

LOCK TABLES `frequency` WRITE;
/*!40000 ALTER TABLE `frequency` DISABLE KEYS */;
INSERT INTO `frequency` VALUES (1,'Hàng tháng'),(2,'6 tháng'),(3,'Hàng năm');
/*!40000 ALTER TABLE `frequency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incident_level`
--

DROP TABLE IF EXISTS `incident_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `incident_level` (
  `id` int NOT NULL AUTO_INCREMENT,
  `level` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incident_level`
--

LOCK TABLES `incident_level` WRITE;
/*!40000 ALTER TABLE `incident_level` DISABLE KEYS */;
INSERT INTO `incident_level` VALUES (1,'Low'),(2,'Medium'),(3,'High');
/*!40000 ALTER TABLE `incident_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issue`
--

DROP TABLE IF EXISTS `issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `issue` (
  `id` int NOT NULL AUTO_INCREMENT,
  `des` varchar(100) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `level_id` int DEFAULT NULL,
  `device_id` int DEFAULT NULL,
  `is_resolved` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `level_id` (`level_id`),
  KEY `issue_ibfk_2` (`device_id`),
  CONSTRAINT `issue_ibfk_1` FOREIGN KEY (`level_id`) REFERENCES `incident_level` (`id`),
  CONSTRAINT `issue_ibfk_2` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issue`
--

LOCK TABLES `issue` WRITE;
/*!40000 ALTER TABLE `issue` DISABLE KEYS */;
INSERT INTO `issue` VALUES (1,'Không khởi động được','2025-05-11',2,3,0),(2,'Kêu to khi hoạt động','2025-01-20',2,1,1),(3,'In bị nhòe mực','2025-03-10',1,2,0),(4,'Mất kết nối mạng','2025-04-05',2,4,0),(17,'Mất kết nối mạng liên tục.','2024-04-15',3,2,0),(18,'Tủ lạnh không làm lạnh.','2024-03-10',2,8,0),(19,'Máy in bị kẹt giấy thường xuyên.','2024-02-22',1,9,0),(20,'Laptop bị chậm, nghi lỗi ổ cứng.','2024-05-01',2,10,0),(21,'Không truy cập được NAS.','2024-01-30',2,15,0),(22,'Máy chiếu không lên hình.','2024-04-05',1,18,0),(23,'Tủ đông phát ra tiếng ồn lớn.','2024-05-10',3,20,0);
/*!40000 ALTER TABLE `issue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance`
--

DROP TABLE IF EXISTS `maintenance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `frequency_id` int DEFAULT NULL,
  `device_id` int DEFAULT NULL,
  `type_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `frequency_id` (`frequency_id`),
  KEY `type_id` (`type_id`),
  KEY `maintenance_ibfk_2` (`device_id`),
  CONSTRAINT `maintenance_ibfk_1` FOREIGN KEY (`frequency_id`) REFERENCES `frequency` (`id`),
  CONSTRAINT `maintenance_ibfk_2` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE CASCADE,
  CONSTRAINT `maintenance_ibfk_3` FOREIGN KEY (`type_id`) REFERENCES `maintenance_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance`
--

LOCK TABLES `maintenance` WRITE;
/*!40000 ALTER TABLE `maintenance` DISABLE KEYS */;
INSERT INTO `maintenance` VALUES (1,'2025-01-01',1,1,1),(2,'2025-03-01',2,2,2),(3,'2025-04-15',3,3,1),(4,'2025-05-01',1,4,2),(7,'2025-06-16',1,8,1),(8,'2025-06-16',1,9,1),(9,'2025-06-16',1,10,1),(20,'2024-01-10',1,1,1),(21,'2024-02-15',2,2,2),(22,'2024-03-20',3,3,1),(23,'2024-04-05',1,4,1),(24,'2024-05-01',2,8,2),(25,'2024-05-12',1,10,1),(26,'2024-06-10',3,12,2),(27,'2024-06-20',1,15,1),(28,'2024-07-01',2,17,2),(29,'2024-07-15',1,20,1),(30,'2025-06-16',1,31,1),(32,'2025-06-16',1,33,1),(35,'2025-06-18',1,35,1);
/*!40000 ALTER TABLE `maintenance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance_type`
--

DROP TABLE IF EXISTS `maintenance_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance_type`
--

LOCK TABLES `maintenance_type` WRITE;
/*!40000 ALTER TABLE `maintenance_type` DISABLE KEYS */;
INSERT INTO `maintenance_type` VALUES (1,'Định kỳ'),(2,'Đột xuất');
/*!40000 ALTER TABLE `maintenance_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair`
--

DROP TABLE IF EXISTS `repair`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repair` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `device_id` int DEFAULT NULL,
  `type_id` int DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `type_id` (`type_id`),
  KEY `account_id` (`account_id`),
  KEY `repair_ibfk_1` (`device_id`),
  CONSTRAINT `repair_ibfk_1` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE CASCADE,
  CONSTRAINT `repair_ibfk_2` FOREIGN KEY (`type_id`) REFERENCES `repair_type` (`id`),
  CONSTRAINT `repair_ibfk_3` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair`
--

LOCK TABLES `repair` WRITE;
/*!40000 ALTER TABLE `repair` DISABLE KEYS */;
INSERT INTO `repair` VALUES (1,'2025-01-10',500000.00,3,1,2),(2,'2025-02-05',200000.00,2,2,4),(3,'2025-03-20',150000.00,1,2,3),(4,'2025-04-25',100000.00,4,1,4),(7,'2025-05-11',4235235.00,1,1,3),(8,'2025-05-13',1214234.00,1,2,2),(9,'2024-02-10',500000.00,1,1,1),(10,'2024-03-15',750000.00,3,2,2),(11,'2024-04-05',200000.00,26,1,3),(12,'2024-04-20',1000000.00,27,1,4),(13,'2024-05-01',450000.00,9,2,1),(14,'2024-05-10',300000.00,12,2,2),(15,'2024-05-25',900000.00,15,1,3),(16,'2024-06-01',650000.00,18,2,4),(17,'2024-06-10',800000.00,20,1,3),(18,'2024-06-15',400000.00,2,1,1);
/*!40000 ALTER TABLE `repair` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair_type`
--

DROP TABLE IF EXISTS `repair_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repair_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair_type`
--

LOCK TABLES `repair_type` WRITE;
/*!40000 ALTER TABLE `repair_type` DISABLE KEYS */;
INSERT INTO `repair_type` VALUES (1,'Sửa chữa bảo trì'),(2,'Sửa chữa sự cố');
/*!40000 ALTER TABLE `repair_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (1,'Hoạt động'),(2,'Đang bảo trì'),(3,'Ngừng hoạt động');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'maintsysdb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-19 10:54:44
