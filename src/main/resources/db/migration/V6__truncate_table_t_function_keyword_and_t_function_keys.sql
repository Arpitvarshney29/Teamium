--To truncate the t_function_keyword and t_function_keys tables.
ALTER TABLE public.t_function DROP CONSTRAINT fk_function_c_idfunctionkeyword;
TRUNCATE TABLE public.t_function_keyword CASCADE;
ALTER TABLE public.t_function_keyword ADD CONSTRAINT t_function_keyword_c_keyword UNIQUE (c_keyword);
ALTER TABLE public.t_function ADD CONSTRAINT fk_function_c_idfunctionkeyword FOREIGN KEY (c_idfunctionkeyword)
      REFERENCES public.t_function_keyword (c_idfunctionkeyword) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;