--To alter table t_record add new coulmn c_modified_by and c_updated_on--

  ALTER table public.t_record ADD COLUMN c_modified_by bigint;
  
  ALTER table public.t_record ADD COLUMN c_updated_on timestamp without time zone;