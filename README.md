# goeuro / dev-test
This system was developed following the premises of original test. Please see this link to get all basic information of it: [goeuro/dev test](https://github.com/goeuro/dev-test)

## Technologies
At this section I'll list and explain which technologies I've chosed to work on:
* Java - the required language for this application
* Google Guice - This library manages object creation and instantiation and it's very lightweight in order to avoid heavy  footprints during bootstrap application [https://github.com/google/guice]
* GSON - This library was used to convert goeuro's response endpoint into objects from the system. Again, it's very simple and lightweight [https://github.com/google/gson]
* Http Components - This library helps on HTTP requests/responses and is pretty simple to set timeouts, retries and other directives [https://hc.apache.org/]
* Logback - Async logging library [http://logback.qos.ch/]
* JUnit - unit tests framework to test each class and method individually [http://junit.org/junit4/]
* Mockito - Framework to create objects mocks helping unit tests to segregate layers and objects dependencies [http://mockito.org/]

## Motivations
The system was designed to be simple but at the same time using some approaches to help understand, maintain and improve the system like some other production should have. Below there's a list of mindsets that guided implementation:
* Logging - every production application must have logging for troubleshooting and to know used parameters on each flow and so on.
* Exceptions handling - the code is supported by an exception mechanism controlling the flow. This approach needs more code (creating custom exceptions and check all exceptions) but lets the system more robust.
* IoC - Using IoC (Inversion of Control) mechanism helps to not creating a lot of objects in memory and avoiding large footprints besides code gets simpler as well. 
* Unit tests - To make sure all the corner cases were coverage including code coverage. The application has 88% of code coverage by unit tests ;)

## How to run
The system produces a fat jar at the end of 'package' step. To generate it, it's mandatory to have Maven and Java installed on machine. After setting up these mandatories, just run the following command:
> `mvn clean package`

The fat jar called 'GoEuroTest.jar' should be generated at target/ folder. To run the application, just execute the command below:
> `java -jar target/GoEuroTest.jar [CITY_NAME]`

where [CITY_NAME] parameter it's the name or partial name to the city to be searched. The system should output some logs describing what it's doing step by step, as below:

```console
2016-08-01 13:35:44,413 [INFO] [main] [application] default path for application.log file: [.]
2016-08-01 13:35:44,416 [INFO] [main] [application] city name received: [Berlin]
2016-08-01 13:35:44,419 [INFO] [main] [application] response CSV file name: [result.csv]
2016-08-01 13:35:44,419 [INFO] [main] [application] response CSV separator value: [,]
2016-08-01 13:35:44,419 [INFO] [main] [application] response CSV output path: [./]
2016-08-01 13:35:44,986 [INFO] [main] [application] requested url: [http://api.goeuro.com/api/v2/position/suggest/en//Berlin]
2016-08-01 13:35:45,660 [INFO] [main] [application] successfull request: [http://api.goeuro.com/api/v2/position/suggest/en//Berlin]
2016-08-01 13:35:45,672 [INFO] [main] [application] found [8] cities in response
2016-08-01 13:35:45,675 [INFO] [main] [application] successfull generated file: [result.csv] in path: [./]
```

## Optional Parameters
The system was designed to accept overriding some parameters to change timeouts, csv file name, csv file path and so on. Below the table of parameters and default values in case of not overriding:

| Property Name | Default Value |
| --- | --- |
|goeuro.url|http://api.goeuro.com/api/v2/position/suggest/en/|
|goeuro.url.read-timeout|10000|
|goeuro.url.connect-timeout|5000|
|goeuro.url.max-retry|3|
|csv.file-name|result.csv|
|csv.output-path|./|
|csv.separator-value|,|

To run with all these parameters in command line, execute the command below:

> `java -jar -Dgoeuro.url=http://api.goeuro.com/api/v2/position/suggest/en/ -Dgoeuro.url.read-timeout=5000 -Dgoeuro.url.connect-timeout=2000 -Dcsv.file-name=result.csv -Dcsv.separator-value=, -Dcsv.output-path=. GoEuroTest.jar [CITY_NAME]`

Note: in case [CITY_NAME] it's a compound word like Sao Paulo, you should wrap like this: "Sao Paulo"

## Output file
After system has executed, it produces a CSV file containing response data. This file has the following information: id,name,type,latitude,longitude of each city returned by goeuro's endpoint. Below some response to illustrate:

```console
376217,Berlin,location,52.52437,13.41053
448103,Berlingo,location,45.50298,10.04366
425332,Berlingerode,location,51.45775,10.2384
425326,Bernau bei Berlin,location,52.67982,13.58708
314826,Berlin Tegel,airport,52.5548,13.28903
314827,Berlin Schönefeld,airport,52.3887261,13.5180874
334196,Berlin Hbf,station,52.525589,13.369548
333977,Berlin Ostbahnhof,station,52.510972,13.434567
```

## FAQ and Troubleshooting
* Running application without provide any city name 

The system will print error message like this:

```console
Please provide at least one argument (city name) to run this application, exitting...
```
and will exit. This parameter is mandatory and should be passed by at startup.

* Error on connection/getting data from goeuro's url: This could happen if there's some issue with goeuro's endpoint or speed problems with internet. If you see this message on application log:

```console
2016-08-01 13:54:52,977 [ERROR] [main] [application] read timeout when trying to get response from url: [http://api.goeuro.com/api/v2/position/suggest/en/]
```
try increasing the 'goeuro.url.read-timeout' parameter value. If you see this message:

```console
2016-08-01 13:54:52,977 [ERROR] [main] [application] connect timeout when trying to connect on url: [http://api.goeuro.com/api/v2/position/suggest/en/]
```
try increasing 'goeuro.url.connect-timeout' parameter value instead.
 