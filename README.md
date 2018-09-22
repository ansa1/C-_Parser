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

* Program finds comments(all, including one line and multiline) and ingore all information between them.
* Program can work with BOM(Byte Order Mark) files. (we lost too much time to find mistake that this is file with BOM!!!)
* Beautiful and easy to understand output

## Assumptions
* Code structure keeps in output
* Lexical analyzer assumes that the given input code is compilable 