-- Populate Members Table
INSERT INTO Members (balance, mem_name, email_address, goal_weight, BMI, weight_kg, height_cm)
VALUES
(20.99,'John Doe', 'john.doe@example.com', 80, 25.2, 85, 180),
(20.99,'Jane Smith', 'jane.smith@example.com', 65, 22.5, 70, 165),
(20.99,'Alice Johnson', 'alice.j@example.com', 55, 18.5, 60, 170),
(20.99,'Bob Brown', 'bob.brown@example.com', 90, 27.8, 95, 175);

-- Populate Trainer Table
INSERT INTO Trainer (train_name, availability)
VALUES
('Chris Fit', 1),
('Pat Strong', 2),
('Terry Lifts', 3),
('Sam Sprint', 1);

-- Populate Administrator Table
INSERT INTO Administrator (full_name)
VALUES
('Admin Istrator'),
('Manage R.Manager');

-- Populate Equipment Table
INSERT INTO Equipment (maintenance_date, description)
VALUES
('2024-03-15', 'Treadmill'),
('2024-01-20', 'Stationary Bike'),
('2024-04-10', 'Rowing Machine'),
('2024-02-25', 'Elliptical Machine');

-- Populate Room Table
INSERT INTO Room 
DEFAULT VALUES;

INSERT INTO Room 
DEFAULT VALUES;

INSERT INTO Room 
DEFAULT VALUES;