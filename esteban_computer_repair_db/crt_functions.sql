CREATE OR REPLACE FUNCTION crt_ticket (p_id INTEGER, t_type VARCHAR(19), details VARCHAR(500))
	RETURNS void AS
$BODY$
BEGIN 
	INSERT INTO ticket(problem_id, type, description)
	VALUES (p_id, t_type::ticket_type, details);

	UPDATE customer_problem
	SET status = 'ACCEPTED'
	WHERE problem_id = p_id;
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;

-----------------

CREATE OR REPLACE FUNCTION crt_work (e_id INTEGER, t_id INTEGER)
	RETURNS void AS
$BODY$
BEGIN 
IF NOT EXISTS (SELECT 1 
	FROM work_on w, ticket t 
	WHERE w.emp_id = e_id
		and w.ticket_id = t_id)
THEN
	INSERT INTO work_on(emp_id, ticket_id)
	VALUES (e_id, t_id);
END IF;
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION edit_work (e_id INTEGER, p_id INTEGER)
	RETURNS void AS
$BODY$
DECLARE
	ticket INTEGER = (SELECT ticket_id FROM ticket WHERE problem_id = p_id);
BEGIN
IF NOT EXISTS (SELECT 1
		FROM work_on w, ticket t, customer_problem p
		WHERE w.ticket_id = t.ticket_id
			and t.problem_id = p.problem_id
			and p.problem_id = p_id and w.emp_id = e_id)
THEN
	INSERT INTO work_on(emp_id, ticket_id)
	VALUES (e_id, ticket);
END IF;
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;

------------------

CREATE OR REPLACE FUNCTION problem_update (p_id INTEGER, e_id INTEGER)
	RETURNS void AS
$BODY$
BEGIN
	UPDATE customer_problem
	SET review_date = current_date, 
		status = 'IN PROGRESS',
		emp_id = e_id
	WHERE problem_id = p_id;
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;

------------------

CREATE OR REPLACE FUNCTION complete_problem (p_id INTEGER)
	RETURNS void AS
$BODY$
BEGIN
	UPDATE customer_problem
	SET review_date = current_date, 
		status = 'ACCEPTED'
	WHERE problem_id = p_id;
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;

------------------

CREATE OR REPLACE FUNCTION decline_problem (p_id INTEGER, e_id INTEGER)
	RETURNS void AS
$BODY$
BEGIN
	UPDATE customer_problem
	SET status = 'DECLINED',
		review_date = current_date,
		emp_id = e_id
	WHERE problem_id = p_id;
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;

------------------

CREATE OR REPLACE FUNCTION revert_problem (p_id INTEGER)
	RETURNS void AS
$BODY$
BEGIN
	UPDATE customer_problem
	SET status = 'IN PROGRESS',
		review_date = null
	WHERE problem_id = p_id;
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;

------------------

CREATE OR REPLACE FUNCTION revert_original_problem (p_id INTEGER)
	RETURNS void AS
$BODY$
BEGIN
	UPDATE customer_problem
	SET status = 'OPEN',
		review_date = null,
		emp_id = null
	WHERE problem_id = p_id;
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;

------------------

CREATE OR REPLACE FUNCTION edit_created_tickets(p_id INTEGER, t_type VARCHAR(19), _desc VARCHAR(500))
	RETURNS void AS
$BODY$
BEGIN
	UPDATE ticket 
	SET description = _desc,
		type = t_type::ticket_type
	WHERE
		problem_id = p_id;
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;

-----------------

CREATE OR REPLACE FUNCTION del_work_on(_emp INTEGER, _pid INTEGER)
	RETURNS void AS
$BODY$
BEGIN
	IF EXISTS (SELECT 1 FROM work_on_emp_tickets where problem_id = _pid)
	THEN
		DELETE FROM work_on
		WHERE emp_id = _emp and
			  ticket_id = (SELECT distinct w.ticket_id 
			  	FROM work_on w, ticket t
			  	WHERE w.ticket_id = t.ticket_id
			  		and t.problem_id = _pid);
    END IF;
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION new_customer(_fname VARCHAR(50), _minit VARCHAR(1),
	_lname VARCHAR(50), _address VARCHAR(50), _city VARCHAR(50),
	_state VARCHAR(2), _zip VARCHAR(5), _phone VARCHAR(12), _email VARCHAR(50))
	RETURNS void AS
$BODY$
BEGIN
	INSERT INTO customer(fname, minit, lname, address, city, state, zip, phone, email)
	VALUES (_fname, _minit, _lname, _address, _city, _state, _zip, _phone, _email);
END
$BODY$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION new_equipment(_cust_id INTEGER, _address VARCHAR(50),
	_city VARCHAR(50), _state VARCHAR(2), _zip VARCHAR(5))
	RETURNS void AS
$BODY$
BEGIN
	INSERT INTO equipment(cust_id, address, city, state, zip, submit_date)
	VALUES (_cust_id, _address, _city, _state, _zip, current_date);
END
$BODY$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION new_problem(_comp_id INTEGER, _problem VARCHAR(500))
	RETURNS void AS
$BODY$
BEGIN
	INSERT INTO customer_problem(comp_id, description, submit_date)
	VALUES (_comp_id, _problem, current_date);
END
$BODY$ LANGUAGE plpgsql;

------------------
--NOT WORKING AS EXPECTED

-- CREATE OR REPLACE FUNCTION status_check()
-- 	RETURNS TRIGGER AS
-- $BODY$
-- BEGIN
-- 	IF (SELECT status FROM customer_problem WHERE problem_id = NEW.problem_id)
-- 		!= 'IN PROGRESS' 
-- 	OR (SELECT status FROM customer_problem WHERE problem_id = NEW.problem_id)
-- 		!= 'ACCEPTED' 
-- 	THEN RAISE EXCEPTION 'Problem status not IN PROGRESS or ACCEPTED';
-- 	END IF;
-- 	RETURN NEW;
-- END
-- $BODY$ LANGUAGE plpgsql;

-- CREATE TRIGGER check_status_bef
-- BEFORE INSERT ON ticket 
-- FOR EACH ROW
-- EXECUTE PROCEDURE status_check();

------------------

CREATE OR REPLACE FUNCTION del_ticket_check()
	RETURNS TRIGGER AS
$BODY$
BEGIN
	DELETE FROM part WHERE ticket_id = OLD.ticket_id;
	DELETE FROM work_on WHERE ticket_id = OLD.ticket_id;
	RETURN OLD;
END
$BODY$ LANGUAGE plpgsql;

CREATE TRIGGER cascade_del_ticket
BEFORE DELETE ON ticket
FOR EACH ROW
EXECUTE PROCEDURE del_ticket_check();

CREATE OR REPLACE FUNCTION update_tc()
	RETURNS TRIGGER AS
$BODY$
BEGIN
	UPDATE ticket 
	SET total_cost = (SELECT SUM(part_price)
			FROM part p
			WHERE p.ticket_id = NEW.ticket_id)
	WHERE ticket_id = NEW.ticket_id;
	RETURN NEW;
END
$BODY$ LANGUAGE plpgsql;

CREATE TRIGGER insert_part_tc
AFTER INSERT OR UPDATE OR DELETE ON part
FOR EACH ROW
EXECUTE PROCEDURE update_tc();

-- super_emp
-- csinfotable
-- problem_info
-- select_emp_table
-- csinfohistory
-- work_on_emp_tickets

-- revert_original_problem
-- decline_problem
-- problem_update
-- crt_ticket
-- crt_work
-- revert_problem
-- complete_problem
-- edit_created_tickets
-- edit_work
-- del_work_on