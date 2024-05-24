# Student-Record-Finder
This Java code is designed to traverse through the file system starting from a specified directory, visiting each file and directory encountered along the way. Here's an overview of what the code does:

1. Importing Necessary Libraries: The code imports classes from `java.io.IOException`, `java.nio.file.*`, and `java.nio.file.attribute.BasicFileAttributes`.

2. Main Class Definition (`Main`): The `Main` class contains the `main` method, serving as the entry point for the program.

3. Creating the Starting Path: An instance of the `Path` class named `startingPath` is created, representing the directory from which the traversal will begin.

4. Instantiating a `StatsVisitor` Object: An object of the inner class `StatsVisitor` is created, which extends `SimpleFileVisitor<Path>`. This visitor class defines the behavior for file and directory traversal.

5. Executing File and Directory Traversal: The `walkFileTree` method from the `Files` class is called, passing `startingPath` and the `StatsVisitor` object. This method initiates the recursive traversal of files and directories.

6. Exception Handling: Any `IOException` occurring during traversal is caught, and a new `RuntimeException` is thrown to terminate the program.

7. Inner Class Definition (`StatsVisitor`): This class extends `SimpleFileVisitor<Path>` and provides methods to handle file and directory traversal:

   - The `visitFile` method is invoked for each file visited, printing the file's name.
   - The `preVisitDirectory` method is called before visiting a directory, printing the directory's name.
   - The `postVisitDirectory` method is executed after visiting a directory, decrementing the current traversal level.

In summary, the code recursively traverses the file system, visiting each file and directory encountered and printing their names during the traversal process.
