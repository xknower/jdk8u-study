package com.sun.tools.example.debug.expr;

public interface ExpressionParserConstants {

  int EOF = 0;
  int SINGLE_LINE_COMMENT = 6;
  int FORMAL_COMMENT = 7;
  int MULTI_LINE_COMMENT = 8;
  int ABSTRACT = 9;
  int BOOLEAN = 10;
  int BREAK = 11;
  int BYTE = 12;
  int CASE = 13;
  int CATCH = 14;
  int CHAR = 15;
  int CLASS = 16;
  int CONST = 17;
  int CONTINUE = 18;
  int _DEFAULT = 19;
  int DO = 20;
  int DOUBLE = 21;
  int ELSE = 22;
  int EXTENDS = 23;
  int FALSE = 24;
  int FINAL = 25;
  int FINALLY = 26;
  int FLOAT = 27;
  int FOR = 28;
  int GOTO = 29;
  int IF = 30;
  int IMPLEMENTS = 31;
  int IMPORT = 32;
  int INSTANCEOF = 33;
  int INT = 34;
  int INTERFACE = 35;
  int LONG = 36;
  int NATIVE = 37;
  int NEW = 38;
  int NULL = 39;
  int PACKAGE = 40;
  int PRIVATE = 41;
  int PROTECTED = 42;
  int PUBLIC = 43;
  int RETURN = 44;
  int SHORT = 45;
  int STATIC = 46;
  int SUPER = 47;
  int SWITCH = 48;
  int SYNCHRONIZED = 49;
  int THIS = 50;
  int THROW = 51;
  int THROWS = 52;
  int TRANSIENT = 53;
  int TRUE = 54;
  int TRY = 55;
  int VOID = 56;
  int VOLATILE = 57;
  int WHILE = 58;
  int INTEGER_LITERAL = 59;
  int DECIMAL_LITERAL = 60;
  int HEX_LITERAL = 61;
  int OCTAL_LITERAL = 62;
  int FLOATING_POINT_LITERAL = 63;
  int EXPONENT = 64;
  int CHARACTER_LITERAL = 65;
  int STRING_LITERAL = 66;
  int IDENTIFIER = 67;
  int LETTER = 68;
  int DIGIT = 69;
  int LPAREN = 70;
  int RPAREN = 71;
  int LBRACE = 72;
  int RBRACE = 73;
  int LBRACKET = 74;
  int RBRACKET = 75;
  int SEMICOLON = 76;
  int COMMA = 77;
  int DOT = 78;
  int ASSIGN = 79;
  int GT = 80;
  int LT = 81;
  int BANG = 82;
  int TILDE = 83;
  int HOOK = 84;
  int COLON = 85;
  int EQ = 86;
  int LE = 87;
  int GE = 88;
  int NE = 89;
  int SC_OR = 90;
  int SC_AND = 91;
  int INCR = 92;
  int DECR = 93;
  int PLUS = 94;
  int MINUS = 95;
  int STAR = 96;
  int SLASH = 97;
  int BIT_AND = 98;
  int BIT_OR = 99;
  int XOR = 100;
  int REM = 101;
  int LSHIFT = 102;
  int RSIGNEDSHIFT = 103;
  int RUNSIGNEDSHIFT = 104;
  int PLUSASSIGN = 105;
  int MINUSASSIGN = 106;
  int STARASSIGN = 107;
  int SLASHASSIGN = 108;
  int ANDASSIGN = 109;
  int ORASSIGN = 110;
  int XORASSIGN = 111;
  int REMASSIGN = 112;
  int LSHIFTASSIGN = 113;
  int RSIGNEDSHIFTASSIGN = 114;
  int RUNSIGNEDSHIFTASSIGN = 115;

  int DEFAULT = 0;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "<SINGLE_LINE_COMMENT>",
    "<FORMAL_COMMENT>",
    "<MULTI_LINE_COMMENT>",
    "\"abstract\"",
    "\"boolean\"",
    "\"break\"",
    "\"byte\"",
    "\"case\"",
    "\"catch\"",
    "\"char\"",
    "\"class\"",
    "\"const\"",
    "\"continue\"",
    "\"default\"",
    "\"do\"",
    "\"double\"",
    "\"else\"",
    "\"extends\"",
    "\"false\"",
    "\"final\"",
    "\"finally\"",
    "\"float\"",
    "\"for\"",
    "\"goto\"",
    "\"if\"",
    "\"implements\"",
    "\"import\"",
    "\"instanceof\"",
    "\"int\"",
    "\"interface\"",
    "\"long\"",
    "\"native\"",
    "\"new\"",
    "\"null\"",
    "\"package\"",
    "\"private\"",
    "\"protected\"",
    "\"public\"",
    "\"return\"",
    "\"short\"",
    "\"static\"",
    "\"super\"",
    "\"switch\"",
    "\"synchronized\"",
    "\"this\"",
    "\"throw\"",
    "\"throws\"",
    "\"transient\"",
    "\"true\"",
    "\"try\"",
    "\"void\"",
    "\"volatile\"",
    "\"while\"",
    "<INTEGER_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<HEX_LITERAL>",
    "<OCTAL_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<CHARACTER_LITERAL>",
    "<STRING_LITERAL>",
    "<IDENTIFIER>",
    "<LETTER>",
    "<DIGIT>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\",\"",
    "\".\"",
    "\"=\"",
    "\">\"",
    "\"<\"",
    "\"!\"",
    "\"~\"",
    "\"?\"",
    "\":\"",
    "\"==\"",
    "\"<=\"",
    "\">=\"",
    "\"!=\"",
    "\"||\"",
    "\"&&\"",
    "\"++\"",
    "\"--\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"&\"",
    "\"|\"",
    "\"^\"",
    "\"%\"",
    "\"<<\"",
    "\">>\"",
    "\">>>\"",
    "\"+=\"",
    "\"-=\"",
    "\"*=\"",
    "\"/=\"",
    "\"&=\"",
    "\"|=\"",
    "\"^=\"",
    "\"%=\"",
    "\"<<=\"",
    "\">>=\"",
    "\">>>=\"",
  };

}
