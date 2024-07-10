-- Database: check

-- DROP DATABASE IF EXISTS "check";

CREATE DATABASE "check"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- Table: public.discount_card

-- DROP TABLE IF EXISTS public.discount_card;

CREATE TABLE IF NOT EXISTS public.discount_card
(
    id BIGSERIAL  NOT NULL ,
    number integer NOT NULL,
    amount smallint NOT NULL,
    CONSTRAINT discount_card_unique PRIMARY KEY (id),
    CONSTRAINT discount_card_number_key UNIQUE (number),
    CONSTRAINT discount_card_amount_check CHECK (amount >= 0 AND amount <= 100) NOT VALID,
    CONSTRAINT discount_card_number_check CHECK (number >= 1000 AND number <= 9999) NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.discount_card
    OWNER to postgres;


-- Table: public.product

-- DROP TABLE IF EXISTS public.product;

CREATE TABLE IF NOT EXISTS public.product
(
    id BIGSERIAL  NOT NULL ,
    description character varying(50) COLLATE pg_catalog."default" NOT NULL,
    price numeric(10,2) NOT NULL,
    quantity_in_stock integer NOT NULL,
    wholesale_product boolean NOT NULL,
    CONSTRAINT product_pkey PRIMARY KEY (id),
    CONSTRAINT product_description_unique UNIQUE (description),
    CONSTRAINT product_price_check CHECK (price >= 0::numeric) NOT VALID,
    CONSTRAINT product_quantity_in_stock_check CHECK (quantity_in_stock >= 0) NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.product
    OWNER to postgres;


INSERT INTO public.discount_card ( number, amount)
VALUES
    ( 1111, 3),
    ( 2222, 3),
    ( 3333, 4),
    ( 4444, 5);


INSERT INTO public.product ( description, price, quantity_in_stock, wholesale_product)
VALUES
    ( 'Milk', 1.07, 10, true),
    ( 'Cream 400g', 2.71, 20, true),
    ( 'Yogurt 400g', 2.10, 7, true),
    ( 'Packed potatoes 1kg', 1.47, 30, false),
    ( 'Packed cabbage 1kg', 1.19, 15, false),
    ( 'Packed tomatoes 350g', 1.60, 50, false),
    ( 'Packed apples 1kg', 2.78, 18, false),
    ( 'Packed oranges 1kg', 3.20, 12, false),
    ( 'Packed bananas 1kg', 1.10, 25, true),
    ( 'Packed beef fillet 1kg', 12.80, 7, false),
    ( 'Packed pork fillet 1kg', 8.52, 14, false),
    ( 'Packed chicken breasts 1kg', 10.75, 18, false),
    ( 'Baguette 360g', 1.30, 10, true),
    ( 'Drinking water 1.5l', 0.80, 100, false),
    ( 'Olive oil 500ml', 5.30, 16, false),
    ( 'Sunflower oil 1l', 1.20, 12, false),
    ( 'Chocolate Ritter sport 100g', 1.10, 50, true),
    ( 'Paulaner 0.5l', 1.10, 100, false),
    ( 'Whiskey Jim Beam 1l', 13.99, 30, false),
    ( 'Whiskey Jack Daniels 1l', 17.19, 20, false);



