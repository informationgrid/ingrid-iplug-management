FROM openjdk:8-jdk-alpine
ENV INGRID_USER=root

ADD ingrid-iplug-management*-installer.jar /

RUN unzip ingrid-*.jar -d /tmp \
    && mkdir -p /opt/ingrid/ingrid-iplug-management \
    && cp -R /tmp/ingrid-iplug-management-*/* /opt/ingrid/ingrid-iplug-management/ \
    #&& chgrp -R ingrid /opt/ingrid \
    #&& chown -R ingrid /opt/ingrid \
    #&& chown -R ingrid /opt/ingrid \
    && rm -Rf /tmp/* \
    && rm ingrid-*.jar

WORKDIR /opt/ingrid/ingrid-iplug-management
EXPOSE 9903

CMD /bin/sh start.sh start && tail -f /dev/null