#
# Copyright (c) 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
# Dell EMC Confidential/Proprietary Information
#
#  USAGE: (from .. dir)
#    docker build -f apm-nagios-distribution/Dockerfile --tag=apm-nagios apm-nagios
#

# Start from base image with Java 8
FROM java:8-jre-alpine

RUN mkdir -p /opt/dell/cpsd/apm-nagios/bin
RUN mkdir -p /opt/dell/cpsd/apm-nagios/logs
RUN mkdir -p /opt/dell/cpsd/apm-nagios/lib
RUN mkdir -p /opt/dell/cpsd/apm-nagios/install

COPY target/dependency/lib/apm-nagios-*.jar /opt/dell/cpsd/apm-nagios/lib/apm-nagios.jar
COPY docker/image/bin/run.sh /opt/dell/cpsd/apm-nagios/bin/

RUN chmod 755 /opt/dell/cpsd/apm-nagios/bin/run.sh

EXPOSE 8080

CMD ["/opt/dell/cpsd/apm-nagios/bin/run.sh"]
