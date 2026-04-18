-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.44 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.14.0.7165
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for spring_fruitshop
CREATE DATABASE IF NOT EXISTS `spring_fruitshop` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `spring_fruitshop`;

-- Dumping structure for table spring_fruitshop.categories
CREATE TABLE IF NOT EXISTS `categories` (
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleted_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `updated_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt8o6pivur7nn124jehx7cygw5` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table spring_fruitshop.categories: ~3 rows (approximately)
INSERT INTO `categories` (`created_at`, `deleted_at`, `id`, `updated_at`, `created_by`, `deleted_by`, `name`, `updated_by`) VALUES
	(NULL, NULL, 1, NULL, NULL, NULL, 'Trái cây khô', NULL),
	(NULL, NULL, 2, NULL, NULL, NULL, 'Trái cây nhập khẩu', NULL),
	(NULL, NULL, 3, NULL, NULL, NULL, 'Trái cây Việt', NULL);

-- Dumping structure for table spring_fruitshop.order_items
CREATE TABLE IF NOT EXISTS `order_items` (
  `price` double NOT NULL,
  `quantity` int NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleted_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updated_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table spring_fruitshop.order_items: ~66 rows (approximately)
INSERT INTO `order_items` (`price`, `quantity`, `created_at`, `deleted_at`, `id`, `order_id`, `product_id`, `updated_at`, `created_by`, `deleted_by`, `updated_by`) VALUES
	(150000, 3, '2026-01-02 10:15:00.000000', NULL, 32, 21, 31, '2026-01-02 10:15:00.000000', NULL, NULL, NULL),
	(130000, 2, '2026-01-02 10:15:00.000000', NULL, 33, 21, 32, '2026-01-02 10:15:00.000000', NULL, NULL, NULL),
	(205000, 2, '2026-01-03 14:30:00.000000', NULL, 34, 22, 33, '2026-01-03 14:30:00.000000', NULL, NULL, NULL),
	(190000, 3, '2026-01-04 09:45:00.000000', NULL, 35, 23, 34, '2026-01-04 09:45:00.000000', NULL, NULL, NULL),
	(20000, 1, '2026-01-04 09:45:00.000000', NULL, 36, 23, 35, '2026-01-04 09:45:00.000000', NULL, NULL, NULL),
	(165000, 2, '2026-01-05 16:20:00.000000', NULL, 37, 24, 36, '2026-01-05 16:20:00.000000', NULL, NULL, NULL),
	(160000, 4, '2026-01-06 11:10:00.000000', NULL, 38, 25, 37, '2026-01-06 11:10:00.000000', NULL, NULL, NULL),
	(30000, 1, '2026-01-06 11:10:00.000000', NULL, 39, 25, 38, '2026-01-06 11:10:00.000000', NULL, NULL, NULL),
	(145000, 3, '2026-01-08 15:40:00.000000', NULL, 40, 26, 20, '2026-01-08 15:40:00.000000', NULL, NULL, NULL),
	(5000, 1, '2026-01-08 15:40:00.000000', NULL, 41, 26, 21, '2026-01-08 15:40:00.000000', NULL, NULL, NULL),
	(170000, 3, '2026-01-09 08:25:00.000000', NULL, 42, 27, 22, '2026-01-09 08:25:00.000000', NULL, NULL, NULL),
	(190000, 2, '2026-01-10 13:50:00.000000', NULL, 43, 28, 23, '2026-01-10 13:50:00.000000', NULL, NULL, NULL),
	(155000, 4, '2026-01-11 10:05:00.000000', NULL, 44, 29, 24, '2026-01-11 10:05:00.000000', NULL, NULL, NULL),
	(195000, 2, '2026-01-12 14:35:00.000000', NULL, 45, 30, 25, '2026-01-12 14:35:00.000000', NULL, NULL, NULL),
	(145000, 5, '2026-01-15 09:50:00.000000', NULL, 46, 31, 26, '2026-01-15 09:50:00.000000', NULL, NULL, NULL),
	(5000, 1, '2026-01-15 09:50:00.000000', NULL, 47, 31, 27, '2026-01-15 09:50:00.000000', NULL, NULL, NULL),
	(150000, 3, '2026-01-16 15:15:00.000000', NULL, 48, 32, 28, '2026-01-16 15:15:00.000000', NULL, NULL, NULL),
	(10000, 1, '2026-01-16 15:15:00.000000', NULL, 49, 32, 29, '2026-01-16 15:15:00.000000', NULL, NULL, NULL),
	(185000, 3, '2026-01-17 10:30:00.000000', NULL, 50, 33, 30, '2026-01-17 10:30:00.000000', NULL, NULL, NULL),
	(5000, 1, '2026-01-17 10:30:00.000000', NULL, 51, 33, 31, '2026-01-17 10:30:00.000000', NULL, NULL, NULL),
	(170000, 2, '2026-01-18 14:55:00.000000', NULL, 52, 34, 32, '2026-01-18 14:55:00.000000', NULL, NULL, NULL),
	(135000, 5, '2026-01-19 09:20:00.000000', NULL, 53, 35, 33, '2026-01-19 09:20:00.000000', NULL, NULL, NULL),
	(15000, 1, '2026-01-19 09:20:00.000000', NULL, 54, 35, 34, '2026-01-19 09:20:00.000000', NULL, NULL, NULL),
	(140000, 3, '2026-01-22 11:45:00.000000', NULL, 55, 36, 35, '2026-01-22 11:45:00.000000', NULL, NULL, NULL),
	(180000, 3, '2026-01-23 16:10:00.000000', NULL, 56, 37, 36, '2026-01-23 16:10:00.000000', NULL, NULL, NULL),
	(185000, 2, '2026-01-24 08:35:00.000000', NULL, 57, 38, 37, '2026-01-24 08:35:00.000000', NULL, NULL, NULL),
	(160000, 4, '2026-01-25 13:00:00.000000', NULL, 58, 39, 38, '2026-01-25 13:00:00.000000', NULL, NULL, NULL),
	(10000, 1, '2026-01-25 13:00:00.000000', NULL, 59, 39, 40, '2026-01-25 13:00:00.000000', NULL, NULL, NULL),
	(205000, 2, '2026-01-26 10:25:00.000000', NULL, 60, 40, 20, '2026-01-26 10:25:00.000000', NULL, NULL, NULL),
	(190000, 3, '2026-01-28 14:50:00.000000', NULL, 61, 41, 21, '2026-01-28 14:50:00.000000', NULL, NULL, NULL),
	(160000, 2, '2026-01-29 09:15:00.000000', NULL, 62, 42, 22, '2026-01-29 09:15:00.000000', NULL, NULL, NULL),
	(140000, 5, '2026-01-30 15:40:00.000000', NULL, 63, 43, 23, '2026-01-30 15:40:00.000000', NULL, NULL, NULL),
	(10000, 1, '2026-01-30 15:40:00.000000', NULL, 64, 43, 24, '2026-01-30 15:40:00.000000', NULL, NULL, NULL),
	(150000, 3, '2026-01-31 10:05:00.000000', NULL, 65, 44, 25, '2026-01-31 10:05:00.000000', NULL, NULL, NULL),
	(190000, 3, '2026-01-31 16:30:00.000000', NULL, 66, 45, 26, '2026-01-31 16:30:00.000000', NULL, NULL, NULL),
	(160000, 3, '2026-02-01 11:20:00.000000', NULL, 67, 46, 27, '2026-02-01 11:20:00.000000', NULL, NULL, NULL),
	(10000, 1, '2026-02-01 11:20:00.000000', NULL, 68, 46, 28, '2026-02-01 11:20:00.000000', NULL, NULL, NULL),
	(180000, 2, '2026-02-02 15:45:00.000000', NULL, 69, 47, 29, '2026-02-02 15:45:00.000000', NULL, NULL, NULL),
	(155000, 4, '2026-02-03 09:10:00.000000', NULL, 70, 48, 30, '2026-02-03 09:10:00.000000', NULL, NULL, NULL),
	(10000, 1, '2026-02-03 09:10:00.000000', NULL, 71, 48, 31, '2026-02-03 09:10:00.000000', NULL, NULL, NULL),
	(200000, 2, '2026-02-04 13:35:00.000000', NULL, 72, 49, 32, '2026-02-04 13:35:00.000000', NULL, NULL, NULL),
	(140000, 5, '2026-02-05 10:00:00.000000', NULL, 73, 50, 33, '2026-02-05 10:00:00.000000', NULL, NULL, NULL),
	(20000, 1, '2026-02-05 10:00:00.000000', NULL, 74, 50, 34, '2026-02-05 10:00:00.000000', NULL, NULL, NULL),
	(140000, 3, '2026-02-08 14:25:00.000000', NULL, 75, 51, 35, '2026-02-08 14:25:00.000000', NULL, NULL, NULL),
	(10000, 1, '2026-02-08 14:25:00.000000', NULL, 76, 51, 36, '2026-02-08 14:25:00.000000', NULL, NULL, NULL),
	(180000, 3, '2026-02-09 08:50:00.000000', NULL, 77, 52, 37, '2026-02-09 08:50:00.000000', NULL, NULL, NULL),
	(10000, 1, '2026-02-09 08:50:00.000000', NULL, 78, 52, 38, '2026-02-09 08:50:00.000000', NULL, NULL, NULL),
	(190000, 2, '2026-02-10 13:15:00.000000', NULL, 79, 53, 40, '2026-02-10 13:15:00.000000', NULL, NULL, NULL),
	(130000, 5, '2026-02-11 10:40:00.000000', NULL, 80, 54, 20, '2026-02-11 10:40:00.000000', NULL, NULL, NULL),
	(20000, 1, '2026-02-11 10:40:00.000000', NULL, 81, 54, 21, '2026-02-11 10:40:00.000000', NULL, NULL, NULL),
	(205000, 2, '2026-02-12 15:05:00.000000', NULL, 82, 55, 22, '2026-02-12 15:05:00.000000', NULL, NULL, NULL),
	(195000, 3, '2026-02-15 11:30:00.000000', NULL, 83, 56, 23, '2026-02-15 11:30:00.000000', NULL, NULL, NULL),
	(5000, 1, '2026-02-15 11:30:00.000000', NULL, 84, 56, 24, '2026-02-15 11:30:00.000000', NULL, NULL, NULL),
	(170000, 2, '2026-02-16 16:55:00.000000', NULL, 85, 57, 25, '2026-02-16 16:55:00.000000', NULL, NULL, NULL),
	(140000, 5, '2026-02-17 09:20:00.000000', NULL, 86, 58, 26, '2026-02-17 09:20:00.000000', NULL, NULL, NULL),
	(10000, 1, '2026-02-17 09:20:00.000000', NULL, 87, 58, 27, '2026-02-17 09:20:00.000000', NULL, NULL, NULL),
	(150000, 3, '2026-02-18 14:45:00.000000', NULL, 88, 59, 28, '2026-02-18 14:45:00.000000', NULL, NULL, NULL),
	(10000, 1, '2026-02-18 14:45:00.000000', NULL, 89, 59, 29, '2026-02-18 14:45:00.000000', NULL, NULL, NULL),
	(175000, 3, '2026-02-19 10:10:00.000000', NULL, 90, 60, 30, '2026-02-19 10:10:00.000000', NULL, NULL, NULL),
	(5000, 1, '2026-02-19 10:10:00.000000', NULL, 91, 60, 31, '2026-02-19 10:10:00.000000', NULL, NULL, NULL),
	(90000, 3, '2026-03-02 20:09:49.000000', NULL, 92, 61, 21, NULL, NULL, NULL, NULL),
	(355000, 2, '2026-03-02 20:09:49.000000', NULL, 93, 61, 25, NULL, NULL, NULL, NULL),
	(90000, 2, '2026-03-02 20:11:13.000000', NULL, 94, 62, 21, NULL, NULL, NULL, NULL),
	(355000, 2, '2026-03-02 20:11:13.000000', NULL, 95, 62, 25, NULL, NULL, NULL, NULL),
	(90000, 2, '2026-03-02 20:14:25.000000', NULL, 96, 63, 21, NULL, NULL, NULL, NULL),
	(355000, 3, '2026-03-02 20:14:25.000000', NULL, 97, 63, 25, NULL, NULL, NULL, NULL),
	(70000, 1, '2026-03-30 22:16:42.908120', NULL, 98, 64, 26, NULL, NULL, NULL, NULL),
	(165000, 1, '2026-04-01 13:52:32.917344', NULL, 99, 65, 23, NULL, NULL, NULL, NULL),
	(160000, 1, '2026-04-01 13:52:32.932236', NULL, 100, 65, 22, NULL, NULL, NULL, NULL),
	(190000, 1, '2026-04-01 13:52:32.934191', NULL, 101, 65, 21, NULL, NULL, NULL, NULL),
	(160000, 1, '2026-04-01 13:52:32.935199', NULL, 102, 65, 27, NULL, NULL, NULL, NULL),
	(265000, 1, '2026-04-01 13:52:32.937166', NULL, 103, 65, 28, NULL, NULL, NULL, NULL),
	(190000, 2, '2026-04-01 20:59:13.864802', NULL, 104, 66, 21, NULL, NULL, NULL, NULL),
	(115000, 3, '2026-04-01 20:59:13.866748', NULL, 105, 66, 24, NULL, NULL, NULL, NULL),
	(180000, 1, '2026-04-01 20:59:13.867726', NULL, 106, 66, 26, NULL, NULL, NULL, NULL),
	(665000, 1, '2026-04-02 15:18:44.997747', NULL, 107, 67, 25, NULL, NULL, NULL, NULL),
	(60000, 1, '2026-04-02 15:18:45.007745', NULL, 108, 67, 20, NULL, NULL, NULL, NULL),
	(190000, 1, '2026-04-02 15:21:05.445726', NULL, 109, 68, 21, NULL, NULL, NULL, NULL),
	(60000, 3, '2026-04-02 15:21:05.448713', NULL, 110, 68, 20, NULL, NULL, NULL, NULL),
	(90000, 1, '2026-04-04 08:34:17.197783', NULL, 111, 69, 21, NULL, NULL, NULL, NULL),
	(355000, 2, '2026-04-04 08:34:17.205225', NULL, 112, 69, 25, NULL, NULL, NULL, NULL),
	(25000, 1, '2026-04-04 09:33:59.395185', NULL, 113, 70, 29, NULL, NULL, NULL, NULL),
	(55000, 3, '2026-04-04 09:33:59.401005', NULL, 114, 70, 22, NULL, NULL, NULL, NULL);

-- Dumping structure for table spring_fruitshop.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `total_amount` double NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_date` datetime(6) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `status` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `created_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleted_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updated_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `full_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `note` text COLLATE utf8mb4_general_ci,
  `phone` varchar(20) COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKgt3o4a5bqj59e9y6wakgk926t` (`code`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table spring_fruitshop.orders: ~63 rows (approximately)
INSERT INTO `orders` (`total_amount`, `created_at`, `deleted_at`, `id`, `order_date`, `updated_at`, `user_id`, `status`, `created_by`, `deleted_by`, `updated_by`, `address`, `full_name`, `note`, `phone`, `code`) VALUES
	(450000, '2025-12-02 10:30:00.000000', NULL, 1, '2025-12-02 10:30:00.000000', '2026-03-03 23:04:52.000000', 1, 'PENDING', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(320000, '2025-12-03 14:20:00.000000', NULL, 2, '2025-12-03 14:20:00.000000', '2025-12-06 09:15:00.000000', 2, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(550000, '2025-12-04 09:15:00.000000', NULL, 3, '2025-12-04 09:15:00.000000', '2025-12-07 16:30:00.000000', 3, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(280000, '2025-12-05 16:40:00.000000', NULL, 4, '2025-12-05 16:40:00.000000', '2025-12-05 17:00:00.000000', 4, 'CANCELLED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(620000, '2025-12-08 11:00:00.000000', NULL, 5, '2025-12-08 11:00:00.000000', '2025-12-11 10:00:00.000000', 5, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(390000, '2025-12-09 15:30:00.000000', NULL, 6, '2025-12-09 15:30:00.000000', '2025-12-12 11:20:00.000000', 1, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(480000, '2025-12-10 08:45:00.000000', NULL, 7, '2025-12-10 08:45:00.000000', '2025-12-13 15:45:00.000000', 2, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(350000, '2025-12-11 13:10:00.000000', NULL, 8, '2025-12-11 13:10:00.000000', '2025-12-14 09:30:00.000000', 3, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(720000, '2025-12-15 10:25:00.000000', NULL, 9, '2025-12-15 10:25:00.000000', '2025-12-18 14:15:00.000000', 4, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(410000, '2025-12-16 14:50:00.000000', NULL, 10, '2025-12-16 14:50:00.000000', '2025-12-19 10:40:00.000000', 5, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(530000, '2025-12-17 09:30:00.000000', NULL, 11, '2025-12-17 09:30:00.000000', '2025-12-20 16:20:00.000000', 1, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(310000, '2025-12-18 16:15:00.000000', NULL, 12, '2025-12-18 16:15:00.000000', '2025-12-18 17:00:00.000000', 2, 'CANCELLED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(680000, '2025-12-22 11:40:00.000000', NULL, 13, '2025-12-22 11:40:00.000000', '2025-12-26 09:00:00.000000', 3, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(420000, '2025-12-23 15:20:00.000000', NULL, 14, '2025-12-23 15:20:00.000000', '2025-12-27 11:30:00.000000', 4, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(560000, '2025-12-24 08:55:00.000000', NULL, 15, '2025-12-24 08:55:00.000000', '2025-12-28 14:45:00.000000', 5, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(380000, '2025-12-25 13:30:00.000000', NULL, 16, '2025-12-25 13:30:00.000000', '2025-12-29 10:15:00.000000', 1, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(490000, '2025-12-27 10:10:00.000000', NULL, 17, '2025-12-27 10:10:00.000000', '2025-12-30 15:20:00.000000', 2, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(340000, '2025-12-28 14:45:00.000000', NULL, 18, '2025-12-28 14:45:00.000000', '2025-12-31 09:30:00.000000', 3, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(610000, '2025-12-29 09:20:00.000000', NULL, 19, '2025-12-29 09:20:00.000000', '2026-01-02 11:00:00.000000', 4, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(370000, '2025-12-30 16:35:00.000000', NULL, 20, '2025-12-30 16:35:00.000000', '2026-01-03 14:20:00.000000', 5, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(520000, '2026-01-02 10:15:00.000000', NULL, 21, '2026-01-02 10:15:00.000000', '2026-01-05 09:30:00.000000', 1, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(410000, '2026-01-03 14:30:00.000000', NULL, 22, '2026-01-03 14:30:00.000000', '2026-01-06 11:45:00.000000', 2, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(590000, '2026-01-04 09:45:00.000000', NULL, 23, '2026-01-04 09:45:00.000000', '2026-01-07 15:20:00.000000', 3, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(330000, '2026-01-05 16:20:00.000000', NULL, 24, '2026-01-05 16:20:00.000000', '2026-01-08 10:00:00.000000', 4, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(670000, '2026-01-06 11:10:00.000000', NULL, 25, '2026-01-06 11:10:00.000000', '2026-01-09 14:30:00.000000', 5, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(440000, '2026-01-08 15:40:00.000000', NULL, 26, '2026-01-08 15:40:00.000000', '2026-01-11 09:15:00.000000', 1, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(510000, '2026-01-09 08:25:00.000000', NULL, 27, '2026-01-09 08:25:00.000000', '2026-01-12 13:40:00.000000', 2, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(380000, '2026-01-10 13:50:00.000000', NULL, 28, '2026-01-10 13:50:00.000000', '2026-01-13 16:10:00.000000', 3, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(620000, '2026-01-11 10:05:00.000000', NULL, 29, '2026-01-11 10:05:00.000000', '2026-01-14 11:25:00.000000', 4, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(390000, '2026-01-12 14:35:00.000000', NULL, 30, '2026-01-12 14:35:00.000000', '2026-01-12 15:00:00.000000', 5, 'CANCELLED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(730000, '2026-01-15 09:50:00.000000', NULL, 31, '2026-01-15 09:50:00.000000', '2026-01-18 10:20:00.000000', 1, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(460000, '2026-01-16 15:15:00.000000', NULL, 32, '2026-01-16 15:15:00.000000', '2026-01-19 14:35:00.000000', 2, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(560000, '2026-01-17 10:30:00.000000', NULL, 33, '2026-01-17 10:30:00.000000', '2026-01-20 09:45:00.000000', 3, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(340000, '2026-01-18 14:55:00.000000', NULL, 34, '2026-01-18 14:55:00.000000', '2026-01-21 13:10:00.000000', 4, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(690000, '2026-01-19 09:20:00.000000', NULL, 35, '2026-01-19 09:20:00.000000', '2026-01-22 16:30:00.000000', 5, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(420000, '2026-01-22 11:45:00.000000', NULL, 36, '2026-01-22 11:45:00.000000', '2026-01-25 10:15:00.000000', 1, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(540000, '2026-01-23 16:10:00.000000', NULL, 37, '2026-01-23 16:10:00.000000', '2026-01-26 14:40:00.000000', 2, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(370000, '2026-01-24 08:35:00.000000', NULL, 38, '2026-01-24 08:35:00.000000', '2026-01-27 09:55:00.000000', 3, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(650000, '2026-01-25 13:00:00.000000', NULL, 39, '2026-01-25 13:00:00.000000', '2026-01-28 11:20:00.000000', 4, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(410000, '2026-01-26 10:25:00.000000', NULL, 40, '2026-01-26 10:25:00.000000', '2026-01-29 15:45:00.000000', 5, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(580000, '2026-01-28 14:50:00.000000', NULL, 41, '2026-01-28 14:50:00.000000', '2026-01-31 10:10:00.000000', 1, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(320000, '2026-01-29 09:15:00.000000', NULL, 42, '2026-01-29 09:15:00.000000', '2026-01-29 10:00:00.000000', 2, 'CANCELLED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(710000, '2026-01-30 15:40:00.000000', NULL, 43, '2026-01-30 15:40:00.000000', '2026-02-02 14:25:00.000000', 3, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(450000, '2026-01-31 10:05:00.000000', NULL, 44, '2026-01-31 10:05:00.000000', '2026-02-03 09:50:00.000000', 4, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(570000, '2026-01-31 16:30:00.000000', NULL, 45, '2026-01-31 16:30:00.000000', '2026-02-04 13:15:00.000000', 5, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(490000, '2026-02-01 11:20:00.000000', NULL, 46, '2026-02-01 11:20:00.000000', '2026-02-04 10:35:00.000000', 1, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(360000, '2026-02-02 15:45:00.000000', NULL, 47, '2026-02-02 15:45:00.000000', '2026-02-05 14:20:00.000000', 2, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(630000, '2026-02-03 09:10:00.000000', NULL, 48, '2026-02-03 09:10:00.000000', '2026-02-03 09:10:00.000000', 3, 'SHIPPING', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(400000, '2026-02-04 13:35:00.000000', NULL, 49, '2026-02-04 13:35:00.000000', '2026-02-07 11:50:00.000000', 4, 'COMPLETED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(720000, '2026-02-05 10:00:00.000000', NULL, 50, '2026-02-05 10:00:00.000000', '2026-02-05 10:00:00.000000', 5, 'SHIPPING', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(430000, '2026-02-08 14:25:00.000000', NULL, 51, '2026-02-08 14:25:00.000000', '2026-02-08 14:25:00.000000', 1, 'CONFIRMED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(550000, '2026-02-09 08:50:00.000000', NULL, 52, '2026-02-09 08:50:00.000000', '2026-02-09 08:50:00.000000', 2, 'SHIPPING', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(380000, '2026-02-10 13:15:00.000000', NULL, 53, '2026-02-10 13:15:00.000000', '2026-02-10 13:15:00.000000', 3, 'CONFIRMED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(670000, '2026-02-11 10:40:00.000000', NULL, 54, '2026-02-11 10:40:00.000000', '2026-02-11 10:40:00.000000', 4, 'SHIPPING', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(410000, '2026-02-12 15:05:00.000000', NULL, 55, '2026-02-12 15:05:00.000000', '2026-02-12 15:05:00.000000', 5, 'CONFIRMED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(590000, '2026-02-15 11:30:00.000000', NULL, 56, '2026-02-15 11:30:00.000000', '2026-02-15 11:30:00.000000', 1, 'SHIPPING', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(340000, '2026-02-16 16:55:00.000000', NULL, 57, '2026-02-16 16:55:00.000000', '2026-02-16 16:55:00.000000', 2, 'CONFIRMED', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(710000, '2026-02-17 09:20:00.000000', NULL, 58, '2026-02-17 09:20:00.000000', '2026-02-17 09:20:00.000000', 3, 'SHIPPING', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(460000, '2026-02-18 14:45:00.000000', NULL, 59, '2026-02-18 14:45:00.000000', '2026-02-18 14:45:00.000000', 4, 'PENDING', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(530000, '2026-02-19 10:10:00.000000', NULL, 60, '2026-02-19 10:10:00.000000', '2026-02-19 10:10:00.000000', 5, 'PENDING', NULL, NULL, NULL, '', '', NULL, '', NULL),
	(980000, '2026-03-02 20:09:49.000000', NULL, 61, '2026-03-02 20:09:49.000000', NULL, 6, 'PENDING', NULL, NULL, NULL, 'Đà Nẵng', 'abc1', '', '3848586918', NULL),
	(890000, '2026-03-02 20:11:13.000000', NULL, 62, '2026-03-02 20:11:13.000000', NULL, 6, 'PENDING', NULL, NULL, NULL, 'Đà Nẵng', 'abc1', '', '3848586918', NULL),
	(1245000, '2026-03-02 20:14:25.000000', NULL, 63, '2026-03-02 20:14:25.000000', NULL, 6, 'PENDING', NULL, NULL, NULL, 'Đà Nẵng', 'abc1', '', '3848586918', NULL),
	(70000, '2026-03-30 22:16:42.887320', NULL, 64, '2026-03-30 22:16:42.881104', '2026-03-30 22:16:42.908933', 7, 'PENDING', NULL, NULL, NULL, 'Admin Address', 'Administrator', '', '0000000000', 'ORD-20260330-0001'),
	(940000, '2026-04-01 13:52:32.914418', NULL, 65, '2026-04-01 13:52:32.858015', NULL, 11, 'PENDING', NULL, NULL, NULL, 'Da Nang', 'Nguyen Van Test', '', '0342347716', 'ORD-20260401-0001'),
	(905000, '2026-04-01 20:59:13.861318', NULL, 66, '2026-04-01 20:59:13.823792', '2026-04-01 23:22:07.076881', 11, 'COMPLETED', NULL, NULL, NULL, 'Da Nang', 'Nguyen Van Test', 'Abc....', '0342347716', 'ORD-20260401-0002'),
	(725000, '2026-04-02 15:18:44.984651', NULL, 67, '2026-04-02 15:18:44.887839', NULL, 11, 'PENDING', NULL, NULL, NULL, 'Da Nang', 'Nguyen Van Test', 'adv', '0342347716', 'ORD-20260402-0001'),
	(370000, '2026-04-02 15:21:05.438830', NULL, 68, '2026-04-02 15:21:05.386862', NULL, 11, 'PENDING', NULL, NULL, NULL, 'Da Nang', 'Nguyen Van T', 'asdfdsafi', '0342347716', 'ORD-20260402-0002'),
	(800000, '2026-04-04 08:34:17.128809', NULL, 69, '2026-04-04 08:34:17.121595', '2026-04-04 08:34:17.211535', 7, 'PENDING', NULL, NULL, NULL, 'Admin Address saf', 'Administrator sadf', 'abc', '0342347716', 'ORD-20260404-0001'),
	(190000, '2026-04-04 09:33:59.332677', NULL, 70, '2026-04-04 09:33:59.310816', '2026-04-04 09:33:59.407096', 11, 'PENDING', NULL, NULL, NULL, 'Da Nang', 'Nguyen Van T', '', '0342347716', 'ORD-20260404-0002');

-- Dumping structure for table spring_fruitshop.product_reviews
CREATE TABLE IF NOT EXISTS `product_reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `comment` text,
  `created_at` datetime(6) DEFAULT NULL,
  `rating` int NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK35kxxqe2g9r4mww80w9e3tnw9` (`product_id`),
  KEY `FK58i39bhws2hss3tbcvdmrm60f` (`user_id`),
  CONSTRAINT `FK35kxxqe2g9r4mww80w9e3tnw9` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FK58i39bhws2hss3tbcvdmrm60f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table spring_fruitshop.product_reviews: ~4 rows (approximately)
INSERT INTO `product_reviews` (`id`, `comment`, `created_at`, `rating`, `updated_at`, `product_id`, `user_id`) VALUES
	(1, 'abc', '2026-04-01 13:50:21.386007', 5, '2026-04-01 13:50:21.386982', 21, 11),
	(2, 'abc', '2026-04-01 13:50:35.102243', 5, '2026-04-01 13:50:35.102243', 20, 11),
	(3, 'a', '2026-04-02 14:56:30.898006', 5, '2026-04-02 14:56:30.898006', 25, 11),
	(4, 'das', '2026-04-02 15:19:46.099567', 5, '2026-04-02 15:19:46.099567', 22, 11);

-- Dumping structure for table spring_fruitshop.products
CREATE TABLE IF NOT EXISTS `products` (
  `price` double NOT NULL,
  `pricesale` double NOT NULL,
  `quantity` int NOT NULL,
  `category_id` bigint DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleted_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updated_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `image_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `slug` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKostq1ec3toafnjok09y9l7dox` (`slug`),
  KEY `FKog2rp4qthbtt2lfyhfo32lsw9` (`category_id`),
  CONSTRAINT `FKog2rp4qthbtt2lfyhfo32lsw9` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table spring_fruitshop.products: ~20 rows (approximately)
INSERT INTO `products` (`price`, `pricesale`, `quantity`, `category_id`, `created_at`, `deleted_at`, `id`, `updated_at`, `created_by`, `deleted_by`, `updated_by`, `name`, `description`, `image_url`, `slug`) VALUES
	(60000, 55000, 49, 3, NULL, NULL, 20, '2026-04-02 15:21:05.422308', NULL, NULL, NULL, 'Sầu riêng Miền Nam', 'Nếu bạn tiêu thụ khoảng 234g sầu riêng điều đó tương đương với bạn hấp thụ khoảng 20% carbohydrate cần trong ngày...', 'assets/images/saurieng1-1000x1000.webp', 'sau-rieng-mien-nam'),
	(190000, 90000, 25, 2, NULL, NULL, 21, '2026-04-02 15:21:05.397866', NULL, NULL, NULL, 'Bông cải đen', 'Thông tin đang được cập nhật', 'assets/images/bongcaitrang1-1000x1000.webp', 'bong-cai-den'),
	(160000, 55000, 67, 3, NULL, NULL, 22, '2026-04-01 13:52:32.878293', NULL, NULL, NULL, 'Quả thanh long', 'Thông tin sản phẩm đang được cập nhật', 'assets/images/thanhlong1-1000x1000.webp', 'qua-thanh-long'),
	(165000, 80000, 62, 3, NULL, NULL, 23, '2026-04-01 13:52:32.863021', NULL, NULL, NULL, 'Măng cụt chín Miền Nam', 'Thông tin đang cập nhật', 'assets/images/mangcut1-1000x1000.webp', 'mang-cut-chin-mien-nam'),
	(115000, 40000, 7, 2, NULL, NULL, 24, '2026-04-01 20:59:13.844424', NULL, NULL, NULL, 'Lựu đỏ Nam Phi nhập khẩu', 'Hạt lựu chín có giá trị dinh dưỡng cao... khi ăn không nên nuốt hạt lựu...', 'assets/images/qualuu1-1000x1000.webp', 'luu-do-nam-phi-nhap-khau'),
	(665000, 355000, 34, 1, NULL, NULL, 25, '2026-04-02 15:18:44.901695', NULL, NULL, NULL, 'Quả đu đủ', 'Thông tin sản phẩm đang được cập nhật', 'assets/images/dudu1.webp', 'qua-du-du'),
	(180000, 70000, 42, 2, NULL, NULL, 26, '2026-04-01 20:59:13.850313', NULL, NULL, NULL, 'Dứa vàng nhập khẩu Mỹ', 'Là một nguồn tốt của nhiều chất dinh dưỡng... bromelain...', 'assets/images/duavang1.webp', 'dua-vang-nhap-khau-my'),
	(160000, 60000, 99, 3, NULL, NULL, 27, '2026-04-01 13:52:32.893176', NULL, NULL, NULL, 'Củ gừng ta', 'Thông tin sản phẩm đang được cập nhật', 'assets/images/cugung1.webp', 'cu-gung-ta'),
	(265000, 235000, 88, 2, NULL, NULL, 28, '2026-04-01 13:52:32.899046', NULL, NULL, NULL, 'Cà rốt nhập khẩu', 'Thông tin sản phẩm đang được cập nhật', 'assets/images/carot1.webp', 'ca-rot-nhap-khau'),
	(30000, 25000, 44, 1, NULL, NULL, 29, '2026-03-01 14:24:51.000000', NULL, NULL, NULL, 'Súp lơ xanh', 'Nguồn gốc: Việt Nam Khối lượng: 1kg...', 'assets/images/suplo1.webp', 'sup-lo-xanh'),
	(60000, 55000, 35, 3, NULL, NULL, 30, NULL, NULL, NULL, NULL, 'Hành tây đặc biệt', 'Nguồn gốc: Việt Nam... làm giảm cholesterol.', 'assets/images/hanhtaydacbiet1.webp', NULL),
	(45000, 30000, 35, 3, NULL, NULL, 31, NULL, NULL, NULL, NULL, 'Quả na miền bắc', 'Nguồn gốc: Việt Nam... bảo vệ sức khỏe', 'assets/images/quanamienbac1.webp', NULL),
	(340000, 240000, 60, 1, NULL, NULL, 32, NULL, NULL, NULL, NULL, 'Rau xanh đặc biệt', 'Thông tin sản phẩm đang được cập nhật', 'assets/images/rauxanhdacbiet1.webp', NULL),
	(260000, 230000, 95, 1, NULL, NULL, 33, NULL, NULL, NULL, NULL, 'Khoai tây đỏ', 'Thông tin sản phẩm đang được cập nhật', 'assets/images/khoaitaydo1.jpg', NULL),
	(220000, 160000, 13, 2, NULL, NULL, 34, NULL, NULL, NULL, NULL, 'Táo xanh Mỹ', 'Nguồn gốc: Mỹ... Táo có màu xanh lá, vị chua đậm...', 'assets/images/taoxanhmy1.webp', NULL),
	(480000, 350000, 42, 3, NULL, NULL, 35, '2026-03-01 14:21:24.000000', NULL, NULL, NULL, 'Khoai lang ta', 'Thông tin sản phẩm đang được cập nhật', 'assets/images/khoailang1.jpg', 'khoai-lang-ta'),
	(39000, 31000, 72, 2, NULL, NULL, 36, NULL, NULL, NULL, NULL, 'Cà chua nhập khẩu', 'Thông tin sản phẩm đang được cập nhật', 'assets/images/cachua1.webp', NULL),
	(40000, 79000, 41, 3, NULL, NULL, 37, NULL, NULL, NULL, NULL, 'Dâu tây Đà Lạt', 'Nguồn gốc: Việt Nam... trái ngon và bổ không thể bỏ qua.', 'assets/images/dautaydalat1.webp', NULL),
	(90000, 30000, 69, 2, NULL, NULL, 38, NULL, NULL, NULL, NULL, 'Chuối Laba nhập khẩu Thái Lan', 'Chuối là một trong những loại trái cây được tiêu thụ rộng rãi nhất...', 'assets/images/chuoi1.webp', NULL),
	(232, 41243, 1234, 2, '2026-03-02 01:20:14.000000', NULL, 40, '2026-04-02 09:40:59.478993', NULL, NULL, NULL, 'abcc', 'ádvs', '/uploads/products/3c2ad28e-8215-41dc-a0d5-a6688df1507c.jpg', 'abcc');

-- Dumping structure for table spring_fruitshop.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `created_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleted_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `updated_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table spring_fruitshop.roles: ~2 rows (approximately)
INSERT INTO `roles` (`created_at`, `deleted_at`, `id`, `updated_at`, `name`, `created_by`, `deleted_by`, `updated_by`) VALUES
	('2026-03-02 20:29:41.000000', NULL, 4, '2026-03-03 21:37:35.000000', 'ROLE_USER', NULL, NULL, NULL),
	('2026-03-02 20:29:41.000000', NULL, 5, '2026-03-03 21:37:35.000000', 'ROLE_ADMIN', NULL, NULL, NULL);

-- Dumping structure for table spring_fruitshop.user_roles
CREATE TABLE IF NOT EXISTS `user_roles` (
  `role_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`),
  KEY `FKhfh9dx7w3ubf1co1vdev94g3f` (`user_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table spring_fruitshop.user_roles: ~7 rows (approximately)
INSERT INTO `user_roles` (`role_id`, `user_id`) VALUES
	(4, 4),
	(4, 5),
	(4, 8),
	(4, 10),
	(4, 11),
	(5, 6),
	(5, 7),
	(5, 9);

-- Dumping structure for table spring_fruitshop.users
CREATE TABLE IF NOT EXISTS `users` (
  `enabled` bit(1) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `deleted_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `full_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `updated_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table spring_fruitshop.users: ~10 rows (approximately)
INSERT INTO `users` (`enabled`, `created_at`, `deleted_at`, `id`, `updated_at`, `created_by`, `deleted_by`, `email`, `full_name`, `updated_by`, `address`, `password`, `phone`, `avatar`) VALUES
	(b'1', '2026-03-02 01:45:09.000000', NULL, 1, '2026-03-02 01:45:09.000000', NULL, NULL, 'nguyenvana@gmail.com', 'Nguyễn Văn A', NULL, '123 Lý Lợi, Quận 1, TP.HCM', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '0901234567', NULL),
	(b'1', '2026-03-02 01:45:09.000000', NULL, 2, '2026-03-02 01:45:09.000000', NULL, NULL, 'tranthib@gmail.com', 'Trần Thị B', NULL, '456 Nguyễn Huệ, Quận 1, TP.HCM', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '0902345678', NULL),
	(b'1', '2026-03-02 01:45:09.000000', NULL, 3, '2026-03-02 01:45:09.000000', NULL, NULL, 'levanc@gmail.com', 'Lê Văn C', NULL, '789 Hai Bà Trưng, Quận 3, TP.HCM', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '0903456789', NULL),
	(b'0', '2026-03-02 01:45:09.000000', NULL, 4, '2026-03-03 21:42:32.000000', NULL, NULL, 'phamthid@gmail.com', 'Phạm Thị D', NULL, '321 Điện Biên Phủ, Bình Thạnh, TP.HCM', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '0904567890', NULL),
	(b'1', '2026-03-02 01:45:09.000000', NULL, 5, '2026-03-03 21:41:18.000000', NULL, NULL, 'hoangvane@gmail.com', 'Hoàng Văn E', NULL, '654 Cách Mạng Tháng 8, Quận 10, TP.HCM', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', '0905678901', NULL),
	(b'1', '2026-03-02 19:09:44.000000', '2026-03-03 22:05:24.000000', 6, '2026-03-03 22:05:24.000000', NULL, 'admin@gmail.com', 'abc1@gmail.com', 'abc2', NULL, 'Đà Nẵng', '$2a$10$WN49X0niF3jPfUiecq/kYOvQ9gJK1Hj91DO8gI5VBg7QXxsIjtf42', '3848586918', '/assets/images/uploads/users/9e809ace-dd69-418c-9e01-37518b19a678.png'),
	(b'1', '2026-03-02 20:21:45.000000', NULL, 7, NULL, NULL, NULL, 'admin@gmail.com', 'Administrator', NULL, 'Admin Address', '$2a$10$tbpWMblYlXuhjwfeppOUw.Cxindt/ogfEXZ7YpNZjWIGgpZNC2QZW', '0000000000', NULL),
	(b'1', '2026-03-03 22:10:16.000000', NULL, 8, '2026-03-03 22:24:26.000000', NULL, NULL, 'abc2@gmail.com', 'abc12432', NULL, 'av', '$2a$10$cYWx6/6zTW4LLVAu.3M59enIp.t5VtuAT70J4qX6CfYuMXChzs0iC', '0342347716', '/uploads/users/d21b1dfe-02df-4437-aa45-4bd07f177c7b.png'),
	(b'1', '2026-03-03 22:28:00.000000', NULL, 9, '2026-03-03 22:30:14.000000', NULL, NULL, 'admin2@gmail.com', 'admin22', NULL, 'Quảng Nam', '$2a$10$.Js3J2uBwYPgqiY8WyzR5O0rm6W/XyPYw1mzWVmnuj9JhWdvJWpO6', '114124', '/uploads/users/8bc28aee-72d8-4121-991d-3643e2c01f60.png'),
	(b'1', '2026-03-03 22:34:56.000000', '2026-04-01 15:06:36.253121', 10, '2026-04-01 15:06:36.254110', NULL, 'admin@gmail.com', 'abc3@gmail.com', 'abc3', NULL, 'Quảng Nam', '$2a$10$47yHdzOOWwdlPFSzz8MIt.3WyGNekFuBphX2qXfDvo8N/1yj68mV.', '0342347716', NULL),
	(b'1', '2026-04-01 13:49:47.278006', NULL, 11, '2026-04-02 15:19:00.226792', NULL, NULL, 'test01@gmail.com', 'Nguyen Van T', NULL, 'Da Nang', '$2a$10$1CmjQJib27BhyHH2NkYFOeNdUPVzpO2sGlweJ9Z63yO8X5zMp.Uk.', '0342347716', NULL);

-- Dumping structure for table spring_fruitshop.wishlists
CREATE TABLE IF NOT EXISTS `wishlists` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `product_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl7ao98u2bm8nijc1rv4jobcrx` (`product_id`),
  KEY `FK330pyw2el06fn5g28ypyljt16` (`user_id`),
  CONSTRAINT `FK330pyw2el06fn5g28ypyljt16` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKl7ao98u2bm8nijc1rv4jobcrx` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table spring_fruitshop.wishlists: ~5 rows (approximately)
INSERT INTO `wishlists` (`id`, `created_at`, `product_id`, `user_id`) VALUES
	(8, '2026-04-01 14:58:13.381860', 25, 11),
	(13, '2026-04-01 20:56:54.188657', 20, 11),
	(17, '2026-04-02 15:18:06.262785', 24, 11),
	(18, '2026-04-02 15:18:08.852836', 40, 11),
	(19, '2026-04-02 15:20:01.732253', 21, 11);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
