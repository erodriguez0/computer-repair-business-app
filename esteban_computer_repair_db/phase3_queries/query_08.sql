-- 8. List customers who have submitted equipment more than once that were located at different addresses than the customer
SELECT distinct c.*
FROM customer c, equipment q1, equipment q2
WHERE c.cust_id = q1.cust_id and c.cust_id = q2.cust_id and c.address != q1.address 
	and c.address != q2.address and q1.comp_id != q2.comp_id
ORDER BY c.cust_id ASC;