FROM maven:3-amazoncorretto-17-alpine AS maven
WORKDIR /sigmacasino
COPY src src
COPY pom.xml .
RUN mvn clean package

FROM golang:1.23-alpine AS overmind
RUN apk add --no-cache git; \
    git clone --depth 1 --branch v2.5.1 https://github.com/DarthSim/overmind.git /overmind
WORKDIR /overmind
RUN go build -o overmind

FROM postgres:17-alpine
ENV POSTGRES_DB=sigmacasino
ENV POSTGRES_USER=sigmacasino
ENV PGDATA=/sigmacasino/db/data
COPY --from=maven /sigmacasino/target/sigmacasino.jar /sigmacasino/sigmacasino.jar
COPY --from=overmind /overmind/overmind /sigmacasino/overmind
COPY Procfile /sigmacasino/Procfile
RUN apk add --no-cache \
    bash \
    openjdk17-jre-headless \
    tmux
VOLUME /sigmacasino/db
EXPOSE 5432 6969
WORKDIR /sigmacasino
USER root
CMD ["bash", "-c", "mkdir -p /sigmacasino/db/data && chown -R postgres:postgres /sigmacasino/db && ./overmind start"]
