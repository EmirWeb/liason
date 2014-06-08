#!/bin/sh

set -e

mvn clean deploy -P deploy -f loaders/pom.xml 
mvn clean deploy -P deploy -f mvvm/pom.xml 
mvn clean deploy -P deploy -f task/pom.xml

