CREATE TABLE PERSON
(
	UNIQUE_ID VARCHAR(200) PRIMARY KEY,
	NAME VARCHAR(200) NOT NULL
);

CREATE TABLE PERSON_IDENTIFICATION
(
	UNIQUE_ID VARCHAR(200),
	ID VARCHAR(200) NOT NULL,
	ID_TYPE VARCHAR(200) NOT NULL,
	NAME VARCHAR(200) NOT NULL,
	DOB VARCHAR(200) NOT NULL,
	--AADHAAR_GENDER VARCHAR(1) NOT NULL
	PRIMARY KEY (UNIQUE_ID, ID, ID_TYPE)
);

CREATE TABLE PERSON_VALIDATION_SCORE
(
	UNIQUE_ID VARCHAR(200) PRIMARY KEY,
	ID VARCHAR(200) NOT NULL,
	ID_TYPE VARCHAR(200) NOT NULL,
	
);