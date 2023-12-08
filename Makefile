clean:
	mvn clean

run:
	mvn spring-boot:run -pl api-gateway
	mvn spring-boot:run -pl daas-graphql
        mvn spring-boot:run -pl daas-scylladb

run-apigateway:
	mvn spring-boot:run -pl api-gateway

run-graphql: 
	mvn spring-boot:run -pl daas-graphql

run-scylladb: 
	mvn spring-boot:run -pl daas-scylladb
