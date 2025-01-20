-- Main client information table.
CREATE TABLE IF NOT EXISTS users (
    user_id serial NOT NULL PRIMARY KEY,
    username character varying(16) NOT NULL,
    salt character(32) NOT NULL,  -- password salt, 32 bytes in hex
    password_hash character(64) NOT NULL,  -- SHA-256 in hex
    email character varying(100) NOT NULL,
    balance numeric(10,2) NOT NULL DEFAULT 10.00
);

-- Stripe deposits and withdrawals.
CREATE TABLE IF NOT EXISTS deposits_withdrawals (
    transaction_id serial NOT NULL PRIMARY KEY,
    user_id integer NOT NULL REFERENCES users,
    date timestamp without time zone NOT NULL,
    amount numeric(10,2) NOT NULL
);

-- Horse Racing game bets and replays.
CREATE TABLE IF NOT EXISTS horses (
    horses_id serial NOT NULL PRIMARY KEY,
    user_id integer NOT NULL REFERENCES users,
    date timestamp without time zone NOT NULL,
    bet numeric(10,2) NOT NULL,
    guess integer NOT NULL,
    times integer[4] NOT NULL,
    bezier_curves double precision[16] NOT NULL
);

-- Roulette game bets and replays.
CREATE TABLE IF NOT EXISTS roulette (
    roulette_id serial NOT NULL PRIMARY KEY,
    user_id integer NOT NULL REFERENCES users,
    date timestamp without time zone NOT NULL,
    bet numeric(10,2) NOT NULL,
    guess character NOT NULL,
    result integer NOT NULL
);
