-- 5. List customers who have submitted equipment that was worked on by Wallas Merrgan
SELECT cu.*
FROM customer cu, equipment eq, customer_problem cp, ticket t, work_on w, employee e
WHERE cu.cust_id = eq.cust_id and eq.comp_id = cp.comp_id and cp.problem_id = t.problem_id
      and t.ticket_id = w.ticket_id and w.emp_id = e.emp_id and e.fname = 'Wallas' 
      and e.lname = 'Merrgan';