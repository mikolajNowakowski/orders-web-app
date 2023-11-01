
CREATE TABLE customers
(
 id INT PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(100)   NOT NULL,
 surname VARCHAR(100)   NOT NULL,
 age DECIMAL(3) NOT NULL,
 email varchar(100) NOT NULL
);


CREATE TABLE products
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name  VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(100) NOT NULL
);

CREATE TABLE orders
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity DECIMAL(10) NOT NULL,
    order_date VARCHAR(100) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE users
(
    id       integer primary key auto_increment,
    username varchar(50)  not null,
    email    varchar(50)  not null,
    password varchar(255) not null,
    role     varchar(50)  not null,
    enabled  boolean      not null
);