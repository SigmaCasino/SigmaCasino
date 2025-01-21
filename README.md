# Sigma Casino

AGH UST *Software Engineering* project.

## How to run locally

### With Docker
By using Docker, you can run all of the necessary things (such as PostgreSQL database) in one container.

- Build the container:
```sh
$ docker build . -t sigmacasino
```

- Run with exposed ports:
```sh
$ docker run -e POSTGRES_PASSWORD=<password> -it -p 5432:5432 -p 6969:6969 sigmacasino
```

- Go to http://localhost:6969/ in your browser to try the casino out.

- Or open interactive database shell:
```sh
$ psql -U sigmacasino -h localhost -p 5432
```

- To test Stripe-based functionality, do the following:
    - Forward Stripe webhook traffic to your computer:
    ```sh
    $ stripe listen --forward-to localhost:6969/webhook/stripe
    ```
    - Set more environment variables, preferably using `.env` file: `STRIPE_KEY`, `STRIPE_PRICE_ID`, `STRIPE_WEBHOOK_SECRET` (don't forget `POSTGRES_PASSWORD`).
    - Start the container with a modified command:
    ```sh
    $ docker run --env-file .env -it -p 5432:5432 -p 6969:6969 sigmacasino
    ```

- You can also configure a Docker volume at `/sigmacasino/db` to make the database persistent.

### With Maven
You can run just the backend part of the project by utilizing Maven exec plugin.
This assumes you've already set the environment variables: `POSTGRES_PASSWORD` and `STRIPE_KEY`.

```sh
$ mvn exec:java
```

**REMEMBER**: You also have to run a PostgreSQL server in the background with the following parameters:
- Username: *sigmacasino*
- Database: *sigmacasino*
- Password: whatever you want, but it has to be matching `POSTGRES_PASSWORD`

## Deploying on [*fly.io*](https://fly.io)

- First, create a new app:
```sh
$ fly launch --no-deploy
```

- Add necessary secrets:
```sh
$ fly secrets set POSTGRES_PASSWORD=<password>
$ fly secrets set STRIPE_KEY=<key>
```

- Allocate an IP address:
```sh
$ fly ips allocate-v4 --shared
```

- And finally, create a new release:
```sh
$ fly deploy
```

- To manage the database manually, you can use:
```sh
$ fly ssh console --pty -C "psql -U sigmacasino"
```
