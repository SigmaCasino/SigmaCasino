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

- You can also configure a Docker volume at `/sigmacasino/db` to make the database persistent.

### With Maven
You can run just the backend part of the project by utilizing Maven exec plugin.

```sh
$ mvn exec:java
```

**REMEMBER**: You also have to run a PostgreSQL server in the background with the following parameters:
- Username: *sigmacasino*
- Database: *sigmacasino*
- Password: whatever you want, but it has to be exported as an environment variable named `POSTGRES_PASSWORD`

## Deploying on [*fly.io*](https://fly.io)

- First, create a new app:
```sh
$ fly launch --no-deploy
```

- Add a necessary secret:
```sh
$ fly secrets set POSTGRES_PASSWORD=<password>
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
