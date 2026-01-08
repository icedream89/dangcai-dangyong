FROM mcr.microsoft.com/devcontainers/base:ubuntu

# 安装Java 17
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends openjdk-17-jdk \
    && apt-get clean -y

# 安装Maven
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends maven \
    && apt-get clean -y

# 安装其他工具
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
    git \
    curl \
    wget \
    vim \
    net-tools \
    && apt-get clean -y

# 设置默认工作目录
WORKDIR /workspace

# 设置环境变量
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV MAVEN_HOME=/usr/share/maven
ENV PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH"
