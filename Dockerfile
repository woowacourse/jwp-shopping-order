FROM amazoncorretto:11-alpine-jdk

WORKDIR /app

COPY /build/libs/jwp-shopping-order.jar /app/jwp-shopping-order.jar

CMD ["java","-Dspring.profiles.active=prod", "-jar", "/app/jwp-shopping-order.jar"]
