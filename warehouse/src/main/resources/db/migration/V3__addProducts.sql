INSERT INTO b_product (`name`, `product_code`, `quantity`, `price`, `status`, `type`, `subtype`, `created`)
VALUES ('product1', '001', '10', '99.99', 'OK', 'TV', 'FLAT_SCREEN', '2022-02-02T00:00:00+01:00');

INSERT INTO b_product (`name`, `product_code`, `quantity`, `price`, `status`, `type`, `subtype`, `created`)
VALUES ('product2', '002', '20', '15.05', 'OK', 'TV', 'FLAT_SCREEN', '2022-02-02T00:00:00+01:00');

INSERT INTO b_product (`name`, `product_code`, `quantity`, `price`, `status`, `type`, `subtype`, `created`)
VALUES ('product3', '003', '80', '13', 'AVAILABLE', 'TOOLS', 'ACCESSORIES', '2022-02-02T00:00:00+01:00');

INSERT INTO b_product (`name`, `product_code`, `quantity`, `price`, `status`, `type`, `subtype`, `created`)
VALUES ('product4', '004', '1', '8', 'ALMOST_EMPTY', 'TOOLS', 'ACCESSORIES', '2022-02-02T00:00:00+01:00');

INSERT INTO b_product_description (`product_id`, `description`) VALUES ('1', 'test1');


INSERT INTO b_product_description (`product_id`, `description`) VALUES ('2', 'test2');


INSERT INTO b_product_description (`product_id`, `description`) VALUES ('4', 'test3');
