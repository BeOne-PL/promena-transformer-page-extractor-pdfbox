FROM almalinux:8

# Set environment for locale
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US
ENV LC_ALL en_US.UTF-8

# Install dependencies, OpenJDK 11, and locale support
RUN dnf -y install \
        java-11-openjdk java-11-openjdk-devel \
        glibc-langpack-en wget tar \
    && dnf clean all

# Set up Java environment variables
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk
ENV JRE_HOME /usr/lib/jvm/java-11-openjdk

# Install Maven
ENV MAVEN_VERSION 3.6.3
RUN cd /opt && \
    wget https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    rm -f apache-maven-${MAVEN_VERSION}-bin.tar.gz

# Set Maven environment variables
ENV M2_HOME /opt/apache-maven-${MAVEN_VERSION}
ENV PATH ${M2_HOME}/bin:${PATH}

# Final cleanup
RUN rm -rf /tmp/* /var/tmp/* /var/cache/dnf/*
