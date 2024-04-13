create table Members
	(member_id serial,
	balance FLOAT,
	mem_name varchar(255) not null,
	email_address varchar(255) not null,
	goal_weight float,
	BMI float,
	weight_kg float,
	height_cm float,
	primary key (member_id)
	);


create table Ex_Routine
	(routine_id serial,
	 member_id int,
	 routine_description TEXT,
	 primary key (routine_id),
	 foreign key (member_id) references Members
	);
	
create table Achievements
	(achievement_id serial,
	 member_id int,
	 achievement_description TEXT,
	 primary key (achievement_id),
	 foreign key (member_id) references Members
	);
	
create table Trainer
	(trainer_id serial,
	train_name varchar(255) not null,
	 availability int,
	 primary key (trainer_id)
	);
	
create table Trains
	(member_id int,
	 trainer_id int,
	 timeslot int,
	 price float,
	 foreign key (member_id) references Members,
	 foreign key (trainer_id) references Trainer
	);

create table Administrator
	(admin_id serial,
	 full_name varchar(255) not null,
	 primary key (admin_id)
	);
	
create table Payment
	(member_id int,
	admin_id int,
	amount float,
	payment_date date,
	foreign key (member_id) references Members,
	foreign key (admin_id) references Administrator
	);
	
create table Equipment
	(equipment_id serial,
	 maintenance_date date,
	 description TEXT,
	 primary key (equipment_id)
	);
	
create table Room
	(room_id serial,
	 primary key (room_id)
	);
	
create table FitClass
	(class_id serial,
	trainer_id int,
	room_id int,
	primary key (class_id),
	foreign key (trainer_id) references Trainer,
	foreign key (room_id) references Room
	);
	
create table Class_Members
	(class_id int,
	 member_id int,
	 foreign key (class_id) references FitClass,
	 foreign key (member_id) references Members
	);
	
create table Teaches
	(trainer_id int,
	class_id int,
	 timeslot int,
	 foreign key (trainer_id) references Trainer,
	 foreign key (class_id) references FitClass
	 );

	
	
	