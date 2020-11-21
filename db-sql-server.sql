--Create database
CREATE DATABASE school_system;

USE school_system;

--Create user entity
CREATE TABLE users(
	user_name VARCHAR(15) PRIMARY KEY,
	password VARCHAR(15) NOT NULL,
	user_role VARCHAR(10) FOREIGN KEY REFERENCES auth_roles(id),
	display_name NVARCHAR(25) NOT NULL,
	dob DATE NOT NULL,
	gender VARCHAR(10) NOT NULL,
	cnic CHAR(13) NOT NULL,
	mobile_no CHAR(12) NOT NULL,
	emergency_contact CHAR(12) NOT NULL,
	qualification NVARCHAR(25),
	registration_date DATE NOT NULL DEFAULT GETDATE(),
	active BIT NOT NULL DEFAULT 1,
);

-- Create session entity
CREATE TABLE session_table(
	code VARCHAR(5) PRIMARY KEY,
	title VARCHAR(MAX) NOT NULL,
	start_date DATE UNIQUE NOT NULL,
	end_date DATE
);

CREATE TABLE department(
	code VARCHAR(5) PRIMARY KEY,
	title VARCHAR(MAX) NOT NULL,
	active BIT
);

-- Create student entity
CREATE TABLE student(
	session_code VARCHAR(5) FOREIGN KEY REFERENCES session_table(code),
	department_code VARCHAR(5) FOREIGN KEY REFERENCES department(code),
	roll_no INT NOT NULL,
	name VARCHAR(25) NOT NULL,
	father_name NVARCHAR(25) NOT NULL,
	mobile_no CHAR(12) NOT NULL,
	emergency_contact CHAR(12) NOT NULL,
	registeration_date DATE NOT NULL,
	dob DATE NOT NULL,
	address NVARCHAR(MAX) NOT NULL,
	gender VARCHAR(10) NOT NULL,
	active BIT NOT NULL DEFAULT 1,
	PRIMARY KEY (session_code, department_code, roll_no)
);

-- Create session enitity
CREATE TABLE school_system.dbo.term(
	id INT PRIMARY KEY IDENTITY(1, 1),
	title NVARCHAR(25) NOT NULL,
	start_date DATE NOT NULL,
	end_date DATE,
);

-- Create exam group entity
CREATE TABLE school_system.dbo.exam_group(
	id INT PRIMARY KEY IDENTITY(1, 1),
	title NVARCHAR(25) NOT NULL, -- Quiz 1, Mid, Final etc
	session_id INT NOT NULL FOREIGN KEY REFERENCES term(id),
);

-- create teacher attendance entity
CREATE TABLE school_system.dbo.attendance_teacher(
	attendance_date DATE NOT NULL,
	teacher_id INT FOREIGN KEY REFERENCES teacher(id) NOT NULL,
	time_in TIME NOT NULL,
	PRIMARY KEY(attendance_date, teacher_id),
);

-- create subject entity
CREATE TABLE school_system.dbo.subject(
	id INT PRIMARY KEY IDENTITY(1, 1),
	title NVARCHAR(25) NOT NULL,
);

-- create class entity
CREATE TABLE school_system.dbo.class(
	id INT PRIMARY KEY IDENTITY(1, 1),
	term_id INT NOT NULL FOREIGN KEY REFERENCES term(id),
	name CHAR(5) NOT NULL,
);

-- create examination entity
CREATE TABLE school_system.dbo.examination(
	id INT PRIMARY KEY IDENTITY(1, 1),
	exam_date DATE NOT NULL,
	exam_time TIME NOT NULL,
	class_id INT FOREIGN KEY REFERENCES class(id) NOT NULL,
	exam_group INT FOREIGN KEY REFERENCES exam_group(id),
	subject_id INT FOREIGN KEY REFERENCES subject(id),
);

-- create exam result entity
CREATE TABLE school_system.dbo.exam_result(
	exam_id INT FOREIGN KEY REFERENCES examination(id),
	student_roll_no INT FOREIGN KEY REFERENCES student(roll_no),
	marks FLOAT NOT NULL,
	PRIMARY KEY(exam_id, student_roll_no),
);

CREATE TABLE school_system.dbo.class_register(
	class_id INT FOREIGN KEY REFERENCES class(id) NOT NULL,
	student_roll_no INT FOREIGN KEY REFERENCES student(roll_no),
	PRIMARY KEY(class_id, student_roll_no),
);

-- create student attendance entity
CREATE TABLE school_system.dbo.attendance_student(
	attendance_date DATE NOT NULL,
	student_roll_no INT FOREIGN KEY REFERENCES student(roll_no) NOT NULL,
	PRIMARY KEY(attendance_date, student_roll_no),
);

-- create time table entity
CREATE TABLE school_system.dbo.time_table(
	term_id INT FOREIGN KEY REFERENCES term(id),
	week_day CHAR(10) NOT NULL,
	class_time TIME NOT NULL,
	subject_id INT FOREIGN KEY REFERENCES subject(id),
	teacher_id INT FOREIGN KEY REFERENCES teacher(id),
	class_id INT FOREIGN KEY REFERENCES class(id),
	PRIMARY KEY(week_day, class_time, class_id),
);

CREATE TABLE auth_roles(
	id VARCHAR(10) PRIMARY KEY
);

-- create predefined roles
INSERT INTO auth_roles VALUES('admin'), ('teacher');

-- stored procedures
CREATE PROCEDURE sp_get_user 
@user_name VARCHAR(15), 
@password VARCHAR(15)
AS
BEGIN
	SELECT * FROM users WHERE user_name=@user_name AND password=@password;
END;

CREATE PROCEDURE sp_patch_user
@user_name VARCHAR(15),
@password VARCHAR(15) = NULL,
@display_name VARCHAR(25) = NULL,
@dob DATE = NULL,
@gender VARCHAR(10) = NULL,
@cnic CHAR(13) = NULL,
@mobile_no CHAR(12) = NULL,
@emergency_contact CHAR(12) = NULL,
@qualification NVARCHAR(25) = NULL,
@address VARCHAR(100) = NULL,
@active BIT = NULL
AS
BEGIN
	IF (@password IS NOT NULL)
	BEGIN
		UPDATE users
		SET password=@password
		WHERE user_name=@user_name
	END
	
	
	IF (@display_name IS NOT NULL)
	BEGIN
		UPDATE users
		SET display_name=@display_name
		WHERE user_name=@user_name
	END
	
	
	IF (@dob IS NOT NULL)
	BEGIN 
		UPDATE users
		SET dob=@dob
		WHERE user_name=@user_name
	END
	
	IF (@gender IS NOT NULL)
	BEGIN 
		UPDATE users
		SET gender=@gender
		WHERE user_name=@user_name	
	END
	
	IF (@cnic IS NOT NULL)
	BEGIN
		UPDATE users
		SET cnic=@cnic
		WHERE user_name=@user_name
	END
	
	IF (@mobile_no IS NOT NULL)
	BEGIN 
		UPDATE users
		SET mobile_no=@mobile_no
		WHERE user_name=@user_name
	END
	
	IF (@emergency_contact IS NOT NULL)
	BEGIN
		UPDATE users
		SET emergency_contact=@emergency_contact
		WHERE user_name=@user_name
	END
	
	IF (@qualification IS NOT NULL)
	BEGIN 
		UPDATE users
		SET qualification=@qualification
		WHERE user_name=@user_name
	END
	
	IF (@address IS NOT NULL)
	BEGIN 
		UPDATE users
		SET address=@address
		WHERE user_name=@user_name
	END
	
	IF (@active IS NOT NULL)
	BEGIN
		UPDATE users
		SET active=@active
		WHERE user_name=@user_name
	END
END;

CREATE PROCEDURE sp_post_session
@code VARCHAR(5),
@title VARCHAR(MAX),
@start_date DATE,
@end_date DATE
AS
BEGIN
	INSERT INTO session_table
	VALUES
	(@code, @title, @start_date, @end_date)
END;

CREATE PROCEDURE sp_post_department
AS
BEGIN
	
END;

CREATE PROCEDURE sp_post_student
@department_code VARCHAR(5),
@session_code VARCHAR(5),
@name VARCHAR(25),
@father_name VARCHAR(25),
@mobile_no VARCHAR(12),
@emergency_contact VARCHAR(12),
@registeration_date DATE,
@dob DATE,
@address VARCHAR(MAX),
@gender VARCHAR(10),
@active BIT
AS
BEGIN
	DECLARE @max_roll_no INT
	
	SELECT @max_roll_no=MAX(roll_no) 
	FROM student 
	WHERE department_code=@department_code
	AND session_code=@session_code
			
	INSERT INTO student
	VALUES(
	@department_code,
	@session_code,
	@roll_no + 1,
	@name,
	@father_name)
END;

































