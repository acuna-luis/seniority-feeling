FROM openjdk:11-jdk-slim
COPY target/td-*.jar /
COPY entrypoint.sh /
ENTRYPOINT ["/bin/sh", "/entrypoint.sh"]

