[![Actions Status](https://github.com/Hakky54/welk-lidwoord/workflows/Build/badge.svg)](https://github.com/Hakky54/welk-lidwoord/actions)
[![JDK compatibility: 17+](https://img.shields.io/badge/JDK_compatibility-17+-blue.svg)](#)
[![Apache2 license](https://img.shields.io/badge/license-Aache2.0-blue.svg)](https://github.com/Hakky54/sslcontext-kickstart/blob/master/LICENSE)

# Welk Lidwoord❓
A desktop application that will help you to improve your Dutch grammar. Should you use `de` or `het` for the word `huis`?
When do you use `ons` or `onze`? What about `die`, `dat`, `dit` or `deze`.
Don't worry, this little app will quicly help you out whenever you need some help. See below for the demo

## Demo
![alt text](https://github.com/Hakky54/welk-lidwoord/blob/master/images/demo.gif?raw=true)

### Requirements:
- Java 17
- Maven

### Building executable
Create and run an executable jar:
1. `mvn clean verify`
2. `java -jar target/Welk-Lidwoord.jar`

Or start it with the exec plugin
1. `mvn clean verify`
2. `mvn exec:jara`
