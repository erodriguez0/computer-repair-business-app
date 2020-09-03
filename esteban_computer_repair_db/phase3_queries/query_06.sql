-- 6. List all employees who are older than 30 years old, are hardware specialists, and have worked on computers from customers named John Doe
SELECT e.*
FROM employee e
WHERE e.bdate < '1988-03-08' and e.jobtitle = 'Hardware Specialist' 
	and EXISTS (
		SELECT 1 
		FROM customer c, equipment q, customer_problem p, ticket t, work_on w
		WHERE e.emp_id = w.emp_id and t.ticket_id = w.ticket_id
			and c.cust_id = q.cust_id and q.comp_id = p.comp_id 
			and p.problem_id = t.problem_id and c.fname = 'John' and c.lname = 'Doe'
);