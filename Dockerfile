FROM eclipse-temurin:8u412-b08-jre

RUN mkdir -p /output

COPY target/*.jar /fakeSMTP.jar

VOLUME /output

EXPOSE 25

ENTRYPOINT ["java","-jar","/fakeSMTP.jar","--background", "--output-dir", "/output", "--port", "25", "--start-server"]
