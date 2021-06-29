#!/bin/bash

#Author: DBryzz
 /home/dbryzz/Documents/IdeaProjects/client-api/src/main/resources/static/src-code-dir/1/abc2 .jar
##
# $1 = imageName,
# $2 = imageTag,
# $3 = appType,
# $4 = imageStatus,
# $5 = srcCodeUrl,
# $6 = codeDir
# $7 = extension
##

#Create Directory and files
echo "==========================================================="
echo "        Welcome to Bash Script: Containerization           "
echo "==========================================================="

echo "Current Working Directory "`pwd`
echo "Assigning Arguments..."
IMAGE_NAME=$1
IMAGE_TAG=$2
IMAGE_TYPE=$3
IMAGE_STATUS=$4
IMAGE_SOURCE_CODE_URL=$5
IMAGE_SOURCE_CODE_DIR=$6
IMAGE_EXTENSION=$7
IMAGE_SOURCE_CODE_NAME=$8

echo "Image Name: " $IMAGE_NAME
echo "Image Tag: " $IMAGE_TAG
echo "Image Type: " $IMAGE_TYPE
echo "Image Status: " $IMAGE_STATUS
echo "Image Source Code Location: " $IMAGE_SOURCE_CODE_URL
echo "Image Source Code Directory: " $IMAGE_SOURCE_CODE_DIR
echo "Image Source Code Extension: " $IMAGE_EXTENSION
echo "Image Source Code Name: " $IMAGE_SOURCE_CODE_NAME

echo "======> Change Working Directory"
cd $IMAGE_SOURCE_CODE_DIR
ls -l
unzip -q $SOURCE_CODE
echo "Current Working Directory "`pwd`

#echo $1 $2 $3 $4 $5 $6 $7

#mkdir /tmp/test00
#echo "Folder is created in /tmp"
#
#
#echo "Changing directory to /tmp"
#cd /tmp/test00
#
#echo "creating 3 files in /tmp/test00 and listing"
#touch file01 file02 file03
#ls -l .
#
#
##Delete file03
#cd /tmp/test00
#echo "Delete file03? enter 'y' for yes or 'n' for no"
#rm file03
#DATE=`date`
#echo $DATE
#ls -l .
