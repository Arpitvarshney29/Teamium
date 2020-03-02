-- create table public.t_timezone

CREATE TABLE IF NOT EXISTS public.t_timezone
(
  c_idtimezone bigint NOT NULL,
  c_offset character varying(255) NOT NULL,
  c_zone_name character varying(255) NOT NULL UNIQUE,
  c_ischeck boolean,
  CONSTRAINT t_timezone_pkey PRIMARY KEY (c_idtimezone)
);

-- alter table public.t_person add column c_signature_path and add column c_ui_language

ALTER TABLE public.t_person ADD COLUMN c_signature_path character varying(255),
	ADD COLUMN c_ui_language character varying(50);
