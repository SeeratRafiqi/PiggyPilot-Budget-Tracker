INSERT INTO users(first_name, last_name, email, phone, password, total_amount) VALUES
    ('John', 'Doe', 'john@gmail.com', 123456789, '$2a$12$JGriZzgFwZNEeuIzFcocjug9wb0/G0EJ1nco27FZoCvVLmfpfiiWe', 1000);
INSERT INTO users(first_name, last_name, email, phone, password, total_amount) VALUES
    ('Alice', 'Smith', 'alice@gmail.com', 987654321, '$2a$12$JGriZzgFwZNEeuIzFcocjug9wb0/G0EJ1nco27FZoCvVLmfpfiiWe', 750);
INSERT INTO users(first_name, last_name, email, phone, password, total_amount) VALUES
    ('Bob', 'Johnson', 'bob@gmail.com', 555123456, '$2a$12$JGriZzgFwZNEeuIzFcocjug9wb0/G0EJ1nco27FZoCvVLmfpfiiWe', 600);
INSERT INTO users(first_name, last_name, email, phone, password, total_amount) VALUES
    ('Sam', 'Alexis', 'imnotsam@gmail.com', 444123456, '$2a$12$JGriZzgFwZNEeuIzFcocjug9wb0/G0EJ1nco27FZoCvVLmfpfiiWe', 700);

-- Insert into groups table with dates in dd-mm-yyyy format
INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Transport', '12-06-2024', '30-06-2024', 40);
INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Groceries', '12-06-2024', '30-06-2024', 300);
INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Shopping', '01-06-2024', '30-06-2024', 200);
INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Bill', '01-06-2024', '30-06-2024', 100);
INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Rent', '01-06-2024', '30-06-2024', 200);
INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Rent', '01-01-2024', '30-01-2024', 200);
INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Utility', '02-01-2024', '25-01-2024', 200);
INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Rent', '02-02-2024', '28-02-2024', 200);
INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Rent', '02-03-2024', '28-03-2024', 200);
INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Rent', '02-04-2024', '28-04-2024', 200);

INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Shopping', '02-02-2024', '28-02-2024', 300);
INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Vacation', '02-02-2024', '28-02-2024', 100);
INSERT INTO groups (Category, Start_date, End_date, BAmount) VALUES ('Rent', '02-01-2024', '28-01-2024', 300);

-- Update user_id in groups table
UPDATE groups SET user_id = 1
WHERE Budget_Id IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 14, 15, 16);

UPDATE groups SET user_id = 2
WHERE Budget_Id IN (11, 12, 13);

-- Insert into transactions table with dates in dd-mm-yyyy format
INSERT INTO transactions (payment_method, note, date, amount, budget_id)
VALUES ('Cash', 'Dinner with friends', '10-06-2024', 50.00, 2);

INSERT INTO transactions (payment_method, note, date, amount, budget_id)
VALUES ('Card', 'Online shopping', '20-06-2024', 149.95, 3);

INSERT INTO transactions (payment_method, note, date, amount, budget_id)
VALUES ('Cash', 'Lunch at work', '05-06-2024', 15.00, 1);

INSERT INTO transactions (payment_method, note, date, amount, budget_id)
VALUES ('Card', 'Grocery shopping', '15-01-2024', 75.25, 6);

INSERT INTO transactions (payment_method, note, date, amount, budget_id)
VALUES ('Cash', 'Movie night', '25-01-2024', 20.00, 13);
