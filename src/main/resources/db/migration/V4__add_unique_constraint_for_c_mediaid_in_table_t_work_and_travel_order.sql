-- Alter table t_work_and_travel_order to add unique constraint for c_mediaid. 
ALTER TABLE public.t_work_and_travel_order
    ADD CONSTRAINT t_work_and_travel_order_c_mediaid UNIQUE (c_mediaid);