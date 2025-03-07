CREATE DATABASE BANK;
USE BANK;

DROP TABLE IF EXISTS Transactions;
DROP TABLE IF EXISTS Accounts;
DROP TABLE IF EXISTS Customers;


CREATE TABLE Customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY, -- 客户ID，自动递增
    customer_name VARCHAR(100) NOT NULL, -- 姓名，不可为空
    email VARCHAR(100) UNIQUE NOT NULL, -- 邮箱，唯一且不可为空
    passwd VARCHAR(100) NOT NULL -- 密码，不可为空
    
); 

CREATE TABLE Accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY, -- 帐户ID，自动递增
    customer_id INT NOT NULL, -- 客户ID
    balance DECIMAL(10, 2) DEFAULT 0.00, -- 账户余额，默认值为0
    passwd VARCHAR(6) NOT NULL, -- 密码，不可为空
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) -- 外键关联
); 

CREATE TABLE Transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY, -- 交易ID，自动递增
    account_id INT NOT NULL, -- 帐户ID
    target_account_id INT, -- 对方账户ID
    amount DECIMAL(10, 2) NOT NULL, -- 交易金额
    transaction_type ENUM('deposit', 'withdraw', 'transfer') NOT NULL, -- 交易类型
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP, -- 交易时间，默认当前时间
    FOREIGN KEY (account_id) REFERENCES Accounts(account_id), -- 外键关联
    FOREIGN KEY (target_account_id) REFERENCES Accounts(account_id) -- 外键关联
);