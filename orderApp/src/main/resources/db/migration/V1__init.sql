
CREATE TABLE IF NOT EXISTS b_orders
(
    id int AUTO_INCREMENT primary key,
    is_payed BOOLEAN,
    payment_method varchar(50),
    delivery_method varchar(50),
    status varchar(50),
    user_id varchar(50),
    created timestamp not null,
    modified timestamp
);

CREATE TABLE IF NOT EXISTS b_ordered_products
(
    id int AUTO_INCREMENT primary key,
    order_id int,
    product_id int,
    quantity_bought int,
    constraint FK_ORDER_PRODUCTS FOREIGN KEY (order_id) REFERENCES b_orders(id)
);

CREATE TABLE IF NOT EXISTS b_address
(
    id int AUTO_INCREMENT primary key,
    order_id int,
    street varchar(100),
    zip_code varchar(6),
    city varchar(50),
    country varchar(50),
    phone_number varchar(12),
    email varchar(50),
    constraint FK_ORDER_ADDRESS FOREIGN KEY (order_id) REFERENCES b_orders(id)
);
