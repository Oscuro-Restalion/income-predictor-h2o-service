FROM oracle/graalvm-ce:1.0.0-rc8
COPY /var/jenkins_home/workspace/ome-predictor-h2o-service_master@2/target/income-predictor-h2o-service-0.0.1-SNAPSHOT.jar app.jar
CMD java -jar -Dgraal.CompilerConfiguration=community app.jar