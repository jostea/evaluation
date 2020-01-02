CREATE TABLE tests.department
(
    id   SERIAL PRIMARY KEY,
    name varchar(255) NOT NULL,
    UNIQUE (name)
);

CREATE TABLE tests.employee
(
    id            SERIAL PRIMARY KEY,
    first_name    varchar(255) NOT NULL,
    last_name     varchar(255) NOT NULL,
    phone         varchar(255) NOT NULL,
    department_id int          NOT NULL REFERENCES tests.department (id),
    salary        int          NOT NULL,
    date_hired    date         NOT NULL,
    date_fired    date         NULL,
    UNIQUE (first_name, last_name)
);

INSERT INTO department(name) VALUES ('Accounting');
INSERT INTO department(name) VALUES ('Commercial');
INSERT INTO department(name) VALUES ('Marketing');

INSERT INTO employee(first_name, last_name, phone, date_hired, salary, department_id)
VALUES ('Anton', 'Ivanov', '636363', '2019-12-23', 500, (SELECT d.id FROM department d WHERE d.name = 'Accounting'));
INSERT INTO employee(first_name, last_name, phone, date_hired, salary, department_id)
VALUES ('Petru', 'Antonov', '111111', '2018-02-23', 300, (SELECT d.id FROM department d WHERE d.name = 'Commercial'));
INSERT INTO employee(first_name, last_name, phone, date_hired, salary, department_id)
VALUES ('Jenea', 'Popescu', '122211', '2019-07-15', 350, (SELECT d.id FROM department d WHERE d.name = 'Marketing'));
INSERT INTO employee(first_name, last_name, phone, date_hired, salary, department_id)
VALUES ('Daniel', 'Sidorov', '9969696', '2019-03-08', 750, (SELECT d.id FROM department d WHERE d.name = 'Accounting'));
INSERT INTO employee(first_name, last_name, phone, date_hired, salary, department_id)
VALUES ('Stefan', 'Obama', '1323232', '2019-07-07', 660, (SELECT d.id FROM department d WHERE d.name = 'Commercial'));
INSERT INTO employee(first_name, last_name, phone, date_hired, salary, department_id)
VALUES ('Petro', 'Porosenso', '777777', '2019-01-11', 1000, (SELECT d.id FROM department d WHERE d.name = 'Marketing'));
INSERT INTO employee(first_name, last_name, phone, date_hired, salary, department_id)
VALUES ('Igor', 'Dodon', '69696969', '2019-02-28', 550, (SELECT d.id FROM department d WHERE d.name = 'Accounting'));
INSERT INTO employee(first_name, last_name, phone, date_hired, salary, department_id)
VALUES ('Vladimir', 'Putin', '1323232', '2010-01-01', 1000, (SELECT d.id FROM department d WHERE d.name = 'Commercial'));
INSERT INTO employee(first_name, last_name, phone, date_hired, salary, department_id)
VALUES ('Traian', 'Basescu', '777777', '2013-09-19', 800, (SELECT d.id FROM department d WHERE d.name = 'Marketing'));