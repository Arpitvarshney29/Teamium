--To alter table t_equipment add new coulmn c_available--

  ALTER table public.t_equipment ADD COLUMN c_available boolean DEFAULT true NOT NULL;
  
  ALTER table public.t_person ADD COLUMN c_available boolean DEFAULT true NOT NULL;
  
  