# Assignment_1

Expression Calculator which take an expression from the input and produce the result of calculations.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Functions

Calculator can sum, substract, multiply factors, compare (>, <, =) terms and prioritize via braces.

## Program Code

I choose Java language, because it is easy to implement, read and modify. In this block, I describe each file of the project. 

### Classes

I have 6 classes for AST tree and 1 class for testing

#### Tree traverser

Main class is, naturally, "Main.java". This class work run calculator and produce output, run tesing class and check the errors.
Other classes have dependences:
Expression ->  Relation
Relation -> Term
Term -> Factor
Factor -> Primary
Primary -> integer | Expression
As we see, this is not a tree, because there is a cycle, so program works recursively.

### Testing

Unit-testing implemented with "Testing.java" and check every operation of calculator. I use AAA-pattern(arrange, act, assert) for comfortable reading. One test - one method.
Every time my program run Testing.java before calculating "in.txt" file, so if problems exist -> program produces error before running input.

### How to run

* Run JAR file
    + via command line `java -jar run.jar` (for Unix systems)
    + double-click to "run.jar" (for Windows)


### Extra

+ Calculator works using long arithmetic(BigInteger in Java, in my case), so program can work with integers which module is bigger than 2^63. Yes, it is a little slowly, but not critically as wrong answer!
