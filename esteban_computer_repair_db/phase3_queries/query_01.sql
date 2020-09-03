-- Lit all employees who worked at least one ticket between 3/5/17 and 3/7/17
SELECT distinct e.*
FROM employee e, work_on w, ticket t
WHERE e.emp_id = w.emp_id and w.start_date >= '2015-03-14' and w.start_date <= '2015-03-17'
ORDER BY emp_id ASC;