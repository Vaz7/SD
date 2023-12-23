#!/bin/bash
#this code was auto generated, please test and fix before usage!!
# Set the number of times to run the program (N)
N=5

# Set the path to the Java compiler (javac) and Java runtime (java)
JAVA_COMPILER_PATH="/bin/javac"
JAVA_RUNTIME_PATH="/bin/java"

# Set the path to the Java source code
JAVA_SOURCE_PATH="cliente/*.java"

# Loop N times
for ((i=1; i<=N; i++)); do
    # Compile the Java source code
    $JAVA_COMPILER_PATH $JAVA_SOURCE_PATH

    # Run the Java program with varying arguments
    gnome-terminal --title="Java Program - $i" -- $JAVA_RUNTIME_PATH -cp . cliente.Main /home/goncalo/Desktop/notas.md $i

    # Add a delay if needed (optional)
    #sleep 1
done
