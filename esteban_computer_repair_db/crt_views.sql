CREATE OR REPLACE VIEW super_emp AS
	SELECT emp_id, username, password, super_id, jobtitle
	FROM employee;

CREATE OR REPLACE VIEW empTickets AS
	SELECT t.ticket_id, t.total_cost, t.status, w.start_date, w.end_date, e.emp_id
	FROM ticket t, work_on w, employee e
	WHERE t.ticket_id = w.ticket_id and w.emp_id = e.emp_id;

CREATE OR REPLACE VIEW csinfotable AS
	SELECT p.problem_id, c.fname, c.lname, c.email, c.username, q.submit_date, p.review_date, p.status
	FROM customer c, equipment q, customer_problem p
	WHERE c.cust_id = q.cust_id and q.comp_id = p.comp_id 
		and (p.status = 'OPEN' or p.status = 'IN PROGRESS')
	ORDER BY p.problem_id ASC;

CREATE OR REPLACE VIEW csinfohistory AS
	SELECT p.problem_id, c.fname, c.lname, c.email, c.username, q.submit_date, p.review_date, p.status
	FROM customer c, equipment q, customer_problem p
	WHERE c.cust_id = q.cust_id and q.comp_id = p.comp_id 
		and (p.status = 'ACCEPTED' or p.status = 'DECLINED')
	ORDER BY p.problem_id ASC;

CREATE OR REPLACE VIEW problemInfo AS
	SELECT p.problem_id, q.comp_id, c.fname, c.lname, c.email, q.submit_date, p.review_date, p.description
	FROM customer c, equipment q, customer_problem p
	WHERE c.cust_id = q.cust_id and q.comp_id = p.comp_id;

CREATE OR REPLACE VIEW empticketcount AS
	SELECT COUNT(ticket_id) AS ticket_count, e.emp_id, e.fname, e.lname, e.jobtitle 
	FROM empTickets t, employee e
	WHERE e.emp_id = t.emp_id and (e.jobtitle = 'Hardware Specialist' or e.jobtitle = 'Software Specialist')
	GROUP BY e.emp_id 
	ORDER BY ticket_count;

CREATE OR REPLACE VIEW work_on_emp_tickets AS
	SELECT e.fname || ' ' || e.lname as name, e.emp_id, e.jobtitle, p.problem_id
	FROM work_on w, ticket t, customer_problem p, employee e
	WHERE t.problem_id = p.problem_id and t.ticket_id = w.ticket_id
		and e.emp_id = w.emp_id;

CREATE OR REPLACE VIEW select_emp_table as
select e.emp_id, (select coalesce(count(w.work_id),0) as ticket_count 
				from work_on w 
				where w.emp_id = e.emp_id),
		e.fname, e.lname, e.jobtitle
from employee e
where e.jobtitle = 'Hardware Specialist' or e.jobtitle = 'Software Specialist';

--CSDept_Report
create or replace view csdept_report as
select c.fname || ' ' || c.lname as customer_name, p.problem_id, p.submit_date, p.review_date,
	p.description, p.status, t.ticket_id, t.type, c.email, t.total_cost,
	e.fname || ' ' || e.lname as ename, q.comp_id, c.cust_id, e.emp_id
from employee e, customer_problem p, ticket t, customer c, equipment q
where e.emp_id = p.emp_id and p.problem_id = t.problem_id and c.cust_id = q.cust_id
	and q.comp_id = p.comp_id
order by p.problem_id;

--CSDeptReport_subreport3
create or replace view subreport_2 as
select t.ticket_id, e.emp_id, e.fname || ' ' || e.lname as emp_name, 
	e.jobtitle, t.problem_id
from employee e, work_on w, ticket t 
where e.emp_id = w.emp_id and w.ticket_id = t.ticket_id;

create or replace view subreport_4 as
select t.problem_id, t.ticket_id, p.part_id, m.mnfr_name, pi.model_name,
	p.serial_num, p.part_price
from ticket t, part p, part_info pi, manufacturer m
where t.ticket_id = p.ticket_id and p.model_id = pi.model_id
	and pi.mnfr_id = m.mnfr_id;

-- --CSDept_stats_subreport3
-- select p.emp_id, '$'||sum(t.total_cost) as sum, 
-- 	'$'||round(avg(t.total_cost)::DECIMAL, 2) as avg, 
-- 	sum(parts_used.part_count) as part_count
-- from customer_problem p, ticket t,
-- 	(select count(*) as part_count, r.ticket_id 
-- 	from part r 
-- 	group by ticket_id) as parts_used
-- where t.problem_id = p.problem_id and parts_used.ticket_id = t.ticket_id
-- 	and p.emp_id = $P{emp_id} and p.review_date >= $P{start_date}
-- 	and p.review_date <= $P{end_date}
-- group by p.emp_id;

-- --CSDept_stats_subreport1
-- select e.fname, e.lname, count(t.ticket_id), t.type, sum(w2.w_count) as emp_count
-- from employee e, customer_problem p, ticket t
-- join
-- 	(select count(*) as w_count, w1.ticket_id from work_on w1 group by w1.ticket_id) as w2
-- on w2.ticket_id = t.ticket_id
-- where e.emp_id = p.emp_id and p.problem_id = t.problem_id
-- 	and e.emp_id = $P{emp} and p.review_date >= $P{start_date}
-- 	and p.review_date <= $P{end_date}
-- group by t.type, e.fname, e.lname 
-- order by fname;

-- --CSDept_stats_subreport2
-- select distinct e.fname || ' ' || e.lname as emp_name, count(p.problem_id), p.status
-- from employee e, customer_problem p
-- where e.emp_id = p.emp_id and e.emp_id = $P{emp_id} 
-- 	and p.review_date >= $P{start_date} and p.review_date <= $P{end_date}
-- group by e.fname, e.lname, p.status
-- order by status desc;