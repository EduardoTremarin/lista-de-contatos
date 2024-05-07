FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} lista-de-contatos.jar
ENTRYPOINT ["java","-jar","/lista-de-contatos.jar"]