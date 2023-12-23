

# Set the path to the Java compiler (javac) and Java runtime (java)
JAVA_COMPILER_PATH="/bin/javac"
JAVA_RUNTIME_PATH="/bin/java"

# Set the path to the Java source code
JAVA_SOURCE_PATH="SecundaryServer/*.java"


    # Compile the Java source code
    $JAVA_COMPILER_PATH $JAVA_SOURCE_PATH

    # Run the Java program with varying arguments
    gnome-terminal --title="Java Program - $i" -- $JAVA_RUNTIME_PATH -cp . SecundaryServer.Main 12345
    gnome-terminal --title="Java Program - $i" -- $JAVA_RUNTIME_PATH -cp . SecundaryServer.Main 12346
    gnome-terminal --title="Java Program - $i" -- $JAVA_RUNTIME_PATH -cp . SecundaryServer.Main 12347
done
