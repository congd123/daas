FROM scylladb/scylla:5.2

COPY init_database.cql /docker-entrypoint-initdb.d/init_database.cql
COPY wrapper.sh /wrapper.sh

RUN chmod +x wrapper.sh

ENTRYPOINT ["/wrapper.sh"]