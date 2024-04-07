FROM flink:1.17-java8

RUN mkdir -p /opt/flink/plugins/flink-s3-fs-hadoop/ &&\
    mkdir -p /opt/flink/plugins/flink-s3-fs-presto/ &&\
    mv -v /opt/flink/opt/flink-s3-fs-hadoop-*.jar /opt/flink/plugins/flink-s3-fs-hadoop/ &&\
    mv -v /opt/flink/opt/flink-s3-fs-presto-*.jar /opt/flink/plugins/flink-s3-fs-presto/ &&\
    chown -R flink:flink /opt/flink/plugins/flink-s3-fs-hadoop &&\
    chown -R flink:flink /opt/flink/plugins/flink-s3-fs-hadoop


RUN wget https://repo1.maven.org/maven2/org/apache/hudi/hudi-hive-sync-bundle/0.14.0/hudi-hive-sync-bundle-0.14.0.jar &&\
    mv -v hudi-hive-sync-bundle-0.14.0.jar /opt/flink/lib
RUN wget https://repo1.maven.org/maven2/org/apache/hudi/hudi-flink1.17-bundle/0.14.0/hudi-flink1.17-bundle-0.14.0.jar &&\
    mv -v hudi-flink1.17-bundle-0.14.0.jar /opt/flink/lib
RUN wget https://repo1.maven.org/maven2/org/apache/hudi/hudi-hadoop-mr-bundle/0.14.0/hudi-hadoop-mr-bundle-0.14.0.jar &&\
    mv -v hudi-hadoop-mr-bundle-0.14.0.jar /opt/flink/lib

RUN chown -R flink:flink /opt/flink/lib

USER flink

#CMD exec /usr/share/flink/bin/flink-console.sh ${COMPONENT} ${ARGS}