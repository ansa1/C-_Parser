# Lexical analyzer

Takes source code from the input, and produces the token which can be divided to the next 6 groups:

1. Keywords
2. Identifiers
3. Comments
4. Operators
5. Delimiters
6. Literals


## Dependencies

To be able to build and run the application you are supposed to have the following software installed:

 * Oracle JDK 8 ver. 181
 * Maven ver. 3.5.4  
 
 
  and the appropriate environment variables set.


## How to run

To run the program you can use the following scripts:

* `run.bat` for Windows OS
* `run.sh` for Unix based OS 

Or you can type the following commands by yourself in the project root directory:

`mvn package`
<br/>
`cd target`
<br/>
`java -jar CSharpParser-1.0-SNAPSHOT.jar`

After that the lexical analysis results will be written to `out.txt` 
line by line based on the provided input C Sharp sources in `in.txt`.  

In order to run only the implemented JUnit tests you can simply use:


`mvn test` 

## Bonus

* Program finds comments(all, including one line and multiline) and ignore all information between them.
* Program can work with BOM(Byte Order Mark) files. (we lost too much time to find mistake that this is file with BOM!!!)
* Beautiful and easy to understand output

## Assumptions
* We maintain the input code structure in a sense, that blank new lines in sources will remain in the output.
* The Lexical Analyzer expect the given input code to be compilable. 
* The program is intended to be platform independent and support all types of line 
separators: "\r\n" for Windows, "\n" for Linux and MacOS X, "\r" for MacOS 9 and older.
However, the use of the last one is error prone and should be avoided, so we hope you will 
choose EOL character among the first two to work with our Lexical Analyzer. Besides, in case
of automatic testing, the same types of line delimiters are supposed to bee used in the input 
files and the files containing the expected results. 