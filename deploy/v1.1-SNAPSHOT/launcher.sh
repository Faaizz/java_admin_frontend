#!/usr/bin/env bash

#SETUP SAVE FILE
DIR=./settings
FILE=main.json

#CHECK IF DIRECTORY DOESN NOT EXISTS
if [[ ! -d $DIR ]]; then 
    mkdir $DIR
fi

#cd INTO THE DIRECTORY
cd $DIR

#IF FILE DOES NOT EXIST, CREATE IT
if [[ ! -a "$FILE" ]]; then
    touch $FILE
fi

#RETURN BACK TO ROOT FOLDER
cd ../

#SETUP JAVAFX MODULE
#SET MODULE PATH TO USE LOCAL JAVAFX SDK
module_path="linux/javafx-sdk-11.0.2/lib"

#RUN APPLICATION WITH REQUIRED COMMAND-LINE ARGUMENTS
java -jar --module-path $module_path --add-modules=javafx.controls,javafx.fxml java_admin_frontend-1.0-SNAPSHOT.jar