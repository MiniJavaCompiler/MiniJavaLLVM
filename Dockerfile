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
RUN adduser --disabled-password --gecos '' mjc
ADD . /project
RUN chown -R mjc:mjc /project

WORKDIR /project
RUN ant deps
USER mjc
RUN ant clean
RUN ant test
