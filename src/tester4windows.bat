@echo off
setlocal enabledelayedexpansion

rem Set the number of times to run the program (N)
set N=15

rem Set the path to the Java compiler (javac) and Java runtime (java)
set JAVA_COMPILER_PATH="C:\Program Files\Java\jdk-21\bin\javac.exe"
set JAVA_RUNTIME_PATH="C:\Program Files\Java\jdk-21\bin\java.exe"

rem Set the path to the Java source code
set JAVA_SOURCE_PATH=cliente\Main.java

rem Loop N times
for /L %%i in (1,1,%N%) do (
    rem Compile the Java source code
    %JAVA_COMPILER_PATH% %JAVA_SOURCE_PATH%

    rem Run the Java program with varying arguments in a separate command window
    start "Java Program - %%i" %JAVA_RUNTIME_PATH% -cp . cliente.Main C:\Users\henri\Desktop\test.txt %%i

    rem Add a delay if needed (optional)
    timeout /t 0 /nobreak >nul
)

endlocal
