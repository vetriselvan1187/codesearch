# codesearch

	codeasearch tool to search redis repository source code. API is exposed to search based on keyword which could be in the function name or parameter
	jetty is used as container to serve http request. it is configured in pom.xml with dependencies.

	How to Start:
		Unzip the Source Code or Clone the Source from github. set the Redis Source code Directory Path in the codesearch.properties. 
		then run
		mvn jetty:run
		
# prerequisites
	1. JDK1.8
	2. Maven 

# HTTP API for codesearch

	the following url searches for the word in the file and returns the filename and the following line matches
	1. http://localhost:8080/codesearch?word=input;

	the following url searches for the word in the file and returns the filename with function matches

	2. http://localhost:8080/codesearch?word=input&type=function

	the following url searches for the word in the file and returns the filename with parameter matches

	3. http://localhost:8080/codesearch?word=input&type=parameter


# Github Repository URL

	https://github.com/vetriselvan1187/codesearch


