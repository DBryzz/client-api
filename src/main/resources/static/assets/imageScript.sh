#!/bin/bash

#Author: DBryzz

#Create Directory and files
mkdir /tmp/test00
echo "Folder is created in /tmp"


echo "Changing directory to /tmp"
cd /tmp/test00

echo "creating 3 files in /tmp/test00 and listing"
touch file01 file02 file03
ls -l .


#Delete file03
cd /tmp/test00
echo "Delete file03? enter 'y' for yes or 'n' for no"
rm file03
DATE=`date`
echo $DATE
ls -l .
