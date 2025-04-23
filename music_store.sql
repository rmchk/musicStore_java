--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

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
-- Name: cart_items; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cart_items (
    id bigint NOT NULL,
    cart_id bigint NOT NULL,
    product_id bigint NOT NULL,
    quantity integer NOT NULL
);


--
-- Name: cart_items_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.cart_items ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.cart_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: cart_products; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cart_products (
    cart_id bigint NOT NULL,
    product_id bigint NOT NULL
);


--
-- Name: carts; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.carts (
    id bigint NOT NULL,
    user_id bigint
);


--
-- Name: carts_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.carts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: carts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.carts_id_seq OWNED BY public.carts.id;


--
-- Name: order_products; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.order_products (
    order_id bigint NOT NULL,
    product_id bigint NOT NULL
);


--
-- Name: orders; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.orders (
    total_price double precision NOT NULL,
    id bigint NOT NULL,
    order_date timestamp(6) without time zone,
    user_id bigint
);


--
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- Name: product_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.product_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: products; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.products (
    in_stock boolean NOT NULL,
    price double precision NOT NULL,
    id bigint NOT NULL,
    category character varying(255),
    description text,
    image character varying(255),
    name character varying(255),
    brand character varying(255)
);


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    email character varying(255),
    password character varying(255),
    role character varying(255),
    username character varying(255)
);


--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: carts id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.carts ALTER COLUMN id SET DEFAULT nextval('public.carts_id_seq'::regclass);


--
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: cart_items; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.cart_items (id, cart_id, product_id, quantity) FROM stdin;
\.


--
-- Data for Name: cart_products; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.cart_products (cart_id, product_id) FROM stdin;
2	6
2	4
2	1
2	5
3	6
3	4
1	3
1	8
1	6
1	6
1	6
1	6
1	1
1	5
1	4
1	4
1	6
\.


--
-- Data for Name: carts; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.carts (id, user_id) FROM stdin;
1	1
2	2
3	3
4	4
\.


--
-- Data for Name: order_products; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.order_products (order_id, product_id) FROM stdin;
1	1
1	4
2	6
2	4
2	1
2	5
3	4
4	6
5	6
6	6
6	4
7	6
7	8
7	3
8	6
8	5
9	6
\.


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.orders (total_price, id, order_date, user_id) FROM stdin;
34990	1	2025-04-19 17:54:57.142837	1
616880	2	2025-04-19 18:27:45.836858	2
15000	3	2025-04-19 18:28:16.590845	1
559900	4	2025-04-19 18:34:47.602434	1
559900	5	2025-04-19 21:08:43.889206	1
574900	6	2025-04-19 23:44:25.843936	3
619900	7	2025-04-19 23:56:35.344493	1
581890	8	2025-04-20 14:32:30.595553	1
559900	9	2025-04-21 21:51:52.642809	1
\.


--
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.products (in_stock, price, id, category, description, image, name, brand) FROM stdin;
t	30000	3	Плееры	Портативный плеер Fiio	https://i.ebayimg.com/images/g/CpYAAOSwVFhfx-wf/s-l1600.jpg	Музыкальный плеер Apple	Apple
t	30000	8	студийные мониторы	Студийный мониторы для небольшого помещения	https://avatars.mds.yandex.net/i?id=9e25538bcc966ecf67bcca6f8a82c14cce51fdc8-10263526-images-thumbs&n=13	Студийные мониторы Yamaha hs5	Yamaha
t	559900	6	synth	мощный синтезатор	https://avatars.mds.yandex.net/get-mpic/5186016/2a000001923397b7eede0e7630a48e9523c0/orig	Arturia Polybrute	Arturia
t	19990	1	Наушники	Полноразмерные закрытые мониторные наушники Audio-Technica ATH-M50x возглавляют обновленную серию M, которая пользуется стабильной популярностью, как у профессионалов, так и среди любителей качественного звучания.	https://doctorhead.ru/upload/resize_cache/iblock/bbd/e7izf3kkv6wd00oa9n5qkamryy2pccxn/688_688_1/audio_technica_ath_m50x.jpg	Audio-Technica ATH-M50x Black	Audio-Technica
t	21990	5	Наушники	Легендарная серия DT 770/880/990 уходит корнями еще в 1981 год, когда была выпущена модель DT 880 — первые динамические наушники с характеристиками электростатических.	https://doctorhead.ru/upload/resize_cache/iblock/4ed/h6dcenytwok67mscimrdo0ql2iwecyuu/688_688_1/1C_10904_Beyerdynamic_DT_770_PRO_000.jpg	Beyerdynamic DT 770 PRO	Beyerdynamic
t	15000	4	Колонки	Беспроводная колонка JBL	https://avatars.mds.yandex.net/i?id=a1032e1cab91b4b1a7d866cb8ce30107723a9771-4570570-images-thumbs&n=13	Колонка JBL	JBL
t	70990	11	Synth	Arturia MiniBrute 2 Noir – это обновленная модель популярного аналогового синтезатора MiniBrute в специальной темной расцветке, обладающая рядом необходимых функций, которых не хватало в первой версии, а также с добавлением совершенно новых возможностей, которые по достоинству оценят профессиональные музыканты.	https://doctorhead.ru/upload/resize_cache/iblock/a03/71v4kstle7tw8i3tz9s05q61azv6jofe/688_688_1/arturia_minibrute2_1.jpg	Arturia MiniBrute 2 Noir	Arturia
t	46990	12	synth	Синтезатор, не похожий ни на один другой, MicroFreak - это своеобразный, исключительный инструмент, вознаграждающий любознательного музыканта, сочетающий в себе волновые и цифровые осцилляторы с аналоговыми фильтрами.	https://doctorhead.ru/upload/dev2fun.imagecompress/webp/resize_cache/iblock/1f1/688_688_1/arturia_microfreak.webp	Arturia MicroFreak	Arturia
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.users (id, email, password, role, username) FROM stdin;
2	user@gmail.com	$2a$10$qO/PwDgg1EPXRJHZF1If7uRxfNG.7KvwTHzpxri8kLV5KpwKKViSO	USER	user
3	roman@gmail.com	$2a$10$w8N9TPE3qdCeGC6IM1p7zOV/USqFefYjEKvqmqWsxC28U.iqxqYsy	ADMIN	romchik
1	admin@gmail.com	$2a$10$L5wPYNs4Cgfikhe8Bckz4OFzPRHRR3nmpZOy7wFsY6ioDOLKcQCzG	ADMIN	admin
4	robot@gmail.com	$2a$10$zMX5pb/SbkSctcYrAA5cIO5g9jS9/hrLYEfiy5vCK/TklLiQbc8E6	USER	Robot
\.


--
-- Name: cart_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.cart_items_id_seq', 17, true);


--
-- Name: carts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.carts_id_seq', 4, true);


--
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.orders_id_seq', 9, true);


--
-- Name: product_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.product_seq', 12, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.users_id_seq', 4, true);


--
-- Name: cart_items cart_items_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT cart_items_pkey PRIMARY KEY (id);


--
-- Name: carts carts_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT carts_pkey PRIMARY KEY (id);


--
-- Name: carts carts_user_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT carts_user_id_key UNIQUE (user_id);


--
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: orders fk32ql8ubntj5uh44ph9659tiih; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk32ql8ubntj5uh44ph9659tiih FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: cart_items fk_cart; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT fk_cart FOREIGN KEY (cart_id) REFERENCES public.carts(id) ON DELETE CASCADE;


--
-- Name: cart_items fk_product; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cart_items
    ADD CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES public.products(id) ON DELETE CASCADE;


--
-- Name: order_products fkawxpt1ns1sr7al76nvjkv21of; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_products
    ADD CONSTRAINT fkawxpt1ns1sr7al76nvjkv21of FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- Name: carts fkb5o626f86h46m4s7ms6ginnop; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.carts
    ADD CONSTRAINT fkb5o626f86h46m4s7ms6ginnop FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: cart_products fkbilp3o9irlsvmbot68kfpthom; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cart_products
    ADD CONSTRAINT fkbilp3o9irlsvmbot68kfpthom FOREIGN KEY (cart_id) REFERENCES public.carts(id);


--
-- Name: cart_products fkdayy17at10up1qqwlri9cocb3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cart_products
    ADD CONSTRAINT fkdayy17at10up1qqwlri9cocb3 FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- Name: order_products fkdxjduvg7991r4qja26fsckxv8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.order_products
    ADD CONSTRAINT fkdxjduvg7991r4qja26fsckxv8 FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- PostgreSQL database dump complete
--

