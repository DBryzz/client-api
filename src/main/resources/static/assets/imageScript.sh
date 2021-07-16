#!/bin/bash

#Author: Domou Brice
#Email: domoubrice@gmail.com

##
# $1 = imageName,   $6 = codeDir
# $2 = imageTag,    $7 = extension
# $3 = appType,     $8 = srcCodeName
# $4 = imageStatus, $9 = srcCodoeNameWithoutExt
# $5 = srcCodeUrl,  $10 = containerCount
##

#Create Directory and files
echo "==========================================================="
echo "        Welcome to Bash Script: Containerization           "
echo "==========================================================="

echo -e "Current Working Directory `pwd`\n"
echo "Assigning Arguments..."
IMAGE_NAME=$1
IMAGE_TAG=$2
IMAGE_TYPE=$3
IMAGE_STATUS=$4
IMAGE_SOURCE_CODE_URL=$5
IMAGE_SOURCE_CODE_DIR=$6
IMAGE_EXTENSION=$7
IMAGE_SOURCE_CODE_NAME=$8
IMAGE_SOURCE_CODE_NAME_WITHOUT_EXT=$9
CONTAINER_COUNT=$10

echo "Image Name: $IMAGE_NAME"
echo "Image Tag: $IMAGE_TAG"
echo "Image Type: $IMAGE_TYPE"
echo "Image Status: $IMAGE_STATUS"
echo "Image Source Code Location: $IMAGE_SOURCE_CODE_URL"
echo "Image Source Code Directory: $IMAGE_SOURCE_CODE_DIR"
echo "Image Source Code Extension: $IMAGE_EXTENSION"
echo -e "Image Source Code Name: $IMAGE_SOURCE_CODE_NAME"
echo -e "Image Source Code Name Without Extension: $IMAGE_SOURCE_CODE_NAME_WITHOUT_EXT\n"

echo `pwd`

echo "======> Change Working Directory To "$IMAGE_SOURCE_CODE_DIR""
cd "$IMAGE_SOURCE_CODE_DIR"
ls -l

if [[ "$IMAGE_TYPE" == "JAVA" ]]; then

  if [[ "$IMAGE_EXTENSION" == ".jar" ]]; then
    echo -e "\nJAVA-JAR: Creating Dockerfile and .dockerignore file\nSTART...\n"

    cat > Dockerfile << EOF
FROM openjdk:8-alpine
ADD $IMAGE_SOURCE_CODE_NAME  /home/$IMAGE_SOURCE_CODE_NAME
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/home/$IMAGE_SOURCE_CODE_NAME"]
EOF

    cat > .dockerignore << EOF
Dockerfile
EOF

    echo -e "\nSTOP: Creating Dockerfile\n"

  fi

  if [[ "$IMAGE_EXTENSION" == ".java" ]]; then
    echo -e "\nJAVA-BASIC: Creating Dockerfile and .dockerignore file\nSTART...\n"

    cat > Dockerfile << EOF
FROM openjdk:8-alpine
COPY . /src/java
WORKDIR /src/java
RUN ["javac", "$IMAGE_SOURCE_CODE_NAME"]
ENTRYPOINT ["java", "$IMAGE_SOURCE_CODE_NAME_WITHOUT_EXT"]
EOF

    cat > .dockerignore << EOF
Dockerfile
EOF
    echo -e "\nSTOP: Creating Dockerfile\n"

  fi

  if [[ "$IMAGE_STATUS" == "DEPLOYED" || "$IMAGE_STATUS" == "DnP" ]]; then
      echo "I'm about to build"
      docker build -f ./Dockerfile -t "$IMAGE_NAME":latest .
      docker tag "$IMAGE_NAME":latest "$IMAGE_NAME":"$IMAGE_TAG"   #TODO Tagging with File Name

      if [[ "$IMAGE_EXTENSION" == ".java" ]]; then
        docker run -it "$IMAGE_NAME":"$IMAGE_TAG"
      fi

      if [[ "$IMAGE_EXTENSION" == ".jar" ]]; then
        docker run -d --name "$IMAGE_NAME" -p 8081:8081 "$IMAGE_NAME":"$IMAGE_TAG"
      fi


    fi

fi


if [[ "$IMAGE_EXTENSION" == ".zip" ]]; then

  echo -e "\nUnzipping zipped file\n"

#    unzip -q $SOURCE_CODE
  unzip $SOURCE_CODE

  echo -e "\nUnzipping Complete\n"

  if[[ "$IMAGE_TYPE" == "MAVEN" ]]; then

    echo -e "\nJAVA-MAVEN: Creating Dockerfile and .dockerignore file\nSTART...\n"
    cat > Dockerfile << EOF
FROM maven:alpine AS devenv
WORKDIR /usr/src/$IMAGE_NAME
COPY $(ls -l | grep "pom.xml") .
RUN mvn -B -f pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
COPY . .
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml package -DskipTes


#
# Deploy application to tomcat
#

FROM tomcat:8.5-alpine
LABEL maintainer="Domou Brice <domoubrice@gmail.com>"

# Tomcat-users.xml sets up user accounts for the Tomcat manager GUI
ADD tomcat/tomcat-users.xml $CATALINA_HOME/conf/

# ADD tomcat/catalina.sh $CATALINA_HOME/bin/run.sh
ADD tomcat/run.sh $CATALINA_HOME/bin/run.sh
RUN chmod +x $CATALINA_HOME/bin/run.sh

# Add MySQL JDBC driver jar
ADD tomcat/mysql-connector-java-5.1.36-bin.jar $CATALINA_HOME/lib/

# Create mount point for volume with application
WORKDIR $CATALINA_HOME/webapps/
COPY --from=devenv /usr/src/$IMAGE_NAME/target/Signup.war

# Add tomcat jpda debugging environmental varaibles
# ENV JPDA_DPTS="-agentlib:jdwp=transport=dt_socket, address=8000,server=y,suspend=n"
ENV JPDA_ADDRESS="8000"
ENV JPDA_TRANSPORT="dt_socket"

# Start tomcat7 with remote debugging
EXPOSE 8080
EOF

    cat > .dockerignore << EOF
Dockerfile
EOF

    echo -e "\nSTOP: Creating Dockerfile\n"
  fi

  if [[ "$IMAGE_TYPE" == "PHP" ]]; then
    echo -e "\nPHP: Creating Dockerfile and .dockerignore file\nSTART...\n"
    cat > Dockerfile << EOF
FROM php:7.2-apache
RUN apt-get update && \
    apt-get install -y php5-mysql && \
    apt-get clean
COPY . /var/www/html/
EOF

    cat > .dockerignore << EOF
Dockerfile
EOF

    echo -e "\nSTOP: Creating Dockerfile\n"

  fi

  if [[ "$IMAGE_TYPE" == "LARAVEL" ]]; then
    echo -e "\nLARAVEL: Creating Dockerfile and .dockerignore file\nSTART...\n"
    cat > Dockerfile << EOF
FROM composer:1.9.3 as vendor
WORKDIR /tmp/
COPY composer.json composer.json
COPY composer.lock composer.lock
RUN composer install \
    --ignore-platform-reqs \
    --no-interaction \
    --no-plugins \
    --no-scripts \
    --prefer-dist

FROM php:7.2-apache
COPY . /var/www/html
COPY --from=vendor /tmp/vendor/ /var/www/html/vendor/
EOF
    cat > .dockerignore << EOF
Dockerfile
EOF

    echo -e "\nSTOP: Creating Dockerfile\n"
  fi

fi


echo "==========================================================="
echo "               Containerization Complete                   "
echo "==========================================================="




#docker run --rm -p 8000:80 -v $(pwd):/var/www/html php:apache #PHP
#
#docker build -t app:1 .
#
#docker build –file .docker/Dockerfile \ -t docker-tutorial .

## todo - Docker Network
#docker network ls
#
### todo - None Network
#docker run -d --net none busybox sleep 1000
#docker exec -it busybox_container_id /bin/ash
#ping 8.8.8.8      #google public dns - This will fail as we are disconnected
##Returns unreachable
#ifconfig  #returns only one network interface lo
#
### todo - Bridge Network
#run container
#docker run -d --name container_1 busybox sleep 1000
#Get into interactive mode and list network interfaces
#docker exec -it container_1 ifconfig
#docker network inspect bridge
#docker run -d --name container_2 busybox sleep 1000
#docker exec -it container_2 /bin/ash
#docker network create --driver bridge my_bridge_network
#docker network inspect my_bridge_network
#docker run -d --net my_bridge_network --name con_3 busybox sleep 1000
#docker network connect bridge con_3
#docker exec -it con_3 ifconfig
#docker network disconnect my_bridge_network con_3
#
#
### todo - Host Network
#docker run -d --name con_4 --net host busybox sleep 1000
#docker exec -it con_4 ifconfig    #all network interface defined on the host will be accessible to the con_4.

echo "Current Working Directory `pwd`"

