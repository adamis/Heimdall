CREATE TABLE Catalogs
(
   id varchar (255) NOT NULL,
   original_name varchar (255) NOT NULL,
   heimall_name varchar (255) NOT NULL,
   extension varchar (100) NOT NULL,
   size_file longblob DEFAULT NULL,
   created_at datetime DEFAULT NULL ON UPDATE current_timestamp (),
   update_at datetime DEFAULT NULL ON UPDATE current_timestamp (),
   PRIMARY KEY (id)
);
CREATE TABLE Usuarios
(
   id int (11) NOT NULL AUTO_INCREMENT,
   usuario varchar (255) NOT NULL,
   senha varchar (255) NOT NULL,
   created_at datetime DEFAULT NULL ON UPDATE current_timestamp (),
   update_at datetime DEFAULT NULL ON UPDATE current_timestamp (),
   PRIMARY KEY (id)
);
CREATE TABLE Temp_links
(
   id int (11) NOT NULL AUTO_INCREMENT,
   usuario int (11) DEFAULT NULL,
   file_original varchar (255) DEFAULT NULL,
   temp_link varchar (255) DEFAULT NULL,
   date_ini datetime DEFAULT NULL ON UPDATE current_timestamp (),
   date_fim datetime DEFAULT NULL ON UPDATE current_timestamp (),
   PRIMARY KEY (id),
   UNIQUE KEY id (id) USING BTREE,
   KEY usuario (usuario),
   CONSTRAINT temp_links_ibfk_1 FOREIGN KEY (usuario) REFERENCES Usuarios (id)
);