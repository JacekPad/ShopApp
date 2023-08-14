CREATE TABLE IF NOT EXISTS b_product
(
    id int AUTO_INCREMENT primary key,
    name varchar(255) not null,
    product_code varchar(255) not null,
    quantity int not null,
    price decimal(10,2) not null,
    status varchar(255) not null,
    type varchar(255) not null,
    subtype varchar(255) not null,
    created timestamp not null,
    modified timestamp
);

CREATE TABLE IF NOT EXISTS b_product_description
(
    id int AUTO_INCREMENT primary key,
    product_id int,
    description varchar(255),
    constraint FK_PRODUCT FOREIGN KEY (product_id) REFERENCES b_product(id)
);

create table IF NOT EXISTS e_product_type (
	id int AUTO_INCREMENT primary key,
    code varchar(30) unique,
    value varchar(30)
);

create table IF NOT EXISTS e_product_subtype (
id int AUTO_INCREMENT primary key,
type_id int not null,
code varchar(30),
value varchar(30),
constraint FK_PRODUCT_TYPE FOREIGN KEY (type_id) REFERENCES e_product_type(id)
);

create table IF NOT EXISTS e_product_status (
	id int AUTO_INCREMENT primary key,
    code varchar(30) unique,
    value varchar(30)
);

create table IF NOT EXISTS t_application_logs (
	id int AUTO_INCREMENT primary key,
    entity_id int not null,
    message varchar(200),
    type varchar(20) not null,
    timestamp timestamp not null
);

create table IF NOT EXISTS t_error_logs (
	id int AUTO_INCREMENT primary key,
    error_code varchar(5) not null,
    message varchar(200),
    trace varchar(4000),
    timestamp timestamp not null
);