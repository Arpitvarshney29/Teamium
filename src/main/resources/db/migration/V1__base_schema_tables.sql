--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.12
-- Dumped by pg_dump version 9.5.12

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: t_action; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_action (
    c_idaction bigint NOT NULL,
    c_actiondate date,
    c_subject character varying,
    c_summary character varying,
    c_idtargetperson bigint,
    c_idfollower bigint,
    c_actiontype character varying,
    c_priority integer,
    c_status character varying,
    c_visibility character varying,
    c_version integer
);


ALTER TABLE public.t_action OWNER TO postgres;

--
-- Name: action_idaction_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.action_idaction_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.action_idaction_seq OWNER TO postgres;

--
-- Name: action_idaction_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.action_idaction_seq OWNED BY public.t_action.c_idaction;


--
-- Name: t_address; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_address (
    c_idaddress bigint NOT NULL,
    c_line1 character varying,
    c_line2 character varying,
    c_zipcode character varying,
    c_city character varying,
    c_state character varying,
    c_country character varying(3),
    c_version integer
);


ALTER TABLE public.t_address OWNER TO postgres;

--
-- Name: address_idaddress_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.address_idaddress_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.address_idaddress_seq OWNER TO postgres;

--
-- Name: address_idaddress_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.address_idaddress_seq OWNED BY public.t_address.c_idaddress;


--
-- Name: t_company; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_company (
    c_idcompany bigint NOT NULL,
    c_name character varying,
    c_idmaincontact bigint,
    c_idaddress bigint,
    c_idbillingaddress bigint,
    c_registrationnumber character varying,
    c_vatnumber character varying,
    c_comments character varying,
    c_discriminator character varying NOT NULL,
    c_customer_type character varying,
    c_version integer,
    c_idlogo bigint,
    c_accountnumber character varying,
    c_website character varying,
    c_bank_iban character varying,
    c_bank_swift character varying,
    c_rating_type character varying,
    c_idfollower bigint,
    c_entity_editiongroup character varying,
    c_rating_discount numeric,
    c_currency character varying,
    c_language character varying,
    c_ics character varying,
    c_entity_validation_threshold numeric,
    c_archived boolean,
    c_domain character varying,
    c_header character varying,
    c_footer character varying
);


ALTER TABLE public.t_company OWNER TO postgres;

--
-- Name: company_idcompany_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.company_idcompany_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.company_idcompany_seq OWNER TO postgres;

--
-- Name: company_idcompany_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.company_idcompany_seq OWNED BY public.t_company.c_idcompany;


--
-- Name: t_contract; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_contract (
    c_idcontract bigint NOT NULL,
    c_idsaleentity bigint,
    c_idstaffmember bigint,
    c_idfunction bigint,
    c_unit character varying,
    c_rate numeric,
    c_hiredate date,
    c_enddate date,
    c_convention character varying,
    c_contract_type character varying,
    c_printeddate date,
    c_signdate date,
    c_version integer,
    c_breakhour_start time without time zone,
    c_convention_key character varying,
    c_convention_threshold integer,
    c_convention_majoration real,
    c_convention_interpige integer,
    c_convention_nightranges character varying,
    c_convention_nightmajoration real,
    c_convention_chargepercent real,
    c_breakhour_end time without time zone,
    c_contracttotal real,
    c_sale_representative bigint,
    c_operation character varying,
    c_coefficiant integer,
    c_numassedic character varying,
    c_description character varying,
    c_staffcategory character varying,
    c_linkedrecord bigint,
    c_exported boolean,
    c_convention_sundaymajoration real,
    c_convention_holidays character varying,
    c_unit_calendarconstant integer
);


ALTER TABLE public.t_contract OWNER TO postgres;

--
-- Name: contract_idcontract_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contract_idcontract_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contract_idcontract_seq OWNER TO postgres;

--
-- Name: contract_idcontract_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contract_idcontract_seq OWNED BY public.t_contract.c_idcontract;


--
-- Name: t_contract_line; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_contract_line (
    c_idcontractline bigint NOT NULL,
    c_duequantity integer,
    c_effectivequantity integer,
    c_extraquantity integer,
    c_nightquantity integer,
    c_idcontract bigint,
    c_version integer,
    c_startperiod timestamp without time zone,
    c_endperiod timestamp without time zone,
    c_holidayquantity integer,
    c_manual_update_on boolean,
    c_travelquantity integer
);


ALTER TABLE public.t_contract_line OWNER TO postgres;

--
-- Name: contract_line_idcontractline_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contract_line_idcontractline_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contract_line_idcontractline_seq OWNER TO postgres;

--
-- Name: contract_line_idcontractline_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contract_line_idcontractline_seq OWNED BY public.t_contract_line.c_idcontractline;


--
-- Name: t_document_file; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_document_file (
    c_iddocument bigint,
    c_filepath character varying,
    c_idfile bigint NOT NULL
);


ALTER TABLE public.t_document_file OWNER TO postgres;

--
-- Name: document_file_idfile_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.document_file_idfile_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.document_file_idfile_seq OWNER TO postgres;

--
-- Name: document_file_idfile_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.document_file_idfile_seq OWNED BY public.t_document_file.c_idfile;


--
-- Name: t_document; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_document (
    c_iddocument bigint NOT NULL,
    c_name character varying,
    c_comments character varying,
    c_documenttype character varying,
    c_path character varying,
    c_version integer,
    c_book_rating integer,
    c_book_category character varying,
    c_discriminator character varying,
    c_url character varying
);


ALTER TABLE public.t_document OWNER TO postgres;

--
-- Name: document_iddocument_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.document_iddocument_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.document_iddocument_seq OWNER TO postgres;

--
-- Name: document_iddocument_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.document_iddocument_seq OWNED BY public.t_document.c_iddocument;


--
-- Name: t_equipment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_equipment (
    c_idequipment bigint NOT NULL,
    c_discriminator character varying,
    c_equipmenttype character varying,
    c_name character varying,
    c_description character varying,
    c_idphoto bigint,
    c_idpackingequipment bigint,
    c_version integer,
    c_idsupplier bigint,
    c_material_brand character varying,
    c_material_model character varying,
    c_material_serialnumber character varying,
    c_idfamily bigint,
    c_infrastructure_capacity integer,
    c_idlocation bigint,
    c_value numeric,
    c_weight numeric,
    c_volume numeric,
    c_creation date,
    c_supplier bigint,
    c_currency character varying,
    c_idsaleentity bigint,
    c_valueata numeric,
    c_currencyata character varying,
    c_brand character varying(255),
    c_location character varying(255),
    c_locationdetails character varying(255),
    c_model character varying(255),
    c_reference character varying(255),
    c_serialnumber character varying(255),
    c_specs_consumption character varying(255),
    c_specs_weight character varying(255),
    c_specs_size character varying(255),
    c_specs_orign character varying(255),
    c_specs_ip character varying(255),
    c_created_by bigint,
    c_updated_by bigint,
    c_updation date,
    c_value_ata double precision,
    c_value_purchage double precision,
    c_value_insurance double precision,
    c_format character varying(255),
    c_from_market_place boolean DEFAULT false
);


ALTER TABLE public.t_equipment OWNER TO postgres;

--
-- Name: equipment_idequipment_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.equipment_idequipment_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.equipment_idequipment_seq OWNER TO postgres;

--
-- Name: equipment_idequipment_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.equipment_idequipment_seq OWNED BY public.t_equipment.c_idequipment;


--
-- Name: t_function; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_function (
    c_idfunction bigint NOT NULL,
    c_description character varying,
    c_version integer,
    c_discriminator character varying,
    c_idparent bigint,
    c_category character varying,
    c_defaultunit character varying,
    c_defaultentity bigint,
    c_pos smallint,
    c_name character varying,
    c_idsupplier bigint,
    c_displaynumber integer,
    c_theme character varying,
    c_functiongroup character varying,
    c_vat_code character varying,
    c_vat_rate numeric,
    c_contractedition character varying,
    c_qualifiedname character varying,
    c_defaultresource bigint,
    c_keyword character varying,
    c_assignation character varying,
    c_currency character varying,
    c_defaultunit_calendarconstant integer,
    c_archived boolean,
    c_idfunctionkeyword bigint,
    c_folder boolean DEFAULT false NOT NULL
);


ALTER TABLE public.t_function OWNER TO postgres;

--
-- Name: function_idfunction_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.function_idfunction_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.function_idfunction_seq OWNER TO postgres;

--
-- Name: function_idfunction_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.function_idfunction_seq OWNED BY public.t_function.c_idfunction;


--
-- Name: t_function_rate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_function_rate (
    c_idrate bigint NOT NULL,
    c_unit character varying,
    c_currency character varying,
    c_price numeric,
    c_cost numeric,
    c_identity bigint,
    c_idcompany bigint,
    c_idfunction bigint NOT NULL,
    c_version integer,
    c_discriminator character varying,
    c_basismin numeric,
    c_extraslot numeric,
    c_extraprice numeric,
    c_extracost numeric,
    c_floorprice numeric,
    c_assignation character varying,
    c_label character varying,
    c_qty_cost numeric,
    c_qty_sale numeric,
    c_unit_sale character varying,
    c_qty_free numeric,
    c_comment character varying,
    c_unit_calendarconstant integer,
    c_unit_sale_calendarconstant integer,
    c_archived boolean,
    c_code character varying,
    c_title character varying
);


ALTER TABLE public.t_function_rate OWNER TO postgres;

--
-- Name: function_rate_idrate_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.function_rate_idrate_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.function_rate_idrate_seq OWNER TO postgres;

--
-- Name: function_rate_idrate_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.function_rate_idrate_seq OWNED BY public.t_function_rate.c_idrate;


--
-- Name: t_staff_contractsetting; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_contractsetting (
    c_idsetting bigint NOT NULL,
    c_version integer,
    c_discriminator character varying,
    c_contracttype character varying,
    c_daystart time without time zone,
    c_dayend time without time zone,
    c_rate character varying,
    c_convention_key character varying,
    c_convention_threshold integer,
    c_convention_majoration bigint,
    c_convention_interpige integer,
    c_convention_nightranges character varying,
    c_convention_nightmajoration real,
    c_convention_chargepercent real,
    c_employeeid bigint,
    c_convention_sundaymajoration real,
    c_convention_holidays character varying,
    c_rate_calendarconstant integer
);


ALTER TABLE public.t_staff_contractsetting OWNER TO postgres;

--
-- Name: person_contractsetting_idsetting_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.person_contractsetting_idsetting_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.person_contractsetting_idsetting_seq OWNER TO postgres;

--
-- Name: person_contractsetting_idsetting_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.person_contractsetting_idsetting_seq OWNED BY public.t_staff_contractsetting.c_idsetting;


--
-- Name: t_person; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_person (
    c_idperson bigint NOT NULL,
    c_courtesy character varying,
    c_lastname character varying,
    c_firstname character varying,
    c_function character varying,
    c_idcompany bigint,
    c_discriminator character varying,
    c_contact_idfollower bigint,
    c_version integer,
    c_idphoto bigint,
    c_idaddress bigint,
    c_idagent bigint,
    c_idcontractsetting bigint,
    c_idsupervisor bigint,
    c_bank_iban character varying,
    c_bank_swift character varying,
    c_birth_city character varying,
    c_birth_state character varying,
    c_birth_date date,
    c_gender character varying,
    c_birth_country character varying,
    c_user_language character varying,
    c_user_login character varying,
    c_user_password character varying,
    c_healthinsurance_number character varying,
    c_healthinsurance_company character varying,
    c_staff_comments character varying,
    c_nationality character varying,
    c_cotisation_spectacle character varying,
    c_numcardpress character varying,
    c_iban character varying,
    c_bic character varying,
    c_domiciliation character varying,
    c_beneficiaire character varying,
    c_marriedname character varying,
    c_birth_province character varying,
    c_google_cal boolean,
    c_role character varying,
    c_emp_code character varying(255),
    c_user_timezone character varying(255),
    c_first_time_login boolean DEFAULT true,
    c_user_email character varying(255),
    c_from_market_place boolean DEFAULT false,
    c_labour_rule bigint,
    c_hiring_date date,
    c_work_percentage smallint DEFAULT 0,
    c_user_leave_record_id bigint
);


ALTER TABLE public.t_person OWNER TO postgres;

--
-- Name: person_idperson_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.person_idperson_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.person_idperson_seq OWNER TO postgres;

--
-- Name: person_idperson_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.person_idperson_seq OWNED BY public.t_person.c_idperson;


--
-- Name: t_produnit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_produnit (
    c_idprodunit bigint NOT NULL,
    c_name character varying,
    c_theme character varying,
    c_version integer,
    c_archived boolean
);


ALTER TABLE public.t_produnit OWNER TO postgres;

--
-- Name: produnit_idprodunit_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.produnit_idprodunit_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.produnit_idprodunit_seq OWNER TO postgres;

--
-- Name: produnit_idprodunit_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.produnit_idprodunit_seq OWNED BY public.t_produnit.c_idprodunit;


--
-- Name: t_record_duedate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_record_duedate (
    c_idduedate bigint NOT NULL,
    c_idrecord bigint,
    c_duedate timestamp without time zone,
    c_version integer,
    c_discriminator character varying,
    c_invoice_paymentdate date,
    c_invoice_amount numeric,
    c_invoice_rate numeric,
    c_milestone_type_id bigint
);


ALTER TABLE public.t_record_duedate OWNER TO postgres;

--
-- Name: record_duedate_idduedate_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.record_duedate_idduedate_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.record_duedate_idduedate_seq OWNER TO postgres;

--
-- Name: record_duedate_idduedate_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.record_duedate_idduedate_seq OWNED BY public.t_record_duedate.c_idduedate;


--
-- Name: t_record_fee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_record_fee (
    c_idfee bigint NOT NULL,
    c_idrecord bigint,
    c_feetype character varying,
    c_rate numeric,
    c_amount numeric,
    c_version integer
);


ALTER TABLE public.t_record_fee OWNER TO postgres;

--
-- Name: record_fee_idfee_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.record_fee_idfee_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.record_fee_idfee_seq OWNER TO postgres;

--
-- Name: record_fee_idfee_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.record_fee_idfee_seq OWNED BY public.t_record_fee.c_idfee;


--
-- Name: t_record; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_record (
    c_idrecord bigint NOT NULL,
    c_idcompany bigint,
    c_recorddate date,
    c_project_idagency bigint,
    c_project_client character varying,
    c_idcontact bigint,
    c_project_iddirector bigint,
    c_idfollower bigint,
    c_status character varying,
    c_project_title character varying,
    c_totalprice numeric,
    c_discount_rate numeric,
    c_totalnetprice numeric,
    c_vat_rate numeric,
    c_vat_amount numeric,
    c_totalpriceivat numeric,
    c_discriminator character varying,
    c_version integer,
    c_info_maxlength character varying,
    c_info_format character varying,
    c_info_version character varying,
    c_payment_terms character varying,
    c_project_idchannel bigint,
    c_project_end timestamp without time zone,
    c_project_start timestamp without time zone,
    c_project_idprodunit bigint,
    c_currency character varying,
    c_identity bigint,
    c_language character varying,
    c_idsource bigint,
    c_client character varying,
    c_invoicenumber character varying,
    c_margin numeric,
    c_totalcost numeric,
    c_project_ponderation numeric,
    c_discount_amount numeric,
    c_program bigint,
    c_numobjet character varying,
    c_theme character varying,
    c_analyticaccountnumber character varying,
    c_nbepisodes integer,
    c_nbsessions integer,
    c_delivery character varying,
    c_csa character varying,
    c_idpad character varying,
    c_idlogo bigint,
    c_prodaddress character varying,
    c_prodorganisation character varying,
    c_start timestamp without time zone,
    c_channel bigint,
    c_minute_duration integer,
    c_produnit bigint,
    c_program_title character varying,
    c_year integer,
    c_reference character varying,
    c_info_comment character varying,
    c_reference_internal character varying,
    c_reference_external character varying,
    c_country character varying,
    c_city character varying,
    c_project_subcategory character varying,
    c_sign_person_hr bigint,
    c_sign_person_parttime bigint,
    c_purchase_order_date date,
    c_financial_status character varying(255),
    c_sessioned boolean DEFAULT false,
    c_season character varying(255),
    c_pdf character varying(512),
    c_procurement_tax numeric,
    c_contract bigint,
    c_category_id bigint,
    c_origin character varying(255)
);


ALTER TABLE public.t_record OWNER TO postgres;

--
-- Name: record_idrecord_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.record_idrecord_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.record_idrecord_seq OWNER TO postgres;

--
-- Name: record_idrecord_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.record_idrecord_seq OWNED BY public.t_record.c_idrecord;


--
-- Name: t_record_line; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_record_line (
    c_idline bigint NOT NULL,
    c_idrecord bigint,
    c_idfunction bigint,
    c_quantity numeric,
    c_unitprice numeric,
    c_unitcost numeric,
    c_currency character varying,
    c_discount numeric,
    c_totallocalprice numeric,
    c_totalprice numeric,
    c_totallocalcost numeric,
    c_totalcost numeric,
    c_version integer,
    c_unit character varying,
    c_disabled boolean,
    c_discriminator character varying,
    c_booking_from timestamp without time zone,
    c_booking_to timestamp without time zone,
    c_booking_idresource bigint,
    c_booking_comments character varying,
    c_booking_status character varying,
    c_booking_completion numeric,
    c_booking_group_idmaster bigint,
    c_booking_group_ordering integer,
    c_duration integer,
    c_status character varying,
    c_invoiceline_ratio numeric,
    c_vat_code character varying,
    c_vat_rate numeric,
    c_idwrappedline bigint,
    c_comment character varying,
    c_booking_realstart timestamp without time zone,
    c_booking_realend timestamp without time zone,
    c_booking_completion_status character varying,
    c_contractstatus character varying,
    c_floorprice numeric,
    c_rate_basismin real,
    c_rate_extraslot real,
    c_rate_unitprice real,
    c_rate_extracost real,
    c_rate_extraprice real,
    c_rate_unitcost real,
    c_mailed boolean,
    c_theme character varying,
    c_reference bigint,
    c_occurrence_count numeric,
    c_occurrence_size numeric,
    c_origin bigint,
    c_quantity_sale numeric,
    c_syncqty boolean,
    c_freequantity numeric,
    c_extracost numeric,
    c_extraprice numeric,
    c_assignation character varying,
    c_rate bigint,
    c_saleentity bigint,
    c_unit_sale character varying,
    c_unit_calendarconstant integer,
    c_unit_sale_calendarconstant integer,
    c_qtytotalsold numeric,
    c_work_order_id bigint,
    c_travel_order_id bigint,
    c_bookings_status integer,
    c_pickup_time timestamp without time zone,
    c_return_time timestamp without time zone,
    c_total_cost_with_occurence_count numeric,
    c_total_price_with_occurence_count numeric,
    c_total_with_tax numeric,
    c_group_id bigint,
    c_user_end_time timestamp without time zone,
    c_user_start_time timestamp without time zone,
    c_media_order_id bigint
);


ALTER TABLE public.t_record_line OWNER TO postgres;

--
-- Name: record_line_idline_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.record_line_idline_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.record_line_idline_seq OWNER TO postgres;

--
-- Name: record_line_idline_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.record_line_idline_seq OWNED BY public.t_record_line.c_idline;


--
-- Name: t_staff_function; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_function (
    c_idstafffunction bigint NOT NULL,
    c_idfunction bigint,
    c_rating integer,
    c_idstaffmember bigint,
    c_version integer
);


ALTER TABLE public.t_staff_function OWNER TO postgres;

--
-- Name: resource_function_idresourcefunction_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.resource_function_idresourcefunction_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.resource_function_idresourcefunction_seq OWNER TO postgres;

--
-- Name: resource_function_idresourcefunction_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.resource_function_idresourcefunction_seq OWNED BY public.t_staff_function.c_idstafffunction;


--
-- Name: t_resource; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_resource (
    c_idresource bigint NOT NULL,
    c_idstaffmember bigint,
    c_idequipment bigint,
    c_discriminator character varying,
    c_version integer,
    c_name character varying,
    c_idfunction bigint,
    c_idorder bigint,
    c_resourcegroup bigint,
    c_parent bigint,
    c_idinout bigint,
    t_external_ref character varying
);


ALTER TABLE public.t_resource OWNER TO postgres;

--
-- Name: resource_idresource_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.resource_idresource_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.resource_idresource_seq OWNER TO postgres;

--
-- Name: resource_idresource_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.resource_idresource_seq OWNED BY public.t_resource.c_idresource;


--
-- Name: t_unavailability; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_unavailability (
    c_idunavailability bigint NOT NULL,
    c_periodic_from date,
    c_periodic_to date,
    c_recurrent_days character varying,
    c_version integer,
    c_idresource bigint,
    c_recurrent_time_min time without time zone,
    c_recurrent_time_max time without time zone,
    c_discriminator character varying,
    c_idgroup bigint,
    c_reference bigint,
    c_detail character varying,
    c_type character varying
);


ALTER TABLE public.t_unavailability OWNER TO postgres;

--
-- Name: resource_unavailability_idunavailability_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.resource_unavailability_idunavailability_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.resource_unavailability_idunavailability_seq OWNER TO postgres;

--
-- Name: resource_unavailability_idunavailability_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.resource_unavailability_idunavailability_seq OWNED BY public.t_unavailability.c_idunavailability;


--
-- Name: t_staff_iddocument; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_iddocument (
    c_iddocument bigint NOT NULL,
    c_typedocument character varying,
    c_number character varying,
    c_deliveredby character varying,
    c_deliveredin character varying,
    c_untildate date,
    c_idcontact bigint,
    c_version integer
);


ALTER TABLE public.t_staff_iddocument OWNER TO postgres;

--
-- Name: staff_iddocument_iddocument_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.staff_iddocument_iddocument_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.staff_iddocument_iddocument_seq OWNER TO postgres;

--
-- Name: staff_iddocument_iddocument_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.staff_iddocument_iddocument_seq OWNED BY public.t_staff_iddocument.c_iddocument;


--
-- Name: t_staff_rate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_rate (
    c_idresourcefunction bigint,
    c_rateunit character varying,
    c_value numeric,
    c_idstaffrate bigint NOT NULL,
    c_version integer,
    c_currency character varying,
    c_chargepercent real,
    c_discriminator character varying,
    c_rateunit_calendarconstant integer
);


ALTER TABLE public.t_staff_rate OWNER TO postgres;

--
-- Name: staff_rate_idstaffrate_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.staff_rate_idstaffrate_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.staff_rate_idstaffrate_seq OWNER TO postgres;

--
-- Name: staff_rate_idstaffrate_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.staff_rate_idstaffrate_seq OWNED BY public.t_staff_rate.c_idstaffrate;


--
-- Name: t_staff_skill; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_skill (
    c_idstaffmember bigint,
    c_name character varying NOT NULL,
    c_rating integer,
    c_idskill bigint NOT NULL
);


ALTER TABLE public.t_staff_skill OWNER TO postgres;

--
-- Name: staff_skill_idskill_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.staff_skill_idskill_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.staff_skill_idskill_seq OWNER TO postgres;

--
-- Name: staff_skill_idskill_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.staff_skill_idskill_seq OWNED BY public.t_staff_skill.c_idskill;


--
-- Name: t_step; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_step (
    c_idstep bigint NOT NULL,
    c_step_position integer,
    c_idfunction bigint,
    c_idbooking bigint,
    c_idworkflow bigint,
    c_version bigint,
    c_discriminator character varying
);


ALTER TABLE public.t_step OWNER TO postgres;

--
-- Name: step_idstep_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.step_idstep_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.step_idstep_seq OWNER TO postgres;

--
-- Name: step_idstep_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.step_idstep_seq OWNED BY public.t_step.c_idstep;


--
-- Name: t_accountancy_number; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_accountancy_number (
    c_id bigint NOT NULL,
    c_type character varying,
    c_value character varying,
    c_version integer
);


ALTER TABLE public.t_accountancy_number OWNER TO postgres;

--
-- Name: t_amortization; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_amortization (
    c_id bigint NOT NULL,
    c_date date,
    c_percent numeric,
    c_value numeric,
    c_equipment bigint,
    c_version integer
);


ALTER TABLE public.t_amortization OWNER TO postgres;

--
-- Name: t_asset; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_asset (
    c_idasset bigint NOT NULL,
    c_version integer,
    c_title character varying,
    c_thumbnail bigint,
    c_guid character varying,
    c_description character varying,
    c_idorigin bigint
);


ALTER TABLE public.t_asset OWNER TO postgres;

--
-- Name: t_asset_comment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_asset_comment (
    c_comment bigint NOT NULL,
    c_asset bigint NOT NULL
);


ALTER TABLE public.t_asset_comment OWNER TO postgres;

--
-- Name: t_attachment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_attachment (
    c_idattachment bigint NOT NULL,
    c_version integer,
    c_feed_by_url boolean,
    c_file_name character varying(255),
    c_type character varying(255),
    c_url character varying(255),
    c_path character varying(255),
    c_idequipment bigint,
    c_idperson bigint,
    c_extension character varying(255),
    c_idrecord bigint,
    c_idcompany bigint
);


ALTER TABLE public.t_attachment OWNER TO postgres;

--
-- Name: t_booking_bookingcombination; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_booking_bookingcombination (
    c_booking_combination bigint NOT NULL,
    c_id bigint NOT NULL
);


ALTER TABLE public.t_booking_bookingcombination OWNER TO postgres;

--
-- Name: t_booking_combination; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_booking_combination (
    c_id bigint NOT NULL,
    c_title character varying,
    c_bookingcombinationtype character varying,
    c_jndi character varying,
    c_update timestamp without time zone,
    c_project bigint,
    c_version integer,
    c_booking_reference bigint,
    c_discriminator character varying
);


ALTER TABLE public.t_booking_combination OWNER TO postgres;

--
-- Name: t_booking_order_form; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_booking_order_form (
    c_idbookingorderform bigint NOT NULL,
    c_version integer,
    c_form_type character varying(255),
    c_order_type character varying(255)
);


ALTER TABLE public.t_booking_order_form OWNER TO postgres;

--
-- Name: t_booking_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_booking_status (
    c_id bigint NOT NULL,
    c_reference bigint,
    c_updated_at timestamp without time zone,
    c_type character varying,
    c_version integer
);


ALTER TABLE public.t_booking_status OWNER TO postgres;

--
-- Name: t_braodcast; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_braodcast (
    c_idmedia bigint NOT NULL,
    c_idchannel bigint NOT NULL,
    c_date timestamp without time zone NOT NULL,
    c_idbroadcast bigint,
    c_version integer,
    c_title character varying
);


ALTER TABLE public.t_braodcast OWNER TO postgres;

--
-- Name: t_brief; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_brief (
    c_id bigint NOT NULL,
    c_description character varying,
    c_version integer,
    c_idrecord bigint,
    c_type character varying
);


ALTER TABLE public.t_brief OWNER TO postgres;

--
-- Name: t_brief_c_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.t_brief_c_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_brief_c_id_seq OWNER TO postgres;

--
-- Name: t_brief_c_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.t_brief_c_id_seq OWNED BY public.t_brief.c_id;


--
-- Name: t_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_category (
    c_category_id bigint NOT NULL,
    c_name character varying(255)
);


ALTER TABLE public.t_category OWNER TO postgres;

--
-- Name: t_channel_format; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_channel_format (
    c_idchannelformat bigint NOT NULL,
    c_idchannel bigint NOT NULL,
    c_idrecord bigint,
    c_idcompany bigint,
    c_format_id bigint
);


ALTER TABLE public.t_channel_format OWNER TO postgres;

--
-- Name: t_channel_format_config; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_channel_format_config (
    c_idcompany bigint NOT NULL,
    c_format_id bigint NOT NULL
);


ALTER TABLE public.t_channel_format_config OWNER TO postgres;

--
-- Name: t_comment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_comment (
    c_idcomment bigint NOT NULL,
    c_detail character varying,
    c_author bigint,
    c_date timestamp without time zone,
    c_version integer
);


ALTER TABLE public.t_comment OWNER TO postgres;

--
-- Name: t_company_accountancy_number; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_company_accountancy_number (
    c_company bigint NOT NULL,
    c_accountancy_number bigint NOT NULL
);


ALTER TABLE public.t_company_accountancy_number OWNER TO postgres;

--
-- Name: t_company_number; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_company_number (
    c_idcompany bigint NOT NULL,
    c_name character varying NOT NULL,
    c_value character varying
);


ALTER TABLE public.t_company_number OWNER TO postgres;

--
-- Name: t_company_profile; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_company_profile (
    c_profile character varying NOT NULL,
    c_value character varying,
    c_idcompany bigint NOT NULL
);


ALTER TABLE public.t_company_profile OWNER TO postgres;

--
-- Name: t_company_vat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_company_vat (
    c_idcompany bigint NOT NULL,
    c_code character varying NOT NULL,
    c_rate numeric
);


ALTER TABLE public.t_company_vat OWNER TO postgres;

--
-- Name: t_contact_record; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_contact_record (
    c_idperson bigint NOT NULL,
    c_idrecord bigint NOT NULL,
    c_role character varying
);


ALTER TABLE public.t_contact_record OWNER TO postgres;

--
-- Name: t_contract_booking; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_contract_booking (
    c_idcontractline bigint NOT NULL,
    c_idbooking bigint NOT NULL
);


ALTER TABLE public.t_contract_booking OWNER TO postgres;

--
-- Name: t_contract_thresholdcouple; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_contract_thresholdcouple (
    c_threshold integer NOT NULL,
    c_majoration real,
    c_idcontract bigint NOT NULL,
    c_thresholdcouple bigint NOT NULL
);


ALTER TABLE public.t_contract_thresholdcouple OWNER TO postgres;

--
-- Name: t_currency; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_currency (
    c_idcurrency bigint NOT NULL,
    c_code character varying(5) NOT NULL,
    c_symbol character varying(55),
    c_active boolean DEFAULT true
);


ALTER TABLE public.t_currency OWNER TO postgres;

--
-- Name: t_customer_channel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_customer_channel (
    c_idcustomer bigint NOT NULL,
    c_idchannel bigint NOT NULL
);


ALTER TABLE public.t_customer_channel OWNER TO postgres;

--
-- Name: t_customer_director; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_customer_director (
    c_idcustomer bigint NOT NULL,
    c_iddirector bigint NOT NULL
);


ALTER TABLE public.t_customer_director OWNER TO postgres;

--
-- Name: t_customer_produnit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_customer_produnit (
    c_idcustomer bigint NOT NULL,
    c_idprodunit bigint NOT NULL
);


ALTER TABLE public.t_customer_produnit OWNER TO postgres;

--
-- Name: t_digital_signature_envelope; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_digital_signature_envelope (
    c_digital_signature_envelope_id bigint NOT NULL,
    c_name character varying(255) NOT NULL,
    c_id_template_type bigint NOT NULL
);


ALTER TABLE public.t_digital_signature_envelope OWNER TO postgres;

--
-- Name: t_document_gerenation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_document_gerenation (
    c_iddocumentgerenation bigint NOT NULL,
    c_version integer,
    c_created_on timestamp without time zone,
    c_document_path character varying(255),
    c_total_required_signatures integer,
    c_updated_on timestamp without time zone,
    c_created_by bigint,
    c_updated_by bigint,
    c_completely_signed boolean,
    c_edition_xsl character varying(255),
    c_edition_key character varying(255)
);


ALTER TABLE public.t_document_gerenation OWNER TO postgres;

--
-- Name: t_document_keyword; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_document_keyword (
    c_iddocument bigint NOT NULL,
    c_keyword character varying NOT NULL
);


ALTER TABLE public.t_document_keyword OWNER TO postgres;

--
-- Name: t_document_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_document_type (
    c_iddocumenttype bigint NOT NULL,
    c_version integer,
    c_type character varying(255)
);


ALTER TABLE public.t_document_type OWNER TO postgres;

--
-- Name: t_edition_tempalte_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_edition_tempalte_type (
    c_edition_tempalte_type_id bigint NOT NULL,
    c_template_name character varying(255) NOT NULL
);


ALTER TABLE public.t_edition_tempalte_type OWNER TO postgres;

--
-- Name: t_envelope_signature_recipient; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_envelope_signature_recipient (
    c_digital_signature_envelope_id bigint NOT NULL,
    c_signature_recipient_id bigint NOT NULL
);


ALTER TABLE public.t_envelope_signature_recipient OWNER TO postgres;

--
-- Name: t_equipment_milestone; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_equipment_milestone (
    equipment_id bigint NOT NULL,
    c_date bytea,
    c_milestone_type_id bigint
);


ALTER TABLE public.t_equipment_milestone OWNER TO postgres;

--
-- Name: t_event; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_event (
    c_id bigint NOT NULL,
    c_version integer,
    c_origin bigint,
    c_from timestamp without time zone,
    c_to timestamp without time zone,
    c_duration integer,
    c_status character varying,
    c_completion numeric,
    c_reference bigint,
    c_detail character varying,
    c_type character varying,
    c_resource bigint,
    c_recurrent_time_min time without time zone,
    c_recurrent_time_max time without time zone,
    c_recurrent_days character varying,
    c_discriminator character varying,
    c_theme character varying,
    c_title character varying,
    c_completion_status character varying,
    c_recurrence_reference bigint,
    c_unavailability bigint,
    c_comment character varying
);


ALTER TABLE public.t_event OWNER TO postgres;

--
-- Name: t_expenses_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_expenses_detail (
    c_idexpensesdetail bigint NOT NULL,
    c_version integer,
    c_amount real,
    c_budget real,
    c_item integer,
    c_personal real,
    c_type character varying(255),
    c_expenses_report_id bigint
);


ALTER TABLE public.t_expenses_detail OWNER TO postgres;

--
-- Name: t_expenses_item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_expenses_item (
    c_expenses_item_id bigint NOT NULL,
    c_company_card_expense double precision,
    c_date timestamp without time zone,
    c_name character varying(255),
    c_personal_expense double precision,
    c_personal_expenses_report_id bigint
);


ALTER TABLE public.t_expenses_item OWNER TO postgres;

--
-- Name: t_expenses_report; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_expenses_report (
    c_idexpensesreport bigint NOT NULL,
    c_version integer,
    c_status character varying(255)
);


ALTER TABLE public.t_expenses_report OWNER TO postgres;

--
-- Name: t_extra; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_extra (
    c_id bigint NOT NULL,
    c_idrate bigint,
    c_label character varying,
    c_basis numeric,
    c_percent numeric,
    c_total numeric,
    c_quantity integer,
    c_version integer,
    c_basis_price numeric,
    c_percent_price numeric,
    c_total_price numeric
);


ALTER TABLE public.t_extra OWNER TO postgres;

--
-- Name: t_format; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_format (
    c_format_id bigint NOT NULL,
    c_name character varying(255)
);


ALTER TABLE public.t_format OWNER TO postgres;

--
-- Name: t_function_accountancy_number; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_function_accountancy_number (
    c_function bigint NOT NULL,
    c_accountancy_number bigint NOT NULL
);


ALTER TABLE public.t_function_accountancy_number OWNER TO postgres;

--
-- Name: t_function_fee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_function_fee (
    c_idfunction bigint NOT NULL,
    c_fee character varying NOT NULL
);


ALTER TABLE public.t_function_fee OWNER TO postgres;

--
-- Name: t_function_keys; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_function_keys (
    c_idfunctionkey bigint NOT NULL,
    c_version integer,
    c_key_value character varying(255),
    c_idfunctionkeyword bigint
);


ALTER TABLE public.t_function_keys OWNER TO postgres;

--
-- Name: t_function_keyword; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_function_keyword (
    c_idfunctionkeyword bigint NOT NULL,
    c_version integer,
    c_keyword character varying(255)
);


ALTER TABLE public.t_function_keyword OWNER TO postgres;

--
-- Name: t_function_siblings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_function_siblings (
    c_idmaster bigint NOT NULL,
    c_idslave bigint NOT NULL,
    c_relationship character varying NOT NULL
);


ALTER TABLE public.t_function_siblings OWNER TO postgres;

--
-- Name: t_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_group (
    c_group_id bigint NOT NULL,
    c_color_theme character varying(255),
    c_dynamic boolean,
    c_name character varying(255) NOT NULL
);


ALTER TABLE public.t_group OWNER TO postgres;

--
-- Name: t_group_member; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_group_member (
    c_group_id bigint NOT NULL,
    c_idperson bigint NOT NULL
);


ALTER TABLE public.t_group_member OWNER TO postgres;

--
-- Name: t_holiday; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_holiday (
    c_idholiday bigint NOT NULL,
    c_holiday_date date,
    c_holiday_name character varying(255),
    c_labour_holiday bigint
);


ALTER TABLE public.t_holiday OWNER TO postgres;

--
-- Name: t_inout; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_inout (
    c_idinout bigint NOT NULL,
    c_media bigint,
    c_inout_type character varying,
    c_comment character varying,
    c_inout_status character varying,
    c_resource bigint,
    c_project bigint,
    c_version integer
);


ALTER TABLE public.t_inout OWNER TO postgres;

--
-- Name: t_input_asset; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_input_asset (
    c_idproject bigint NOT NULL,
    c_idasset bigint NOT NULL
);


ALTER TABLE public.t_input_asset OWNER TO postgres;

--
-- Name: t_issue; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_issue (
    c_id bigint NOT NULL,
    c_reporter bigint,
    c_date timestamp without time zone,
    c_severity character varying,
    c_comment character varying,
    c_booking bigint,
    c_version integer
);


ALTER TABLE public.t_issue OWNER TO postgres;

--
-- Name: t_keyword; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_keyword (
    c_idkeyword bigint NOT NULL,
    c_version integer,
    c_key character varying(255),
    c_booking_order_form_id bigint
);


ALTER TABLE public.t_keyword OWNER TO postgres;

--
-- Name: t_labour_rule; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_labour_rule (
    c_labour_rule_id bigint NOT NULL,
    c_average_weekly_duration bigint,
    c_distance_to_production bigint,
    c_extar_hour_charges_percentage bigint,
    c_holiday_hour_charges_percentage bigint,
    c_labour_rule_name character varying(255),
    c_lunch_duration bigint,
    c_maximum_freelancer_working_day bigint,
    c_maximum_weekly_duration bigint,
    c_maximum_working_duration bigint,
    c_morning_end_time time without time zone,
    c_morning_start_time time without time zone,
    c_night_end_time timestamp without time zone,
    c_night_hour_charges_percentage bigint,
    c_night_start_time time without time zone,
    c_overtime_charges_for_1_to_4_hour bigint,
    c_overtime_charges_for_5_to_8_hour bigint,
    c_overtime_charges_for_9_to_above_hour bigint,
    c_rest_duration_between_booking bigint,
    c_special_holiday_hour_charges_percentage bigint,
    c_sunday_hour_charges_percentage bigint,
    c_travel_time_period bigint,
    c_working_day_per_year bigint,
    c_working_duration bigint,
    c_working_duration_per_month bigint
);


ALTER TABLE public.t_labour_rule OWNER TO postgres;

--
-- Name: t_labour_staffmember; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_labour_staffmember (
    labour_id bigint NOT NULL,
    staff_member_id bigint NOT NULL
);


ALTER TABLE public.t_labour_staffmember OWNER TO postgres;

--
-- Name: t_leave_record; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_leave_record (
    c_leave_record_id bigint NOT NULL,
    c_credit_leave integer,
    c_debit_leave integer,
    c_leave_type_id bigint,
    leave_record_id bigint
);


ALTER TABLE public.t_leave_record OWNER TO postgres;

--
-- Name: t_leave_request; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_leave_request (
    c_leave_request_id bigint NOT NULL,
    c_comment character varying(255),
    c_end_time timestamp without time zone,
    c_leave_status character varying(255),
    c_number_of_day integer,
    c_start_time timestamp without time zone,
    c_leave_record_id bigint,
    leave_request_id bigint,
    c_attachment_id bigint
);


ALTER TABLE public.t_leave_request OWNER TO postgres;

--
-- Name: t_leave_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_leave_type (
    c_leave_type_id bigint NOT NULL,
    c_active boolean,
    c_type character varying(255)
);


ALTER TABLE public.t_leave_type OWNER TO postgres;

--
-- Name: t_line_extra; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_line_extra (
    c_id bigint NOT NULL,
    c_version integer,
    c_label character varying,
    c_basis numeric,
    c_percent numeric,
    c_quantity integer,
    c_idline bigint,
    c_total numeric,
    c_basis_price numeric,
    c_percent_price numeric,
    c_total_price numeric
);


ALTER TABLE public.t_line_extra OWNER TO postgres;

--
-- Name: t_line_fee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_line_fee (
    c_idline bigint NOT NULL,
    c_fee character varying NOT NULL
);


ALTER TABLE public.t_line_fee OWNER TO postgres;

--
-- Name: t_linked_event; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_linked_event (
    c_event_id bigint NOT NULL,
    c_link_event_id bigint NOT NULL
);


ALTER TABLE public.t_linked_event OWNER TO postgres;

--
-- Name: t_location; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_location (
    c_idlocation bigint NOT NULL,
    c_idcustomer bigint,
    c_building character varying,
    c_floor character varying,
    c_room character varying,
    c_version integer,
    c_country character varying
);


ALTER TABLE public.t_location OWNER TO postgres;

--
-- Name: t_location_c_idlocation_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.t_location_c_idlocation_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_location_c_idlocation_seq OWNER TO postgres;

--
-- Name: t_location_c_idlocation_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.t_location_c_idlocation_seq OWNED BY public.t_location.c_idlocation;


--
-- Name: t_maintenance_contract; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_maintenance_contract (
    c_id bigint NOT NULL,
    c_start date,
    c_end date,
    c_type character varying,
    c_amount numeric,
    c_currency character varying,
    c_supplier bigint,
    c_equipment bigint,
    c_version integer
);


ALTER TABLE public.t_maintenance_contract OWNER TO postgres;

--
-- Name: t_media; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_media (
    c_idmedia bigint NOT NULL,
    c_libelle character varying,
    c_asset bigint,
    c_version integer,
    c_format character varying,
    c_length character varying,
    c_csa character varying,
    c_file bigint,
    c_umid character varying,
    c_localization character varying
);


ALTER TABLE public.t_media OWNER TO postgres;

--
-- Name: t_milestone_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_milestone_type (
    c_milestone_type_id bigint NOT NULL,
    c_name character varying(255),
    c_discriminator character varying(255)
);


ALTER TABLE public.t_milestone_type OWNER TO postgres;

--
-- Name: t_order_form; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_order_form (
    c_idorderform bigint NOT NULL,
    c_version integer,
    c_form_type character varying(255),
    c_order_type character varying(255),
    c_work_and_travel_order_id bigint
);


ALTER TABLE public.t_order_form OWNER TO postgres;

--
-- Name: t_order_keyword; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_order_keyword (
    c_idorderkeyword bigint NOT NULL,
    c_version integer,
    c_keyword character varying(255),
    c_work_and_travel_order_id bigint,
    c_order_form_id bigint,
    c_keyword_value character varying(255)
);


ALTER TABLE public.t_order_keyword OWNER TO postgres;

--
-- Name: t_person_number; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_person_number (
    c_idperson bigint NOT NULL,
    c_name character varying NOT NULL,
    c_value character varying
);


ALTER TABLE public.t_person_number OWNER TO postgres;

--
-- Name: t_person_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_person_role (
    c_idperson bigint NOT NULL,
    c_role character varying NOT NULL
);


ALTER TABLE public.t_person_role OWNER TO postgres;

--
-- Name: t_personal_document; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_personal_document (
    c_idpersonaldocument bigint NOT NULL,
    c_version integer,
    c_expiration_date timestamp without time zone,
    c_issue_date timestamp without time zone,
    c_number character varying(255),
    c_idperson bigint,
    c_iddocumenttype bigint
);


ALTER TABLE public.t_personal_document OWNER TO postgres;

--
-- Name: t_personal_expenses_report; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_personal_expenses_report (
    c_personal_expenses_report_id bigint NOT NULL,
    c_date timestamp without time zone,
    c_status character varying(255),
    c_total_company_card_expenses double precision,
    c_total_personal_expenses double precision,
    c_idrecord bigint NOT NULL,
    c_idperson bigint NOT NULL
);


ALTER TABLE public.t_personal_expenses_report OWNER TO postgres;

--
-- Name: t_program; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_program (
    c_id bigint NOT NULL,
    c_title character varying,
    c_start timestamp without time zone,
    c_minute_duration integer,
    c_version integer,
    c_status character varying,
    c_idfollower bigint,
    c_reference character varying,
    c_year integer,
    c_channel bigint,
    c_theme character varying,
    c_analyticaccountnumber character varying,
    c_produnit bigint
);


ALTER TABLE public.t_program OWNER TO postgres;

--
-- Name: t_program_c_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.t_program_c_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.t_program_c_id_seq OWNER TO postgres;

--
-- Name: t_program_c_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.t_program_c_id_seq OWNED BY public.t_program.c_id;


--
-- Name: t_program_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_program_members (
    c_idrecord bigint NOT NULL,
    c_idperson bigint NOT NULL
);


ALTER TABLE public.t_program_members OWNER TO postgres;

--
-- Name: t_promo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_promo (
    c_id bigint NOT NULL,
    c_quotation bigint,
    c_title character varying,
    c_idclean character varying,
    c_rdv character varying,
    c_csa character varying,
    c_hour time without time zone,
    c_week integer,
    c_version integer
);


ALTER TABLE public.t_promo OWNER TO postgres;

--
-- Name: t_record_channel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_record_channel (
    c_idrecord bigint NOT NULL,
    c_idchannel bigint NOT NULL
);


ALTER TABLE public.t_record_channel OWNER TO postgres;

--
-- Name: t_record_contact; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_record_contact (
    c_idperson bigint NOT NULL,
    c_idrecord bigint NOT NULL
);


ALTER TABLE public.t_record_contact OWNER TO postgres;

--
-- Name: t_record_currency; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_record_currency (
    c_idrecord bigint NOT NULL,
    c_currency character varying NOT NULL,
    c_rate numeric
);


ALTER TABLE public.t_record_currency OWNER TO postgres;

--
-- Name: t_record_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_record_history (
    c_idrecordhistory bigint NOT NULL,
    c_version integer,
    c_record_discriminator character varying(255),
    c_idrecord bigint NOT NULL,
    c_date_viewed timestamp without time zone,
    c_idperson bigint
);


ALTER TABLE public.t_record_history OWNER TO postgres;

--
-- Name: t_record_informations_status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_record_informations_status (
    c_name character varying NOT NULL,
    c_status boolean,
    c_idrecord bigint NOT NULL
);


ALTER TABLE public.t_record_informations_status OWNER TO postgres;

--
-- Name: t_record_vat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_record_vat (
    c_idrecord bigint NOT NULL,
    c_code character varying NOT NULL,
    c_rate numeric
);


ALTER TABLE public.t_record_vat OWNER TO postgres;

--
-- Name: t_reel; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_reel (
    c_idreel bigint NOT NULL,
    c_version integer,
    c_bio text,
    c_title character varying(255),
    c_type character varying(255),
    c_udated_on bytea,
    c_upload boolean,
    c_url character varying(255),
    c_idstaff bigint
);


ALTER TABLE public.t_reel OWNER TO postgres;

--
-- Name: t_resource_function; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_resource_function (
    c_idresourcefunction bigint DEFAULT nextval('public.resource_function_idresourcefunction_seq'::regclass) NOT NULL,
    c_rating integer,
    c_idfunction bigint,
    c_idresource bigint,
    c_version integer,
    c_contractedition character varying,
    c_date_created timestamp without time zone,
    c_primary_function boolean DEFAULT false,
    c_rate_selected boolean
);


ALTER TABLE public.t_resource_function OWNER TO postgres;

--
-- Name: t_resource_information; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_resource_information (
    c_id bigint NOT NULL,
    c_description character varying,
    c_type character varying,
    c_equipment bigint,
    c_resource bigint,
    c_version integer,
    c_keyword character varying,
    c_function_keyword_value character varying(255),
    c_function_key_value character varying(255),
    c_function_id bigint
);


ALTER TABLE public.t_resource_information OWNER TO postgres;

--
-- Name: t_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_role (
    role_id bigint NOT NULL,
    active boolean NOT NULL,
    role_name character varying(255)
);


ALTER TABLE public.t_role OWNER TO postgres;

--
-- Name: t_roster_event; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_roster_event (
    c_idrosterevent bigint NOT NULL,
    c_version integer,
    c_from timestamp without time zone,
    c_function_type character varying(255),
    c_quantity real,
    c_to timestamp without time zone,
    c_title character varying(255),
    c_idfunction bigint
);


ALTER TABLE public.t_roster_event OWNER TO postgres;

--
-- Name: t_roster_resource; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_roster_resource (
    c_id bigint NOT NULL,
    c_idresource bigint NOT NULL
);


ALTER TABLE public.t_roster_resource OWNER TO postgres;

--
-- Name: t_signature_person; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_signature_person (
    c_idsignatureperson bigint NOT NULL,
    c_version integer,
    c_document_signed_on timestamp without time zone,
    c_flag_number integer,
    c_signed_by bigint,
    c_idsignature_person bigint
);


ALTER TABLE public.t_signature_person OWNER TO postgres;

--
-- Name: t_signature_recipient; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_signature_recipient (
    c_signature_recipient_id bigint NOT NULL,
    c_approval_required boolean,
    c_routing_order integer,
    c_idrecipient bigint
);


ALTER TABLE public.t_signature_recipient OWNER TO postgres;

--
-- Name: t_spreadsheet_upload_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_spreadsheet_upload_log (
    c_idspreadsheetuploadlog bigint NOT NULL,
    c_version integer,
    c_document_type character varying(255),
    c_error boolean,
    importfor character varying(255),
    c_log_details text,
    c_upload_time timestamp without time zone,
    c_uploaded_by character varying(255),
    c_staff_id bigint,
    c_url character varying(255),
    c_file_name character varying(255),
    c_path character varying(255)
);


ALTER TABLE public.t_spreadsheet_upload_log OWNER TO postgres;

--
-- Name: t_staff_book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_book (
    c_idstaffmember bigint NOT NULL,
    c_iddocument bigint NOT NULL
);


ALTER TABLE public.t_staff_book OWNER TO postgres;

--
-- Name: t_staff_email; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_email (
    c_idstaffemail bigint NOT NULL,
    c_version integer,
    c_email character varying(255),
    c_primary_email boolean DEFAULT false,
    c_idperson bigint
);


ALTER TABLE public.t_staff_email OWNER TO postgres;

--
-- Name: t_staff_employee_id; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_employee_id (
    c_staffemployeeid bigint NOT NULL,
    c_saleentity bigint,
    c_staffid bigint,
    c_employeeid bigint,
    c_version integer
);


ALTER TABLE public.t_staff_employee_id OWNER TO postgres;

--
-- Name: t_staff_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_group (
    c_idparent bigint NOT NULL,
    c_idresource bigint NOT NULL
);


ALTER TABLE public.t_staff_group OWNER TO postgres;

--
-- Name: t_staff_language; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_language (
    c_idstaffmember bigint NOT NULL,
    c_language character varying NOT NULL
);


ALTER TABLE public.t_staff_language OWNER TO postgres;

--
-- Name: t_staff_milestone; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_milestone (
    c_idstaff bigint NOT NULL,
    c_date bytea,
    c_milestone_type_id bigint
);


ALTER TABLE public.t_staff_milestone OWNER TO postgres;

--
-- Name: t_staff_specialty; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_specialty (
    c_idstaffmember bigint NOT NULL,
    c_specialty character varying NOT NULL
);


ALTER TABLE public.t_staff_specialty OWNER TO postgres;

--
-- Name: t_staff_telephone; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_staff_telephone (
    c_idstafftelephone bigint NOT NULL,
    c_version integer,
    c_telephone character varying(255),
    c_primary_telephone boolean DEFAULT false,
    c_idperson bigint
);


ALTER TABLE public.t_staff_telephone OWNER TO postgres;

--
-- Name: t_teamium_seq_store; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_teamium_seq_store (
    teamium_seq_id character varying NOT NULL,
    teamium_seq_value bigint
);


ALTER TABLE public.t_teamium_seq_store OWNER TO postgres;

--
-- Name: t_user_booking; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_user_booking (
    c_user_booking_id bigint NOT NULL,
    c_end_time timestamp without time zone,
    c_name character varying(255),
    c_start_time timestamp without time zone,
    c_user bigint,
    c_booking_id bigint,
    c_theme character varying(255),
    c_user_end_time timestamp without time zone,
    c_user_start_time timestamp without time zone,
    c_sick boolean
);


ALTER TABLE public.t_user_booking OWNER TO postgres;

--
-- Name: t_user_leave_record; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_user_leave_record (
    c_user_leave_record_id bigint NOT NULL
);


ALTER TABLE public.t_user_leave_record OWNER TO postgres;

--
-- Name: t_user_password_recovery; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_user_password_recovery (
    c_iduserpasswordrecovery bigint NOT NULL,
    c_version integer,
    c_created_on timestamp without time zone,
    c_token character varying(255),
    c_idperson bigint,
    c_link_validated boolean DEFAULT false
);


ALTER TABLE public.t_user_password_recovery OWNER TO postgres;

--
-- Name: t_user_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_user_role (
    c_idperson bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.t_user_role OWNER TO postgres;

--
-- Name: t_work_and_travel_order; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_work_and_travel_order (
    c_idworkandtravelorder bigint NOT NULL,
    c_version integer,
    c_comment character varying(255),
    c_order_type character varying(255) NOT NULL,
    c_status character varying(255),
    c_mediaid character varying(255),
    c_form_type character varying(255) NOT NULL,
    c_project_id bigint NOT NULL
);


ALTER TABLE public.t_work_and_travel_order OWNER TO postgres;

--
-- Name: t_workflow; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.t_workflow (
    c_idworkflow bigint NOT NULL,
    c_version integer,
    c_name character varying,
    c_ordered boolean,
    c_idproject bigint,
    c_discriminator character varying
);


ALTER TABLE public.t_workflow OWNER TO postgres;

--
-- Name: workflow_idworkflow_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.workflow_idworkflow_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.workflow_idworkflow_seq OWNER TO postgres;

--
-- Name: workflow_idworkflow_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.workflow_idworkflow_seq OWNED BY public.t_workflow.c_idworkflow;


--
-- Name: c_idaction; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_action ALTER COLUMN c_idaction SET DEFAULT nextval('public.action_idaction_seq'::regclass);


--
-- Name: c_idaddress; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_address ALTER COLUMN c_idaddress SET DEFAULT nextval('public.address_idaddress_seq'::regclass);


--
-- Name: c_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_brief ALTER COLUMN c_id SET DEFAULT nextval('public.t_brief_c_id_seq'::regclass);


--
-- Name: c_idcompany; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company ALTER COLUMN c_idcompany SET DEFAULT nextval('public.company_idcompany_seq'::regclass);


--
-- Name: c_idcontract; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract ALTER COLUMN c_idcontract SET DEFAULT nextval('public.contract_idcontract_seq'::regclass);


--
-- Name: c_idcontractline; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract_line ALTER COLUMN c_idcontractline SET DEFAULT nextval('public.contract_line_idcontractline_seq'::regclass);


--
-- Name: c_iddocument; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_document ALTER COLUMN c_iddocument SET DEFAULT nextval('public.document_iddocument_seq'::regclass);


--
-- Name: c_idfile; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_document_file ALTER COLUMN c_idfile SET DEFAULT nextval('public.document_file_idfile_seq'::regclass);


--
-- Name: c_idequipment; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_equipment ALTER COLUMN c_idequipment SET DEFAULT nextval('public.equipment_idequipment_seq'::regclass);


--
-- Name: c_idfunction; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function ALTER COLUMN c_idfunction SET DEFAULT nextval('public.function_idfunction_seq'::regclass);


--
-- Name: c_idrate; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_rate ALTER COLUMN c_idrate SET DEFAULT nextval('public.function_rate_idrate_seq'::regclass);


--
-- Name: c_idperson; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person ALTER COLUMN c_idperson SET DEFAULT nextval('public.person_idperson_seq'::regclass);


--
-- Name: c_idprodunit; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_produnit ALTER COLUMN c_idprodunit SET DEFAULT nextval('public.produnit_idprodunit_seq'::regclass);


--
-- Name: c_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_program ALTER COLUMN c_id SET DEFAULT nextval('public.t_program_c_id_seq'::regclass);


--
-- Name: c_idrecord; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record ALTER COLUMN c_idrecord SET DEFAULT nextval('public.record_idrecord_seq'::regclass);


--
-- Name: c_idduedate; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_duedate ALTER COLUMN c_idduedate SET DEFAULT nextval('public.record_duedate_idduedate_seq'::regclass);


--
-- Name: c_idfee; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_fee ALTER COLUMN c_idfee SET DEFAULT nextval('public.record_fee_idfee_seq'::regclass);


--
-- Name: c_idline; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line ALTER COLUMN c_idline SET DEFAULT nextval('public.record_line_idline_seq'::regclass);


--
-- Name: c_idresource; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource ALTER COLUMN c_idresource SET DEFAULT nextval('public.resource_idresource_seq'::regclass);


--
-- Name: c_idsetting; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_contractsetting ALTER COLUMN c_idsetting SET DEFAULT nextval('public.person_contractsetting_idsetting_seq'::regclass);


--
-- Name: c_idstafffunction; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_function ALTER COLUMN c_idstafffunction SET DEFAULT nextval('public.resource_function_idresourcefunction_seq'::regclass);


--
-- Name: c_iddocument; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_iddocument ALTER COLUMN c_iddocument SET DEFAULT nextval('public.staff_iddocument_iddocument_seq'::regclass);


--
-- Name: c_idstaffrate; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_rate ALTER COLUMN c_idstaffrate SET DEFAULT nextval('public.staff_rate_idstaffrate_seq'::regclass);


--
-- Name: c_idskill; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_skill ALTER COLUMN c_idskill SET DEFAULT nextval('public.staff_skill_idskill_seq'::regclass);


--
-- Name: c_idstep; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_step ALTER COLUMN c_idstep SET DEFAULT nextval('public.step_idstep_seq'::regclass);


--
-- Name: c_idunavailability; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_unavailability ALTER COLUMN c_idunavailability SET DEFAULT nextval('public.resource_unavailability_idunavailability_seq'::regclass);


--
-- Name: c_idworkflow; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_workflow ALTER COLUMN c_idworkflow SET DEFAULT nextval('public.workflow_idworkflow_seq'::regclass);


--
-- Name: pk_accountancy_number_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_accountancy_number
    ADD CONSTRAINT pk_accountancy_number_id PRIMARY KEY (c_id);


--
-- Name: pk_action; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_action
    ADD CONSTRAINT pk_action PRIMARY KEY (c_idaction);


--
-- Name: pk_address; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_address
    ADD CONSTRAINT pk_address PRIMARY KEY (c_idaddress);


--
-- Name: pk_amortization; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_amortization
    ADD CONSTRAINT pk_amortization PRIMARY KEY (c_id);


--
-- Name: pk_asset; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_asset
    ADD CONSTRAINT pk_asset PRIMARY KEY (c_idasset);


--
-- Name: pk_asset_comment; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_asset_comment
    ADD CONSTRAINT pk_asset_comment PRIMARY KEY (c_asset, c_comment);


--
-- Name: pk_booking_combination_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_booking_combination
    ADD CONSTRAINT pk_booking_combination_id PRIMARY KEY (c_id);


--
-- Name: pk_booking_status_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_booking_status
    ADD CONSTRAINT pk_booking_status_id PRIMARY KEY (c_id);


--
-- Name: pk_broadcast; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_braodcast
    ADD CONSTRAINT pk_broadcast PRIMARY KEY (c_idmedia, c_idchannel, c_date);


--
-- Name: pk_comment; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_comment
    ADD CONSTRAINT pk_comment PRIMARY KEY (c_idcomment);


--
-- Name: pk_company; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company
    ADD CONSTRAINT pk_company PRIMARY KEY (c_idcompany);


--
-- Name: pk_company_accountancy_number_company_accountancy_number; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company_accountancy_number
    ADD CONSTRAINT pk_company_accountancy_number_company_accountancy_number PRIMARY KEY (c_company, c_accountancy_number);


--
-- Name: pk_company_number; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company_number
    ADD CONSTRAINT pk_company_number PRIMARY KEY (c_idcompany, c_name);


--
-- Name: pk_company_profile; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company_profile
    ADD CONSTRAINT pk_company_profile PRIMARY KEY (c_idcompany, c_profile);


--
-- Name: pk_company_vat; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company_vat
    ADD CONSTRAINT pk_company_vat PRIMARY KEY (c_idcompany, c_code);


--
-- Name: pk_contact_record; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contact_record
    ADD CONSTRAINT pk_contact_record PRIMARY KEY (c_idperson, c_idrecord);


--
-- Name: pk_contract; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract
    ADD CONSTRAINT pk_contract PRIMARY KEY (c_idcontract);


--
-- Name: pk_contract_booking; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract_booking
    ADD CONSTRAINT pk_contract_booking PRIMARY KEY (c_idcontractline, c_idbooking);


--
-- Name: pk_contract_line; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract_line
    ADD CONSTRAINT pk_contract_line PRIMARY KEY (c_idcontractline);


--
-- Name: pk_contract_thresholdcouple; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract_thresholdcouple
    ADD CONSTRAINT pk_contract_thresholdcouple PRIMARY KEY (c_thresholdcouple);


--
-- Name: pk_customer_channel; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_customer_channel
    ADD CONSTRAINT pk_customer_channel PRIMARY KEY (c_idcustomer, c_idchannel);


--
-- Name: pk_customer_director; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_customer_director
    ADD CONSTRAINT pk_customer_director PRIMARY KEY (c_idcustomer, c_iddirector);


--
-- Name: pk_customer_produnit; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_customer_produnit
    ADD CONSTRAINT pk_customer_produnit PRIMARY KEY (c_idcustomer, c_idprodunit);


--
-- Name: pk_document; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_document
    ADD CONSTRAINT pk_document PRIMARY KEY (c_iddocument);


--
-- Name: pk_document_book; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_book
    ADD CONSTRAINT pk_document_book PRIMARY KEY (c_idstaffmember, c_iddocument);


--
-- Name: pk_document_file; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_document_file
    ADD CONSTRAINT pk_document_file PRIMARY KEY (c_idfile);


--
-- Name: pk_document_keyword; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_document_keyword
    ADD CONSTRAINT pk_document_keyword PRIMARY KEY (c_iddocument, c_keyword);


--
-- Name: pk_equipment; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_equipment
    ADD CONSTRAINT pk_equipment PRIMARY KEY (c_idequipment);


--
-- Name: pk_event; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_event
    ADD CONSTRAINT pk_event PRIMARY KEY (c_id);


--
-- Name: pk_extra; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_extra
    ADD CONSTRAINT pk_extra PRIMARY KEY (c_id);


--
-- Name: pk_function; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function
    ADD CONSTRAINT pk_function PRIMARY KEY (c_idfunction);


--
-- Name: pk_function_accountancy_number_function_accountancy_number; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_accountancy_number
    ADD CONSTRAINT pk_function_accountancy_number_function_accountancy_number PRIMARY KEY (c_function, c_accountancy_number);


--
-- Name: pk_function_fee; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_fee
    ADD CONSTRAINT pk_function_fee PRIMARY KEY (c_idfunction, c_fee);


--
-- Name: pk_function_rate; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_rate
    ADD CONSTRAINT pk_function_rate PRIMARY KEY (c_idrate);


--
-- Name: pk_function_siblings; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_siblings
    ADD CONSTRAINT pk_function_siblings PRIMARY KEY (c_idmaster, c_idslave, c_relationship);


--
-- Name: pk_inout; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_inout
    ADD CONSTRAINT pk_inout PRIMARY KEY (c_idinout);


--
-- Name: pk_input_asset; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_input_asset
    ADD CONSTRAINT pk_input_asset PRIMARY KEY (c_idproject, c_idasset);


--
-- Name: pk_issue; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_issue
    ADD CONSTRAINT pk_issue PRIMARY KEY (c_id);


--
-- Name: pk_line_extra; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_line_extra
    ADD CONSTRAINT pk_line_extra PRIMARY KEY (c_id);


--
-- Name: pk_line_fee; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_line_fee
    ADD CONSTRAINT pk_line_fee PRIMARY KEY (c_idline, c_fee);


--
-- Name: pk_location; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_location
    ADD CONSTRAINT pk_location PRIMARY KEY (c_idlocation);


--
-- Name: pk_maintenance_contract; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_maintenance_contract
    ADD CONSTRAINT pk_maintenance_contract PRIMARY KEY (c_id);


--
-- Name: pk_media; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_media
    ADD CONSTRAINT pk_media PRIMARY KEY (c_idmedia);


--
-- Name: pk_person; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person
    ADD CONSTRAINT pk_person PRIMARY KEY (c_idperson);


--
-- Name: pk_person_contractsetting; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_contractsetting
    ADD CONSTRAINT pk_person_contractsetting PRIMARY KEY (c_idsetting);


--
-- Name: pk_person_function; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_function
    ADD CONSTRAINT pk_person_function PRIMARY KEY (c_idstafffunction);


--
-- Name: pk_person_language; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_language
    ADD CONSTRAINT pk_person_language PRIMARY KEY (c_idstaffmember, c_language);


--
-- Name: pk_person_number; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person_number
    ADD CONSTRAINT pk_person_number PRIMARY KEY (c_idperson, c_name);


--
-- Name: pk_person_role; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person_role
    ADD CONSTRAINT pk_person_role PRIMARY KEY (c_idperson, c_role);


--
-- Name: pk_person_staffmember; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_specialty
    ADD CONSTRAINT pk_person_staffmember PRIMARY KEY (c_idstaffmember, c_specialty);


--
-- Name: pk_produnit; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_produnit
    ADD CONSTRAINT pk_produnit PRIMARY KEY (c_idprodunit);


--
-- Name: pk_project; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT pk_project PRIMARY KEY (c_idrecord);


--
-- Name: pk_promo; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_promo
    ADD CONSTRAINT pk_promo PRIMARY KEY (c_id);


--
-- Name: pk_record_channel; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_channel
    ADD CONSTRAINT pk_record_channel PRIMARY KEY (c_idrecord, c_idchannel);


--
-- Name: pk_record_currency; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_currency
    ADD CONSTRAINT pk_record_currency PRIMARY KEY (c_idrecord, c_currency);


--
-- Name: pk_record_duedate; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_duedate
    ADD CONSTRAINT pk_record_duedate PRIMARY KEY (c_idduedate);


--
-- Name: pk_record_fee; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_fee
    ADD CONSTRAINT pk_record_fee PRIMARY KEY (c_idfee);


--
-- Name: pk_record_line; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line
    ADD CONSTRAINT pk_record_line PRIMARY KEY (c_idline);


--
-- Name: pk_record_vat; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_vat
    ADD CONSTRAINT pk_record_vat PRIMARY KEY (c_idrecord, c_code);


--
-- Name: pk_resource; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource
    ADD CONSTRAINT pk_resource PRIMARY KEY (c_idresource);


--
-- Name: pk_resource_function; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource_function
    ADD CONSTRAINT pk_resource_function PRIMARY KEY (c_idresourcefunction);


--
-- Name: pk_resource_information; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource_information
    ADD CONSTRAINT pk_resource_information PRIMARY KEY (c_id);


--
-- Name: pk_resource_unavailability; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_unavailability
    ADD CONSTRAINT pk_resource_unavailability PRIMARY KEY (c_idunavailability);


--
-- Name: pk_staff_employee_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_employee_id
    ADD CONSTRAINT pk_staff_employee_id PRIMARY KEY (c_staffemployeeid);


--
-- Name: pk_staff_iddocument; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_iddocument
    ADD CONSTRAINT pk_staff_iddocument PRIMARY KEY (c_iddocument);


--
-- Name: pk_staff_rate; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_rate
    ADD CONSTRAINT pk_staff_rate PRIMARY KEY (c_idstaffrate);


--
-- Name: pk_staff_skill; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_skill
    ADD CONSTRAINT pk_staff_skill PRIMARY KEY (c_idskill);


--
-- Name: pk_step; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_step
    ADD CONSTRAINT pk_step PRIMARY KEY (c_idstep);


--
-- Name: pk_t_brief; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_brief
    ADD CONSTRAINT pk_t_brief PRIMARY KEY (c_id);


--
-- Name: pk_t_program; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_program
    ADD CONSTRAINT pk_t_program PRIMARY KEY (c_id);


--
-- Name: pk_t_record_information; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_informations_status
    ADD CONSTRAINT pk_t_record_information PRIMARY KEY (c_name, c_idrecord);


--
-- Name: pk_t_tvshow_members; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_program_members
    ADD CONSTRAINT pk_t_tvshow_members PRIMARY KEY (c_idrecord, c_idperson);


--
-- Name: pk_teamium_seq_store; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_teamium_seq_store
    ADD CONSTRAINT pk_teamium_seq_store PRIMARY KEY (teamium_seq_id);


--
-- Name: pk_workflow; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_workflow
    ADD CONSTRAINT pk_workflow PRIMARY KEY (c_idworkflow);


--
-- Name: t_attachment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_attachment
    ADD CONSTRAINT t_attachment_pkey PRIMARY KEY (c_idattachment);


--
-- Name: t_booking_order_form_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_booking_order_form
    ADD CONSTRAINT t_booking_order_form_pkey PRIMARY KEY (c_idbookingorderform);


--
-- Name: t_category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_category
    ADD CONSTRAINT t_category_pkey PRIMARY KEY (c_category_id);


--
-- Name: t_channel_format_config_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_channel_format_config
    ADD CONSTRAINT t_channel_format_config_pkey PRIMARY KEY (c_idcompany, c_format_id);


--
-- Name: t_channel_format_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_channel_format
    ADD CONSTRAINT t_channel_format_pkey PRIMARY KEY (c_idchannelformat);


--
-- Name: t_currency_c_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_currency
    ADD CONSTRAINT t_currency_c_code_key UNIQUE (c_code);


--
-- Name: t_currency_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_currency
    ADD CONSTRAINT t_currency_pkey PRIMARY KEY (c_idcurrency);


--
-- Name: t_digital_signature_envelope_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_digital_signature_envelope
    ADD CONSTRAINT t_digital_signature_envelope_pkey PRIMARY KEY (c_digital_signature_envelope_id);


--
-- Name: t_document_gerenation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_document_gerenation
    ADD CONSTRAINT t_document_gerenation_pkey PRIMARY KEY (c_iddocumentgerenation);


--
-- Name: t_document_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_document_type
    ADD CONSTRAINT t_document_type_pkey PRIMARY KEY (c_iddocumenttype);


--
-- Name: t_edition_tempalte_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_edition_tempalte_type
    ADD CONSTRAINT t_edition_tempalte_type_pkey PRIMARY KEY (c_edition_tempalte_type_id);


--
-- Name: t_event_linked; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_linked_event
    ADD CONSTRAINT t_event_linked PRIMARY KEY (c_event_id, c_link_event_id);


--
-- Name: t_expenses_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_expenses_detail
    ADD CONSTRAINT t_expenses_detail_pkey PRIMARY KEY (c_idexpensesdetail);


--
-- Name: t_expenses_item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_expenses_item
    ADD CONSTRAINT t_expenses_item_pkey PRIMARY KEY (c_expenses_item_id);


--
-- Name: t_expenses_report_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_expenses_report
    ADD CONSTRAINT t_expenses_report_pkey PRIMARY KEY (c_idexpensesreport);


--
-- Name: t_format_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_format
    ADD CONSTRAINT t_format_pkey PRIMARY KEY (c_format_id);


--
-- Name: t_function_keys_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_keys
    ADD CONSTRAINT t_function_keys_pkey PRIMARY KEY (c_idfunctionkey);


--
-- Name: t_function_keyword_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_keyword
    ADD CONSTRAINT t_function_keyword_pkey PRIMARY KEY (c_idfunctionkeyword);


--
-- Name: t_group_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_group
    ADD CONSTRAINT t_group_pkey PRIMARY KEY (c_group_id);


--
-- Name: t_holiday_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_holiday
    ADD CONSTRAINT t_holiday_pkey PRIMARY KEY (c_idholiday);


--
-- Name: t_keyword_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_keyword
    ADD CONSTRAINT t_keyword_pkey PRIMARY KEY (c_idkeyword);


--
-- Name: t_labour_rule_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_labour_rule
    ADD CONSTRAINT t_labour_rule_pkey PRIMARY KEY (c_labour_rule_id);


--
-- Name: t_leave_record_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_leave_record
    ADD CONSTRAINT t_leave_record_pkey PRIMARY KEY (c_leave_record_id);


--
-- Name: t_leave_request_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_leave_request
    ADD CONSTRAINT t_leave_request_pkey PRIMARY KEY (c_leave_request_id);


--
-- Name: t_leave_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_leave_type
    ADD CONSTRAINT t_leave_type_pkey PRIMARY KEY (c_leave_type_id);


--
-- Name: t_milestone_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_milestone_type
    ADD CONSTRAINT t_milestone_type_pkey PRIMARY KEY (c_milestone_type_id);


--
-- Name: t_order_form_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_order_form
    ADD CONSTRAINT t_order_form_pkey PRIMARY KEY (c_idorderform);


--
-- Name: t_order_keyword_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_order_keyword
    ADD CONSTRAINT t_order_keyword_pkey PRIMARY KEY (c_idorderkeyword);


--
-- Name: t_personal_document_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_personal_document
    ADD CONSTRAINT t_personal_document_pkey PRIMARY KEY (c_idpersonaldocument);


--
-- Name: t_personal_expenses_report_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_personal_expenses_report
    ADD CONSTRAINT t_personal_expenses_report_pkey PRIMARY KEY (c_personal_expenses_report_id);


--
-- Name: t_reel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_reel
    ADD CONSTRAINT t_reel_pkey PRIMARY KEY (c_idreel);


--
-- Name: t_role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_role
    ADD CONSTRAINT t_role_pkey PRIMARY KEY (role_id);


--
-- Name: t_roster_event_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_roster_event
    ADD CONSTRAINT t_roster_event_pkey PRIMARY KEY (c_idrosterevent);


--
-- Name: t_signature_person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_signature_person
    ADD CONSTRAINT t_signature_person_pkey PRIMARY KEY (c_idsignatureperson);


--
-- Name: t_signature_recipient_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_signature_recipient
    ADD CONSTRAINT t_signature_recipient_pkey PRIMARY KEY (c_signature_recipient_id);


--
-- Name: t_spreadsheet_upload_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_spreadsheet_upload_log
    ADD CONSTRAINT t_spreadsheet_upload_log_pkey PRIMARY KEY (c_idspreadsheetuploadlog);


--
-- Name: t_staff_email_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_email
    ADD CONSTRAINT t_staff_email_pkey PRIMARY KEY (c_idstaffemail);


--
-- Name: t_staff_telephone_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_telephone
    ADD CONSTRAINT t_staff_telephone_pkey PRIMARY KEY (c_idstafftelephone);


--
-- Name: t_t_record_history_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_history
    ADD CONSTRAINT t_t_record_history_pkey PRIMARY KEY (c_idrecordhistory);


--
-- Name: t_user_booking_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_user_booking
    ADD CONSTRAINT t_user_booking_pkey PRIMARY KEY (c_user_booking_id);


--
-- Name: t_user_leave_record_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_user_leave_record
    ADD CONSTRAINT t_user_leave_record_pkey PRIMARY KEY (c_user_leave_record_id);


--
-- Name: t_user_password_recovery_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_user_password_recovery
    ADD CONSTRAINT t_user_password_recovery_pkey PRIMARY KEY (c_iduserpasswordrecovery);


--
-- Name: t_work_and_travel_order_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_work_and_travel_order
    ADD CONSTRAINT t_work_and_travel_order_pkey PRIMARY KEY (c_idworkandtravelorder);


--
-- Name: uk_c_idrecord_c_idperson; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_personal_expenses_report
    ADD CONSTRAINT uk_c_idrecord_c_idperson UNIQUE (c_idrecord, c_idperson);


--
-- Name: uk_c_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_digital_signature_envelope
    ADD CONSTRAINT uk_c_name UNIQUE (c_name);


--
-- Name: uk_c_template_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_edition_tempalte_type
    ADD CONSTRAINT uk_c_template_name UNIQUE (c_template_name);


--
-- Name: uk_category_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_category
    ADD CONSTRAINT uk_category_name UNIQUE (c_name);


--
-- Name: uk_customer_channel; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_customer_channel
    ADD CONSTRAINT uk_customer_channel UNIQUE (c_idchannel, c_idcustomer);


--
-- Name: uk_dc16mcon2eb9ci7fcrar8x5s8; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract_booking
    ADD CONSTRAINT uk_dc16mcon2eb9ci7fcrar8x5s8 UNIQUE (c_idbooking);


--
-- Name: uk_format_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_format
    ADD CONSTRAINT uk_format_name UNIQUE (c_name);


--
-- Name: uk_group_name; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_group
    ADD CONSTRAINT uk_group_name UNIQUE (c_name);


--
-- Name: uk_micgw8mmqkyvnfvtgt9pksce6; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_book
    ADD CONSTRAINT uk_micgw8mmqkyvnfvtgt9pksce6 UNIQUE (c_iddocument);


--
-- Name: uk_reylpxuh1utmb78hv5kb1nb0b; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_customer_produnit
    ADD CONSTRAINT uk_reylpxuh1utmb78hv5kb1nb0b UNIQUE (c_idprodunit);


--
-- Name: uk_ssvoukc1fqv2bgfgi9itig6x3; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_roster_resource
    ADD CONSTRAINT uk_ssvoukc1fqv2bgfgi9itig6x3 UNIQUE (c_idresource);


--
-- Name: uk_unique_key_constraint; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_document_type
    ADD CONSTRAINT uk_unique_key_constraint UNIQUE (c_type);


--
-- Name: fki_booking_combination_booking; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_booking_combination_booking ON public.t_booking_combination USING btree (c_booking_reference);


--
-- Name: fki_company_address; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_company_address ON public.t_company USING btree (c_idaddress);


--
-- Name: fki_company_billingaddress; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_company_billingaddress ON public.t_company USING btree (c_idbillingaddress);


--
-- Name: fki_company_logo; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_company_logo ON public.t_company USING btree (c_idlogo);


--
-- Name: fki_company_person_idmaincontact; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_company_person_idmaincontact ON public.t_company USING btree (c_idmaincontact);


--
-- Name: fki_contract_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_contract_person ON public.t_contract USING btree (c_sale_representative);


--
-- Name: fki_document_book_document; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_document_book_document ON public.t_staff_book USING btree (c_iddocument);


--
-- Name: fki_employee_id_staff; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_employee_id_staff ON public.t_staff_employee_id USING btree (c_staffid);


--
-- Name: fki_equipment_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_equipment_company ON public.t_equipment USING btree (c_idsupplier);


--
-- Name: fki_equipment_equipment_family; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_equipment_equipment_family ON public.t_equipment USING btree (c_idfamily);


--
-- Name: fki_event_combination; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_event_combination ON public.t_event USING btree (c_reference);


--
-- Name: fki_function_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_function_company ON public.t_function USING btree (c_defaultentity);


--
-- Name: fki_function_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_function_name ON public.t_function USING btree (c_idparent);


--
-- Name: fki_function_siblings_slave; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_function_siblings_slave ON public.t_function_siblings USING btree (c_idslave);


--
-- Name: fki_line_extra_line; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_line_extra_line ON public.t_line_extra USING btree (c_idline);


--
-- Name: fki_person_address; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_address ON public.t_person USING btree (c_idaddress);


--
-- Name: fki_person_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_company ON public.t_person USING btree (c_idcompany);


--
-- Name: fki_person_person__agent; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_person__agent ON public.t_person USING btree (c_idagent);


--
-- Name: fki_person_person__idfollower; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_person__idfollower ON public.t_person USING btree (c_contact_idfollower);


--
-- Name: fki_person_person__idsupervisor; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_person__idsupervisor ON public.t_person USING btree (c_idsupervisor);


--
-- Name: fki_person_person_contactsetting; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_person_contactsetting ON public.t_person USING btree (c_idcontractsetting);


--
-- Name: fki_person_photo; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_photo ON public.t_person USING btree (c_idphoto);


--
-- Name: fki_person_skill_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_person_skill_person ON public.t_staff_language USING btree (c_idstaffmember);


--
-- Name: fki_pk_event_booking; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_pk_event_booking ON public.t_event USING btree (c_origin);


--
-- Name: fki_pk_extra_rate; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_pk_extra_rate ON public.t_extra USING btree (c_idrate);


--
-- Name: fki_project_source; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_project_source ON public.t_record USING btree (c_idsource);


--
-- Name: fki_record_saleentity; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_record_saleentity ON public.t_record USING btree (c_identity);


--
-- Name: fki_resource_function_function_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_resource_function_function_name ON public.t_resource_function USING btree (c_idfunction);


--
-- Name: fki_resource_function_resource; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_resource_function_resource ON public.t_resource_function USING btree (c_idresource);


--
-- Name: fki_staff_iddocument_person; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_staff_iddocument_person ON public.t_staff_iddocument USING btree (c_idcontact);


--
-- Name: fki_step_workflow; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_step_workflow ON public.t_step USING btree (c_idworkflow);


--
-- Name: fki_t_tvshow_t_company; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_t_tvshow_t_company ON public.t_program USING btree (c_channel);


--
-- Name: i_action_actiondate; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX i_action_actiondate ON public.t_action USING btree (c_actiondate DESC NULLS LAST);


--
-- Name: i_action_subject; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX i_action_subject ON public.t_action USING btree (c_subject DESC NULLS LAST);


--
-- Name: i_company_discriminator; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX i_company_discriminator ON public.t_company USING btree (c_discriminator NULLS FIRST);


--
-- Name: i_company_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX i_company_name ON public.t_company USING btree (c_name NULLS FIRST);


--
-- Name: i_person_firstname; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX i_person_firstname ON public.t_person USING btree (c_firstname DESC NULLS LAST);


--
-- Name: i_person_function; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX i_person_function ON public.t_person USING btree (c_function DESC NULLS LAST);


--
-- Name: i_person_lastname; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX i_person_lastname ON public.t_person USING btree (c_lastname DESC NULLS LAST);


--
-- Name: index_function_functiongroup; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX index_function_functiongroup ON public.t_function USING btree (c_functiongroup NULLS FIRST);


--
-- Name: fk_1wjo9hmx2dd7oeqkgppxk6qru; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_1wjo9hmx2dd7oeqkgppxk6qru FOREIGN KEY (c_sign_person_hr) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_5ut7mu1dgnc55a2j1425ldgw3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_roster_event
    ADD CONSTRAINT fk_5ut7mu1dgnc55a2j1425ldgw3 FOREIGN KEY (c_idfunction) REFERENCES public.t_function(c_idfunction);


--
-- Name: fk_769llvv0uiidcpjxnxnxuw0i0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_769llvv0uiidcpjxnxnxuw0i0 FOREIGN KEY (c_program) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_89wd8mgis97l7ltgalgx4oiay; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_roster_resource
    ADD CONSTRAINT fk_89wd8mgis97l7ltgalgx4oiay FOREIGN KEY (c_id) REFERENCES public.t_roster_event(c_idrosterevent);


--
-- Name: fk_abvpcdfkfq0dlbrxamt15k7n2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_step
    ADD CONSTRAINT fk_abvpcdfkfq0dlbrxamt15k7n2 FOREIGN KEY (c_idbooking) REFERENCES public.t_record_line(c_idline);


--
-- Name: fk_accountancy_number_accountancy_number_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_accountancy_number
    ADD CONSTRAINT fk_accountancy_number_accountancy_number_id FOREIGN KEY (c_accountancy_number) REFERENCES public.t_accountancy_number(c_id);


--
-- Name: fk_accountancy_number_accountancy_number_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company_accountancy_number
    ADD CONSTRAINT fk_accountancy_number_accountancy_number_id FOREIGN KEY (c_accountancy_number) REFERENCES public.t_accountancy_number(c_id);


--
-- Name: fk_ah6pepjqxexl7w359tdv5itsl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_order_keyword
    ADD CONSTRAINT fk_ah6pepjqxexl7w359tdv5itsl FOREIGN KEY (c_work_and_travel_order_id) REFERENCES public.t_work_and_travel_order(c_idworkandtravelorder);


--
-- Name: fk_ajtb9mufiio2eiqljjvgtgoyt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource
    ADD CONSTRAINT fk_ajtb9mufiio2eiqljjvgtgoyt FOREIGN KEY (c_idinout) REFERENCES public.t_inout(c_idinout);


--
-- Name: fk_amortization_equipment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_amortization
    ADD CONSTRAINT fk_amortization_equipment FOREIGN KEY (c_equipment) REFERENCES public.t_equipment(c_idequipment) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_asset_comment_asset; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_asset_comment
    ADD CONSTRAINT fk_asset_comment_asset FOREIGN KEY (c_asset) REFERENCES public.t_asset(c_idasset) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_asset_comment_comment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_asset_comment
    ADD CONSTRAINT fk_asset_comment_comment FOREIGN KEY (c_comment) REFERENCES public.t_comment(c_idcomment) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_asset_document; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_asset
    ADD CONSTRAINT fk_asset_document FOREIGN KEY (c_thumbnail) REFERENCES public.t_document(c_iddocument) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_attachment_c_idcompany; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_attachment
    ADD CONSTRAINT fk_attachment_c_idcompany FOREIGN KEY (c_idcompany) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_attachment_c_idperson; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_attachment
    ADD CONSTRAINT fk_attachment_c_idperson FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_attachment_c_idrecord; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_attachment
    ADD CONSTRAINT fk_attachment_c_idrecord FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_b8m5j8ntnpj33hdsevd3gblug; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_keyword
    ADD CONSTRAINT fk_b8m5j8ntnpj33hdsevd3gblug FOREIGN KEY (c_booking_order_form_id) REFERENCES public.t_booking_order_form(c_idbookingorderform);


--
-- Name: fk_blny5fmv9pr7pr4abn7cfk28f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_blny5fmv9pr7pr4abn7cfk28f FOREIGN KEY (c_sign_person_parttime) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_booking_bookingcombination; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_booking_bookingcombination
    ADD CONSTRAINT fk_booking_bookingcombination FOREIGN KEY (c_booking_combination) REFERENCES public.t_booking_combination(c_id) ON DELETE CASCADE;


--
-- Name: fk_booking_bookingcombination_event; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_booking_bookingcombination
    ADD CONSTRAINT fk_booking_bookingcombination_event FOREIGN KEY (c_id) REFERENCES public.t_event(c_id) ON DELETE CASCADE;


--
-- Name: fk_booking_combination_project_record_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_booking_combination
    ADD CONSTRAINT fk_booking_combination_project_record_id FOREIGN KEY (c_project) REFERENCES public.t_record(c_idrecord) ON DELETE CASCADE;


--
-- Name: fk_booking_combination_reference; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_booking_combination
    ADD CONSTRAINT fk_booking_combination_reference FOREIGN KEY (c_booking_reference) REFERENCES public.t_event(c_id) ON DELETE CASCADE;


--
-- Name: fk_booking_resource; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line
    ADD CONSTRAINT fk_booking_resource FOREIGN KEY (c_booking_idresource) REFERENCES public.t_resource(c_idresource) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_broadcast_channel; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_braodcast
    ADD CONSTRAINT fk_broadcast_channel FOREIGN KEY (c_idchannel) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_broadcast_media; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_braodcast
    ADD CONSTRAINT fk_broadcast_media FOREIGN KEY (c_idmedia) REFERENCES public.t_media(c_idmedia);


--
-- Name: fk_c_group_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line
    ADD CONSTRAINT fk_c_group_id FOREIGN KEY (c_group_id) REFERENCES public.t_group(c_group_id);


--
-- Name: fk_c_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_roster_resource
    ADD CONSTRAINT fk_c_id FOREIGN KEY (c_id) REFERENCES public.t_roster_event(c_idrosterevent);


--
-- Name: fk_c_idresource; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_roster_resource
    ADD CONSTRAINT fk_c_idresource FOREIGN KEY (c_idresource) REFERENCES public.t_resource(c_idresource);


--
-- Name: fk_c_media_order_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line
    ADD CONSTRAINT fk_c_media_order_id FOREIGN KEY (c_media_order_id) REFERENCES public.t_work_and_travel_order(c_idworkandtravelorder);


--
-- Name: fk_c_project_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_work_and_travel_order
    ADD CONSTRAINT fk_c_project_id FOREIGN KEY (c_project_id) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_c_travel_order_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line
    ADD CONSTRAINT fk_c_travel_order_id FOREIGN KEY (c_travel_order_id) REFERENCES public.t_work_and_travel_order(c_idworkandtravelorder);


--
-- Name: fk_c_work_order_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line
    ADD CONSTRAINT fk_c_work_order_id FOREIGN KEY (c_work_order_id) REFERENCES public.t_work_and_travel_order(c_idworkandtravelorder);


--
-- Name: fk_comment_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_comment
    ADD CONSTRAINT fk_comment_person FOREIGN KEY (c_author) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_company_address; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company
    ADD CONSTRAINT fk_company_address FOREIGN KEY (c_idaddress) REFERENCES public.t_address(c_idaddress) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_company_billingaddress; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company
    ADD CONSTRAINT fk_company_billingaddress FOREIGN KEY (c_idbillingaddress) REFERENCES public.t_address(c_idaddress) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_company_company_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company_accountancy_number
    ADD CONSTRAINT fk_company_company_id FOREIGN KEY (c_company) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_company_logo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company
    ADD CONSTRAINT fk_company_logo FOREIGN KEY (c_idlogo) REFERENCES public.t_document(c_iddocument) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_company_number_company; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company_number
    ADD CONSTRAINT fk_company_number_company FOREIGN KEY (c_idcompany) REFERENCES public.t_company(c_idcompany) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_company_person__follower; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company
    ADD CONSTRAINT fk_company_person__follower FOREIGN KEY (c_idfollower) REFERENCES public.t_person(c_idperson) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_company_person_idmaincontact; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company
    ADD CONSTRAINT fk_company_person_idmaincontact FOREIGN KEY (c_idmaincontact) REFERENCES public.t_person(c_idperson) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_company_profile_company; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company_profile
    ADD CONSTRAINT fk_company_profile_company FOREIGN KEY (c_idcompany) REFERENCES public.t_company(c_idcompany) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_company_vat_company; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_company_vat
    ADD CONSTRAINT fk_company_vat_company FOREIGN KEY (c_idcompany) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_contact_record_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contact_record
    ADD CONSTRAINT fk_contact_record_person FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson) ON DELETE CASCADE;


--
-- Name: fk_contact_record_record; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contact_record
    ADD CONSTRAINT fk_contact_record_record FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord) ON DELETE CASCADE;


--
-- Name: fk_contract_booking_contract_line; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract_booking
    ADD CONSTRAINT fk_contract_booking_contract_line FOREIGN KEY (c_idcontractline) REFERENCES public.t_contract_line(c_idcontractline) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_contract_booking_record_line; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract_booking
    ADD CONSTRAINT fk_contract_booking_record_line FOREIGN KEY (c_idbooking) REFERENCES public.t_record_line(c_idline) ON UPDATE CASCADE;


--
-- Name: fk_contract_function; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract
    ADD CONSTRAINT fk_contract_function FOREIGN KEY (c_idfunction) REFERENCES public.t_function(c_idfunction);


--
-- Name: fk_contract_line_contract; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract_line
    ADD CONSTRAINT fk_contract_line_contract FOREIGN KEY (c_idcontract) REFERENCES public.t_contract(c_idcontract) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_contract_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract
    ADD CONSTRAINT fk_contract_person FOREIGN KEY (c_sale_representative) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_contract_saleentity; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract
    ADD CONSTRAINT fk_contract_saleentity FOREIGN KEY (c_idsaleentity) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_contract_staffmember; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract
    ADD CONSTRAINT fk_contract_staffmember FOREIGN KEY (c_idstaffmember) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_contract_thresholdcouple_contract; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract_thresholdcouple
    ADD CONSTRAINT fk_contract_thresholdcouple_contract FOREIGN KEY (c_idcontract) REFERENCES public.t_staff_contractsetting(c_idsetting) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_customer_channel_channel; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_customer_channel
    ADD CONSTRAINT fk_customer_channel_channel FOREIGN KEY (c_idchannel) REFERENCES public.t_company(c_idcompany) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_customer_channel_customer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_customer_channel
    ADD CONSTRAINT fk_customer_channel_customer FOREIGN KEY (c_idcustomer) REFERENCES public.t_company(c_idcompany) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_customer_director_customer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_customer_director
    ADD CONSTRAINT fk_customer_director_customer FOREIGN KEY (c_idcustomer) REFERENCES public.t_company(c_idcompany) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_customer_director_director; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_customer_director
    ADD CONSTRAINT fk_customer_director_director FOREIGN KEY (c_iddirector) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_customer_produnit_customer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_customer_produnit
    ADD CONSTRAINT fk_customer_produnit_customer FOREIGN KEY (c_idcustomer) REFERENCES public.t_company(c_idcompany) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_customer_produnit_produnit; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_customer_produnit
    ADD CONSTRAINT fk_customer_produnit_produnit FOREIGN KEY (c_idprodunit) REFERENCES public.t_produnit(c_idprodunit) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_document_book_document; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_book
    ADD CONSTRAINT fk_document_book_document FOREIGN KEY (c_iddocument) REFERENCES public.t_document(c_iddocument) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_document_book_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_book
    ADD CONSTRAINT fk_document_book_person FOREIGN KEY (c_idstaffmember) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_document_file_document; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_document_file
    ADD CONSTRAINT fk_document_file_document FOREIGN KEY (c_iddocument) REFERENCES public.t_document(c_iddocument) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_document_keyword_document; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_document_keyword
    ADD CONSTRAINT fk_document_keyword_document FOREIGN KEY (c_iddocument) REFERENCES public.t_document(c_iddocument) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_edition_template_type_c_id_template_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_digital_signature_envelope
    ADD CONSTRAINT fk_edition_template_type_c_id_template_type FOREIGN KEY (c_id_template_type) REFERENCES public.t_edition_tempalte_type(c_edition_tempalte_type_id);


--
-- Name: fk_employee_id_saleentity; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_employee_id
    ADD CONSTRAINT fk_employee_id_saleentity FOREIGN KEY (c_saleentity) REFERENCES public.t_company(c_idcompany) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_employee_id_staff; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_employee_id
    ADD CONSTRAINT fk_employee_id_staff FOREIGN KEY (c_staffid) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_equipment_company; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_equipment
    ADD CONSTRAINT fk_equipment_company FOREIGN KEY (c_idsupplier) REFERENCES public.t_company(c_idcompany) ON DELETE SET NULL;


--
-- Name: fk_equipment_company_supplier; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_equipment
    ADD CONSTRAINT fk_equipment_company_supplier FOREIGN KEY (c_supplier) REFERENCES public.t_company(c_idcompany) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_equipment_location; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_equipment
    ADD CONSTRAINT fk_equipment_location FOREIGN KEY (c_idlocation) REFERENCES public.t_location(c_idlocation) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_equipment_milestone_equipment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_equipment_milestone
    ADD CONSTRAINT fk_equipment_milestone_equipment FOREIGN KEY (equipment_id) REFERENCES public.t_equipment(c_idequipment);


--
-- Name: fk_equipment_packingequipment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_equipment
    ADD CONSTRAINT fk_equipment_packingequipment FOREIGN KEY (c_idpackingequipment) REFERENCES public.t_equipment(c_idequipment) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_equipment_photo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_equipment
    ADD CONSTRAINT fk_equipment_photo FOREIGN KEY (c_idphoto) REFERENCES public.t_document(c_iddocument) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_equipment_saleentity; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_equipment
    ADD CONSTRAINT fk_equipment_saleentity FOREIGN KEY (c_idsaleentity) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_event_booking; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_event
    ADD CONSTRAINT fk_event_booking FOREIGN KEY (c_origin) REFERENCES public.t_record_line(c_idline) ON DELETE CASCADE;


--
-- Name: fk_event_combination; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_event
    ADD CONSTRAINT fk_event_combination FOREIGN KEY (c_reference) REFERENCES public.t_booking_combination(c_id);


--
-- Name: fk_event_unavailability; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_event
    ADD CONSTRAINT fk_event_unavailability FOREIGN KEY (c_unavailability) REFERENCES public.t_unavailability(c_idunavailability) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_evg5ad7wxakjiwx9fyp8kqcad; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract_thresholdcouple
    ADD CONSTRAINT fk_evg5ad7wxakjiwx9fyp8kqcad FOREIGN KEY (c_idcontract) REFERENCES public.t_contract(c_idcontract);


--
-- Name: fk_fb6sw2val64dboyxye9fcxsn9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_order_keyword
    ADD CONSTRAINT fk_fb6sw2val64dboyxye9fcxsn9 FOREIGN KEY (c_order_form_id) REFERENCES public.t_order_form(c_idorderform);


--
-- Name: fk_function_c_idfunctionkeyword; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function
    ADD CONSTRAINT fk_function_c_idfunctionkeyword FOREIGN KEY (c_idfunctionkeyword) REFERENCES public.t_function_keyword(c_idfunctionkeyword);


--
-- Name: fk_function_company; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function
    ADD CONSTRAINT fk_function_company FOREIGN KEY (c_defaultentity) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_function_fee_function; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_fee
    ADD CONSTRAINT fk_function_fee_function FOREIGN KEY (c_idfunction) REFERENCES public.t_function(c_idfunction) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_function_function_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_accountancy_number
    ADD CONSTRAINT fk_function_function_id FOREIGN KEY (c_function) REFERENCES public.t_function(c_idfunction);


--
-- Name: fk_function_name; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function
    ADD CONSTRAINT fk_function_name FOREIGN KEY (c_idparent) REFERENCES public.t_function(c_idfunction) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_function_rate_customer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_rate
    ADD CONSTRAINT fk_function_rate_customer FOREIGN KEY (c_idcompany) REFERENCES public.t_company(c_idcompany) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_function_rate_entity; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_rate
    ADD CONSTRAINT fk_function_rate_entity FOREIGN KEY (c_identity) REFERENCES public.t_company(c_idcompany) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_function_rate_function; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_rate
    ADD CONSTRAINT fk_function_rate_function FOREIGN KEY (c_idfunction) REFERENCES public.t_function(c_idfunction) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_function_siblings_master; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_siblings
    ADD CONSTRAINT fk_function_siblings_master FOREIGN KEY (c_idmaster) REFERENCES public.t_function(c_idfunction) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_function_siblings_slave; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_siblings
    ADD CONSTRAINT fk_function_siblings_slave FOREIGN KEY (c_idslave) REFERENCES public.t_function(c_idfunction) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_function_supplier; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function
    ADD CONSTRAINT fk_function_supplier FOREIGN KEY (c_idsupplier) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_gpeq7ma7g17w7e4e09udvxujo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_order_form
    ADD CONSTRAINT fk_gpeq7ma7g17w7e4e09udvxujo FOREIGN KEY (c_work_and_travel_order_id) REFERENCES public.t_work_and_travel_order(c_idworkandtravelorder);


--
-- Name: fk_hk5hc1xd9ire6ghnqfpjgoxyt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_asset
    ADD CONSTRAINT fk_hk5hc1xd9ire6ghnqfpjgoxyt FOREIGN KEY (c_idorigin) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_idfollower; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_action
    ADD CONSTRAINT fk_idfollower FOREIGN KEY (c_idfollower) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_idtargetperson; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_action
    ADD CONSTRAINT fk_idtargetperson FOREIGN KEY (c_idtargetperson) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_inout_media; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_inout
    ADD CONSTRAINT fk_inout_media FOREIGN KEY (c_media) REFERENCES public.t_media(c_idmedia) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_inout_project; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_inout
    ADD CONSTRAINT fk_inout_project FOREIGN KEY (c_project) REFERENCES public.t_record(c_idrecord) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_inout_resource; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_inout
    ADD CONSTRAINT fk_inout_resource FOREIGN KEY (c_resource) REFERENCES public.t_resource(c_idresource) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_issue_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_issue
    ADD CONSTRAINT fk_issue_person FOREIGN KEY (c_reporter) REFERENCES public.t_person(c_idperson) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_issue_record_line; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_issue
    ADD CONSTRAINT fk_issue_record_line FOREIGN KEY (c_booking) REFERENCES public.t_record_line(c_idline) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_jcinvklbpmbftrd6d2813wmvn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_contract
    ADD CONSTRAINT fk_jcinvklbpmbftrd6d2813wmvn FOREIGN KEY (c_linkedrecord) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_k1nbhd25bsfyb8yo3ujns8jq4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_informations_status
    ADD CONSTRAINT fk_k1nbhd25bsfyb8yo3ujns8jq4 FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_kbould2xhd1on5unwr899hs5a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_expenses_detail
    ADD CONSTRAINT fk_kbould2xhd1on5unwr899hs5a FOREIGN KEY (c_expenses_report_id) REFERENCES public.t_expenses_report(c_idexpensesreport);


--
-- Name: fk_kvmfrq5u1tbri4do0oempu5lg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_brief
    ADD CONSTRAINT fk_kvmfrq5u1tbri4do0oempu5lg FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_line_extra_line; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_line_extra
    ADD CONSTRAINT fk_line_extra_line FOREIGN KEY (c_idline) REFERENCES public.t_record_line(c_idline) ON DELETE CASCADE;


--
-- Name: fk_line_fee_record_line; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_line_fee
    ADD CONSTRAINT fk_line_fee_record_line FOREIGN KEY (c_idline) REFERENCES public.t_record_line(c_idline) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_location_customer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_location
    ADD CONSTRAINT fk_location_customer FOREIGN KEY (c_idcustomer) REFERENCES public.t_company(c_idcompany) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_maintenance_contract_company; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_maintenance_contract
    ADD CONSTRAINT fk_maintenance_contract_company FOREIGN KEY (c_supplier) REFERENCES public.t_company(c_idcompany) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_maintenance_contract_equipment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_maintenance_contract
    ADD CONSTRAINT fk_maintenance_contract_equipment FOREIGN KEY (c_equipment) REFERENCES public.t_equipment(c_idequipment) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_media_asset; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_media
    ADD CONSTRAINT fk_media_asset FOREIGN KEY (c_asset) REFERENCES public.t_asset(c_idasset);


--
-- Name: fk_media_document; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_media
    ADD CONSTRAINT fk_media_document FOREIGN KEY (c_file) REFERENCES public.t_document(c_iddocument) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_o8cnpnhs5voj6marrrtnpkvw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line
    ADD CONSTRAINT fk_o8cnpnhs5voj6marrrtnpkvw FOREIGN KEY (c_saleentity) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_person_address; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person
    ADD CONSTRAINT fk_person_address FOREIGN KEY (c_idaddress) REFERENCES public.t_address(c_idaddress);


--
-- Name: fk_person_company; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person
    ADD CONSTRAINT fk_person_company FOREIGN KEY (c_idcompany) REFERENCES public.t_company(c_idcompany) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_person_function_function; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_function
    ADD CONSTRAINT fk_person_function_function FOREIGN KEY (c_idfunction) REFERENCES public.t_function(c_idfunction) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_person_language_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_language
    ADD CONSTRAINT fk_person_language_person FOREIGN KEY (c_idstaffmember) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_person_number_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person_number
    ADD CONSTRAINT fk_person_number_person FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_person_person__agent; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person
    ADD CONSTRAINT fk_person_person__agent FOREIGN KEY (c_idagent) REFERENCES public.t_person(c_idperson) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_person_person__idfollower; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person
    ADD CONSTRAINT fk_person_person__idfollower FOREIGN KEY (c_contact_idfollower) REFERENCES public.t_person(c_idperson) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_person_person__idsupervisor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person
    ADD CONSTRAINT fk_person_person__idsupervisor FOREIGN KEY (c_idsupervisor) REFERENCES public.t_person(c_idperson) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_person_photo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person
    ADD CONSTRAINT fk_person_photo FOREIGN KEY (c_idphoto) REFERENCES public.t_document(c_iddocument) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_person_role_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person_role
    ADD CONSTRAINT fk_person_role_person FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_person_skill_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_skill
    ADD CONSTRAINT fk_person_skill_person FOREIGN KEY (c_idstaffmember) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_person_specialty_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_specialty
    ADD CONSTRAINT fk_person_specialty_person FOREIGN KEY (c_idstaffmember) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_personal_document_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_personal_document
    ADD CONSTRAINT fk_personal_document_person FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_personal_document_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_personal_document
    ADD CONSTRAINT fk_personal_document_type FOREIGN KEY (c_iddocumenttype) REFERENCES public.t_document_type(c_iddocumenttype);


--
-- Name: fk_program_produnit; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_program
    ADD CONSTRAINT fk_program_produnit FOREIGN KEY (c_id) REFERENCES public.t_produnit(c_idprodunit);


--
-- Name: fk_project_agency; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_project_agency FOREIGN KEY (c_project_idagency) REFERENCES public.t_company(c_idcompany) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_project_channel; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_project_channel FOREIGN KEY (c_project_idchannel) REFERENCES public.t_company(c_idcompany) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_project_director; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_project_director FOREIGN KEY (c_project_iddirector) REFERENCES public.t_person(c_idperson) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_project_produnit; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_project_produnit FOREIGN KEY (c_project_idprodunit) REFERENCES public.t_produnit(c_idprodunit) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_project_source; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_project_source FOREIGN KEY (c_idsource) REFERENCES public.t_record(c_idrecord) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_ra07w1ts0l2lbyxv3yrr9y9e8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_promo
    ADD CONSTRAINT fk_ra07w1ts0l2lbyxv3yrr9y9e8 FOREIGN KEY (c_quotation) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_record_channel; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_record_channel FOREIGN KEY (c_channel) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_record_channel_channel; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_channel
    ADD CONSTRAINT fk_record_channel_channel FOREIGN KEY (c_idchannel) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_record_channel_record; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_channel
    ADD CONSTRAINT fk_record_channel_record FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_record_company; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_record_company FOREIGN KEY (c_idcompany) REFERENCES public.t_company(c_idcompany) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_record_contact; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_record_contact FOREIGN KEY (c_idcontact) REFERENCES public.t_person(c_idperson) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_record_currency_record; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_currency
    ADD CONSTRAINT fk_record_currency_record FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_record_document; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_record_document FOREIGN KEY (c_idlogo) REFERENCES public.t_document(c_iddocument);


--
-- Name: fk_record_duedate_record; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_duedate
    ADD CONSTRAINT fk_record_duedate_record FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_record_fee_record; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_fee
    ADD CONSTRAINT fk_record_fee_record FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_record_follower; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_record_follower FOREIGN KEY (c_idfollower) REFERENCES public.t_person(c_idperson) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_record_line_function; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line
    ADD CONSTRAINT fk_record_line_function FOREIGN KEY (c_idfunction) REFERENCES public.t_function(c_idfunction) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_record_line_origin_record_line_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line
    ADD CONSTRAINT fk_record_line_origin_record_line_id FOREIGN KEY (c_origin) REFERENCES public.t_record_line(c_idline) ON DELETE SET NULL;


--
-- Name: fk_record_line_rate; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line
    ADD CONSTRAINT fk_record_line_rate FOREIGN KEY (c_rate) REFERENCES public.t_function_rate(c_idrate) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_record_line_record; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line
    ADD CONSTRAINT fk_record_line_record FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_record_line_wrapped_line; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_line
    ADD CONSTRAINT fk_record_line_wrapped_line FOREIGN KEY (c_idwrappedline) REFERENCES public.t_record_line(c_idline) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_record_produnit; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_record_produnit FOREIGN KEY (c_produnit) REFERENCES public.t_produnit(c_idprodunit);


--
-- Name: fk_record_saleentity; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_record_saleentity FOREIGN KEY (c_identity) REFERENCES public.t_company(c_idcompany) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: fk_record_vat_record; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_vat
    ADD CONSTRAINT fk_record_vat_record FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_reel_person_staff; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_reel
    ADD CONSTRAINT fk_reel_person_staff FOREIGN KEY (c_idstaff) REFERENCES public.t_person(c_idperson) ON DELETE CASCADE;


--
-- Name: fk_resource_equipment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource
    ADD CONSTRAINT fk_resource_equipment FOREIGN KEY (c_idequipment) REFERENCES public.t_equipment(c_idequipment) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_resource_function; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource
    ADD CONSTRAINT fk_resource_function FOREIGN KEY (c_idfunction) REFERENCES public.t_function(c_idfunction) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_resource_function_function; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource_function
    ADD CONSTRAINT fk_resource_function_function FOREIGN KEY (c_idfunction) REFERENCES public.t_function(c_idfunction) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_resource_function_resource; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource_function
    ADD CONSTRAINT fk_resource_function_resource FOREIGN KEY (c_idresource) REFERENCES public.t_resource(c_idresource) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_resource_information_equipment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource_information
    ADD CONSTRAINT fk_resource_information_equipment FOREIGN KEY (c_equipment) REFERENCES public.t_equipment(c_idequipment) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_resource_information_resource; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource_information
    ADD CONSTRAINT fk_resource_information_resource FOREIGN KEY (c_resource) REFERENCES public.t_resource(c_idresource) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_resource_order; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource
    ADD CONSTRAINT fk_resource_order FOREIGN KEY (c_idorder) REFERENCES public.t_record(c_idrecord) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_resource_resource_parent; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource
    ADD CONSTRAINT fk_resource_resource_parent FOREIGN KEY (c_parent) REFERENCES public.t_resource(c_idresource) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_resource_staff; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_resource
    ADD CONSTRAINT fk_resource_staff FOREIGN KEY (c_idstaffmember) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_resource_unavailability_resource; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_unavailability
    ADD CONSTRAINT fk_resource_unavailability_resource FOREIGN KEY (c_idresource) REFERENCES public.t_resource(c_idresource) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_ssvoukc1fqv2bgfgi9itig6x3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_roster_resource
    ADD CONSTRAINT fk_ssvoukc1fqv2bgfgi9itig6x3 FOREIGN KEY (c_idresource) REFERENCES public.t_resource(c_idresource);


--
-- Name: fk_staff_function_staff; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_function
    ADD CONSTRAINT fk_staff_function_staff FOREIGN KEY (c_idstaffmember) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_staff_iddocument_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_iddocument
    ADD CONSTRAINT fk_staff_iddocument_person FOREIGN KEY (c_idcontact) REFERENCES public.t_person(c_idperson) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_staff_milestone_staff; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_milestone
    ADD CONSTRAINT fk_staff_milestone_staff FOREIGN KEY (c_idstaff) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_staff_rate_resource_function; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_rate
    ADD CONSTRAINT fk_staff_rate_resource_function FOREIGN KEY (c_idresourcefunction) REFERENCES public.t_resource_function(c_idresourcefunction) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_staff_staff_contractsetting; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person
    ADD CONSTRAINT fk_staff_staff_contractsetting FOREIGN KEY (c_idcontractsetting) REFERENCES public.t_staff_contractsetting(c_idsetting) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_step_function; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_step
    ADD CONSTRAINT fk_step_function FOREIGN KEY (c_idfunction) REFERENCES public.t_function(c_idfunction) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_step_workflow; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_step
    ADD CONSTRAINT fk_step_workflow FOREIGN KEY (c_idworkflow) REFERENCES public.t_workflow(c_idworkflow) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fk_t_attachment_c_attachment_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_leave_request
    ADD CONSTRAINT fk_t_attachment_c_attachment_id FOREIGN KEY (c_attachment_id) REFERENCES public.t_attachment(c_idattachment);


--
-- Name: fk_t_attachment_c_idequipment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_attachment
    ADD CONSTRAINT fk_t_attachment_c_idequipment FOREIGN KEY (c_idequipment) REFERENCES public.t_equipment(c_idequipment);


--
-- Name: fk_t_channel_format_c_format_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_channel_format
    ADD CONSTRAINT fk_t_channel_format_c_format_id FOREIGN KEY (c_format_id) REFERENCES public.t_format(c_format_id);


--
-- Name: fk_t_channel_format_c_idchannel; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_channel_format
    ADD CONSTRAINT fk_t_channel_format_c_idchannel FOREIGN KEY (c_idchannel) REFERENCES public.t_company(c_idcompany) ON DELETE CASCADE;


--
-- Name: fk_t_channel_format_c_idcompany; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_channel_format
    ADD CONSTRAINT fk_t_channel_format_c_idcompany FOREIGN KEY (c_idcompany) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_t_channel_format_c_idrecord; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_channel_format
    ADD CONSTRAINT fk_t_channel_format_c_idrecord FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_t_company_c_idcompany; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_channel_format_config
    ADD CONSTRAINT fk_t_company_c_idcompany FOREIGN KEY (c_idcompany) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_t_digital_signature_envelope_envelope_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_envelope_signature_recipient
    ADD CONSTRAINT fk_t_digital_signature_envelope_envelope_id FOREIGN KEY (c_digital_signature_envelope_id) REFERENCES public.t_digital_signature_envelope(c_digital_signature_envelope_id);


--
-- Name: fk_t_document_gerenation_c_created_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_document_gerenation
    ADD CONSTRAINT fk_t_document_gerenation_c_created_by FOREIGN KEY (c_created_by) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_document_gerenation_c_updated_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_document_gerenation
    ADD CONSTRAINT fk_t_document_gerenation_c_updated_by FOREIGN KEY (c_updated_by) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_equipment_c_created_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_equipment
    ADD CONSTRAINT fk_t_equipment_c_created_by FOREIGN KEY (c_updated_by) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_equipment_c_updated_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_equipment
    ADD CONSTRAINT fk_t_equipment_c_updated_by FOREIGN KEY (c_created_by) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_equipment_milestone_c_milestone_type_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_equipment_milestone
    ADD CONSTRAINT fk_t_equipment_milestone_c_milestone_type_id FOREIGN KEY (c_milestone_type_id) REFERENCES public.t_milestone_type(c_milestone_type_id);


--
-- Name: fk_t_event_linked_c_event_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_linked_event
    ADD CONSTRAINT fk_t_event_linked_c_event_id FOREIGN KEY (c_event_id) REFERENCES public.t_event(c_id) ON DELETE CASCADE;


--
-- Name: fk_t_event_linked_c_link_event_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_linked_event
    ADD CONSTRAINT fk_t_event_linked_c_link_event_id FOREIGN KEY (c_link_event_id) REFERENCES public.t_event(c_id) ON DELETE CASCADE;


--
-- Name: fk_t_format_t_format; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_channel_format_config
    ADD CONSTRAINT fk_t_format_t_format FOREIGN KEY (c_format_id) REFERENCES public.t_format(c_format_id);


--
-- Name: fk_t_function_keys_c_idfunctionkeyword; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_function_keys
    ADD CONSTRAINT fk_t_function_keys_c_idfunctionkeyword FOREIGN KEY (c_idfunctionkeyword) REFERENCES public.t_function_keyword(c_idfunctionkeyword);


--
-- Name: fk_t_group_c_group_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_group_member
    ADD CONSTRAINT fk_t_group_c_group_id FOREIGN KEY (c_group_id) REFERENCES public.t_group(c_group_id);


--
-- Name: fk_t_leave_record_c_leave_record_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_leave_request
    ADD CONSTRAINT fk_t_leave_record_c_leave_record_id FOREIGN KEY (c_leave_record_id) REFERENCES public.t_leave_record(c_leave_record_id);


--
-- Name: fk_t_leave_type_c_leave_type_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_leave_record
    ADD CONSTRAINT fk_t_leave_type_c_leave_type_id FOREIGN KEY (c_leave_type_id) REFERENCES public.t_leave_type(c_leave_type_id);


--
-- Name: fk_t_person_c_idperson; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_group_member
    ADD CONSTRAINT fk_t_person_c_idperson FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_person_c_idperson; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_personal_expenses_report
    ADD CONSTRAINT fk_t_person_c_idperson FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_person_c_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_user_booking
    ADD CONSTRAINT fk_t_person_c_user FOREIGN KEY (c_user) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_personal_expenses_report_c_personal_expenses_report_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_expenses_item
    ADD CONSTRAINT fk_t_personal_expenses_report_c_personal_expenses_report_id FOREIGN KEY (c_personal_expenses_report_id) REFERENCES public.t_personal_expenses_report(c_personal_expenses_report_id);


--
-- Name: fk_t_personc_idrecipient; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_signature_recipient
    ADD CONSTRAINT fk_t_personc_idrecipient FOREIGN KEY (c_idrecipient) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_production_members_t_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_program_members
    ADD CONSTRAINT fk_t_production_members_t_person FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_production_members_t_record; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_program_members
    ADD CONSTRAINT fk_t_production_members_t_record FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_t_record_c_category_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT fk_t_record_c_category_id FOREIGN KEY (c_category_id) REFERENCES public.t_category(c_category_id);


--
-- Name: fk_t_record_c_idrecord; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_personal_expenses_report
    ADD CONSTRAINT fk_t_record_c_idrecord FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_t_record_contact_c_idperson; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_contact
    ADD CONSTRAINT fk_t_record_contact_c_idperson FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_record_contact_c_idrecord; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_contact
    ADD CONSTRAINT fk_t_record_contact_c_idrecord FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_t_record_duedate_c_milestone_type_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_duedate
    ADD CONSTRAINT fk_t_record_duedate_c_milestone_type_id FOREIGN KEY (c_milestone_type_id) REFERENCES public.t_milestone_type(c_milestone_type_id);


--
-- Name: fk_t_record_history_c_idperson; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_history
    ADD CONSTRAINT fk_t_record_history_c_idperson FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_record_history_c_idrecord; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record_history
    ADD CONSTRAINT fk_t_record_history_c_idrecord FOREIGN KEY (c_idrecord) REFERENCES public.t_record(c_idrecord);


--
-- Name: fk_t_signature_person_c_idsignature_person; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_signature_person
    ADD CONSTRAINT fk_t_signature_person_c_idsignature_person FOREIGN KEY (c_idsignature_person) REFERENCES public.t_document_gerenation(c_iddocumentgerenation);


--
-- Name: fk_t_signature_person_c_signed_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_signature_person
    ADD CONSTRAINT fk_t_signature_person_c_signed_by FOREIGN KEY (c_signed_by) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_signature_recipient_recipient_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_envelope_signature_recipient
    ADD CONSTRAINT fk_t_signature_recipient_recipient_id FOREIGN KEY (c_signature_recipient_id) REFERENCES public.t_signature_recipient(c_signature_recipient_id);


--
-- Name: fk_t_staff_email_c_idperson; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_email
    ADD CONSTRAINT fk_t_staff_email_c_idperson FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_staff_group_c_idparent; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_group
    ADD CONSTRAINT fk_t_staff_group_c_idparent FOREIGN KEY (c_idparent) REFERENCES public.t_resource(c_idresource);


--
-- Name: fk_t_staff_group_c_idresource; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_group
    ADD CONSTRAINT fk_t_staff_group_c_idresource FOREIGN KEY (c_idresource) REFERENCES public.t_resource(c_idresource);


--
-- Name: fk_t_staff_milestone_c_milestone_type_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_milestone
    ADD CONSTRAINT fk_t_staff_milestone_c_milestone_type_id FOREIGN KEY (c_milestone_type_id) REFERENCES public.t_milestone_type(c_milestone_type_id);


--
-- Name: fk_t_staff_telephone_c_idperson; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_staff_telephone
    ADD CONSTRAINT fk_t_staff_telephone_c_idperson FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_t_tvshow_t_company; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_program
    ADD CONSTRAINT fk_t_tvshow_t_company FOREIGN KEY (c_channel) REFERENCES public.t_company(c_idcompany);


--
-- Name: fk_t_user_leave_record_c_user_leave_record_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person
    ADD CONSTRAINT fk_t_user_leave_record_c_user_leave_record_id FOREIGN KEY (c_user_leave_record_id) REFERENCES public.t_user_leave_record(c_user_leave_record_id);


--
-- Name: fk_t_user_leave_record_leave_record_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_leave_record
    ADD CONSTRAINT fk_t_user_leave_record_leave_record_id FOREIGN KEY (leave_record_id) REFERENCES public.t_user_leave_record(c_user_leave_record_id);


--
-- Name: fk_t_user_leave_record_leave_request_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_leave_request
    ADD CONSTRAINT fk_t_user_leave_record_leave_request_id FOREIGN KEY (leave_request_id) REFERENCES public.t_user_leave_record(c_user_leave_record_id);


--
-- Name: fk_t_user_password_recovery_c_idperson; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_user_password_recovery
    ADD CONSTRAINT fk_t_user_password_recovery_c_idperson FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson);


--
-- Name: fk_unavailability_unavailability_reference; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_unavailability
    ADD CONSTRAINT fk_unavailability_unavailability_reference FOREIGN KEY (c_reference) REFERENCES public.t_unavailability(c_idunavailability) ON UPDATE SET NULL ON DELETE SET NULL;


--
-- Name: fk_workflow_project; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_workflow
    ADD CONSTRAINT fk_workflow_project FOREIGN KEY (c_idproject) REFERENCES public.t_record(c_idrecord) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: fka9c8iiy6ut0gnx491fqx4pxam; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_user_role
    ADD CONSTRAINT fka9c8iiy6ut0gnx491fqx4pxam FOREIGN KEY (role_id) REFERENCES public.t_role(role_id);


--
-- Name: fky3hra2e4ctvrg69ki1cv38kx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_user_role
    ADD CONSTRAINT fky3hra2e4ctvrg69ki1cv38kx FOREIGN KEY (c_idperson) REFERENCES public.t_person(c_idperson);


--
-- Name: pk_extra_rate; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_extra
    ADD CONSTRAINT pk_extra_rate FOREIGN KEY (c_idrate) REFERENCES public.t_function_rate(c_idrate) ON DELETE CASCADE;


--
-- Name: pk_input_asset_asset; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_input_asset
    ADD CONSTRAINT pk_input_asset_asset FOREIGN KEY (c_idasset) REFERENCES public.t_asset(c_idasset);


--
-- Name: pk_input_asset_project; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_input_asset
    ADD CONSTRAINT pk_input_asset_project FOREIGN KEY (c_idproject) REFERENCES public.t_record(c_idrecord);


--
-- Name: t_event_c_resource_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_event
    ADD CONSTRAINT t_event_c_resource_fkey FOREIGN KEY (c_resource) REFERENCES public.t_resource(c_idresource) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: t_holiday_c_labour_holiday; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_holiday
    ADD CONSTRAINT t_holiday_c_labour_holiday FOREIGN KEY (c_labour_holiday) REFERENCES public.t_labour_rule(c_labour_rule_id);


--
-- Name: t_labour_staff_member_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_labour_staffmember
    ADD CONSTRAINT t_labour_staff_member_id FOREIGN KEY (staff_member_id) REFERENCES public.t_person(c_idperson);


--
-- Name: t_person_c_labour_rule; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_person
    ADD CONSTRAINT t_person_c_labour_rule FOREIGN KEY (c_labour_rule) REFERENCES public.t_labour_rule(c_labour_rule_id);


--
-- Name: t_record_c_contract; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.t_record
    ADD CONSTRAINT t_record_c_contract FOREIGN KEY (c_contract) REFERENCES public.t_contract(c_idcontract);
    
    
REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

