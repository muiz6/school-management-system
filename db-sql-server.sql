--Create database
CREATE DATABASE school_system;

USE school_system;

CREATE TABLE auth_roles(
	id VARCHAR(10) PRIMARY KEY
);

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
	active BIT NOT NULL DEFAULT 1
);
ALTER TABLE users ADD CONSTRAINT chk_gender CHECK (gender IN ('male', 'female'));

-- Create session entity
CREATE TABLE session_table(
	code VARCHAR(5) PRIMARY KEY,
	title VARCHAR(MAX) NOT NULL,
	start_date DATE UNIQUE NOT NULL,
	end_date DATE
);

CREATE TABLE departments(
	code VARCHAR(5) PRIMARY KEY,
	title VARCHAR(MAX) NOT NULL,
	active BIT NOT NULL DEFAULT 1
);

CREATE TABLE classes(
	session_code VARCHAR(5) FOREIGN KEY REFERENCES session_table(code),
	department_code VARCHAR(5) FOREIGN KEY REFERENCES departments(code),
	code VARCHAR(5) NOT NULL,
	active BIT DEFAULT 1,
	PRIMARY KEY(session_code, department_code, code)
);

-- Create student entity
CREATE TABLE students(
	session_code VARCHAR(5) FOREIGN KEY REFERENCES session_table(code),
	department_code VARCHAR(5) FOREIGN KEY REFERENCES departments(code),
	roll_no INT NOT NULL,
	name VARCHAR(25) NOT NULL,
	father_name NVARCHAR(25) NOT NULL,
	cnic VARCHAR(13),
	mobile_no CHAR(12) NOT NULL,
	emergency_contact CHAR(12) NOT NULL,
	dob DATE NOT NULL,
	address VARCHAR(MAX) NOT NULL,
	gender VARCHAR(10) NOT NULL,
	registeration_date DATE NOT NULL DEFAULT GETDATE(),
	active BIT NOT NULL DEFAULT 1,
	PRIMARY KEY (session_code, department_code, roll_no)
);

CREATE TABLE class_register(
	department_code VARCHAR(5) FOREIGN KEY REFERENCES departments(code),
	session_code VARCHAR(5) FOREIGN KEY REFERENCES session_table(code),
	class_code VARCHAR(5) NOT NULL,
	student_roll_no INT NOT NULL,
	PRIMARY KEY(department_code, session_code, class_code, student_roll_no)
);

CREATE TABLE audit_student(
	log_date DATE NOT NULL
);

CREATE TABLE audit_user(
	log_date DATE NOT NULL
);

-- Create exam group entity
--CREATE TABLE school_system.dbo.exam_group(
--	id INT PRIMARY KEY IDENTITY(1, 1),
--	title NVARCHAR(25) NOT NULL, -- Quiz 1, Mid, Final etc
--	session_id INT NOT NULL FOREIGN KEY REFERENCES term(id),
--);

-- create subject entity
--CREATE TABLE school_system.dbo.subject(
--	id INT PRIMARY KEY IDENTITY(1, 1),
--	title NVARCHAR(25) NOT NULL,
--);

-- create examination entity
--CREATE TABLE school_system.dbo.examination(
--	id INT PRIMARY KEY IDENTITY(1, 1),
--	exam_date DATE NOT NULL,
--	exam_time TIME NOT NULL,
--	class_id INT FOREIGN KEY REFERENCES class(id) NOT NULL,
--	exam_group INT FOREIGN KEY REFERENCES exam_group(id),
--	subject_id INT FOREIGN KEY REFERENCES subject(id),
--);

-- create exam result entity
--CREATE TABLE school_system.dbo.exam_result(
--	exam_id INT FOREIGN KEY REFERENCES examination(id),
--	student_roll_no INT FOREIGN KEY REFERENCES student(roll_no),
--	marks FLOAT NOT NULL,
--	PRIMARY KEY(exam_id, student_roll_no),
--);

-- create student attendance entity
--CREATE TABLE school_system.dbo.attendance_student(
--	attendance_date DATE NOT NULL,
--	student_roll_no INT FOREIGN KEY REFERENCES student(roll_no) NOT NULL,
--	PRIMARY KEY(attendance_date, student_roll_no),
--);

-- create time table entity
--CREATE TABLE school_system.dbo.time_table(
--	term_id INT FOREIGN KEY REFERENCES term(id),
--	week_day CHAR(10) NOT NULL,
--	class_time TIME NOT NULL,
--	subject_id INT FOREIGN KEY REFERENCES subject(id),
--	teacher_id INT FOREIGN KEY REFERENCES teacher(id),
--	class_id INT FOREIGN KEY REFERENCES class(id),
--	PRIMARY KEY(week_day, class_time, class_id),
--);

-- stored procedures
CREATE PROCEDURE sp_get_user 
@user_name VARCHAR(15), 
@password VARCHAR(15)
AS
BEGIN
	SELECT * FROM users WHERE user_name=@user_name AND password=@password;
END;

CREATE PROCEDURE sp_get_user_by_user_name
@user_name VARCHAR(15)
AS
BEGIN
	SELECT * FROM users WHERE user_name=@user_name;
END;

CREATE PROCEDURE sp_post_user
@user_name VARCHAR(15),
@password VARCHAR(15),
@auth_role VARCHAR(10),
@display_name VARCHAR(25),
@dob DATE,
@gender VARCHAR(10),
@cnic CHAR(13),
@mobile_no CHAR(12),
@emergency_contact CHAR(12),
@qualification NVARCHAR(25),
@address VARCHAR(100)
AS
BEGIN
	INSERT INTO users(
	user_name,
	password,
	user_role,
	display_name,
	dob,
	gender,
	cnic,
	mobile_no,
	emergency_contact,
	qualification,
	address)
	VALUES(
	@user_name,
	@password,
	@auth_role,
	@display_name,
	@dob,
	@gender,
	@cnic,
	@mobile_no,
	@emergency_contact,
	@qualification,
	@address)
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

CREATE PROCEDURE sp_get_teachers
AS
BEGIN
	SELECT * FROM users 
	WHERE user_role = 'teacher' AND active=1;
END;

CREATE PROCEDURE sp_get_teachers_by_search
@query VARCHAR(20)
AS
BEGIN
	SELECT * FROM users 
	WHERE user_role = 'teacher' 
	AND active = 1 
	AND display_name LIKE CONCAT('%', @query, '%')
	OR user_name LIKE CONCAT('%', @query, '%')
END;

CREATE PROCEDURE sp_get_sessions
AS
BEGIN
	SELECT * FROM session_table ORDER BY start_date DESC
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

CREATE PROCEDURE sp_get_sessions_by_search
@query VARCHAR(MAX)
AS
BEGIN
	SELECT * FROM session_table
	WHERE title LIKE CONCAT('%', @query, '%')
	OR code LIKE CONCAT('%', @query, '%')
END;

CREATE PROCEDURE sp_patch_session
@session_code VARCHAR(5),
@session_title VARCHAR(MAX),
@start_date DATE,
@end_date DATE
AS
BEGIN
	UPDATE session_table
	SET title = @session_title,
	start_date = @start_date,
	end_date = @end_date
	WHERE code = @session_code
END;

ALTER PROCEDURE sp_get_departments
AS
BEGIN
	SELECT * FROM departments WHERE active=1
END;

CREATE PROCEDURE sp_post_department
@code VARCHAR(5),
@title VARCHAR(MAX)
AS
BEGIN
	INSERT INTO departments(
	code,
	title)
	VALUES(
	@code,
	@title);
END;

CREATE PROCEDURE sp_patch_department
@department_code VARCHAR(5),
@title VARCHAR(MAX),
@active BIT = true
AS
BEGIN
	UPDATE departments 
	SET title = @title,
	active = @active
	WHERE code = @department_code
END;

CREATE PROCEDURE sp_get_classes_by_department_session
@department_code VARCHAR(5),
@session_code VARCHAR(5)
AS
BEGIN
	SELECT * FROM classes 
	WHERE department_code=@department_code
	AND session_code=@session_code
	AND active=1
END;

CREATE PROCEDURE sp_post_class
@code VARCHAR(5),
@department_code VARCHAR(5),
@session_code VARCHAR(5)
AS
BEGIN
	INSERT INTO classes(
	code,
	department_code,
	session_code)
	VALUES(
	@code,
	@department_code,
	@session_code)
END;

CREATE PROCEDURE sp_get_students
AS
BEGIN
	SELECT * FROM students WHERE active=1 ORDER BY registration_date DESC
END;

CREATE PROCEDURE sp_get_students_by_search
@query_name VARCHAR(25)
AS
BEGIN
	SELECT * FROM students
	WHERE name LIKE CONCAT('%', @query_name, '%') 
END;

CREATE PROCEDURE sp_post_student
@department_code VARCHAR(5),
@session_code VARCHAR(5),
@name VARCHAR(25),
@father_name VARCHAR(25),
@cnic VARCHAR(13),
@mobile_no VARCHAR(12),
@emergency_contact VARCHAR(12),
@dob DATE,
@address VARCHAR(MAX),
@gender VARCHAR(10)
AS
BEGIN
	DECLARE @roll_no INT
	EXEC sp_student_roll_no_new @department_code, @session_code, @roll_no OUTPUT
	
	INSERT INTO students
	(department_code,
	session_code,
	roll_no,
	name,
	father_name,
	cnic,
	mobile_no,
	emergency_contact,
	dob,
	address,
	gender)
	VALUES(
	@department_code,
	@session_code,
	@roll_no,
	@name,
	@father_name,
	@cnic,
	@mobile_no,
	@emergency_contact,
	@dob,
	@address,
	@gender)
END;

CREATE PROCEDURE sp_patch_student
@department_code VARCHAR(5),
@session_code VARCHAR(5),
@roll_no INT,
@name VARCHAR(25),
@father_name NVARCHAR(25),
@cnic VARCHAR(13),
@mobile_no VARCHAR(12),
@emergency_contact VARCHAR(12),
@dob DATE,
@address VARCHAR(MAX),
@gender VARCHAR(10),
@active BIT
AS
BEGIN
	UPDATE students
	SET name = @name,
	father_name = @father_name,
	cnic = @cnic,
	mobile_no = @mobile_no,
	emergency_contact = @emergency_contact,
	dob = @dob,
	address = @address,
	gender = @gender,
	active = @active
	WHERE department_code = @department_code
	AND session_code = @session_code
	AND roll_no = @roll_no
END;

CREATE PROCEDURE sp_student_roll_no_new
@department_code VARCHAR(5),
@session_code VARCHAR(5),
@roll_no INT OUTPUT
AS
BEGIN
	DECLARE @max_roll_no INT
	
	SELECT @max_roll_no=MAX(roll_no)
	FROM students
	WHERE department_code=@department_code
	AND session_code=@session_code
	
	IF (@max_roll_no IS NULL) 
	BEGIN
		SET @roll_no=1
	END ELSE
	BEGIN
		SET @roll_no=@max_roll_no + 1
	END
END;

CREATE PROCEDURE sp_get_student_roll_no_new
@department_code VARCHAR(5),
@session_code VARCHAR(5)
AS
BEGIN
	DECLARE @new_roll_no INT
	
	SELECT @new_roll_no=MAX(roll_no)
	FROM students
	WHERE department_code=@department_code
	AND session_code=@session_code
	
	IF (@new_roll_no IS NULL) 
	BEGIN
		SET @new_roll_no=1
	END ELSE
	BEGIN
		SET @new_roll_no=@new_roll_no + 1
	END
	
	SELECT @new_roll_no AS new_roll_no
END;

CREATE PROCEDURE sp_post_class_register_entry
@department_code VARCHAR(5),
@session_code VARCHAR(5),
@class_code VARCHAR(5),
@student_roll_no INT
AS
BEGIN
	INSERT INTO class_register 
	VALUES(
	@department_code,
	@session_code,
	@class_code,
	@student_roll_no)
END;

CREATE TRIGGER tr_on_insert_student
ON students
FOR INSERT
AS
BEGIN
	PRINT('tr_on_insert_student executed')
	INSERT INTO audit_student VALUES(GETDATE())
END;

CREATE TRIGGER tr_on_update_user
ON users
FOR UPDATE
AS
BEGIN
	PRINT('tr_on_update_user executed')
	INSERT INTO audit_user VALUES(GETDATE())
END;

ALTER VIEW vw_admin
AS
SELECT * FROM users WHERE user_role=(SELECT user_role FROM users WHERE user_name='admin1');

/*
 * [Concepts Covered]
 * Create Table
 * Primary Key
 * Not Null constraint
 * Default constraint
 * Check constaint
 * Alter table
 * Insert row
 * Update row
 * Where with Like clause
 * Order by clause
 * Stored procedures with default parameters
 * Procedure with output parameter 
 * Procedure inside procedure
 * Triggers for inert and update
 * View
 * Subquery
 */
