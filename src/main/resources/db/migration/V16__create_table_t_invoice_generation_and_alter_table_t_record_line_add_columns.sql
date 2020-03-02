-- create table public.t_invoice_generation

CREATE TABLE IF NOT EXISTS public.t_invoice_generation
(
  c_idinvoice_generation bigint NOT NULL,
  c_invoice_percent double precision,
  c_invoice_type character varying(255),
  c_invoice_amount double precision,
  c_user_from timestamp without time zone,
  c_user_to timestamp without time zone,
  c_idinvoice bigint,
  c_invoice_date timestamp without time zone,
  c_invoice_pdf_path character varying(255),
  CONSTRAINT t_invoice_generation_pkey PRIMARY KEY (c_idinvoice_generation),
  CONSTRAINT fk_record_c_idinvoice FOREIGN KEY (c_idinvoice)
      REFERENCES public.t_record (c_idrecord) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- alter table public.t_record_line add column c_signature_path and add column c_ui_language

ALTER TABLE public.t_record_line ADD COLUMN c_invoice_days bigint,
	ADD COLUMN c_total_paid_amount real;
