FROM oracle/graalvm-ce:1.0.0-rc8
ADD ./data/income-predictor-h2o-service-0.0.1-SNAPSHOT.jar app.jar
CMD java -jar -Dgraal.CompilerConfiguration=community app.jar