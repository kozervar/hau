FROM java:openjdk-8u111-alpine

WORKDIR /
COPY build/libs/hau*.jar /hau.jar

CMD java -jar hau.jar --spring.data.mongodb.host=mongodb