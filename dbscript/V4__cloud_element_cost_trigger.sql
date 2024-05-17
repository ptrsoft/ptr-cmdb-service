-- drop function cloud_element_add;
CREATE OR REPLACE FUNCTION cloud_element_add()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO cloud_element_cost (cloud_element_id, status, created_on, created_by)
    VALUES (NEW.id, NEW.status, current_timestamp, 'System');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--DROP TRIGGER cloud_element_add_trigger ON public.cloud_element;
CREATE  TRIGGER cloud_element_add_trigger
AFTER insert  ON cloud_element
FOR EACH ROW
EXECUTE FUNCTION cloud_element_add();

-- drop function cloud_element_delete;
CREATE OR REPLACE FUNCTION cloud_element_delete()
RETURNS TRIGGER AS $$
BEGIN
    delete from cloud_element_cost where cloud_element_id = OLD.id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

--DROP TRIGGER cloud_element_delete_trigger ON public.cloud_element;
CREATE  TRIGGER cloud_element_delete_trigger
BEFORE DELETE  ON cloud_element
FOR EACH ROW
EXECUTE FUNCTION cloud_element_delete();

-- drop function cloud_element_update;
CREATE OR REPLACE FUNCTION cloud_element_update()
RETURNS TRIGGER AS $$
begin
		update cloud_element
			set is_tagged =
				case
					when
						hosted_services -> 'HOSTEDSERVICES' is not null and jsonb_array_length(hosted_services -> 'HOSTEDSERVICES') > 0
					then true
					else false
				end
			where id = OLD.id ;
		RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--DROP TRIGGER cloud_element_update_trigger ON public.cloud_element;
CREATE  TRIGGER cloud_element_update_trigger
AFTER UPDATE of hosted_services ON cloud_element
FOR EACH ROW
EXECUTE FUNCTION cloud_element_update();
