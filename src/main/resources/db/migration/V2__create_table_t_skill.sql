CREATE TABLE public.t_skill
(
  c_idskill bigint NOT NULL,
  c_version integer,
  c_name character varying(255) NOT NULL UNIQUE,
  CONSTRAINT t_skill_pkey PRIMARY KEY (c_idskill)
);

TRUNCATE table public.t_staff_skill;

ALTER TABLE public.t_staff_skill RENAME COLUMN c_idskill TO c_idstaffskill;

ALTER TABLE public.t_staff_skill ADD COLUMN c_idskill bigint;

ALTER TABLE public.t_staff_skill DROP COLUMN c_name;

ALTER TABLE public.t_staff_skill ADD CONSTRAINT fk_c_idskill FOREIGN KEY (c_idskill) REFERENCES public.t_skill (c_idskill); 