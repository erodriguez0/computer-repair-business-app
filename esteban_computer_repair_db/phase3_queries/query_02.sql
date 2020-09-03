-- 2. List all computers located in Bakersfield worked on after 3-5-15
SELECT distinct q.*
FROM equipment q, customer_problem p, ticket t, work_on w
WHERE q.comp_id = p.comp_id and p.problem_id = t.problem_id
	and w.ticket_id = t.ticket_id and q.city = 'Bakersfield'
	and w.start_date > '2015-03-08';