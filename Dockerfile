FROM openjdk:11-jdk-slim
COPY target/seniority-*.jar /
COPY entrypoint.sh /
ENTRYPOINT ["/bin/sh", "/entrypoint.sh"]

