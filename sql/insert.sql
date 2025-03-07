INSERT INTO Customers (customer_name, email, passwd) VALUES 
('Alice', 'alice@example.com', '123456'), 
('Bob', 'bob@example.com', '654321'); -- 插入客户数据

INSERT INTO Accounts (customer_id, balance, passwd) VALUES 
(1, 1000.00, '111111'), 
(2, 500.00, '222222'); -- 插入账户数据

INSERT INTO Transactions (account_id, amount, transaction_type) VALUES 
(1, 200.00, 'deposit'), 
(1, 50.00, 'withdraw'), 
(2, 300.00, 'deposit'); -- 插入交易数据