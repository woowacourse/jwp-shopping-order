./gradlew bootjar -x test
docker build -t waterricecake/jwp-shopping-order .
docker push waterricecake/jwp-shopping-order:latest
