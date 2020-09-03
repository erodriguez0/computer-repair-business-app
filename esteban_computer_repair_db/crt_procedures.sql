CREATE OR REPLACE FUNCTION getEmployeeName ( IN id Integer )
RETURNS VARCHAR AS
$$
DECLARE 

    empName varchar = null;

BEGIN

    SELECT fname || ' ' || minit || ' ' || lname || ' '
    INTO empName FROM employee  WHERE  emp_id  =  id;
    return empName;
END; 
$$ LANGUAGE plpgsql; 