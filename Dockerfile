FROM ubuntu

RUN apt-get update
#Need these before running ant deps
RUN apt-get install -y \
            default-jdk \
            default-jre \
            ant-contrib \
            astyle \
            wget \
            git
RUN ant deps
RUN adduser --disabled-password --gecos '' mjc
ADD . /project
RUN chown -R mjc:mjc /project
USER mjc

WORKDIR /project
RUN ant test
