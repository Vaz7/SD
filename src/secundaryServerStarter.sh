

# Set the path to the Java compiler (javac) and Java runtime (java)
JAVA_COMPILER_PATH="/bin/javac"
JAVA_RUNTIME_PATH="/bin/java"

# Set the path to the JAR file
JAR_FILE="sd23.jar"

# Compile the Java source code
$JAVA_COMPILER_PATH -cp $JAR_FILE SecundaryServer/*.java cmd/*.java server/*.java cliente/*.java

# Run the Java program with varying arguments
gnome-terminal --title="Java Program - $i" -- $JAVA_RUNTIME_PATH -cp $JAR_FILE:. SecundaryServer.Main 12345
gnome-terminal --title="Java Program - $i" -- $JAVA_RUNTIME_PATH -cp $JAR_FILE:. SecundaryServer.Main 12346
gnome-terminal --title="Java Program - $i" -- $JAVA_RUNTIME_PATH -cp $JAR_FILE:. SecundaryServer.Main 12347

