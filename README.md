# Server-log-analytics

The parser analyses access server logs of format: 

127.0.0.1 - - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326,
where 

- 127.0.0.1 - is the IP address of the client (remote host) which made the request to the server

- frank - is the userid of the person requesting the document as determined by HTTP authentication

- [10/Oct/2000:13:55:36 -0700] - is the time that the server finished processing the request

- "GET /apache_pb.gif HTTP/1.0" - is the request line from the client

- 200 - is the status code that the server sends back to the client

- 2326 - is the size of the object returned to the client, not including the response headers

It writes parsed inforamtion to the PostgreSQL Database.

## Usage

Command line commands:

- In order to write information to database from log:
```bash
spring-boot:run -Dspring-boot.run.arguments=--spring.main.banner-mode=off,--logFile="path_to_file"
```
where "path_to_file" - path to access log

- In order to get the busiest time:

```bash
spring-boot:run -Dspring-boot.run.arguments=--spring.main.banner-mode=off,--activity
```

- In order to get the most repeated requests from database:
```bash
spring-boot:run -Dspring-boot.run.arguments=--spring.main.banner-mode=off,--popRes
```
