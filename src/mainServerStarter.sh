

# Set the path to the Java compiler (javac) and Java runtime (java)
JAVA_COMPILER_PATH="/bin/javac"
JAVA_RUNTIME_PATH="/bin/java"

# Set the path to the Java source code
JAVA_SOURCE_PATH="server/*.java"


    # Compile the Java source code
    $JAVA_COMPILER_PATH $JAVA_SOURCE_PATH

    # Run the Java program with varying arguments
    gnome-terminal --title="Java Program - $i" -- $JAVA_RUNTIME_PATH -cp . server.Main 127.0.0.1 12345 127.0.0.1 12346 127.0.0.1 12347

done
