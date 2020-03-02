-- Creating this table to store all the signature history.

CREATE TABLE IF NOT EXISTS public.t_signature_history
(
  c_idsignature_history bigint NOT NULL,
  c_version integer,
  c_approved boolean DEFAULT NULL,
  c_signature_date timestamp without time zone DEFAULT NULL,
  c_signed boolean DEFAULT false NOT NULL,
  c_edition_template_type_id bigint NOT NULL,
  c_record_id bigint NOT NULL,
  c_idrecipient bigint NOT NULL,
  c_comment character varying(255),
  c_routing_order integer NOT NULL,
  CONSTRAINT t_signature_history_pkey PRIMARY KEY (c_idsignature_history),
  CONSTRAINT fk_c_edition_template_type_id FOREIGN KEY (c_edition_template_type_id)
      REFERENCES public.t_edition_tempalte_type (c_edition_tempalte_type_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_c_idrecipient FOREIGN KEY (c_idrecipient)
      REFERENCES public.t_person (c_idperson) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkc_c_record_id FOREIGN KEY (c_record_id)
      REFERENCES public.t_record (c_idrecord) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)