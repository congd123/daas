FROM openjdk:8-slim

ARG HADOOP_VERSION=3.3.4
ARG HIVE_VERSION=3.1.3
ARG MYSQL_DRIVER_VERSION=8.0.23
ARG HUDI_VERSION=1.0.0-SNAPSHOT

RUN apt-get update && apt-get install -y curl

RUN curl https://archive.apache.org/dist/hadoop/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz -o hadoop-${HADOOP_VERSION}.tar.gz &&\
    tar -xvf hadoop-${HADOOP_VERSION}.tar.gz -C /opt/

RUN ln -s /opt/hadoop-$HADOOP_VERSION /opt/hadoop \
	&& rm -r /opt/hadoop/share/doc


RUN curl https://archive.apache.org/dist/hive/hive-${HIVE_VERSION}/apache-hive-${HIVE_VERSION}-bin.tar.gz | tar xvf -C /opt/ \
	&& ln -s /opt/apache-hive-${HIVE_VERSION}-bin /opt/hive

COPY hudi-hadoop-mr-bundle-${HUDI_VERSION}.jar /opt/hadoop/share/hadoop/common/lib/hudi-hadoop-mr-bundle-${HUDI_VERSION}.jar
COPY hudi-hadoop-mr-bundle-${HUDI_VERSION}.jar /opt/hive/lib/hudi-hadoop-mr-bundle-${HUDI_VERSION}.jar

RUN curl https://repo1.maven.org/maven2/mysql/mysql-connector-java/${MYSQL_DRIVER_VERSION}/mysql-connector-java-${MYSQL_DRIVER_VERSION}.jar -o /opt/hive/lib/mysql-connector-java-${MYSQL_DRIVER_VERSION}.jar

ENV HADOOP_HOME="/opt/hadoop"
ENV PATH="${HADOOP_HOME}/bin:/opt/hive/bin:${PATH}"

COPY core-site.xml /opt/hadoop/etc/hadoop/
COPY hive-site.xml /opt/hive/conf