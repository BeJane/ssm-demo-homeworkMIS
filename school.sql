CREATE DATABASE `school` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE TABLE `s_user` (
  `account` char(8) NOT NULL,
  `password` char(18) NOT NULL,
  `name` varchar(20) NOT NULL,
  `type` int NOT NULL,
  PRIMARY KEY (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `s_homework` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(20) DEFAULT NULL,
  `content` text,
  `score` int NOT NULL,
  `techerid` char(8) NOT NULL,
  `create_time` timestamp NOT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `teacher_name` char(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `s_student_homework` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` char(8) DEFAULT NULL,
  `homework_id` bigint NOT NULL,
  `homework_title` varchar(45) NOT NULL,
  `homework_content` text,
  `score` int DEFAULT NULL,
  `comment` char(150) DEFAULT NULL,
  `create_time` timestamp NOT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
