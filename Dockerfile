FROM openjdk:8-jdk-alpine

MAINTAINER fausto.tna@hotmail.com

RUN apk update && apk add bash

RUN mkdir -p /opt/shorturlcreation

ENV PROJECT_HOME /opt/shorturlcreation

COPY target/shorturlcreation.jar $PROJECT_HOME/shorturlcreation.jar

WORKDIR $PROJECT_HOME

ENTRYPOINT ["java", "-jar", "./shorturlcreation.jar"]
