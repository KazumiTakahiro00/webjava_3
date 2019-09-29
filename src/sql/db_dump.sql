--
-- PostgreSQL database dump
--

-- Dumped from database version 10.10
-- Dumped by pg_dump version 10.10

-- Started on 2019-09-29 09:46:55

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12924)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2808 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 196 (class 1259 OID 16394)
-- Name: items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.items (
    item_id integer NOT NULL,
    item_name text NOT NULL,
    price integer NOT NULL
);


ALTER TABLE public.items OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 16402)
-- Name: players; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.players (
    no integer NOT NULL,
    num integer,
    name text NOT NULL,
    profession text NOT NULL,
    method text
);


ALTER TABLE public.players OWNER TO postgres;

--
-- TOC entry 2799 (class 0 OID 16394)
-- Dependencies: 196
-- Data for Name: items; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.items (item_id, item_name, price) FROM stdin;
2222	ええええ	111111
11111	ああああ	10003
33333	ええええ	40004
\.


--
-- TOC entry 2800 (class 0 OID 16402)
-- Dependencies: 197
-- Data for Name: players; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.players (no, num, name, profession, method) FROM stdin;
1	0	aaaa	戦士	\N
2	0	bbbb	魔法使い	\N
\.


--
-- TOC entry 2675 (class 2606 OID 16401)
-- Name: items items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.items
    ADD CONSTRAINT items_pkey PRIMARY KEY (item_id);


--
-- TOC entry 2677 (class 2606 OID 16409)
-- Name: players players_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.players
    ADD CONSTRAINT players_pkey PRIMARY KEY (no);


-- Completed on 2019-09-29 09:46:55

--
-- PostgreSQL database dump complete
--

