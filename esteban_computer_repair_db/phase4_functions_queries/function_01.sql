CREATE OR REPLACE FUNCTION crt_ticket (p_id INTEGER, t_type ticket_type, details VARCHAR(500))
	RETURNS void AS
$BODY$
BEGIN INSERT INTO ticket(problem_id, type, description)
	VALUES (p_id, t_type, details);
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;