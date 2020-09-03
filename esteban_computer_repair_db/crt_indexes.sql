DROP   INDEX  IF  EXISTS     idx_customer_lname;
CREATE INDEX  IF NOT EXISTS  idx_customer_lname
ON customer(lname);

DROP   INDEX  IF  EXISTS     idx_customer_fname;
CREATE INDEX  IF NOT EXISTS  idx_customer_fname
ON customer(fname);

DROP   INDEX  IF  EXISTS     idx_customer_username;
CREATE INDEX  IF NOT EXISTS  idx_customer_username
ON customer(username);

DROP   INDEX  IF  EXISTS     idx_employee_lname;
CREATE INDEX  IF NOT EXISTS  idx_employee_lname
ON employee(lname);

DROP   INDEX  IF  EXISTS     idx_employee_fname;
CREATE INDEX  IF NOT EXISTS  idx_employee_fname
ON employee(fname);

DROP   INDEX  IF  EXISTS     idx_employee_ssn;
CREATE INDEX  IF NOT EXISTS  idx_employee_ssn
ON employee(ssn);

DROP   INDEX  IF  EXISTS     idx_part_serial;
CREATE INDEX  IF NOT EXISTS  idx_part_serial
ON part(serial_num);