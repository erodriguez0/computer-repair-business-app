CREATE OR REPLACE FUNCTION del_ticket_check()
	RETURNS TRIGGER AS
$BODY$
BEGIN
	IF EXISTS (SELECT 1 
		FROM part 
		WHERE ticket_id = OLD.ticket_id)
	THEN DELETE FROM part WHERE ticket_id = OLD.ticket_id;
		END IF;
	IF EXISTS (SELECT 1
		FROM work_on
		WHERE ticket_id = OLD.ticket_id)
	THEN DELETE FROM work_on WHERE ticket_id = OLD.ticket_id;
	END IF;
	RETURN OLD;
END
$BODY$ LANGUAGE plpgsql;

CREATE TRIGGER cascade_del_ticket
BEFORE DELETE ON ticket
FOR EACH ROW
EXECUTE PROCEDURE del_ticket_check();

-- Two most frequent causes of black screen can be either the monitor, motherboard, and/or graphics card. If graphics card is good after tested, then motherboard may be the cause and need a new one. The motherboard could have damaged other components, be sure to check them.