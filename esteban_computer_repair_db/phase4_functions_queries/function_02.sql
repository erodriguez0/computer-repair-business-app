CREATE OR REPLACE FUNCTION delete_part (part INTEGER)
	RETURNS void AS
$BODY$
BEGIN
	DELETE FROM part WHERE part_id = $1;
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;