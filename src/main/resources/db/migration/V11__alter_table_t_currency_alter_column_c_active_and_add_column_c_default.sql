-- alter alter_table_t_currency_alter_column_c_active_and_add_column_c_default

ALTER table public.t_currency ALTER COLUMN c_active SET DEFAULT false,
	ADD COLUMN c_default_currency boolean DEFAULT false;
