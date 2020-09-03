-- 7. List all tickets with status complete and were worked on after 3/9/15 by Claudine Cant.
SELECT t.*
FROM ticket t, work_on w, employee e
WHERE t.ticket_id = w.ticket_id and w.emp_id = e.emp_id and t.status = 'COMPLETED' 
      and w.start_date >= '2015-03-09'and e.fname = 'Claudine' and e.lname = 'Cant';