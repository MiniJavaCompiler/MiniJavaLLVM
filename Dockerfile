FROM ubuntu

RUN apt-get update
RUN apt-get install -y \
            build-essential \
            default-jdk \
            default-jre \
            ant-contrib \
            libcommons-cli-java \
            junit4 \
            libjna-java \
            astyle \
            clang-3.5 \
            llvm-3.5-dev \
            llvm-dev \
            libc6-dev-i386 \
            python \
            wget \
            git
RUN adduser --disabled-password --gecos '' mjc
ADD . /project
RUN chown -R mjc:mjc /project
USER mjc

WORKDIR /project
RUN ant test
