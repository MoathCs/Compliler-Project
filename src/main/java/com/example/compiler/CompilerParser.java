package com.example.compiler;
import java.io.File;
import java.util.Stack;

public class CompilerParser {

    private static CompilerScanner scanner;
    private String currentToken = "";
//    private String prevToken= currentToken;


    public CompilerParser(File file)  {
        // pass file in constructor to parse it
        scanner = new CompilerScanner(file);

    }

    public CompilerParser(){}


    //Reserved Words
    static  String[] reservedWords = {"module", "begin", "end", "const", "var", "integer", "real", "char", "procedure", "mod", "div", "readint", "readreal", "readchar", "readln", "writeint", "writereal", "writechar","writeln", "if", "then", "else", "while", "do", "loop", "until", "exit", "call"};


    //LL(1) Parsing Table
    //in row terminals in  , columns non terminals
   static int[][] parsingTable = {
            {-1, 1 ,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, 2,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,3 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,4 ,-1 ,4 ,-1, -1,4 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , 4 , -1},
            {-1, -1,6 ,-1 ,5 ,-1, -1,6 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , 6 , -1},
            {7, -1,8 ,-1 ,-1 ,-1, -1,8 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , 8 , -1},
            {-1, -1,10 ,-1 ,-1 ,-1, -1,9 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , 10 , -1},
            {11, -1,12 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , 12 , -1},
            {13, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {14, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,16 ,15,-1 ,-1 ,-1 ,-1 ,-1 , 16 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,17 ,18 ,19 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,21 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , 20 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , 22 , -1},
            {23, -1,23 ,24 ,-1 ,-1, 23,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , 23 , 23 , 23 , 23 ,23 ,23 ,23 ,23 ,23 ,-1 ,24 ,23 ,-1 ,23 ,24 ,23 ,23 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {25, -1,33 ,-1 ,-1 ,-1, 34,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , 26 , 26 , 26 , 26 ,27 ,27 ,27 ,27 ,28 ,-1 ,-1 ,29 ,-1 ,30 ,-1 ,31 ,32 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {35, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {36, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,36 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,36 ,36 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, 38,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , 38 , 37 , 37 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {39, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,39 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,39 ,39 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, 41,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , 41 , 41 , 41 , 40 , 40 , 40 , 40 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {43, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,42 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,43 ,43 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , 44 , 45 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , 46 , 47 , 48 , 49 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , 50 , 51 , 52 , 53 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,54 ,55 ,56 ,57 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {58, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,58 ,58 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,59,-1 ,-1 ,-1 ,-1 ,-1 , 60 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {61, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,62 ,62 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,63 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,-1 ,65 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,64 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,66 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,67 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,68 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,69 ,-1 ,-1 ,-1 ,-1 , -1,-1 ,-1 , -1 , -1},
            {70, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,70 ,70 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,71, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,72 ,73 ,74 ,75 , 76,-1 ,-1 , -1 , -1},
            {77, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,78 ,78 , -1 , -1},
            {-1, -1,-1 ,-1 ,-1 ,-1, -1,-1 ,-1 ,-1,-1 ,-1 ,-1 ,-1 ,-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 ,-1 , -1,79 ,80 , -1 , -1},
    };


   //all Production
    static String[] productions = {
            "module-heading declarations block name .", // 0
            "module name ;", // 1
            "begin stmt-list end", // 2
            "const-decl var-decl procedure-decl", // 3
            "const const-list", // 4
            "", //5
            "name = value ; const-list", // 6
            "",//7
            "var var-list", // 8
            "",//9
            "var-item ; var-list", // 10
            "",//11
            "name-list : data-type", // 12
            "name more-names", // 13
            ", name-list", // 14
            "",//15
            "integer", // 16
            "real",//17
            "char",//18
            "procedure-heading declarations block name ; procedure-decl", // 19
            "",//20
            "procedure name ;", // 21
            "statement ; stmt-list", // 22
            "",//23
            "ass-stmt", // 24
            "read-stmt",//25
            "write-stmt",//26
            "if-stmt",//27
            "while-stmt",//28
            "loop-stmt",//29
            "exit-stmt",//30
            "call-stmt",//31
            "block",//32
            "",//33
            "name := exp", // 34
            "term exp-prime", // 35
            "add-oper term exp-prime", // 36
            "",//37
            "factor term-prime", // 38
            "mul-oper factor term-prime", // 39
            "",//40
            "( exp )", // 41
            "name-value",//42
            "+", // 43
            "-",//44
            "*", //45
            "/",//46
            "mod",//47
            "div",//48
            "readint ( name-list )", // 49
            "readreal ( name-list )",//50
            "readchar ( name-list )",//51
            "readln",//52
            "writeint ( write-list )", // 53
            "writereal ( write-list )",//54
            "writechar ( write-list )",//55
            "writeln",//56
            "write-item more-write-value", // 57
            ", write-list", // 58
            "",//59
            "name", // 60
            "value",//61
            "if condition then stmt-list else-part end", // 62
            "else stmt-list", // 63
            "",//64
            "while condition do stmt-list end", // 65
            "loop stmt-list until condition", // 66
            "exit", // 67
            "call name", // 68
            "name-value relational-oper name-value", // 69
            "=", // 70
            "|=",//71
            "<",//72
            "<=",//73
            ">",//74
            ">=",//75
            "name", // 76
            "value",//78
            "integer-value", // 79
            "real-value"//80
    };

    //non Terminals
    static String[] nonTerminals= {
            "module-decl","module-heading","block","declarations","const-decl","const-list","var-decl","var-list","var-item"
            ,"name-list","more-names","data-type","procedure-decl","procedure-heading","stmt-list","statement"
            ,"ass-stmt","exp","exp-prime","term","term-prime","factor","add-oper","mul-oper","read-stmt","write-stmt",
            "write-list","more-write-value","write-item","if-stmt","else-part","while-stmt","loop-stmt","exit-stmt"
            ,"call-stmt","condition","relational-oper","name-value","value"
    };

    //Terminals
   static String[] terminals={
            "name","module","begin","end","const","=",";","var",":", ","  ,"integer","real","char",":=", "(",")","+","-","*","/","mod","div","readint","readreal","readchar"
            ,"readln","writeint","writereal","writechar","writeln"
            ,"if","then","else","while","do","loop","until","exit","call","|=","<","<=",">",">=","integer-value","real-value","procedure","."
    };



   //this method to get the next token
    private void getNextToken() {
        currentToken = scanner.nextToken();
    }

    //this method for integers numbers
    public static boolean isNumeric(String num) {
        return num != null && num.matches("\\d+");
    }

    //this method for float numbers
    public static boolean isFloat(String num) {
        if (num == null || num.isEmpty()) {
            return false;
        }
        try {
            Float.parseFloat(num);
            return num.contains("."); // Ensure it is a floating-point number
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
    this method is to check if the name is valid according to the given language
    the name must start with a letter, then you have the option to put a letter or a number
    */
    public static boolean isName(String name) throws Exception {
        for (int i=0 ; i < reservedWords.length ; i++){ //the name must be not from the reserved Words
            if(name.equals(reservedWords[i])){
                return false;
//                throw new IllegalArgumentException ("Do not use reserved  words for language  " + (scanner.tokenLineNum));
            }
        }
        if (name == null || name.isEmpty()) {
            return false;
        }
        boolean hasLetter = false;
        for (char c : name.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
        }
        return hasLetter;
    }

    //lead Method :
    public void parse() throws Exception {
        Stack<String> stack = new Stack<>();
        stack.push("module-decl"); // Starting
        getNextToken();
        while (!stack.isEmpty()) { //loop until the stack is empty
            if (isTerminal(stack.peek())) { //if top of the stack terminals do
                if (isName(currentToken)) { //if the currentToken is name i need to pop from the stack and get the next token
                    stack.pop();
                    getNextToken();
                } else if (isNumeric(currentToken)) { //if currentToken is number (number without . not floating point) i need to pop from the stack and get the next token
                    stack.pop();
                    getNextToken();
                }
                else if (isFloat(currentToken)) { //if currentToken is number has . i need to pop from the stack and get the next token
                stack.pop();
                getNextToken();
                }
                else if(stack.peek().equals(currentToken)) { //if the top of the stack equal the currentToken i need to pop from the stack and get the next token
                        stack.pop();
                        getNextToken();
                    }
                else { //otherwise  Error
                  throw new RuntimeException("Syntax error: Expected '" + stack.peek() + "' but found '" + currentToken+"' at line '"+scanner.tokenLineNum+"'");
                }
            }

            /*
            Note: The order of the terminal and the non-terminal in the arrays (terminal and non-terminal) is the same
            as their order in the table, so I bring the index from the arrays and find the production number from the table
            and bring the production from the production array according to production number
             */
            else { //if top of the stack is non terminals
                Integer ruleIndex; // to get production number from the parsing table
                if (isNumeric(currentToken)) {
                    ruleIndex = parsingTable[getNonTerminalIndex(stack.peek())][getTerminalIndex("integer-value")]; //get index for terminal and 'integer-value'
                }
                else if (isFloat(currentToken)) {
                        ruleIndex = parsingTable[getNonTerminalIndex(stack.peek())][getTerminalIndex("real-value")]; //get index for terminal and 'real-value'
                }
                else if (isName(currentToken)){
                    ruleIndex = parsingTable[getNonTerminalIndex(stack.peek())][getTerminalIndex("name")];//get index for terminal and 'name'
                }else {
                    ruleIndex = parsingTable[getNonTerminalIndex(stack.peek())][getTerminalIndex(currentToken)];//find the index of the terminal from terminal array & Find the index of the non-terminal from non-terminal array
                }
                if (ruleIndex != -1) {
                    String rule = productions[ruleIndex - 1]; // Get the production rule from the array his name is  productions ( [ruleIndex-1]  from i start from 0 in the array)
                    stack.pop();
                    String[] ruleSymbols = rule.split("\\s+"); // Split rule
                    // Push rule to the stack in reverse order
                    for (int i = ruleSymbols.length - 1; i >= 0; i--) {
                        if (!ruleSymbols[i].equals("")) { // Handle (lamda) as an empty string
                            stack.push(ruleSymbols[i]);
                        }
                    }
                } else { // in this the ruleIndex will be = -1 (not found) so throw Exception
                    throw new RuntimeException("Syntax error: Unexpected token '" + currentToken + "' at line '" + scanner.tokenLineNum+"'");
                }
            }
        }
        if (!currentToken.isEmpty()) { //if there is anything left after '.' : Error
            scanner.close();
            throw new Exception ("Code must not have any token after '.' and you have " + currentToken + " after '.'");
        }
        // If parsing is successful and no syntax errors
        System.out.println("Parsing successful!");
    }
//===========================================================================



    //this method to check if a symbol is terminal
    private boolean isTerminal(String symbol) {
        for (String terminal : terminals) {
            if (terminal.equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    //this method to check if the name is from Reserved Words
    private boolean isReservedWords(String symbol) {
        for (String reservedWord : reservedWords) {
            if (reservedWord.equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    //this method to get the index of a non-terminal in the parsing table
    private int getNonTerminalIndex(String nonTerminal) {
        for (int i = 0; i < nonTerminals.length; i++) {
            if (nonTerminals[i].equals(nonTerminal)) {
                return i;
            }
        }
        return -1;
    }

    // this method to get the index of a terminal in the parsing table
    private int getTerminalIndex(String terminal) {
        for (int i = 0; i < terminals.length; i++) {
            if (terminals[i].equals(terminal)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        try {
            CompilerParser parser = new CompilerParser(new File("code.txt"));
            parser.parse();
        } catch (Exception e) {
            System.err.println("Error parsing input: ");
            e.printStackTrace();
        }

    }
}