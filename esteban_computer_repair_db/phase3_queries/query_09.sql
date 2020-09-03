-- 7. List the employee who used at least 2 different part types on one ticket whose prices > $500 and are different in price.
SELECT distinct e.*
FROM ticket t, work_on w, employee e, part p1, part p2
WHERE t.ticket_id = w.ticket_id and w.emp_id = e.emp_id and p1.ticket_id = t.ticket_id
      and p1.part_price >= 100.00 and p2.ticket_id = t.ticket_id and p2.part_price >= 100.00 
      and p1.part_id != p2.part_id;