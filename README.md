# png-to-ascii
![](screenshot/0.png?raw=true)
png-to-ascii is a GUI program written in Java utilizing the JavaFX library. png-to-ascii is a simple program that lets you quickly generate ASCII renditions of any PNG image you provide, providing additional control through a pixel sampling ratio slider and line skipper. Your generated ASCII can be exported as a text document or copied directly from the text output area.

# running
You can find the latest builds in the **Actions** tab. Currently there are only builds that support Linux. To run the program, navigate to the bin folder in your install and run the shell script.
```
$ bash ./app.sh
```

# build
png-to-ascii requires **Maven** to build. Maven is provided in many package managers, but can also be found [here](https://maven.apache.org/download.cgi). Further dependencies (such as JavaFX) will be provided to you through Maven. To build png-to-ascii, run the following command:
```
# To build and run
$ mvn clean javafx:run
# To just build
$ mvn clean javafx:jlink
```
