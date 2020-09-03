-- 10. List all employees who haven’t examined a computer owned by John Doe between the dates 3/8/15 and 3/8/15
SELECT e1.*
FROM employee e1
EXCEPT
SELECT e2.*
FROM employee e2, customer c2, customer_links l2, work_on w2
WHERE c2.fname = 'John' and c2.lname = 'Doe' and e2.emp_id = w2.emp_id
	and c2.cust_id = l2.cust_id and l2.ticket_id = w2.ticket_id
	and w2.start_date >= '2015-03-08' and w2.end_date < '2018-03-10'
ORDER BY emp_id ASC;