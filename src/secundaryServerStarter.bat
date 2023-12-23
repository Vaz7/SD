@echo off
setlocal enabledelayedexpansion


rem Set the path to the Java compiler (javac) and Java runtime (java)
set JAVA_COMPILER_PATH="C:\Program Files\Java\jdk-21\bin\javac.exe"
set JAVA_RUNTIME_PATH="C:\Program Files\Java\jdk-21\bin\java.exe"

rem Set the path to the Java source code
set JAVA_SOURCE_PATH=SecundaryServer\*.java

    rem Compile the Java source code
    %JAVA_COMPILER_PATH% %JAVA_SOURCE_PATH%

    rem Run the Java program with varying arguments in a separate command window
    rem Each server requires the port number to work
    start "Java Program - %%i" %JAVA_RUNTIME_PATH% -cp . SecundaryServer.Main 12345
    start "Java Program - %%i" %JAVA_RUNTIME_PATH% -cp . SecundaryServer.Main 12346
    start "Java Program - %%i" %JAVA_RUNTIME_PATH% -cp . SecundaryServer.Main 12347

endlocal