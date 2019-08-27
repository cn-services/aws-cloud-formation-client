# AWS POC #

[![](https://img.shields.io/github/license/jnonino/aws-cloud-formation-client)](https://github.com/jnonino/aws-cloud-formation-client)
[![](https://img.shields.io/github/issues/jnonino/aws-cloud-formation-client)](https://github.com/jnonino/aws-cloud-formation-client)
[![](https://img.shields.io/github/issues-closed/jnonino/aws-cloud-formation-client)](https://github.com/jnonino/aws-cloud-formation-client)
[![](https://img.shields.io/github/languages/code-size/jnonino/aws-cloud-formation-client)](https://github.com/jnonino/aws-cloud-formation-client)
[![](https://img.shields.io/github/repo-size/jnonino/aws-cloud-formation-client)](https://github.com/jnonino/aws-cloud-formation-client)

## Building ##

In order to build this project, run the following command:
    
    ./gradlew build
    
That command will generate a jar file to run the project:

    build/libs/aws-poc-*.jar
    
## Environment Management #

This program can be used to create and delete environments in AWS.
Before running it, please install AWS CLI and run the command "aws configure".
This is to set up the credentials to be used to connect to AWS.

The, to run the application from repository root, run the command:  

    java -jar build/libs/aws-poc-*.jar (PARAMETERS)
    
The jar execution requires three parameters:  
- ENVIRONMENT_NAME: The name of the environment to work with.  
- ACTION: The action to perform on the environment.  
- LAYERS_LIST: Comma separated list of layers without spaces.  

Current supported actions are:  
- create  
- delete  
 
Current supported layers are:  
- base  

### Examples ###

Create a new environment:  

    java -jar build/libs/aws-poc-*.jar testEnvironment create base
   
Delete an environment:  

    java -jar build/libs/aws-poc-*.jar testEnvironment delete base
