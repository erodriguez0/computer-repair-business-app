create type ticket_type as enum ('HW', 'SW', 'SW/HW', 'HW/SW');
create type part_type as enum ('SW','GPU','CPU','MB','HDD','SSD','PSU','SOUND CARD','RAM','SYSTEM FAN','CPU COOLER','TV TUNER','MONITOR','WEBCAM','KB','MOUSE');
create type status_type as enum ('OPEN','IN PROGRESS','COMPLETED');
create type p_status as enum ('OPEN','IN PROGRESS','ACCEPTED', 'DECLINED');

CREATE TABLE IF NOT EXISTS employee (
	emp_id 		serial 		PRIMARY KEY,
	super_id 	int 		UNIQUE,
	fname 		varchar(50) NOT NULL,
	minit 		varchar(1),
	lname 		varchar(50) NOT NULL,
	address 	varchar(50) NOT NULL,
	city 		varchar(50) NOT NULL,
	state 		varchar(2) 	NOT NULL,
	zip 		varchar(5) 	NOT NULL,
	phone 		varchar(12) NOT NULL,
	bdate 		date 		NOT NULL,
	ssn 		varchar(11) UNIQUE NOT NULL,
	jobtitle 	varchar(19) NOT NULL,
	username	varchar(50) UNIQUE NOT NULL,
	password	varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS customer (
	cust_id 	serial 		PRIMARY KEY,
	username 	VARCHAR(50) null UNIQUE,
	password 	VARCHAR(50) null,
	fname 		VARCHAR(50) not null,
	minit 		VARCHAR(1) null,
	lname 		VARCHAR(50) not null,
	address 	VARCHAR(50) not null,
	city 		VARCHAR(50) not null,
	state 		VARCHAR(2) 	not null,
	zip 		VARCHAR(5) 	not null,
	phone 		VARCHAR(12) not null,
	email		VARCHAR(50) not null UNIQUE
);

CREATE TABLE IF NOT EXISTS equipment (
	comp_id 	serial 		PRIMARY KEY,
	cust_id 	int 		references customer(cust_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	submit_date date 		not null,
	address 	varchar(50) not null,
	city 		varchar(50) not null,
	state 		varchar(2) 	not null,
	zip 		varchar(5) 	not null
);

CREATE TABLE IF NOT EXISTS customer_problem (
	problem_id 		serial 		 PRIMARY KEY,
	comp_id 		int 		 references equipment(comp_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	emp_id 			int 		 references employee(emp_id) ON DELETE RESTRICT ON UPDATE CASCADE null,
	submit_date     date 		 not null,
	review_date 	date,
	status 			p_status default 'OPEN',
	description 	varchar(500) not null
);

CREATE TABLE IF NOT EXISTS ticket (
	ticket_id 		serial 		  PRIMARY KEY,
	problem_id 		int 		  references customer_problem(problem_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	status 			status_type   default 'OPEN',
	type 			ticket_type   not null,
	description 	varchar(500)  not null,
	return_label 	varchar(50),
	total_cost 		decimal(10,2) default 0.00 check (total_cost >= 0)
);

CREATE TABLE IF NOT EXISTS manufacturer (
	mnfr_id		serial 		PRIMARY KEY,
	mnfr_name	varchar(50) not null,
	mnfr_phone	varchar(50) not null,
	mnfr_addr	varchar(50) not null,
	mnfr_city	varchar(50) not null,
	mnfr_state  varchar(50) not null,
	mnfr_zip	varchar(50) not null
);

CREATE TABLE IF NOT EXISTS part_info (
	model_id	serial 		PRIMARY KEY,
	mnfr_id		int 		references manufacturer(mnfr_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	model_name	varchar(50)	not null,
	part_type	part_type	not null
);

CREATE TABLE IF NOT EXISTS part (
	part_id		serial 		  PRIMARY KEY,
	ticket_id 	int 		  references ticket(ticket_id) ON DELETE RESTRICT ON UPDATE CASCADE,	
	model_id 	int 		  references part_info(model_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	serial_num	varchar(50)	  not null,
	-- quantity 	int 		  not null check (quantity > 0),
	part_price 	decimal(10,2) not null check (part_price > 0)
);

CREATE TABLE IF NOT EXISTS work_on (
	work_id 	serial 	PRIMARY KEY,
	ticket_id 	int 	references ticket(ticket_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	emp_id 		int 	references employee(emp_id) ON DELETE RESTRICT ON UPDATE CASCADE,
	start_date 	date,
	end_date 	date
);