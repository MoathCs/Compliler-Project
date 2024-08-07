package com.example.compiler;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class CompilerScanner {
    Scanner scanner;//for reading the file
    int currentIndex = -1;// iniatialize the current index to -1
    String currentLine = "";// Initialize the current Line to ""
    int tokenLineNum = 0;//Initialize tokenLineNum to 0



    public CompilerScanner(File file) {
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found: " + file.getPath(), e); // if file not found
        }
    }

    public String nextToken() {
        //Repeat until a character is found or the end is reached
        while (currentIndex == currentLine.length() - 1 || (currentLine.isEmpty() || currentLine.isBlank())) {
            if (!scanner.hasNextLine()) { // Check if there is no next line in fIle
                scanner.close();//close scanner
                return "";//empty string indicates that the file has ended (reached the end).
            }
            getNewLine();//get new line
        }
        getNextChar();//next char
        return getToken();//token from current
    }

    //this method to get line from the file
    private void getNewLine() {
        currentLine = scanner.nextLine();//read next line from file
        tokenLineNum++; //Increase by one to know the number of line
        currentIndex = -1;//reset the current index to -1
    }

    //this method to get next char from current line
    private Character getNextChar() {
        if (currentIndex == currentLine.length() - 1) // return null if this is end of line
            return null;
        else // otherwise increment currentIndex to get new char
            currentIndex++;
        return currentLine.charAt(currentIndex);  // return new char
    }

    private void getPrevChar() {
        if (currentIndex > 0) { //go back if current Index is greater than 0
            currentIndex--; // Decrement currentIndex to go back
        }
    }

    //this method to get the token form the currnt char
    private String getToken() {
        char currentChar = currentLine.charAt(currentIndex);//currnt char
        if (currentChar == ' ' || currentChar == '\t') { // if space or tap get the next
            return nextToken();
        } else if (currentChar == '.' || currentChar == ';' || currentChar == '(' || currentChar == ',' ||
                currentChar == ')' || currentChar == '+' || currentChar == '-' || currentChar == '*' ||
                currentChar == '/' || currentChar == '=') { // if the current char from this symbol return it
            return String.valueOf(currentChar);
        } else if (currentChar == '|' || currentChar == '<' || currentChar == '>' || currentChar == ':') { // If the current character is one of these symbols,,, it may be followed by '=' so I return the method to check it
            return getCompleteToken(String.valueOf(currentChar));
        } else if (Character.isDigit(currentChar)) { //if the character is digit complete the number to return the full number
            return completeNumber();
        } else if (Character.isLetter(currentChar)) { //if the character is digit complete the letter to return the full letter
            return completeName();
        } else { //if the character is illegal, throw an exception
            scanner.close(); //
            throw new IllegalArgumentException("Line: " + tokenLineNum + " ,illegal character: '" + currentLine.charAt(currentIndex)+"'"); //IllegalArgumentException
        }
    }



    //this method for if the current cahr is: ">" OR "<" OR "|" OR ":" I want to check if the character after it is "=" the current and next character will be returned as one symbol if not "=" the current character alone will be returned
    private String getCompleteToken(String completeToken) {
        char expectedChar = '=';
        Character nextChar = getNextChar();
        if (nextChar != null && nextChar == '=') { //if next is "="
            return completeToken + "="; //return the current and next
        } else {
            getPrevChar(); // otherwise go back
            if (expectedChar != '\0') {
                return completeToken; //return the current only
            } else {//throw exception
                scanner.close();
                throw new IllegalArgumentException("Line: " + tokenLineNum + " ,illegal character: '" + currentLine.charAt(currentIndex)+"'");
            }
        }
    }



    //this method if the curr char is letter ,,,,letter ( letter | digit )*
    private String completeName() {
        Character currentChar = currentLine.charAt(currentIndex); // get first char
        StringBuilder name = new StringBuilder(); //string builder to reseve name in it
        while (currentChar != null && Character.isLetterOrDigit(currentChar)) { // Loop until a NON-letter or NON-digit is found
            name.append(currentChar); //append to String Builder
            currentChar = getNextChar(); //get next char
        }
        if (currentChar != null)  //if currentChar not null mean is a NON-letter or NON-digit is found so i go back
            getPrevChar();

        return name.toString();
    }




    //this method to if the curr char is digit
    private String completeNumber() {
        StringBuilder number = new StringBuilder();//string builder to reseve number in it
        Character currentChar = currentLine.charAt(currentIndex); //to get the first char

        while (currentChar != null && (Character.isDigit(currentChar) || currentChar == '.')) { // Loop until a non-digit and non-dot is found
            if (currentChar == '.') {
                // Check if a decimal point has already been added to the number
                if (number.toString().contains(".")) {
                    break; //exit the loop if a second decimal point is found
                }
            }

            number.append(currentChar); // Append the current character to the number
            currentChar = getNextChar(); // Move to the next character
        }

        //ensure the last character is a digit
        if (number.length() == 0 || !Character.isDigit(number.charAt(number.length() - 1))) {
            scanner.close();
            throw new IllegalArgumentException("Line " + tokenLineNum + " ,illegal character: '" + number + "'");

        }

        if (currentChar != null) {
            getPrevChar(); //go back if the current char is not null
        }

        return number.toString();
    }


    //this method to close scanner
    public void close() {
        scanner.close();
    }


    public static void main(String[] args) {
        try {
            CompilerScanner compilerScanner = new CompilerScanner(new File("code.txt"));
            String token = compilerScanner.nextToken();
            while (!token.isEmpty()) {
                System.out.println(token);
                token = compilerScanner.nextToken();
            }
        } catch (Exception e) {
            System.err.println("Error reading file: ");
            e.printStackTrace();
        }
    }

}
