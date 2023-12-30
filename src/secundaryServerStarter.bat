@echo off
setlocal enabledelayedexpansion

rem Set the path to the Java compiler (javac) and Java runtime (java)
set JAVA_COMPILER_PATH="C:\Program Files\Java\jdk-21\bin\javac.exe"
set JAVA_RUNTIME_PATH="C:\Program Files\Java\jdk-21\bin\java.exe"

rem Set the path to the Java source code
set JAVA_SOURCE_PATH=.\cmd\*.java .\server\*.java .\SecundaryServer\*.java

rem Compile the Java source code
%JAVA_COMPILER_PATH% -cp sd23.jar %JAVA_SOURCE_PATH%
rem Run the Java program with varying arguments in a separate command window
rem Each server requires the port number to work
start "Java Program - 12345" %JAVA_RUNTIME_PATH% -cp ".;sd23.jar" SecundaryServer.Main 12345
start "Java Program - 12346" %JAVA_RUNTIME_PATH% -cp ".;sd23.jar" SecundaryServer.Main 12346
start "Java Program - 12347" %JAVA_RUNTIME_PATH% -cp ".;sd23.jar" SecundaryServer.Main 12347

endlocal
