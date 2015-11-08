#!/bin/bash

#JAVA=/home/gorshing/devl/java/jdk1.5.0_01/bin/java
JAVA=java

if [ -z "$1" ]; then
  $JAVA -cp .:htmlparser1_5/lib/htmlparser.jar MainEntry http://adhara.gorshing.net:2000
else
  $JAVA -cp .:htmlparser1_5/lib/htmlparser.jar MainEntry "$1"
fi

#$(JAVA) -cp .:htmlparser1_5/lib/htmlparser.jar cbot
