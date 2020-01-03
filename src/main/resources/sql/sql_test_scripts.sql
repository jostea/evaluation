CREATE TABLE tests.disciplines
(
    id   SERIAL PRIMARY KEY,
    name varchar(255) NOT NULL,
    UNIQUE (name)
);

CREATE TABLE tests.persons
(
    id            SERIAL PRIMARY KEY,
    role          varchar(255) NOT NULL,
    pers_name     varchar(255) NOT NULL,
    discipline_id int          NOT NULL REFERENCES tests.disciplines (id),
    UNIQUE (pers_name)
);

INSERT INTO tests.disciplines (name)
VALUES ('AM');
INSERT INTO tests.disciplines (name)
VALUES ('Testing');
INSERT INTO tests.disciplines (name)
VALUES ('Development');
INSERT INTO tests.persons (role, pers_name, discipline_id)
VALUES ('intern', 'John', (SELECT d.id FROM tests.disciplines d WHERE d.name = 'AM'));
INSERT INTO tests.persons (role, pers_name, discipline_id)
VALUES ('intern', 'Mary', (SELECT d.id FROM tests.disciplines d WHERE d.name = 'Testing'));
INSERT INTO tests.persons (role, pers_name, discipline_id)
VALUES ('engineer', 'Sienna', (SELECT d.id FROM tests.disciplines d WHERE d.name = 'AM'));

CREATE TABLE tests.employee_info
(
    id         SERIAL PRIMARY KEY,
    l_name     varchar(20) NULL,
    f_name     varchar(30) NULL,
    department varchar(30) NULL,
    project    varchar(20) NULL,
    adress     varchar(20) NULL,
    birthday   varchar(20) NULL,
    gender     varchar(10) NULL
);

CREATE TABLE tests.employee_position
(
    empid           int         NOT NULL REFERENCES tests.employee_info (id) NOT NULL,
    position        varchar(30) NULL,
    date_of_joining varchar(20) NULL,
    salary          int         NULL
);

insert into tests.employee_info(l_name, f_name, department, project, adress, birthday, gender)
values ('WILSON', 'John', 'HR', 'P1', 'NY', '1/12/1976', 'M');
insert into tests.employee_info(l_name, f_name, department, project, adress, birthday, gender)
values ('ANDERSON', 'Julia', 'Admin', 'P2', 'LA', '2/5/1968', 'F');
insert into tests.employee_info(l_name, f_name, department, project, adress, birthday, gender)
values ('Rohan', 'Chris', 'Account', 'P3', 'CA', '1/1/1980', 'M');
insert into tests.employee_info(l_name, f_name, department, project, adress, birthday, gender)
values ('JACKSON', 'Anna', 'HR', 'P1', 'WA', '2/5/1992', 'F');
insert into tests.employee_info(l_name, f_name, department, project, adress, birthday, gender)
values ('THOMAS', 'Robert', 'Admin', 'P2', 'NY', '3/7/1994', 'M');
insert into tests.employee_position
values (1, 'Manager', '1/5/2019', 5000);
insert into tests.employee_position
values (2, 'Executive', '2/5/2019', 7500);
insert into tests.employee_position
values (3, 'Manager', '1/5/2019', 9000);
insert into tests.employee_position
values (2, 'Lead', '2/5/2019', 8500);
insert into tests.employee_position
values (1, 'Executive', '1/5/2019', 5500);