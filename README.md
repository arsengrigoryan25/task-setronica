## Server
-   Server run with command `java MyServer [port_as_parameter]` default value of port is `8080`
    - Server supports these endpoints
        *   service name - `service1`
            -   methods
                *   `sleep(Long)`  parameter decides how long time to sleep Thread in millis
                *   `Date getCurrentDate()` get current date in format `Mon Feb 01 23:44:56 AMT 2021`
    
## Client
-   Client run with `main` method in class `MyClient`

## Code and messages of Class Response
    00 - When called Method's return type is void
    01 - Not found service
    02 - No such method
    03 - Incorrect parameters type
    04 - Incorrect parameters count