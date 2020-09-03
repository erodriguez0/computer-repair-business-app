-- 4. List customer's who have had computer repairs where a GPU was used and cost more than $500.00
SELECT c.*
FROM customer c
WHERE EXISTS (
	SELECT 1 
	FROM equipment q, customer_problem p, ticket t, part s, part_info i
	WHERE c.cust_id = q.cust_id and q.comp_id = p.comp_id 
		and p.problem_id = t.problem_id and t.ticket_id = s.ticket_id 
		and s.part_price > 500.00 and s.model_id = i.model_id and i.part_type = 'GPU'
);