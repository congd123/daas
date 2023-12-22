clean:
	mvn clean

run:
	nohup mvn spring-boot:run -pl api-gateway &
	nohup mvn spring-boot:run -pl daas-graphql &
    nohup mvn spring-boot:run -pl daas-scylladb &

run-apigateway:
	mvn spring-boot:run -pl api-gateway

run-graphql: 
	mvn spring-boot:run -pl daas-graphql

run-scylladb: 
	mvn spring-boot:run -pl daas-scylladb
