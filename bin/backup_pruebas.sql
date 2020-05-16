-- MySQL dump 10.13  Distrib 5.7.14, for Win64 (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	5.7.14

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clientes` (
  `Dni` varchar(9) NOT NULL,
  `Nombre` varchar(45) DEFAULT NULL,
  `Apellido` varchar(45) DEFAULT NULL,
  `Domicilio` varchar(45) DEFAULT NULL,
  `Telefono` varchar(45) DEFAULT NULL,
  `Email` varchar(45) DEFAULT NULL,
  `Eliminado` int(1) DEFAULT '0',
  PRIMARY KEY (`Dni`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES ('10024223','juancito','','','','juancitoelcapo@gmail.com',1),('12345670','Pepe','Grillo','','','',1),('12345678','Test Subject #01','Ahre','','','a@b.com',0),('13902761','pepito','grillo','','','',1),('27217212','ddadsda','','','','',1),('34868304','Lucas Martín','Rolón','Mz 10 Pc 1 B° M. Moreno','0364-4420748','lucasmrolon@gmail.com',0),('34901009','Sabrina Romina','Aquino','','','',0),('40034659','Alexis Agustín','Romaniuk','Calle 11 E/ 10 y 12 E. Sur','03644 616844','alexisromaniuk@gmail.com',0),('52412431','juancito','','','','',1),('563523453','Emmanuel','Chavez','','','',1),('63525221','Pepe Alberto','Mujica','','','elpresipepe@gmail.com',NULL),('63535353','pepe','','','','',1),('63635352','pepe','','','','',1),('63635532','pepe mujica','','','','',1);
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cuentascorrientes`
--

DROP TABLE IF EXISTS `cuentascorrientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cuentascorrientes` (
  `CodigoCuenta` int(11) NOT NULL AUTO_INCREMENT,
  `Estado` double DEFAULT NULL,
  `Clientes_Dni` varchar(9) DEFAULT NULL,
  `Eliminado` int(1) DEFAULT NULL,
  PRIMARY KEY (`CodigoCuenta`),
  KEY `fk_CuentasCorrientes_Clientes1_idx` (`Clientes_Dni`),
  CONSTRAINT `fk_CuentasCorrientes_Clientes1` FOREIGN KEY (`Clientes_Dni`) REFERENCES `clientes` (`Dni`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuentascorrientes`
--

LOCK TABLES `cuentascorrientes` WRITE;
/*!40000 ALTER TABLE `cuentascorrientes` DISABLE KEYS */;
INSERT INTO `cuentascorrientes` VALUES (1,2603.02,'34868304',NULL),(2,2058.7799999999997,'34901009',NULL),(3,10828.21,'40034659',NULL),(4,500,NULL,1),(5,300,NULL,1),(6,585,NULL,1),(7,1000,NULL,1),(8,300,NULL,1),(9,100,NULL,1),(10,100,NULL,1);
/*!40000 ALTER TABLE `cuentascorrientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleados`
--

DROP TABLE IF EXISTS `empleados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `empleados` (
  `IdEmpleado` int(11) NOT NULL,
  `Tipo` varchar(20) NOT NULL,
  `Nombre` varchar(45) DEFAULT NULL,
  `Apellido` varchar(45) DEFAULT NULL,
  `Dni` varchar(45) DEFAULT NULL,
  `Domicilio` varchar(45) DEFAULT NULL,
  `Email` varchar(45) DEFAULT NULL,
  `Telefono` varchar(45) DEFAULT NULL,
  `Usuario` varchar(20) NOT NULL,
  `Password` varchar(20) NOT NULL,
  `Conectado` int(1) NOT NULL DEFAULT '0',
  `Eliminado` int(1) DEFAULT NULL,
  PRIMARY KEY (`IdEmpleado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleados`
--

LOCK TABLES `empleados` WRITE;
/*!40000 ALTER TABLE `empleados` DISABLE KEYS */;
INSERT INTO `empleados` VALUES (1,'administrador','Administrador',NULL,NULL,NULL,NULL,NULL,'admin','pass',1,NULL),(2,'normal','Lucas ','Rolón','34868304','Mz 10 Pc 1 B° M. Moreno','lucasmrolon@gmail.com','0364-4420745','lucas','lrolon',0,NULL),(3,'normal','Sabrina','Aquino','34901009','Pueyrredon 1183','sabriyuly@gmail.com',NULL,'sabrina','saquino',0,NULL),(4,'normal','Alexis','Romaniuk','40034659',NULL,'alexisromaniuk@gmail.com',NULL,'alexis','aromaniuk',0,NULL),(5,'administrador','Juan','Perez','37361527','','','','juan','jperez',0,1),(6,'administrador','Juan','Perez','37361527','','','','juan','',0,1),(7,'normal','Test Subject #01','','','','','','test','test',0,1);
/*!40000 ALTER TABLE `empleados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formasdepago`
--

DROP TABLE IF EXISTS `formasdepago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formasdepago` (
  `CodigoForma` int(11) NOT NULL,
  `Tipo` varchar(45) DEFAULT NULL,
  `Recargo` double DEFAULT NULL,
  `Descuento` double DEFAULT NULL,
  PRIMARY KEY (`CodigoForma`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formasdepago`
--

LOCK TABLES `formasdepago` WRITE;
/*!40000 ALTER TABLE `formasdepago` DISABLE KEYS */;
INSERT INTO `formasdepago` VALUES (1,'Efectivo',0,0),(2,'Tarjeta de crédito',13,0),(3,'Tarjeta de débito',0,15),(4,'Cuenta corriente',0,0);
/*!40000 ALTER TABLE `formasdepago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lineasdepedido`
--

DROP TABLE IF EXISTS `lineasdepedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lineasdepedido` (
  `Pedidos_CodigoPedido1` int(11) NOT NULL,
  `CodigoLinea` int(11) NOT NULL AUTO_INCREMENT,
  `Productos_CodigoProducto` int(11) NOT NULL,
  `Precio_u` double DEFAULT NULL,
  `Cantidad` double DEFAULT NULL,
  `Subtotal` double DEFAULT NULL,
  `Recibido` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`CodigoLinea`),
  KEY `fk_Lineas de pedido_Productos1_idx` (`Productos_CodigoProducto`),
  KEY `fk_Lineas de pedido_Pedidos1_idx` (`Pedidos_CodigoPedido1`),
  CONSTRAINT `fk_Lineas de pedido_Pedidos` FOREIGN KEY (`Pedidos_CodigoPedido1`) REFERENCES `pedidos` (`CodigoPedido`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Lineas de pedido_Productos1` FOREIGN KEY (`Productos_CodigoProducto`) REFERENCES `productos` (`CodigoProducto`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lineasdepedido`
--

LOCK TABLES `lineasdepedido` WRITE;
/*!40000 ALTER TABLE `lineasdepedido` DISABLE KEYS */;
INSERT INTO `lineasdepedido` VALUES (1,1,1,40,10,400,1),(1,2,5,275,10,2750,1),(1,3,14,200,30,6000,1),(2,4,5,300,25,7500,1),(3,5,1,333,10,3330,1),(3,6,2,75,20,1500,1),(4,7,7,33.5,10,335,1),(4,8,14,150,4,600,1),(5,9,5,400,20,8000,1),(5,10,3,22,2,44,1),(6,11,1,33,20,660,1),(6,12,2,50,5,250,1),(7,13,2,137,10,1370,1),(8,14,5,340,10,3400,1),(8,15,3,30,2,60,1),(9,16,2,250,10,2500,1),(9,17,3,20,2,40,1),(9,18,14,457.62,53,24253.86,1),(10,19,24,350,2,700,1),(11,20,27,235.55,22,5182.1,1),(12,21,28,33.44,43,1437.92,1),(13,22,20,345.51,22,7601.22,1),(14,23,20,333.3,2,666.6,1),(15,24,30,450,3,1350,1),(16,25,22,250,3,750,1),(17,26,22,350,22,7700,1),(17,27,5,400,10,4000,1),(18,28,5,450,20,9000,1),(18,29,22,350,12,4200,1),(19,30,22,255,25,6375,1),(19,31,5,500,20,10000,1),(20,32,7,540,3,1620,1),(20,33,18,450,10,4500,1),(21,34,2,344.44,22,7577.68,1),(21,35,1,325,45,14625,1);
/*!40000 ALTER TABLE `lineasdepedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lineasdereparto`
--

DROP TABLE IF EXISTS `lineasdereparto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lineasdereparto` (
  `CodigoLinea` int(11) NOT NULL AUTO_INCREMENT,
  `NombreyApellido` varchar(50) NOT NULL,
  `Direccion` varchar(45) NOT NULL,
  `Turno` varchar(45) DEFAULT NULL,
  `Observaciones` varchar(50) DEFAULT NULL,
  `LineasDeVenta_CodigoLinea` int(11) NOT NULL,
  `Pendiente` int(1) NOT NULL,
  `Repartos_CodigoReparto` int(11) DEFAULT NULL,
  PRIMARY KEY (`CodigoLinea`),
  KEY `fk_LineasDeReparto_LineasDeVenta1_idx` (`LineasDeVenta_CodigoLinea`),
  KEY `fk_LineasDeReparto_Repartos1_idx` (`Repartos_CodigoReparto`),
  CONSTRAINT `fk_LineasDeReparto_LineasDeVenta1` FOREIGN KEY (`LineasDeVenta_CodigoLinea`) REFERENCES `lineasdeventa` (`CodigoLinea`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_LineasDeReparto_Reparo` FOREIGN KEY (`Repartos_CodigoReparto`) REFERENCES `repartos` (`CodigoReparto`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lineasdereparto`
--

LOCK TABLES `lineasdereparto` WRITE;
/*!40000 ALTER TABLE `lineasdereparto` DISABLE KEYS */;
INSERT INTO `lineasdereparto` VALUES (1,'','Azcuenaga 300','Tarde','Dia lunes',28,0,NULL),(2,'','Azcuenaga 300','Mañana','',29,0,NULL),(3,'','Azcuenaga 300','Mañana','',30,0,NULL),(4,'','San Martin 300','Mañana','',31,0,2),(5,'','San Martin 300','Mañana','',32,0,6),(6,'','San Martin 300','Mañana','',38,0,4),(7,'','San Martin 350','Tarde','',40,0,4),(8,'','Belgrano 277','Mañana','',48,0,6),(9,'','Pueyrredon 1183','Mañana','',49,0,6),(10,'','Pueyrredon 1183','Tarde','',56,0,3),(11,'','Mz 10 Pc 1 B° Mariano Moreno','Tarde','Dia Lunes',65,0,5),(12,'','Mz 10 Pc 1 B° Mariano Moreno','Tarde','Dia Lunes',68,0,8),(13,'','Mz 10 Pc 1 B° Mariano Moreno','Tarde','Eliminado por admin',69,0,NULL),(14,'','San Martin 1317','Mañana','',73,0,10),(15,'','Azcuenaga 155','Tarde','',79,0,5),(16,'','Mariano Moreno 350','Mañana','',82,0,8),(17,'Martin Lopez','Hernandarias 188','Mañana','',102,0,12),(18,'Juan Perez','Malvinas 371','Mañana','',103,0,13),(19,'Martin Perez','Güemes 325','Tarde','',105,0,11),(20,'Martin Perez','Güemes 325','Tarde','',106,0,5),(21,'Lucas Rolón','Cap Diz 407','Mañana','',107,0,5),(22,'Nelson Nuñez','33 e/ 14 y 16','Mañana','',108,0,7),(23,'Fulano de Tal','Siempreviva 1234','Mañana','',109,0,5),(25,'Sabrina Aquino','Pueyrredon 1183','Mañana','',111,0,9),(26,'','','Mañana','',116,0,14),(27,'Alexis Romaniuk','Siempreviva 1234','Mañana','',135,0,14),(28,'Roma','Siempreviva 1234','Mañana','ELIMINADO POR admin',136,0,NULL),(29,'Lucas Rolón','Moreno 134','Tarde','',138,0,15),(30,'Rolón Lucas','Azcuenaga 325','Tarde','',152,0,16),(31,'Lucas Rolon','Mz 10 Pc 1 Mariano Moreno','Mañana','',163,0,17),(32,'Alexis Romaniuk','Mendoza 1364','Mañana','Eliminado (Devolucion)',210,0,NULL),(33,'Miguel Lopez','Azcuenaga 1356','Mañana','Eliminado (Devolucion)',211,0,NULL),(34,'Martin Güemes','San Lorenzo 325','Mañana','',212,0,21),(35,'Elias Perez','Quinta 122','Mañana','Eliminado (Devolucion)',213,0,NULL),(36,'Lucas Rolón','Siempre Feliz 123','Mañana','Eliminado (Devolucion)',214,0,NULL),(37,'Sabrina','Pueyrredon 1183','Mañana','',217,0,26),(38,'','','Mañana','',218,0,25),(39,'','','Mañana','',219,0,24),(40,'','','Mañana','',220,0,23),(41,'','','Mañana','ELIMINADO POR admin',221,0,NULL),(42,'sabrina','manzanita 1234','Tarde','tiene un perrito',270,0,27),(43,'Fulano Ahre','Lolacio 8135','Mañana','Compar pan a la vuelta',323,0,28),(44,'Fulano Ahre','Lolacio 8135','Mañana','Compar pan a la vuelta',324,0,28),(45,'Fulano Ahre','Lolacio 8135','Mañana','Compar pan a la vuelta',325,0,28),(46,'Juan','Manzana 22','Mañana','',345,0,29),(47,'Nombre Apellido','Direccion Estandar','Mañana','ELIMINADO POR admin',346,0,NULL),(48,'Nombre Apellido','Direccion Estandar','Mañana','Eliminado (Devolucion)',347,0,NULL),(49,'Nombre Apellido','Direccion Estandar','Mañana','Testeo de reporte',348,0,29),(50,'Sabrina Aquino','Pueyrredon 1183','Mañana','ELIMINADO POR admin',354,0,NULL),(51,'Lucas Rolón','Mz 10 Pc 1 Mariano Moreno','Tarde','',358,0,31),(52,'Lucas Rolón','Mz 10 Pc 1 Mariano Moreno','Tarde','',359,0,31),(53,'Alexis Romaniuk','Plazoleta','Mañana','',369,0,32),(54,'Alexis','Checoslovaquia 756','Mañana','',374,0,34),(55,'Alexis','Checoslovaquia 756','Mañana','',375,0,33),(56,'Lucas Rolón','Mz 10 Pc 1 B° San Martin','Mañana','',395,0,35),(57,'Juan Perez','San Martin 300','Mañana','',422,0,36),(58,'Juan Perez','Avenida Belgrano','Mañana','',423,0,37),(59,'Juan Perez','Avenida Belgrano','Mañana','',424,0,37),(60,'Juan Lopez','Avenida Belgrano','Mañana','',425,0,37);
/*!40000 ALTER TABLE `lineasdereparto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lineasdeventa`
--

DROP TABLE IF EXISTS `lineasdeventa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lineasdeventa` (
  `CodigoLinea` int(11) NOT NULL AUTO_INCREMENT,
  `Ventas_CodigoVenta` int(11) DEFAULT NULL,
  `Productos_CodigoProducto` int(11) NOT NULL,
  `Precio_u` double NOT NULL,
  `Cantidad` double DEFAULT NULL,
  `Subtotal` double DEFAULT NULL,
  `Reparte` int(1) DEFAULT NULL,
  PRIMARY KEY (`CodigoLinea`),
  KEY `fk_LineasDeVenta_Productos1_idx` (`Productos_CodigoProducto`),
  KEY `fk_LineasDeVenta_Ventas1_idx` (`Ventas_CodigoVenta`),
  CONSTRAINT `fk_LineasDeVenta_Productos1` FOREIGN KEY (`Productos_CodigoProducto`) REFERENCES `productos` (`CodigoProducto`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_LineasDeVenta_Ventas1` FOREIGN KEY (`Ventas_CodigoVenta`) REFERENCES `ventas` (`CodigoVenta`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=452 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lineasdeventa`
--

LOCK TABLES `lineasdeventa` WRITE;
/*!40000 ALTER TABLE `lineasdeventa` DISABLE KEYS */;
INSERT INTO `lineasdeventa` VALUES (1,1,1,137.16,0,0,0),(2,1,3,117.54,1,117.54,0),(3,2,1,137.16,16,2194.56,0),(4,2,3,117.54,3,352.62,0),(5,2,5,487.53,21,10238.13,0),(6,2,7,389.67,20,7793.4,0),(7,2,8,58.78,54,3174.12,0),(8,2,11,35689.23,1,35689.23,0),(9,3,1,137.16,32,4389.12,0),(10,3,3,117.54,6,705.24,0),(11,3,5,487.53,42,20476.26,0),(12,3,7,389.67,40,15586.8,0),(13,3,8,58.78,108,6348.24,0),(14,3,11,35689.23,2,71378.46,0),(15,4,11,3549.9,1,3549.9,0),(16,4,11,3549.9,0,0,0),(17,5,1,137.16,2,274.32,0),(18,6,1,137.16,1,137.16,0),(19,7,2,117.54,2,235.08,0),(22,9,1,137.16,0,0,0),(23,9,2,117.54,0,0,0),(24,9,3,117.54,1,117.54,0),(25,10,1,137.16,1,137.16,1),(26,11,1,137.16,1,137.16,0),(27,11,2,117.54,1,117.54,0),(28,11,5,487.53,1,487.53,1),(29,12,5,487.53,1,487.53,1),(30,12,11,3549.9,1,3549.9,1),(31,13,2,117.54,1,117.54,1),(32,13,11,3549.9,1,3549.9,1),(33,14,3,117.54,1,117.54,0),(34,15,3,117.54,0,0,0),(35,15,1,137.16,1,137.16,0),(36,15,1,137.16,1,137.16,0),(37,16,2,117.54,1,117.54,0),(38,16,5,487.53,1,487.53,1),(39,16,7,389.67,1,389.67,0),(40,17,5,487.53,1,487.53,1),(41,18,3,117.54,1,117.54,0),(42,19,1,137.16,1,137.16,0),(43,19,1,137.16,4,548.64,0),(44,20,1,137.16,2,274.32,0),(45,20,5,487.53,1,487.53,0),(46,20,7,389.67,3,1169.01,0),(47,20,11,3549.9,1,3549.9,0),(48,21,8,58.78,3,176.34,1),(49,22,1,137.16,1,137.16,1),(50,23,1,137.16,1,137.16,0),(51,23,2,117.54,1,117.54,0),(52,24,1,137.16,1,137.16,0),(53,25,1,137.16,1,137.16,0),(54,26,1,137.16,1,137.16,0),(55,26,2,117.54,1,117.54,0),(56,26,5,487.53,1,487.53,1),(57,27,8,58.78,2,117.56,0),(58,28,8,58.78,1,58.78,0),(59,28,5,487.53,1,487.53,0),(60,28,2,117.54,1,117.54,0),(61,28,9,979.44,1,979.44,0),(62,29,1,137.16,1,137.16,0),(63,30,1,137.16,1,137.16,0),(64,30,2,117.54,1,117.54,0),(65,30,5,487.53,1,487.53,1),(66,30,7,389.67,1,389.67,0),(67,30,8,58.78,1,58.78,0),(68,30,9,979.44,1,979.44,1),(69,30,11,3549.9,1,3549.9,1),(70,31,2,117.54,1,117.54,0),(71,31,5,487.53,1,487.53,0),(72,32,5,487.53,1,487.53,0),(73,33,1,137.16,1,137.16,1),(74,34,1,137.16,1,137.16,0),(75,34,5,487.53,1,487.53,0),(76,34,8,58.78,1,58.78,0),(77,35,2,117.54,1,117.54,0),(78,35,8,58.78,1,58.78,0),(79,35,5,487.53,3,1462.59,1),(80,36,1,137.16,1,137.16,0),(81,37,1,137.16,1,137.16,0),(82,38,9,979.44,1,979.44,1),(83,39,1,137.16,1,137.16,0),(84,40,1,137.16,1,137.16,0),(85,41,1,137.16,1,137.16,0),(88,43,1,137.16,1,137.16,0),(89,44,9,979.44,1,979.44,0),(90,44,1,137.16,1,137.16,0),(91,44,1,137.16,1,137.16,0),(92,45,9,979.44,1,979.44,0),(93,45,1,137.16,1,137.16,0),(94,46,1,137.16,1,137.16,0),(95,47,1,137.16,1,137.16,0),(96,48,1,137.16,1,137.16,0),(97,49,1,137.16,1,137.16,0),(98,50,1,137.16,1,137.16,0),(99,51,7,389.67,1,389.67,0),(100,51,8,58.78,1,58.78,0),(101,52,9,979.44,1,979.44,1),(102,53,9,979.44,1,979.44,1),(103,54,2,117.54,1,117.54,1),(104,55,5,487.53,1,487.53,0),(105,56,2,117.54,1,117.54,1),(106,56,5,487.53,1,487.53,1),(107,57,5,487.53,1,487.53,1),(108,58,1,137.16,1,137.16,1),(109,59,5,487.53,3,1462.59,1),(110,60,7,389.67,2,779.34,1),(111,61,11,3549.9,1,3549.9,1),(112,62,8,58.78,2,117.56,0),(113,63,3,117.54,0.1,11.75,0),(114,64,3,117.54,1.2000000000000002,141.05,0),(115,64,5,487.53,2,975.06,0),(116,65,9,979.44,1,979.44,1),(117,66,9,979.44,7,6856.08,0),(118,67,11,3549.9,1,3549.9,0),(119,67,8,58.78,1,58.78,0),(120,68,7,389.67,1,389.67,0),(121,69,8,58.78,1,58.78,0),(122,70,3,117.54,0.2,23.51,0),(123,71,8,58.78,2,117.56,0),(124,72,9,979.44,1,979.44,0),(125,73,3,117.54,0.2,23.51,0),(126,74,11,3549.9,1,3549.9,0),(127,74,9,979.44,2,1958.88,0),(128,75,5,487.53,3,1462.59,0),(129,76,5,487.53,10,4875.3,0),(130,77,5,487.53,3,1462.59,0),(131,78,8,58.78,1,58.78,0),(132,79,2,117.54,2,235.08,0),(133,80,2,117.54,2,235.08,0),(134,81,3,117.54,2.7,317.36,0),(135,81,8,58.78,1,58.78,1),(136,82,5,487.53,3,1462.59,1),(137,83,5,487.53,2,975.06,0),(138,84,5,487.53,1,487.53,1),(139,85,5,520.2,5,2601,0),(140,86,5,520.2,4,2080.8,0),(141,87,7,415.77,2,831.54,0),(142,88,1,146.35,1,146.35,0),(143,89,7,415.77,1,415.77,0),(144,NULL,4,400,1,400,0),(145,90,5,520.2,2,1040.4,0),(146,91,5,520.2,4,2080.8,0),(147,92,8,62.78,4,251.12,0),(148,93,11,3787.81,1,3787.81,0),(149,94,5,520.2,2,1040.4,0),(150,94,6,350,1,350,0),(151,95,4,400,1,400,0),(152,96,5,520.2,2,1040.4,1),(153,NULL,8,62.78,2,125.56,0),(154,97,9,1045.07,1,1045.07,0),(155,98,1,146.35,1,146.35,0),(156,NULL,2,125.41,1,125.41,0),(157,NULL,6,350,1,350,0),(158,99,7,415.77,0,0,0),(159,NULL,1,146.35,1,146.35,0),(160,100,2,125.41,0,0,0),(161,100,4,400,0,0,0),(162,101,5,520.2,0,0,0),(163,102,5,520.2,2,1040.4,1),(164,102,7,415.77,1,415.77,0),(165,103,1,146.35,1,146.35,0),(166,104,1,146.35,2,292.7,0),(167,104,3,90.14,0.3,27.04,0),(168,105,5,520.2,2,1040.4,0),(169,106,1,146.35,8,1170.8,0),(170,107,1,146.35,0,0,0),(171,108,1,146.35,2,292.7,0),(172,109,4,400,1,400,0),(173,110,1,146.35,1,146.35,0),(174,111,1,146.35,0,0,0),(175,112,6,350,1,350,0),(176,113,6,350,0,0,0),(177,114,6,350,0,0,0),(178,115,8,62.78,4,251.12,0),(179,116,8,62.78,24,1506.72,0),(180,117,11,3787.81,4,15151.24,0),(181,118,8,62.78,4,251.12,0),(182,119,4,400,2,800,0),(183,120,6,350,0,0,0),(184,121,4,400,3,1200,0),(185,122,6,350,3,1050,0),(186,123,8,64.04,1,64.04,0),(187,124,8,64.04,2,128.08,0),(188,125,12,990,5,4950,0),(189,126,1,149.29,1,149.29,0),(190,126,2,127.93,1,127.93,0),(191,126,3,91.95,0.1,9.2,0),(192,126,4,408.04,1,408.04,0),(193,126,5,530.65,1,530.65,0),(194,126,6,357.04,1,357.04,0),(195,126,7,424.13,1,424.13,0),(196,126,8,64.05,1,64.05,0),(197,126,9,1066.08,1,1066.08,0),(198,126,10,75.01,1,75.01,0),(199,126,11,3863.96,1,3863.96,0),(200,126,12,990.1,1,990.1,0),(201,126,13,355.04,1,355.04,0),(202,127,2,127.93,4,511.72,0),(203,128,7,424.13,1,424.13,0),(204,129,3,91.95,1,91.95,0),(205,130,1,149.29,3,447.87,0),(206,131,4,408.04,1,408.04,0),(207,131,7,424.13,1,424.13,0),(208,132,8,64.05,3,192.15,0),(209,133,1,149.29,2,298.58,0),(210,134,5,530.65,3,1591.9499999999998,1),(211,135,4,408.04,0,0,1),(212,136,5,530.65,2,1061.3,1),(213,137,5,530.65,0,0,1),(214,138,5,530.65,0,0,1),(215,139,5,530.65,2,1061.3,0),(216,140,7,424.13,2,848.26,0),(217,141,5,530.65,2,1061.3,1),(218,142,2,127.93,1,127.93,1),(219,142,8,64.05,1,64.05,1),(220,143,6,357.04,1,357.04,1),(221,143,2,127.93,1,127.93,1),(222,144,6,357.04,1,357.04,0),(223,145,5,530.65,1,530.65,0),(224,146,6,357.04,1,357.04,0),(225,147,2,127.93,1,127.93,0),(226,147,3,91.95,0.5,45.98,0),(227,147,3,91.95,0.1,9.2,0),(228,148,6,357.04,1,357.04,0),(229,148,10,75.01,2,150.02,0),(230,148,11,3863.96,1,3863.96,0),(231,149,5,530.65,2,1061.3,0),(232,149,7,424.13,1,424.13,0),(233,150,8,64.05,2,128.1,0),(234,150,9,1066.08,1,1066.08,0),(235,151,5,530.65,1,530.65,0),(236,151,8,64.05,1,64.05,0),(237,152,5,530.65,1,530.65,0),(238,152,3,91.95,0.1,9.2,0),(239,153,5,530.65,1,530.65,0),(240,153,3,91.95,0.3,27.59,0),(241,154,10,75.01,1,75.01,0),(242,155,9,1066.08,1,1066.08,0),(243,155,10,75.01,1,75.01,0),(244,156,5,530.65,1,530.65,0),(245,157,10,75.01,1,75.01,0),(246,157,11,3863.96,1,3863.96,0),(247,158,10,75.01,1,75.01,0),(248,159,9,1066.08,1,1066.08,0),(249,160,8,64.05,1,64.05,0),(250,160,10,75.01,1,75.01,0),(251,161,10,75.01,1,75.01,0),(252,161,10,75.01,1,75.01,0),(253,162,5,530.65,1,530.65,0),(254,162,1,149.29,1,149.29,0),(255,163,1,149.29,1,149.29,0),(256,163,3,91.95,0.1,9.2,0),(257,163,5,530.65,1,530.65,0),(258,164,8,64.05,1,64.05,0),(259,164,9,1066.08,1,1066.08,0),(260,164,10,75.01,1,75.01,0),(261,165,11,3863.96,1,3863.96,0),(262,165,10,75.01,1,75.01,0),(263,166,8,64.05,1,64.05,0),(264,166,10,75.01,1,75.01,0),(265,167,10,75.01,1,75.01,0),(266,167,11,3863.96,1,3863.96,0),(267,168,3,91.95,0.7,64.36,0),(268,168,5,530.65,4,2122.6,0),(269,168,8,64.05,1,64.05,0),(270,169,5,530.65,5,2653.25,1),(271,170,1,149.29,16,2388.64,0),(272,171,1,149.29,1,149.29,0),(273,172,5,530.65,1,530.65,0),(274,173,5,530.65,1,530.65,0),(275,174,10,75.01,1,75.01,0),(276,175,3,91.95,0.4,36.78,0),(277,176,3,91.95,1,91.95,0),(278,177,10,75.01,1,75.01,0),(279,178,11,3863.96,1,3863.96,0),(280,179,10,75.01,1,75.01,0),(281,179,13,355.04,1,355.04,0),(282,180,3,91.95,0.1,9.2,0),(283,181,3,91.95,1,91.95,0),(284,182,5,530.65,1,530.65,0),(285,183,2,127.93,1,127.93,0),(286,183,1,149.29,2,298.58,0),(287,183,10,75.01,2,150.02,0),(288,183,13,355.04,1,355.04,0),(289,184,1,149.29,1,149.29,0),(290,184,5,530.65,1,530.65,0),(291,184,10,75.01,1,75.01,0),(292,184,3,91.95,1,91.95,0),(293,185,1,149.29,1,149.29,0),(294,185,2,127.93,1,127.93,0),(295,185,3,91.95,0.1,9.2,0),(296,185,4,408.04,1,408.04,0),(297,185,5,530.65,1,530.65,0),(298,185,6,357.04,1,357.04,0),(299,185,7,424.13,1,424.13,0),(300,185,8,64.05,1,64.05,0),(301,185,9,1066.08,1,1066.08,0),(302,186,1,149.29,1,149.29,0),(303,186,2,127.93,1,127.93,0),(304,186,3,91.95,0.1,9.2,0),(305,186,5,530.65,1,530.65,0),(306,186,6,357.04,1,357.04,0),(307,186,7,424.13,1,424.13,0),(308,186,8,64.05,1,64.05,0),(309,186,9,1066.08,1,1066.08,0),(310,187,7,424.13,2,848.26,0),(311,187,9,1066.08,1,1066.08,0),(312,188,6,357.04,3,1071.12,0),(313,188,7,424.13,1,424.13,0),(314,188,2,127.93,1,127.93,0),(315,188,12,990.1,1,990.1,0),(316,189,5,530.65,3,1591.95,0),(317,189,7,424.13,1,424.13,0),(318,189,9,1066.08,1,1066.08,0),(319,189,2,127.93,1,127.93,0),(320,189,1,149.29,1,149.29,0),(321,191,1,149.29,7,1045.03,0),(323,192,3,91.95,0.9,82.76,1),(324,192,5,530.65,2,1061.3,1),(325,192,7,424.13,7,2968.91,1),(326,193,8,64.05,2,128.1,0),(327,193,1,149.29,5,746.45,0),(328,194,1,149.29,5,746.45,0),(329,194,2,127.93,1,127.93,0),(330,195,7,424.13,10,4241.3,0),(331,196,7,424.13,5,2120.65,0),(332,197,7,424.13,2,848.26,0),(333,198,7,424.13,2,848.26,0),(334,199,7,424.13,4,1696.52,0),(335,200,7,424.13,4,1696.52,0),(336,201,3,91.95,2,183.9,0),(337,202,6,357.04,3,1071.12,0),(338,202,8,64.05,2,128.1,0),(339,202,10,75.01,3,225.03,0),(340,202,12,990.1,1,990.1,0),(341,202,1,149.29,3,447.87,0),(342,203,6,357.04,1,357.04,0),(343,203,8,64.05,1,64.05,0),(344,203,9,1066.08,1,1066.08,0),(345,204,8,64.05,1,64.05,1),(346,205,13,355.04,5,1775.2,1),(347,205,12,990.1,2,1980.2,1),(348,205,11,3863.96,1,3863.96,1),(349,205,10,75.01,2,150.02,0),(350,205,11,3863.96,1,3863.96,0),(351,206,3,91.95,0.2,18.39,0),(352,206,8,64.05,1,64.05,0),(353,206,10,75.01,1,75.01,0),(354,207,9,1066.08,1,1066.08,1),(355,208,5,450,1,450,0),(356,208,6,357.04,1,357.04,0),(357,208,3,91.95,0.7,64.365,0),(358,209,5,450,1,450,1),(359,209,6,357.04,1,357.04,1),(360,210,6,357.04,1,357.04,0),(361,210,8,64.05,1,64.05,0),(362,211,1,149.29,1,149.29,0),(363,212,1,149.29,2,298.58,0),(364,213,1,158.34,3,475.02,0),(365,213,2,135.68,1,135.68,0),(366,214,1,158.34,1,158.34,0),(367,215,1,158.34,3,475.02,0),(368,216,2,135.68,1,135.68,0),(369,217,4,432.77,2,865.54,1),(370,218,11,4098.14,2,8196.28,0),(371,220,11,4098.14,1,4098.14,0),(372,219,11,4098.14,1,4098.14,0),(373,221,11,4098.14,2,8196.28,0),(374,222,4,432.77,1,432.77,1),(375,222,3,97.52,0.3,29.26,1),(376,223,5,482.05,1,482.05,0),(377,223,10,80.36,1,80.36,0),(378,224,1,159.92,1,159.92,0),(379,224,4,437.1,1,437.1,0),(380,225,4,437.1,6,2622.6,0),(389,229,7,454.33,1,454.33,0),(390,229,9,1142,1,1142,0),(391,226,5,482.05,1,482.05,0),(392,226,7,454.33,1,454.33,0),(393,226,10,80.36,2,160.72,0),(394,230,4,437.1,1,437.1,0),(395,228,5,482.05,3,1446.15,1),(396,227,4,437.1,4,1748.4,0),(397,227,5,482.05,2,964.1,0),(398,231,6,382.47,1,382.47,0),(399,231,4,437.1,1,437.1,0),(400,232,2,137.04,2,274.08,0),(401,233,4,437.1,1,437.1,0),(402,234,6,382.47,2,764.94,0),(403,235,5,482.05,1,482.05,0),(404,236,6,382.47,1,382.47,0),(405,236,4,437.1,1,437.1,0),(406,237,6,382.47,1,382.47,0),(407,240,12,1060.6,1,1060.6,0),(408,239,10,80.36,1,80.36,0),(409,239,4,437.1,1,437.1,0),(410,238,4,437.1,2,874.2,0),(411,238,10,80.36,2,160.72,0),(412,241,6,382.47,1,382.47,0),(413,242,4,437.1,3,1311.3,0),(414,242,7,454.33,1,454.33,0),(415,243,6,382.47,1,382.47,0),(416,248,11,4139.12,1,4139.12,0),(417,244,9,1142,1,1142,0),(418,249,5,482.05,1,482.05,0),(419,251,5,482.05,1,482.05,0),(420,252,4,437.1,2,874.2,0),(421,254,5,482.05,1,482.05,0),(422,255,10,80.36,1,80.36,1),(423,256,4,437.1,2,874.2,1),(424,256,5,482.05,1,482.05,1),(425,257,10,80.36,1,80.36,1),(426,259,7,454.33,1,454.33,0),(427,253,6,382.47,1,382.47,0),(428,258,3,98.5,0.1,9.85,0),(429,260,4,445.84,1,445.84,0),(430,261,5,491.69,1,491.69,0),(431,262,5,491.69,5,2458.45,0),(432,263,3,100.47,1.3,130.61,0),(433,264,24,700,2,1400,0),(434,265,6,390.12,4,1560.48,0),(435,266,31,40,4,160,0),(436,267,31,40,4,160,0),(437,267,2,139.78,3,419.34,0),(438,268,22,255,3,765,0),(439,268,31,40,4,160,0),(440,268,10,81.97,1,81.97,0),(441,269,29,20,0.1,2,0),(442,269,22,255,4,1020,0),(443,269,31,40,3,120,0),(444,270,27,650,2,1300,0),(445,270,31,40,1,40,0),(446,270,14,327.79,2,655.58,0),(447,270,3,100.47,0.2,20.09,0),(448,271,4,445.84,3,1337.52,0),(449,272,3,101.47,0.1,10.15,0),(450,273,9,1176.49,1,1176.49,0),(451,274,3,101.47,0.5,50.74,0);
/*!40000 ALTER TABLE `lineasdeventa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lineasdeventapendientes`
--

DROP TABLE IF EXISTS `lineasdeventapendientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lineasdeventapendientes` (
  `CodigoLinea` int(11) NOT NULL AUTO_INCREMENT,
  `CodigoVenta` int(11) NOT NULL,
  `CodigoProducto` int(11) NOT NULL,
  `Precio_u` double NOT NULL,
  `Cantidad` double NOT NULL,
  `Subtotal` double NOT NULL,
  `Reparte` int(1) NOT NULL,
  PRIMARY KEY (`CodigoLinea`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lineasdeventapendientes`
--

LOCK TABLES `lineasdeventapendientes` WRITE;
/*!40000 ALTER TABLE `lineasdeventapendientes` DISABLE KEYS */;
/*!40000 ALTER TABLE `lineasdeventapendientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pedidos` (
  `CodigoPedido` int(11) NOT NULL AUTO_INCREMENT,
  `Emision` timestamp NULL DEFAULT NULL,
  `Recepcion` timestamp NULL DEFAULT NULL,
  `Proveedores_CodigoProveedor` varchar(11) NOT NULL,
  `Empleados_IdEmpleado` int(11) DEFAULT NULL,
  PRIMARY KEY (`CodigoPedido`),
  KEY `fk_Pedidos_Proveedores1_idx` (`Proveedores_CodigoProveedor`),
  KEY `fk_Pedidos_Empleados1_idx` (`Empleados_IdEmpleado`),
  KEY `Proveedores_CodigoProveedor` (`Proveedores_CodigoProveedor`),
  CONSTRAINT `fk_Pedidos_Empleados1` FOREIGN KEY (`Empleados_IdEmpleado`) REFERENCES `empleados` (`IdEmpleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pedidos_Proveedores1` FOREIGN KEY (`Proveedores_CodigoProveedor`) REFERENCES `proveedores` (`CodigoProveedor`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
INSERT INTO `pedidos` VALUES (1,'2018-05-02 03:00:00','2018-05-23 03:00:00','1',1),(2,'2018-05-02 03:00:00','2018-06-05 13:07:15','AB123',1),(3,'2018-05-03 03:00:00','2018-06-05 13:09:52','AB123',1),(4,'2018-05-03 03:00:00','2018-05-24 19:04:07','1',1),(5,'2018-05-03 03:00:00','2018-05-23 03:00:00','2',2),(6,'2018-05-03 03:00:00','2018-06-05 13:06:14','1',1),(7,'2018-05-18 03:00:00','2018-05-30 23:34:50','CD456',2),(8,'2018-05-24 18:54:06','2018-05-24 18:55:42','2',1),(9,'2018-05-30 23:15:01','2018-05-30 23:17:45','1',1),(10,'2018-06-01 21:31:37','2018-06-01 21:31:59','2',1),(11,'2018-06-01 21:39:07','2018-06-01 21:40:21','CD456',1),(12,'2018-06-02 23:32:45','2018-06-02 23:33:02','1',1),(13,'2018-06-02 23:36:28','2018-06-02 23:36:48','2',1),(14,'2018-06-02 23:55:23','2018-06-02 23:55:32','2',1),(15,'2018-06-02 23:57:02','2018-06-02 23:57:12','1',1),(16,'2018-06-04 19:41:34','2018-06-04 19:41:52','AB123',1),(17,'2018-06-04 19:42:42','2018-06-04 19:43:39','AB123',1),(18,'2018-06-04 20:00:05','2018-06-04 20:00:18','AB123',1),(19,'2018-06-04 22:10:53','2018-06-04 22:11:27','AB123',1),(20,'2018-06-05 12:16:39','2018-06-05 12:16:52','2',1),(21,'2018-06-10 14:05:30','2018-06-10 14:48:30','1',1);
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `productos` (
  `CodigoProducto` int(11) NOT NULL,
  `Descripcion` varchar(100) DEFAULT NULL,
  `Rubro_CodigoRubro` int(11) NOT NULL,
  `Precio` double DEFAULT NULL,
  `Cantidad` double DEFAULT NULL,
  `Unidad` varchar(45) DEFAULT NULL,
  `Proveedores_CodigoProveedor` varchar(11) NOT NULL,
  `Observaciones` varchar(45) DEFAULT NULL,
  `CantMinima` double DEFAULT NULL,
  `Estado` varchar(20) DEFAULT NULL,
  `Eliminado` int(1) DEFAULT NULL,
  PRIMARY KEY (`CodigoProducto`),
  KEY `fk_Productos_Proveedores_idx` (`Proveedores_CodigoProveedor`),
  KEY `fk_Productos_Rubro1_idx` (`Rubro_CodigoRubro`),
  CONSTRAINT `fk_Productos_Proveedores` FOREIGN KEY (`Proveedores_CodigoProveedor`) REFERENCES `proveedores` (`CodigoProveedor`),
  CONSTRAINT `fk_Productos_Rubro1` FOREIGN KEY (`Rubro_CodigoRubro`) REFERENCES `rubro` (`CodigoRubro`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,'PINZA',1,325,120,'UNIDAD','1','NO SIRVEN',15,'NORMAL',NULL),(2,'MARTILLO',1,344.44,95,'UNIDAD','1','',1,'NORMAL',NULL),(3,'CLAVO',3,101.47,26.9,'KILOGRAMOS','2','',1,'NORMAL',NULL),(4,'PALA PUNTA',1,450.3,9,'UNIDAD','1','',10,'BAJO',NULL),(5,'CHAPAS',1,404,140,'UNIDAD','AB123','',6,'NORMAL',NULL),(6,'PALA ANCHA',1,394.02,3,'UNIDAD','1','',4,'BAJO',NULL),(7,'MACHETE',2,545.4,42,'UNIDAD','2','',10,'NORMAL',NULL),(8,'DESTORNILLADOR 3/4 BAHCO ALUMINIO AAAAA',1,70.69,20,'UNIDAD','1','',10,'NORMAL',1),(9,'SACABOLLOS',2,1176.49,21,'UNIDAD','2','',-1,'NORMAL',NULL),(10,'DESTORNILLADOR PHILIPS',1,82.79,8,'UNIDAD','1','',5,'NORMAL',NULL),(11,'SOLDADORA',2,4264.12,3,'UNIDAD','1','',2,'NORMAL',NULL),(12,'HIDROLAVADORA',1,1092.63,35,'UNIDAD','1','',-1,'NORMAL',NULL),(13,'HACHA',1,391.82,25,'UNIDAD','1','',2,'NORMAL',NULL),(14,'PINTURA ROJA',1,331.07,27,'LITROS','CD456','',10,'NORMAL',NULL),(15,'CHAPAS',1,283.3,10,'UNIDAD','1','',-1,'NORMAL',NULL),(16,'PINTURA ROJA',1,206.04,30,'LITROS','1','',-1,'NORMAL',NULL),(17,'MACHETE',2,34.51,5,'UNIDAD','1','',-1,'NORMAL',NULL),(18,'CHAPAS',1,412.08,20,'UNIDAD','2','',-1,'NORMAL',NULL),(19,'PINZA',1,343.06,10,'UNIDAD','AB123','',-1,'NORMAL',NULL),(20,'ALICATE',1,336.63,24,'UNIDAD','2','',-1,'',NULL),(21,'MARTILLO',1,141.14,10,'UNIDAD','CD456','',-1,'NORMAL',NULL),(22,'MARTILLO',1,257.55,75,'UNIDAD','AB123','',-1,'NORMAL',NULL),(23,'ELECTRODOS',2,22.22,5,'UNIDAD','1','',-1,'',NULL),(24,'REMACHADORA',1,707,0,'UNIDAD','2','',25,'AGOTADO',NULL),(25,'CASA',1,25.25,2,'UNIDAD','1','',-1,'',1),(26,'CASONA',1,0,0,'UNIDAD','AB123','',-1,'',1),(27,'PINTURA VERDE',1,656.5,20,'UNIDAD','CD456','',-1,'NORMAL',NULL),(28,'LLAVE INGLESA',1,328.25,43,'UNIDAD','1','',-1,'',NULL),(29,'CLAVO',1,20.2,1.9,'KILOGRAMOS','1','',-1,'NORMAL',NULL),(30,'LLAVE FRANCESA',1,454.5,3,'UNIDAD','1','',-1,'',NULL),(31,'CAÑO ESTRUCTURAL ACERO TEMPLADO 3X45 COLOR VERDE S/ IMPERFECCIONES',3,40.4,59,'UNIDAD','1','',-1,'NORMAL',NULL);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedores`
--

DROP TABLE IF EXISTS `proveedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proveedores` (
  `CodigoProveedor` varchar(11) NOT NULL,
  `Nombre` varchar(45) DEFAULT NULL,
  `Domicilio` varchar(45) DEFAULT NULL,
  `Telefono` varchar(45) DEFAULT NULL,
  `Email` varchar(45) DEFAULT NULL,
  `ListaPrecios` varchar(300) DEFAULT NULL,
  `Eliminado` int(1) DEFAULT NULL,
  PRIMARY KEY (`CodigoProveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedores`
--

LOCK TABLES `proveedores` WRITE;
/*!40000 ALTER TABLE `proveedores` DISABLE KEYS */;
INSERT INTO `proveedores` VALUES ('','pinturitas','manzanita','364425896','',NULL,1),('1','BAHCO SRL.','AZCUENAGA 300','0364-4422651','bahco@gmail.com','C:\\Users\\Lucas\\Documents\\EclipseProjects\\Proyecto Diseño v0.1\\src\\listas_de_precios\\Java Printing.pdf',NULL),('2','HIERROS LIDER','RUTA 16 KM 78','0362-4423432','hierros_lider@gmail.com','C:\\Users\\Lucas\\Documents\\EclipseProjects\\Proyecto Diseño v0.1\\src\\listas_de_precios\\Java Printing.pdf',NULL),('AB123','Metalpar','San Fernando 335','0362-4424633','metalpa@gmail.com','C:\\Users\\Lucas\\Documents\\EclipseProjects\\Proyecto Diseño v0.1\\src\\listas_de_precios\\lista_3.xlsx',NULL),('CD456','ALBA','RUTA 95 KM 1365','0364-4426491','albapinturas@gmail.com','C:\\Users\\Lucas\\Documents\\EclipseProjects\\Proyecto Diseño v0.1\\src\\listas_de_precios\\lista_4.xlsx',NULL),('ddfv4','gh','nnn','5555','dgjhkj',NULL,1),('DFGH','Pinturitas','naranjita km11','03644-253625','',NULL,1);
/*!40000 ALTER TABLE `proveedores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repartos`
--

DROP TABLE IF EXISTS `repartos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `repartos` (
  `CodigoReparto` int(11) NOT NULL AUTO_INCREMENT,
  `FechaCreacion` date NOT NULL,
  `Empleados_IdEmpleado` int(11) DEFAULT NULL,
  `FechaConfirm` date DEFAULT NULL,
  PRIMARY KEY (`CodigoReparto`),
  KEY `fk_Repartos_Empleados1_idx` (`Empleados_IdEmpleado`),
  CONSTRAINT `fk_Repartos_Empleados1` FOREIGN KEY (`Empleados_IdEmpleado`) REFERENCES `empleados` (`IdEmpleado`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repartos`
--

LOCK TABLES `repartos` WRITE;
/*!40000 ALTER TABLE `repartos` DISABLE KEYS */;
INSERT INTO `repartos` VALUES (2,'2018-01-24',1,'2018-01-31'),(3,'2018-01-24',1,'2018-01-31'),(4,'2018-01-27',1,'2018-01-31'),(5,'2018-01-27',1,'2018-01-31'),(6,'2018-01-28',1,'2018-01-31'),(7,'2018-01-30',1,'2018-01-31'),(8,'2018-01-31',1,'2018-01-31'),(9,'2018-01-31',1,'2018-01-31'),(10,'2018-01-31',1,'2018-01-31'),(11,'2018-01-31',1,'2018-01-31'),(12,'2018-01-31',1,'2018-02-03'),(13,'2018-01-31',1,'2018-02-06'),(14,'2018-02-06',1,'2018-02-07'),(15,'2018-02-07',1,'2018-02-07'),(16,'2018-02-09',1,'2018-02-09'),(17,'2018-02-19',1,'2018-02-19'),(18,'2018-03-04',1,'2018-03-04'),(19,'2018-03-07',2,'2018-03-07'),(20,'2018-03-07',2,'2018-03-07'),(21,'2018-03-08',1,'2018-03-10'),(22,'2018-03-10',1,'2018-03-10'),(23,'2018-03-11',1,'2018-03-11'),(24,'2018-03-11',1,'2018-03-11'),(25,'2018-03-11',1,'2018-03-11'),(26,'2018-03-11',1,'2018-03-14'),(27,'2018-03-13',4,'2018-03-13'),(28,'2018-03-14',1,'2018-03-14'),(29,'2018-03-15',1,'2018-03-15'),(30,'2018-03-15',1,'2018-03-15'),(31,'2018-03-16',1,'2018-03-20'),(32,'2018-03-20',4,'2018-03-20'),(33,'2018-03-20',1,'2018-04-24'),(34,'2018-04-24',1,'2018-05-03'),(35,'2018-04-26',2,'2018-05-30'),(36,'2018-05-03',2,'2018-06-05'),(37,'2018-05-03',2,'2018-06-05');
/*!40000 ALTER TABLE `repartos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rubro`
--

DROP TABLE IF EXISTS `rubro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rubro` (
  `CodigoRubro` int(11) NOT NULL,
  `Nombre` varchar(45) DEFAULT NULL,
  `Subrubro` varchar(45) DEFAULT NULL,
  `Eliminado` int(1) DEFAULT NULL,
  PRIMARY KEY (`CodigoRubro`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rubro`
--

LOCK TABLES `rubro` WRITE;
/*!40000 ALTER TABLE `rubro` DISABLE KEYS */;
INSERT INTO `rubro` VALUES (1,'HERRAMIENTAS DE MANO',NULL,NULL),(2,'ELECTRICAS',NULL,NULL),(3,'CONSTRUCCION',NULL,NULL),(4,'MAQUINITAS',NULL,1),(5,'SALSAS',NULL,1);
/*!40000 ALTER TABLE `rubro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ventas`
--

DROP TABLE IF EXISTS `ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ventas` (
  `CodigoVenta` int(11) NOT NULL AUTO_INCREMENT,
  `Fecha` timestamp NULL DEFAULT NULL,
  `MontoTotal` double DEFAULT NULL,
  `RecDesc` double DEFAULT NULL,
  `Empleados_IdEmpleado` int(11) NOT NULL,
  `CuentasCorrientes_CodigoCuenta` int(11) DEFAULT NULL,
  `FormasDePago_CodigoForma` int(11) DEFAULT NULL,
  PRIMARY KEY (`CodigoVenta`),
  KEY `fk_Ventas_Empleados1_idx` (`Empleados_IdEmpleado`),
  KEY `fk_Ventas_CuentasCorrientes1_idx` (`CuentasCorrientes_CodigoCuenta`),
  KEY `fk_Ventas_FormasDePago1_idx` (`FormasDePago_CodigoForma`),
  CONSTRAINT `fk_Ventas_CuentasCorrientes1` FOREIGN KEY (`CuentasCorrientes_CodigoCuenta`) REFERENCES `cuentascorrientes` (`CodigoCuenta`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Ventas_Empleados1` FOREIGN KEY (`Empleados_IdEmpleado`) REFERENCES `empleados` (`IdEmpleado`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Ventas_FormasDePago1` FOREIGN KEY (`FormasDePago_CodigoForma`) REFERENCES `formasdepago` (`CodigoForma`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=275 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ventas`
--

LOCK TABLES `ventas` WRITE;
/*!40000 ALTER TABLE `ventas` DISABLE KEYS */;
INSERT INTO `ventas` VALUES (1,'2018-01-15 23:45:28',117.53999999999999,0,1,NULL,1),(2,'2018-01-15 23:51:46',59442.06,2972.1,1,NULL,2),(3,'2018-01-15 23:57:46',118884.12,-5944.21,1,NULL,3),(4,'2018-01-16 00:01:02',3549.900000000001,0,1,NULL,1),(5,'2018-01-16 00:19:53',274.32,0,1,NULL,1),(6,'2018-01-16 00:43:56',137.16,0,1,NULL,1),(7,'2018-01-16 00:44:42',235.08,0,1,NULL,1),(9,'2018-01-16 02:15:23',117.54,0,1,NULL,1),(10,'2018-01-16 03:01:25',137.16,0,1,NULL,1),(11,'2018-01-16 03:03:58',742.23,0,1,NULL,1),(12,'2018-01-16 03:07:47',4037.4300000000003,0,1,NULL,1),(13,'2018-01-16 03:19:45',3667.44,0,1,NULL,1),(14,'2018-01-16 03:21:35',117.54,-5.88,1,NULL,3),(15,'2018-01-16 03:22:12',274.32,0,1,NULL,1),(16,'2018-01-16 03:32:25',994.74,0,1,NULL,1),(17,'2018-01-16 03:34:21',487.53,0,1,NULL,1),(18,'2018-01-16 03:38:46',117.54,0,1,NULL,1),(19,'2018-01-17 12:33:24',685.8,0,1,NULL,1),(20,'2018-01-17 12:43:12',5480.76,0,1,NULL,1),(21,'2018-01-17 12:43:50',176.34,8.82,1,NULL,2),(22,'2018-01-17 22:08:49',137.16,0,1,NULL,1),(23,'2018-01-17 22:37:24',254.7,0,1,NULL,1),(24,'2018-01-17 22:37:41',137.16,0,1,NULL,1),(25,'2018-01-17 22:42:58',137.16,0,1,NULL,1),(26,'2018-01-17 22:45:19',742.23,0,1,NULL,1),(27,'2018-01-17 22:48:37',117.56,0,1,NULL,1),(28,'2018-01-17 22:50:09',1643.29,0,1,NULL,1),(29,'2018-01-17 22:51:44',137.16,0,1,NULL,1),(30,'2018-01-18 00:13:25',5720.02,0,1,NULL,1),(31,'2018-01-18 03:47:46',605.0699999999999,30.25,1,NULL,2),(32,'2018-01-18 03:50:56',487.53,-24.38,1,NULL,3),(33,'2018-01-18 04:25:57',137.16,0,1,NULL,1),(34,'2018-01-18 04:26:09',683.4699999999999,0,1,NULL,1),(35,'2018-01-18 17:24:26',1638.9099999999999,0,1,NULL,1),(36,'2018-01-18 18:08:54',137.16,0,1,NULL,4),(37,'2018-01-18 18:17:07',137.16,-6.86,1,NULL,3),(38,'2018-01-18 22:10:43',979.44,0,1,NULL,1),(39,'2018-01-18 22:42:37',137.16,0,1,NULL,1),(40,'2018-01-18 22:43:15',137.16,6.86,1,NULL,2),(41,'2018-01-18 22:43:59',137.16,-6.86,1,NULL,3),(43,'2018-01-18 23:55:29',137.16,0,1,NULL,1),(44,'2018-01-18 23:59:22',1253.76,0,1,NULL,1),(45,'2018-01-19 00:05:41',1116.6,0,1,NULL,1),(46,'2018-01-19 00:08:30',137.16,0,1,1,4),(47,'2018-01-19 00:23:28',137.16,0,1,1,4),(48,'2018-01-19 00:24:37',137.16,0,1,1,4),(49,'2018-01-19 00:24:44',137.16,0,1,NULL,1),(50,'2018-01-19 00:26:23',137.16,0,1,NULL,1),(51,'2018-01-19 00:26:42',448.45000000000005,0,1,1,4),(52,'2018-01-19 00:41:11',979.44,0,1,1,4),(53,'2018-01-19 00:43:01',979.44,0,1,1,4),(54,'2018-01-19 00:54:12',117.54,0,1,1,4),(55,'2018-01-23 19:43:56',487.53,0,1,1,4),(56,'2018-01-23 20:46:05',605.0699999999999,30.25,1,NULL,2),(57,'2018-01-24 15:48:00',487.53,24.38,1,NULL,2),(58,'2018-01-25 12:38:34',137.16,6.86,1,NULL,2),(59,'2018-01-27 19:03:40',1462.59,0,1,NULL,1),(60,'2018-01-27 19:13:58',779.34,0,1,NULL,1),(61,'2018-01-27 19:18:50',3549.9,177.5,1,NULL,2),(62,'2018-01-27 19:24:17',117.56,-5.88,1,NULL,3),(63,'2018-01-27 21:03:24',11.75,0,1,1,4),(64,'2018-01-30 22:49:15',1116.11,55.81,1,NULL,2),(65,'2018-01-31 16:26:47',979.44,0,1,NULL,1),(66,'2018-01-31 23:24:00',6856.08,0,1,NULL,1),(67,'2018-02-01 21:56:22',3608.6800000000003,0,1,NULL,1),(68,'2018-02-02 14:08:41',389.67,0,1,NULL,1),(69,'2018-02-02 16:03:51',58.78,0,1,NULL,1),(70,'2018-02-02 16:04:03',23.51,0,1,NULL,1),(71,'2018-02-03 21:18:47',117.56,0,1,NULL,1),(72,'2018-02-03 21:19:14',979.44,0,1,NULL,1),(73,'2018-02-04 18:05:22',23.51,0,1,1,4),(74,'2018-02-05 14:42:46',5508.780000000001,-275.44,1,NULL,3),(75,'2018-02-05 17:51:13',1462.59,-73.13,1,NULL,3),(76,'2018-02-05 20:12:59',4875.3,-243.76,1,NULL,3),(77,'2018-02-05 20:15:24',1462.59,-73.13,1,NULL,3),(78,'2018-02-05 20:31:34',58.78,0,1,NULL,1),(79,'2018-02-05 20:35:32',235.08,0,1,NULL,1),(80,'2018-02-05 20:35:45',235.08,-11.75,1,NULL,3),(81,'2018-02-06 11:39:17',376.14,18.81,1,NULL,2),(82,'2018-02-06 11:41:39',1462.59,0,1,NULL,1),(83,'2018-02-06 21:07:51',975.06,48.75,1,NULL,2),(84,'2018-02-06 21:24:53',487.53,0,1,NULL,1),(85,'2018-02-07 21:26:56',2601,0,1,NULL,1),(86,'2018-02-08 14:12:29',2080.8,0,1,NULL,1),(87,'2018-02-08 14:35:56',831.54,41.58,1,NULL,2),(88,'2018-02-08 14:44:27',146.35,-7.32,1,NULL,3),(89,'2018-02-08 14:45:46',415.77,-20.79,1,NULL,3),(90,'2018-02-08 14:46:18',1040.4,0,1,NULL,1),(91,'2018-02-08 14:46:46',2080.8,104.04,1,NULL,2),(92,'2018-02-08 19:22:11',251.12,-12.56,1,NULL,3),(93,'2018-02-08 22:54:54',3787.81,0,1,NULL,1),(94,'2018-02-08 22:56:44',1390.4,69.52,1,NULL,2),(95,'2018-02-09 12:57:57',400,0,1,NULL,1),(96,'2018-02-09 14:44:31',1040.4,-52.02,1,NULL,3),(97,'2018-02-09 23:02:28',1045.07,0,1,NULL,1),(98,'2018-02-09 23:24:38',146.35,0,1,NULL,1),(99,'2018-02-10 01:07:28',0,0,1,NULL,1),(100,'2018-02-10 19:55:08',0,0,1,NULL,1),(101,'2018-02-10 20:00:00',0,0,1,NULL,1),(102,'2018-02-19 17:11:28',1456.17,-72.81,1,NULL,3),(103,'2018-03-02 12:54:53',146.35,0,1,NULL,1),(104,'2018-03-02 13:34:05',319.74,15.99,1,NULL,2),(105,'2018-03-02 14:00:03',1040.4,0,1,NULL,1),(106,'2018-03-02 14:09:37',1170.8,-58.54,1,NULL,3),(107,'2018-03-02 14:17:38',0,0,1,NULL,1),(108,'2018-03-02 14:20:29',292.7,0,1,NULL,1),(109,'2018-03-02 14:33:00',400,0,1,1,4),(110,'2018-03-02 14:40:21',146.35,7.32,1,NULL,2),(111,'2018-03-02 15:03:56',0,0,1,NULL,1),(112,'2018-03-02 15:09:00',350,0,1,NULL,1),(113,'2018-03-02 15:13:26',0,0,1,NULL,1),(114,'2018-03-02 17:46:04',0,0,1,NULL,1),(115,'2018-03-02 17:49:11',251.12,0,1,NULL,1),(116,'2018-03-02 17:50:38',1506.72,0,1,NULL,1),(117,'2018-03-02 17:59:32',15151.24,757.56,1,NULL,2),(118,'2018-03-02 18:00:49',251.12,0,1,NULL,1),(119,'2018-03-02 18:02:55',800,0,1,NULL,1),(120,'2018-03-02 18:12:15',0,0,1,NULL,1),(121,'2018-03-02 18:17:41',1200,0,1,NULL,1),(122,'2018-03-02 18:21:26',1050,0,1,NULL,1),(123,'2018-03-02 18:51:17',64.04,0,1,NULL,1),(124,'2018-03-02 18:59:21',128.08,0,1,NULL,1),(125,'2018-03-02 20:30:17',4950,0,1,NULL,1),(126,'2018-03-03 18:56:31',8420.52,421.03,1,NULL,2),(127,'2018-03-04 22:25:01',511.72,25.59,1,NULL,2),(128,'2018-03-06 00:23:35',424.13,0,1,NULL,1),(129,'2018-03-06 02:58:52',91.95,4.6,1,NULL,2),(130,'2018-03-06 14:51:36',447.87,0,1,NULL,1),(131,'2018-03-06 14:59:28',832.17,41.61,1,NULL,2),(132,'2018-03-06 23:43:57',192.15,0,1,NULL,1),(133,'2018-03-07 18:26:40',298.58,0,2,NULL,1),(134,'2018-03-07 18:43:00',1591.95,0,1,NULL,1),(135,'2018-03-07 18:54:14',0,0,1,NULL,1),(136,'2018-03-07 19:09:56',1061.3,53.07,1,NULL,2),(137,'2018-03-10 14:04:37',0,0,1,NULL,1),(138,'2018-03-10 18:05:43',0,0,1,NULL,1),(139,'2018-03-11 12:57:35',1061.3,0,1,NULL,1),(140,'2018-03-11 13:45:13',848.26,0,1,NULL,1),(141,'2018-03-11 13:47:26',1061.3,0,1,NULL,1),(142,'2018-03-11 13:48:57',191.98,0,1,NULL,1),(143,'2018-03-11 14:53:56',484.97,0,1,NULL,1),(144,'2018-03-11 23:06:41',357.04,0,1,2,4),(145,'2018-03-12 01:11:16',530.65,0,1,1,4),(146,'2018-03-12 19:44:54',357.04,0,4,NULL,1),(147,'2018-03-13 13:51:38',183.11,0,1,NULL,1),(148,'2018-03-13 16:59:12',4371.02,0,1,NULL,1),(149,'2018-03-13 17:06:24',1485.43,0,1,NULL,1),(150,'2018-03-13 17:08:37',1194.18,0,1,NULL,1),(151,'2018-03-13 17:15:35',594.7,0,1,NULL,1),(152,'2018-03-13 17:19:04',539.85,0,1,NULL,1),(153,'2018-03-13 17:33:15',558.24,0,1,NULL,1),(154,'2018-03-13 17:38:49',75.01,0,1,NULL,1),(155,'2018-03-13 17:41:09',1141.09,0,1,NULL,1),(156,'2018-03-13 17:44:45',530.65,0,1,NULL,1),(157,'2018-03-13 17:46:19',3938.97,0,1,NULL,1),(158,'2018-03-13 17:48:51',75.01,0,1,NULL,1),(159,'2018-03-13 17:50:04',1066.08,0,1,NULL,1),(160,'2018-03-13 17:52:51',139.06,0,1,NULL,1),(161,'2018-03-13 17:55:25',150.02,0,1,NULL,1),(162,'2018-03-13 17:57:22',679.94,0,1,NULL,1),(163,'2018-03-13 17:59:19',689.14,0,1,NULL,1),(164,'2018-03-13 18:03:29',1205.14,0,1,NULL,1),(165,'2018-03-13 18:07:46',3938.97,0,1,NULL,1),(166,'2018-03-13 20:19:05',139.06,0,2,NULL,1),(167,'2018-03-13 20:24:14',3938.97,0,1,NULL,1),(168,'2018-03-13 20:24:52',2251.01,0,1,NULL,1),(169,'2018-03-13 20:27:20',2653.25,0,1,NULL,1),(170,'2018-03-13 20:27:20',2388.64,0,1,NULL,1),(171,'2018-03-13 20:27:22',149.29,0,4,NULL,1),(172,'2018-03-13 20:58:27',530.65,0,1,NULL,1),(173,'2018-03-13 21:00:06',530.65,0,1,NULL,1),(174,'2018-03-13 21:04:43',75.01,0,1,NULL,1),(175,'2018-03-13 21:15:32',36.78,0,2,NULL,1),(176,'2018-03-13 21:18:55',91.95,0,2,NULL,1),(177,'2018-03-13 21:28:38',75.01,0,3,NULL,1),(178,'2018-03-13 21:53:23',3863.96,0,4,NULL,1),(179,'2018-03-13 21:58:42',430.05,0,3,NULL,1),(180,'2018-03-13 22:04:13',9.2,0,4,NULL,1),(181,'2018-03-13 22:11:45',91.95,0,3,NULL,1),(182,'2018-03-13 22:15:03',530.65,0,3,NULL,1),(183,'2018-03-13 22:44:02',931.57,0,2,NULL,1),(184,'2018-03-13 22:46:19',846.9,0,2,NULL,1),(185,'2018-03-13 22:53:07',3136.41,0,1,NULL,1),(186,'2018-03-13 22:54:43',2728.37,0,2,NULL,1),(187,'2018-03-13 23:05:35',1914.34,0,2,NULL,1),(188,'2018-03-14 04:14:39',2613.28,261.33,2,NULL,2),(189,'2018-03-14 04:27:48',3359.38,-503.91,2,NULL,3),(191,'2018-03-14 12:54:38',1045.03,0,1,NULL,1),(192,'2018-03-14 13:19:55',4112.97,0,1,NULL,1),(193,'2018-03-14 14:36:16',874.55,0,2,NULL,1),(194,'2018-03-14 14:36:18',874.38,0,3,NULL,1),(195,'2018-03-14 14:39:20',4241.3,0,3,NULL,1),(196,'2018-03-14 14:40:32',2120.65,0,2,NULL,1),(197,'2018-03-14 14:42:04',848.26,0,2,NULL,1),(198,'2018-03-14 14:42:05',848.26,0,3,NULL,1),(199,'2018-03-14 14:44:53',1696.52,0,4,NULL,1),(200,'2018-03-14 14:44:54',1696.52,0,1,NULL,1),(201,'2018-03-14 14:47:26',183.9,0,3,NULL,1),(202,'2018-03-14 14:52:17',2862.22,-429.33,1,NULL,3),(203,'2018-03-14 14:57:05',1487.17,0,1,NULL,1),(204,'2018-03-14 22:13:44',64.05,0,1,NULL,1),(205,'2018-03-14 22:48:06',11633.34,0,1,NULL,1),(206,'2018-03-15 20:25:22',157.45,0,1,NULL,1),(207,'2018-03-15 20:26:24',1066.08,0,1,2,4),(208,'2018-03-16 02:08:51',871.4,0,1,NULL,1),(209,'2018-03-16 17:21:24',807.04,0,1,NULL,1),(210,'2018-03-17 19:26:32',421.09,0,1,NULL,1),(211,'2018-03-17 22:11:03',149.29,0,1,NULL,1),(212,'2018-03-20 15:00:16',298.58,0,1,NULL,1),(213,'2018-03-20 19:10:21',610.7,0,1,NULL,1),(214,'2018-03-20 19:11:17',158.34,0,4,NULL,1),(215,'2018-03-20 19:12:14',475.02,0,1,NULL,1),(216,'2018-03-20 19:14:23',135.68,0,4,NULL,1),(217,'2018-03-20 19:16:07',865.54,0,1,NULL,1),(218,'2018-03-20 19:26:49',8196.28,0,2,NULL,1),(219,'2018-03-20 19:28:11',4098.14,0,4,NULL,1),(220,'2018-03-20 19:28:11',4098.14,0,2,NULL,1),(221,'2018-03-20 19:29:35',8196.28,0,4,NULL,1),(222,'2018-03-20 19:37:06',462.03,0,1,NULL,1),(223,'2018-03-21 20:36:12',562.41,0,1,NULL,1),(224,'2018-03-26 23:08:20',597.02,0,1,NULL,1),(225,'2018-04-17 21:36:14',2622.6,0,1,1,4),(226,'2018-04-26 15:19:26',1097.1,0,1,NULL,1),(227,'2018-04-26 15:57:37',2712.5,-406.87,1,NULL,3),(228,'2018-04-26 15:44:30',1446.15,0.38,1,NULL,2),(229,'2018-04-26 15:19:18',1596.33,0,1,NULL,1),(230,'2018-04-26 15:39:37',437.1,0,3,NULL,1),(231,'2018-04-26 18:20:32',819.57,0,1,NULL,1),(232,'2018-04-26 18:42:41',274.08,0,1,NULL,1),(233,'2018-04-26 18:46:34',437.1,0,1,NULL,1),(234,'2018-04-26 18:51:17',764.94,0,1,NULL,1),(235,'2018-04-27 15:00:39',482.05,0,1,NULL,1),(236,'2018-04-27 15:31:44',819.57,0,1,NULL,1),(237,'2018-04-27 15:34:57',382.47,0,2,NULL,1),(238,'2018-04-27 16:09:02',1034.92,0,1,NULL,1),(239,'2018-04-27 16:08:53',517.46,0,1,NULL,1),(240,'2018-04-27 15:35:07',1060.6,-159.09,1,NULL,3),(241,'2018-04-27 16:09:17',382.47,0,1,NULL,1),(242,'2018-04-29 12:46:53',1765.6299999999999,0,1,3,4),(243,'2018-04-29 12:48:06',382.47,0,1,2,4),(244,'2018-04-29 15:16:07',1142,0,1,NULL,1),(245,'2018-04-29 14:55:58',1443.07,NULL,1,NULL,NULL),(246,'2018-04-29 14:57:49',2284,NULL,1,NULL,NULL),(247,'2018-04-29 15:15:40',2695.12,NULL,1,NULL,NULL),(248,'2018-04-29 15:15:54',4139.12,0,1,NULL,1),(249,'2018-04-29 23:13:19',482.05,0,1,NULL,1),(250,'2018-05-02 20:33:14',695.41,NULL,1,NULL,NULL),(251,'2018-05-03 13:19:05',482.05,0,1,NULL,1),(252,'2018-05-03 18:32:14',874.2,0,1,1,4),(253,'2018-05-19 00:14:00',382.47,0,1,NULL,1),(254,'2018-05-03 18:33:15',482.05,0,2,NULL,1),(255,'2018-05-03 18:34:42',80.36,0,1,NULL,1),(256,'2018-05-03 18:49:29',1356.25,0,1,NULL,1),(257,'2018-05-03 18:49:52',80.36,0,1,NULL,1),(258,'2018-05-19 00:14:09',9.85,0,2,NULL,1),(259,'2018-05-19 00:13:48',454.33,0,1,NULL,1),(260,'2018-05-23 15:17:30',445.84,0,1,NULL,1),(261,'2018-05-24 18:10:32',491.69,63.92,1,NULL,2),(262,'2018-05-30 23:28:27',2458.45,0,1,NULL,1),(263,'2018-06-04 19:18:05',130.61,0,1,NULL,1),(264,'2018-06-05 17:57:53',1400,0,1,NULL,1),(265,'2018-06-05 18:08:40',1560.48,0,1,NULL,1),(266,'2018-06-05 21:08:10',160,0,1,NULL,1),(267,'2018-06-05 21:13:24',579.3399999999999,0,1,NULL,1),(268,'2018-06-05 21:16:37',1006.97,0,1,NULL,1),(269,'2018-06-05 21:17:55',1142,0,1,NULL,1),(270,'2018-06-05 21:19:02',2015.67,0,1,NULL,1),(271,'2018-06-06 01:15:47',1337.52,0,1,NULL,1),(272,'2018-06-10 14:16:03',10.15,0,1,NULL,1),(273,'2018-06-15 20:50:57',1176.49,0,1,NULL,1),(274,'2018-06-18 01:27:10',50.74,0,1,NULL,1);
/*!40000 ALTER TABLE `ventas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-17 23:19:48
