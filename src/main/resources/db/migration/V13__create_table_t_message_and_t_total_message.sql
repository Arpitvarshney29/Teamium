-- Creating tables for chatting app(Node server).
CREATE TABLE public.t_message
(
  identifier character varying,
  message_number bigint,
  message character varying,
  message_from bigint,
  message_to bigint,
  message_send_time timestamp with time zone,
  seen boolean,
  active boolean,
  CONSTRAINT fk_person_message_from FOREIGN KEY (message_from)
      REFERENCES public.t_person (c_idperson) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_person_message_to FOREIGN KEY (message_to)
      REFERENCES public.t_person (c_idperson) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE public.t_total_message
(
  identifier character varying,
  total_message bigint,
  CONSTRAINT constraint_name UNIQUE (identifier)
);