FROM oracle/graalvm-ce:1.0.0-rc9 AS builder

RUN yum install git -y

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY build-cli.sh build-cli.sh

COPY library/settings.gradle library/settings.gradle
COPY library/build.gradle library/build.gradle
COPY library/src library/src

COPY command-line/settings.gradle command-line/settings.gradle
COPY command-line/build.gradle command-line/build.gradle
COPY command-line/src command-line/src

RUN /build-cli.sh


FROM alpine:3.8
COPY --from=builder /linalyzer /linalyzer
ENTRYPOINT ["/linalyzer"]

