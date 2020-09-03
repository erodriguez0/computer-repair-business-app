-- 11. List customer who have the second most expensive repair (total_cost in ticket)
SELECT c.*
FROM customer c, equipment q, customer_problem p, ticket t
WHERE c.cust_id = q.cust_id
	and q.comp_id = p.comp_id and p.problem_id = t.problem_id
	and t.total_cost = (
 		SELECT MAX(total_cost)
		FROM ticket
		WHERE total_cost < (
			SELECT MAX(total_cost)
			FROM ticket)
 );