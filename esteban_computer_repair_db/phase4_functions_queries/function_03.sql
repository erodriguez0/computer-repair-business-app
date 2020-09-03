CREATE OR REPLACE FUNCTION avg_cost(column_name VARCHAR(40)) 
RETURNS decimal AS 
$BODY$
DECLARE
	average decimal(10,2);
BEGIN
    EXECUTE format('SELECT avg(%I)
    		FROM ticket',column_name) into average;
    RETURN average;
EXCEPTION
	WHEN others THEN
	RAISE 'Failed due to [%]', SQLERRM;
END
$BODY$ LANGUAGE plpgsql;