CREATE DATABASE  IF NOT EXISTS `foro_hub` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `foro_hub`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: foro_hub
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
-- Table structure for table `flyway_schema_history`
--

DROP TABLE IF EXISTS `flyway_schema_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flyway_schema_history`
--

LOCK TABLES `flyway_schema_history` WRITE;
/*!40000 ALTER TABLE `flyway_schema_history` DISABLE KEYS */;
INSERT INTO `flyway_schema_history` VALUES (1,'1','<< Flyway Baseline >>','BASELINE','<< Flyway Baseline >>',NULL,'root','2024-12-01 19:41:15',0,1),(2,'2','update topics status enum','SQL','V2__update_topics_status_enum.sql',-77646558,'root','2024-12-06 20:35:46',262,1),(3,'3','create-users-table','SQL','V3__create-users-table.sql',1070881802,'root','2024-12-07 18:48:57',121,1),(4,'4','Change default status to PENDING','SQL','V4__Change_default_status_to_PENDING.sql',1341747448,'root','2024-12-07 19:16:03',237,1),(5,'5','rename name to username in users','SQL','V5__rename_name_to_username_in_users.sql',-1461736113,'root','2024-12-08 17:21:18',224,1),(6,'6','Renombrar columna email','SQL','V6__Renombrar_columna_email.sql',-1980429571,'root','2024-12-08 18:35:32',173,1),(7,'7','drop-column-e mail','SQL','V7__drop-column-e_mail.sql',809473464,'root','2024-12-08 18:44:22',102,1);
/*!40000 ALTER TABLE `flyway_schema_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topics`
--

DROP TABLE IF EXISTS `topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `topics` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `message` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(10) DEFAULT 'PENDING',
  `author` varchar(255) DEFAULT NULL,
  `course` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `topics`
--

LOCK TABLES `topics` WRITE;
/*!40000 ALTER TABLE `topics` DISABLE KEYS */;
INSERT INTO `topics` VALUES (1,'¿Cómo usar correctamente Flyway?','Tengo problemas con mis migraciones.','2024-12-04 10:15:47','ACTIVE','Mariana','Spring Boot'),(2,'¿Cómo empezar con Spring Boot?','Soy nuevo en Spring Boot, ¿qué recursos me recomiendan para aprender?','2024-12-04 11:14:06','ACTIVE','Juan Pérez','Spring Boot Basics'),(3,'Errores más comunes en JPA','Buenas tardes. Cuáles son los errores más comunes al trabajar con JPA y cómo solucionarlos.','2024-12-04 11:14:19','ACTIVE','Carla Gómez','Java Persistence API'),(4,'Patrones de diseño para REST APIs','¿Qué patrones de diseño son recomendables para desarrollar APIs REST eficientes?','2024-12-04 11:14:34','ACTIVE','Luis Fernández','Arquitectura de Software'),(5,'Problemas con validaciones en Spring','Estoy usando `@Valid` en mis controladores, pero no se activan las validaciones. ¿Qué puede estar fallando?','2024-12-04 11:14:51','ACTIVE','Ana López','Spring MVC'),(6,'Configuración de Flyway en Spring','¿Cómo manejar migraciones de base de datos en proyectos grandes con Flyway?','2024-12-04 11:15:06','ACTIVE','Miguel Rodríguez','DevOps para Developers'),(7,'Integración de Insomnia con APIs','¿Cómo puedo usar Insomnia para probar una API REST de manera eficiente?','2024-12-04 11:43:46','ACTIVE','Sofía García','Herramientas para Desarrollo Web'),(8,'Errores comunes en Java','Discutamos cuáles son los errores más comunes al aprender Java y cómo solucionarlos.','2024-12-04 14:34:24','ACTIVE','Gabriela Peralta','Java Básico'),(9,'Mejoras en aplicaciones web','¿Qué técnicas y herramientas están usando para optimizar aplicaciones web en términos de rendimiento?','2024-12-04 14:36:20','ACTIVE','Luciana López','Desarrollo Web'),(10,'Cómo aprender patrones de diseño en Java','Estoy buscando consejos y recursos para aprender patrones de diseño en Java. ¿Alguien puede ayudarme?','2024-12-05 17:24:17','ACTIVE','Sofía Ramírez','Java Avanzado'),(11,'Buenas prácticas en programación','¿Qué técnicas y enfoques utilizan para mantener su código limpio y entendible?','2024-12-06 12:17:47','ACTIVE','Lucía Fernández','Ingeniería de Software'),(12,'Problemas al ejecutar scripts SQL en MySQL','Estoy teniendo dificultades para ejecutar scripts SQL que tienen múltiples sentencias separadas por punto y coma. ¿Es un problema del cliente o de la configuración del servidor?','2024-12-06 14:01:20','ACTIVE','Sofía López','Bases de Datos Avanzadas'),(13,'Duplicated Topic','Este mensaje ya fue registrado','2024-12-06 15:40:51','ACTIVE','Ana López','Spring'),(16,'Hi','Mensaje válido','2024-12-06 15:42:17','DELETED','Juan Pérez','Spring'),(18,'Cómo mejorar en programación','Quiero compartir algunos tips para aprender programación más rápido.','2024-12-07 16:21:09','PENDING','Juan Pérez','Introducción a la Programación'),(19,'Patrones de diseño en Spring Boot','Una guía detallada para implementar patrones de diseño comunes en proyectos Spring.','2024-12-07 16:22:03','PENDING','Laura Fernández','Arquitectura y Diseño de Software'),(20,'Problemas con Netflix en mi Smart TV','No puedo iniciar sesión en mi televisor Samsung, ¿alguien sabe cómo solucionarlo?','2024-12-07 16:51:58','DELETED','Usuario Despistado','Soporte Técnico General'),(21,'Problema con configuración de Spring Boot','Tengo problemas para configurar un datasource en mi proyecto. ¿Cómo puedo usar HikariCP con múltiples bases de datos?','2024-12-08 14:30:33','PENDING','Ana Martínez','Spring Boot avanzado');
/*!40000 ALTER TABLE `topics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Juan Perez','$2a$12$Reyu/LAyZIHSdrxkI.3j2emZCUOelm22YvTMRmLWwDqmaRDLC07b2','juan.perez@forohub.com'),(2,'Carlos Lopez','$2a$12$TPlzjeVwlaHcIIMYuQCz3uQGtMYCFqRQkMY26SVozeaXBXfMs/Iwy','carlos.lopez@forohub.com'),(3,'Maria Garcia','$2a$12$FitsBgeIfjyzglG1lgu.CeHJhwoTrk4h3iZJp20b4RAt4adm4budu','maria.garcia@forohub.com'),(4,'usuario.prueba1','$2a$12$1pqd4x2gw1J//LUrO5a5AONLDU.1nlkRVHneETDaNUiQJYCg6DaGa','usuario.prueba1@forohub.com'),(5,'usuario.prueba2','$2a$12$TDJbdAIAFVIKPXLyBS0LTumqKKLmtllIuSKueFPvai3sIQdwXB9Pu','usuario.prueba2@forohub.com'),(6,'mariana.lois','$2a$12$QyEQK92EQ.n1el1WfWLl4ODfSvQ34PnMJuAh4fAde8UVbV5LhSJ06','mariana.lois@forohub.com');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'foro_hub'
--

--
-- Dumping routines for database 'foro_hub'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-15 14:34:47
