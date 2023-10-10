CREATE TABLE IF NOT EXISTS b_orders
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