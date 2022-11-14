## What Services are in this project ?
### Application server
* Accepts and returns a response according to a specific JSON format
* Only has 1 API
* Also do some validation for that request, if not valid request will return an error
### Eureka server
* Acts as a service registry for application server instances
* Upon booting the application servers will register with the eureka server, so we can retrieve the instances address from the eureka server
### Routing server
* Can only route post request for now
* Will forward requests to one of the application server instances
* Runs a periodic task every 60s to update the list of available servers
* Uses simple round-robin algorithm

## How to run
### Gradle
```aidl
//build
export JAVA_HOME=path_to_java_11 && ./gradlew clean build
// run eureka server
./gradlew bootRun eureka_server
// Run application server(s)
PORT=XXX ./gradlew :application_server:bootRun
// Run routing server
./gradlew :routing_server:bootRun
```

### Docker compose
```aidl
// To be defined, still not figured out all the bugs yet
```

## Some possible improvements
* Add more tests to cover more cases
* Add more routing algorithms to routing server so that it can take into request / response time in real time
* Optimize docker images and fix docker compose related bugs for better containerization experiences