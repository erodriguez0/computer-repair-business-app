-- 3. List all customers that have submitted their computers at least 2 times in 2015.
SELECT distinct c.*
FROM customer c, equipment e1, equipment e2
WHERE c.cust_id = e1.cust_id and e1.submit_date >= '2015-01-01' and 
      e2.submit_date >= '2015-01-01' and e1.submit_date <= '2015-12-31' and 
      e2.submit_date <= '2015-12-31' and c.cust_id = e1.cust_id and c.cust_id = e2.cust_id and e1.comp_id != e2.comp_id
ORDER BY cust_id ASC;