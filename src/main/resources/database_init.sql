-- Main client information table.
CREATE TABLE IF NOT EXISTS users (
    user_id integer NOT NULL PRIMARY KEY,
    username character varying(16),
    salt character(16),  -- password salt
    password_hash character(64),  -- SHA-256 in hex
    email character varying(100),
    balance numeric(10,2)
);

-- Stripe deposits and withdrawals.
CREATE TABLE IF NOT EXISTS deposits_withdrawals (
    transaction_id integer NOT NULL PRIMARY KEY,
    user_id integer REFERENCES users,
    date timestamp without time zone,
    amount numeric(10,2)
);

-- Horse Racing game bets and replays.
CREATE TABLE IF NOT EXISTS horses (
    horses_id integer NOT NULL PRIMARY KEY,
    user_id integer REFERENCES users,
    date timestamp without time zone,
    bet numeric(10,2),
    guess integer,
    times double precision[4],
    bezier_curves double precision[4]
);

-- Roulette game bets and replays.
CREATE TABLE IF NOT EXISTS roulette (
    roulette_id integer NOT NULL PRIMARY KEY,
    user_id integer REFERENCES users,
    date timestamp without time zone,
    bet numeric(10,2),
    guess integer,
    result integer
);
