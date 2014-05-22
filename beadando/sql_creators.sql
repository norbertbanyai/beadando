create table worksessions
(
  worksession_id number(10,0),
  employee_id number(6,0),
  session_date date,
  duration number(2,0),
  type varchar2(50),
  constraint ws_pk primary key(worksession_id),
  constraint ws_fk foreign key(employee_id) references employees(employee_id)
);

create sequence worksession_sequence
minvalue 1
maxvalue 99999999999999999999999999
start with 1
increment by 1
cache 20;

create table employees as
select employee_id, first_name || ' ' || last_name name, hire_date, salary, d.department_name
from hr.employees e inner join hr.departments d on(e.department_id = d.department_id);