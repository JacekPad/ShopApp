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

