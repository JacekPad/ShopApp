CREATE TABLE IF NOT EXISTS b_product
(
    id int AUTO_INCREMENT primary key,
    name varchar(255) not null,
    product_code varchar(255) not null,
    quantity int not null,
    price int not null,
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
type_code varchar(50) not null,
code varchar(30),
value varchar(30),
constraint FK_PRODUCT_TYPE FOREIGN KEY (type_code) REFERENCES e_product_type(code)
);

