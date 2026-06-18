--
-- PostgreSQL database dump
--

\restrict MW8GGaf14idSULg9gP3T2WAen1WS7H4abNhcQw5ySqop4qOsePOXkxgYb8ijwQr

-- Dumped from database version 18.1 (Debian 18.1-1.pgdg13+2)
-- Dumped by pg_dump version 18.0

-- Started on 2026-05-16 19:52:56 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 242 (class 1259 OID 17716)
-- Name: accounting_entries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.accounting_entries (
    id bigint NOT NULL,
    accountable_name character varying(80),
    accountable_id bigint,
    accountable_member_id bigint,
    transaction_type character varying(10) NOT NULL,
    transaction_date date NOT NULL,
    transaction_amount numeric(12,2) NOT NULL,
    accounting_date date NOT NULL,
    description character varying(250),
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL
);


ALTER TABLE public.accounting_entries OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 17715)
-- Name: accounting_entries_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.accounting_entries ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.accounting_entries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 226 (class 1259 OID 17609)
-- Name: amount_domain_values; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.amount_domain_values (
    id bigint NOT NULL,
    code character varying(20) NOT NULL,
    amount numeric(12,2) NOT NULL,
    valid_from date NOT NULL,
    valid_to date NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL
);


ALTER TABLE public.amount_domain_values OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17608)
-- Name: amount_domain_values_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.amount_domain_values ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.amount_domain_values_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 17564)
-- Name: associations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.associations (
    id bigint NOT NULL,
    name character varying(80) NOT NULL,
    description character varying(4000),
    street character varying(80) NOT NULL,
    number character varying(20) NOT NULL,
    zip character varying(10) NOT NULL,
    city character varying(80) NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL
);


ALTER TABLE public.associations OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 17563)
-- Name: associations_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.associations ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.associations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 228 (class 1259 OID 17623)
-- Name: events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.events (
    id bigint NOT NULL,
    date date,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL
);


ALTER TABLE public.events OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 17622)
-- Name: events_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.events ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.events_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 236 (class 1259 OID 17680)
-- Name: member_offers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.member_offers (
    id bigint NOT NULL,
    is_activ boolean NOT NULL,
    offer_id bigint,
    member_id bigint,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL
);


ALTER TABLE public.member_offers OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 17679)
-- Name: member_offers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.member_offers ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.member_offers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 232 (class 1259 OID 17646)
-- Name: members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.members (
    id bigint NOT NULL,
    member_nmbr integer NOT NULL,
    salutation character varying(20),
    title character varying(20),
    institution character varying(80),
    first_name character varying(80) NOT NULL,
    last_name character varying(80) NOT NULL,
    birthdate date NOT NULL,
    email character varying(80),
    password character varying(250),
    joining_date date NOT NULL,
    resignation_date date,
    street character varying(80),
    number character varying(20),
    zip character varying(10),
    city character varying(80),
    direct_debit_authorization boolean DEFAULT false NOT NULL,
    is_imported_member boolean DEFAULT true NOT NULL,
    accumulated_hours integer,
    role_id bigint,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL,
    phone_number character varying(30)
);


ALTER TABLE public.members OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 17645)
-- Name: members_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.members ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.members_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 234 (class 1259 OID 17667)
-- Name: membership_fees; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.membership_fees (
    id bigint NOT NULL,
    for_year date NOT NULL,
    transaction_date date NOT NULL,
    amount numeric(12,2) NOT NULL,
    member_id bigint,
    accounted_by_id bigint,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL
);


ALTER TABLE public.membership_fees OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 17666)
-- Name: membership_fees_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.membership_fees ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.membership_fees_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 222 (class 1259 OID 17581)
-- Name: offers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offers (
    id bigint NOT NULL,
    code character varying(10),
    description character varying(250),
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL
);


ALTER TABLE public.offers OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 17580)
-- Name: offers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.offers ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.offers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 230 (class 1259 OID 17633)
-- Name: registration_codes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.registration_codes (
    id bigint NOT NULL,
    code character varying(10) NOT NULL,
    email character varying(80) NOT NULL,
    expires_at timestamp without time zone NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL,
    failed_attempts integer
);


ALTER TABLE public.registration_codes OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17632)
-- Name: registration_codes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.registration_codes ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.registration_codes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 224 (class 1259 OID 17591)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    is_board_member boolean NOT NULL,
    is_treasurer boolean NOT NULL,
    is_secretary boolean NOT NULL,
    is_auditor boolean NOT NULL,
    is_time_keeper boolean NOT NULL,
    is_admin boolean NOT NULL,
    is_miscellaneous boolean NOT NULL,
    role_name character varying(80) NOT NULL,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 17590)
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.roles ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 245 (class 1259 OID 17825)
-- Name: spring_session; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.spring_session (
    primary_id character(36) NOT NULL,
    session_id character(36) NOT NULL,
    creation_time bigint NOT NULL,
    last_access_time bigint NOT NULL,
    max_inactive_interval integer NOT NULL,
    expiry_time bigint NOT NULL,
    principal_name character varying(100)
);


ALTER TABLE public.spring_session OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 17839)
-- Name: spring_session_attributes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.spring_session_attributes (
    session_primary_id character(36) NOT NULL,
    attribute_name character varying(200) NOT NULL,
    attribute_bytes bytea NOT NULL
);


ALTER TABLE public.spring_session_attributes OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 17855)
-- Name: text_contents; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.text_contents (
    id bigint NOT NULL,
    content_code character varying(20),
    md_text character varying(40000),
    html_text character varying(40000),
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL
);


ALTER TABLE public.text_contents OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 17854)
-- Name: text_contents_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.text_contents ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.text_contents_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 240 (class 1259 OID 17703)
-- Name: time_cheques; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.time_cheques (
    id bigint NOT NULL,
    hours smallint NOT NULL,
    transaction_date date NOT NULL,
    amount numeric(12,2) NOT NULL,
    assigned_to_id bigint,
    accounted_by_id bigint,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL
);


ALTER TABLE public.time_cheques OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 17702)
-- Name: time_cheques_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.time_cheques ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.time_cheques_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 238 (class 1259 OID 17691)
-- Name: time_transfers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.time_transfers (
    id bigint NOT NULL,
    date_of_service date NOT NULL,
    hours smallint NOT NULL,
    note character varying(250),
    offer_id bigint,
    from_member_id bigint,
    to_member_id bigint,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL
);


ALTER TABLE public.time_transfers OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 17690)
-- Name: time_transfers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.time_transfers ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.time_transfers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 244 (class 1259 OID 17730)
-- Name: transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transactions (
    id bigint NOT NULL,
    transaction_type character varying(10) NOT NULL,
    transaction_date date NOT NULL,
    amount numeric(12,2) NOT NULL,
    description character varying(250) NOT NULL,
    accounted_by_id bigint,
    created_at timestamp with time zone DEFAULT now() NOT NULL,
    created_by_id bigint,
    updated_at timestamp with time zone,
    updated_by_id bigint,
    version integer NOT NULL
);


ALTER TABLE public.transactions OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 17729)
-- Name: transactions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.transactions ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.transactions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 3411 (class 2606 OID 17728)
-- Name: accounting_entries accounting_entries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounting_entries
    ADD CONSTRAINT accounting_entries_pkey PRIMARY KEY (id);


--
-- TOC entry 3387 (class 2606 OID 17621)
-- Name: amount_domain_values amount_domain_values_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.amount_domain_values
    ADD CONSTRAINT amount_domain_values_pkey PRIMARY KEY (id);


--
-- TOC entry 3379 (class 2606 OID 17579)
-- Name: associations associations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.associations
    ADD CONSTRAINT associations_pkey PRIMARY KEY (id);


--
-- TOC entry 3389 (class 2606 OID 17631)
-- Name: events events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_pkey PRIMARY KEY (id);


--
-- TOC entry 3403 (class 2606 OID 17689)
-- Name: member_offers member_offers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.member_offers
    ADD CONSTRAINT member_offers_pkey PRIMARY KEY (id);


--
-- TOC entry 3393 (class 2606 OID 17665)
-- Name: members members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT members_pkey PRIMARY KEY (id);


--
-- TOC entry 3399 (class 2606 OID 17678)
-- Name: membership_fees membership_fees_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.membership_fees
    ADD CONSTRAINT membership_fees_pkey PRIMARY KEY (id);


--
-- TOC entry 3383 (class 2606 OID 17589)
-- Name: offers offers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offers
    ADD CONSTRAINT offers_pkey PRIMARY KEY (id);


--
-- TOC entry 3391 (class 2606 OID 17644)
-- Name: registration_codes registration_codes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.registration_codes
    ADD CONSTRAINT registration_codes_pkey PRIMARY KEY (id);


--
-- TOC entry 3385 (class 2606 OID 17607)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- TOC entry 3422 (class 2606 OID 17848)
-- Name: spring_session_attributes spring_session_attributes_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.spring_session_attributes
    ADD CONSTRAINT spring_session_attributes_pk PRIMARY KEY (session_primary_id, attribute_name);


--
-- TOC entry 3420 (class 2606 OID 17835)
-- Name: spring_session spring_session_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.spring_session
    ADD CONSTRAINT spring_session_pk PRIMARY KEY (primary_id);


--
-- TOC entry 3424 (class 2606 OID 17865)
-- Name: text_contents text_contents_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.text_contents
    ADD CONSTRAINT text_contents_pkey PRIMARY KEY (id);


--
-- TOC entry 3409 (class 2606 OID 17714)
-- Name: time_cheques time_cheques_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_cheques
    ADD CONSTRAINT time_cheques_pkey PRIMARY KEY (id);


--
-- TOC entry 3407 (class 2606 OID 17701)
-- Name: time_transfers time_transfers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_transfers
    ADD CONSTRAINT time_transfers_pkey PRIMARY KEY (id);


--
-- TOC entry 3415 (class 2606 OID 17742)
-- Name: transactions transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (id);


--
-- TOC entry 3413 (class 2606 OID 17815)
-- Name: accounting_entries uc_class_id_accounting_entries; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounting_entries
    ADD CONSTRAINT uc_class_id_accounting_entries UNIQUE (accountable_name, accountable_id);


--
-- TOC entry 3395 (class 2606 OID 17809)
-- Name: members uc_email_members; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT uc_email_members UNIQUE (email);


--
-- TOC entry 3381 (class 2606 OID 17805)
-- Name: associations uc_name_associations; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.associations
    ADD CONSTRAINT uc_name_associations UNIQUE (name);


--
-- TOC entry 3397 (class 2606 OID 17807)
-- Name: members uc_nmbr_members; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT uc_nmbr_members UNIQUE (member_nmbr);


--
-- TOC entry 3405 (class 2606 OID 17813)
-- Name: member_offers uc_offer_member_member_offers; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.member_offers
    ADD CONSTRAINT uc_offer_member_member_offers UNIQUE (offer_id, member_id);


--
-- TOC entry 3401 (class 2606 OID 17811)
-- Name: membership_fees uc_year_member_membership_fees; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.membership_fees
    ADD CONSTRAINT uc_year_member_membership_fees UNIQUE (for_year, member_id);


--
-- TOC entry 3416 (class 1259 OID 17836)
-- Name: spring_session_ix1; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX spring_session_ix1 ON public.spring_session USING btree (session_id);


--
-- TOC entry 3417 (class 1259 OID 17837)
-- Name: spring_session_ix2; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX spring_session_ix2 ON public.spring_session USING btree (expiry_time);


--
-- TOC entry 3418 (class 1259 OID 17838)
-- Name: spring_session_ix3; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX spring_session_ix3 ON public.spring_session USING btree (principal_name);


--
-- TOC entry 3435 (class 2606 OID 17794)
-- Name: accounting_entries fk_accounting_entries_accountable_member_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounting_entries
    ADD CONSTRAINT fk_accounting_entries_accountable_member_id FOREIGN KEY (accountable_member_id) REFERENCES public.members(id);


--
-- TOC entry 3428 (class 2606 OID 17764)
-- Name: member_offers fk_member_offers_member_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.member_offers
    ADD CONSTRAINT fk_member_offers_member_id FOREIGN KEY (member_id) REFERENCES public.members(id);


--
-- TOC entry 3429 (class 2606 OID 17759)
-- Name: member_offers fk_member_offers_offer_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.member_offers
    ADD CONSTRAINT fk_member_offers_offer_id FOREIGN KEY (offer_id) REFERENCES public.offers(id);


--
-- TOC entry 3425 (class 2606 OID 17744)
-- Name: members fk_members_role_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.members
    ADD CONSTRAINT fk_members_role_id FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- TOC entry 3426 (class 2606 OID 17754)
-- Name: membership_fees fk_membership_fees_accounted_by_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.membership_fees
    ADD CONSTRAINT fk_membership_fees_accounted_by_id FOREIGN KEY (accounted_by_id) REFERENCES public.accounting_entries(id);


--
-- TOC entry 3427 (class 2606 OID 17749)
-- Name: membership_fees fk_membership_fees_member_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.membership_fees
    ADD CONSTRAINT fk_membership_fees_member_id FOREIGN KEY (member_id) REFERENCES public.members(id);


--
-- TOC entry 3433 (class 2606 OID 17789)
-- Name: time_cheques fk_time_cheques_accounted_by_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_cheques
    ADD CONSTRAINT fk_time_cheques_accounted_by_id FOREIGN KEY (accounted_by_id) REFERENCES public.accounting_entries(id);


--
-- TOC entry 3434 (class 2606 OID 17784)
-- Name: time_cheques fk_time_cheques_assigned_to_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_cheques
    ADD CONSTRAINT fk_time_cheques_assigned_to_id FOREIGN KEY (assigned_to_id) REFERENCES public.members(id);


--
-- TOC entry 3430 (class 2606 OID 17774)
-- Name: time_transfers fk_time_transfers_from_member_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_transfers
    ADD CONSTRAINT fk_time_transfers_from_member_id FOREIGN KEY (from_member_id) REFERENCES public.members(id);


--
-- TOC entry 3431 (class 2606 OID 17769)
-- Name: time_transfers fk_time_transfers_offer_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_transfers
    ADD CONSTRAINT fk_time_transfers_offer_id FOREIGN KEY (offer_id) REFERENCES public.offers(id);


--
-- TOC entry 3432 (class 2606 OID 17779)
-- Name: time_transfers fk_time_transfers_to_member_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.time_transfers
    ADD CONSTRAINT fk_time_transfers_to_member_id FOREIGN KEY (to_member_id) REFERENCES public.members(id);


--
-- TOC entry 3436 (class 2606 OID 17799)
-- Name: transactions fk_transactions_accounted_by_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT fk_transactions_accounted_by_id FOREIGN KEY (accounted_by_id) REFERENCES public.accounting_entries(id);


--
-- TOC entry 3437 (class 2606 OID 17849)
-- Name: spring_session_attributes spring_session_attributes_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.spring_session_attributes
    ADD CONSTRAINT spring_session_attributes_fk FOREIGN KEY (session_primary_id) REFERENCES public.spring_session(primary_id) ON DELETE CASCADE;


-- Completed on 2026-05-16 19:52:56 CEST

--
-- PostgreSQL database dump complete
--

\unrestrict MW8GGaf14idSULg9gP3T2WAen1WS7H4abNhcQw5ySqop4qOsePOXkxgYb8ijwQr

