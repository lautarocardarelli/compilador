package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.ParserSym;
import lyc.compiler.model.*;
import lyc.compiler.files.SymbolTableGenerator;
import static lyc.compiler.constants.Constants.*;

%%

%public
%class Lexer
%unicode
%cup
%line
%column
%throws CompilerException
%eofval{
  return symbol(ParserSym.EOF);
%eofval}


%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }

  SymbolTableGenerator tabla = SymbolTableGenerator.getStgInstance();
%}


LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Identation =  [ \t\f]

Space = " "
Plus = "+"
Mult = "*"
Sub = {Space}"-"{Space}
Div = "/"
Assig = "="
Porc = "%"
OpenBracket = "("
CloseBracket = ")"
QuotationMark = \"
Letter = [a-zA-Z] | "á" | "é" | "í" | "ó" | "ú"
Digit = [0-9]
COMA = ","
PUNTO_COMA = ";"
PUNTO = "."
OPERACION_TIPO = ":"
CORCH_ABIERTO = "["
CORCH_CERRADO = "]"
LLAVE_ABIERTA = "{"
LLAVE_CERRADA = "}"
MENOR_IG = "<="
MENOR = "<"
MAYOR_IG = ">="
MAYOR = ">"
IGUAL = "=="
DISTINTO = "!="
ARROBA = "@"
CARACT = "½"
CARACT2 = "╗"
WEIRD_QUOTATION_OP = “
WEIRD_QUOTATION_CL = ”
AllowedSymbols = {Plus} | {Mult} | {Sub} | {Div} | {Assig} | {OpenBracket} | {CloseBracket} | {Porc} | {COMA} |
                 {PUNTO_COMA} | {OPERACION_TIPO} | {ARROBA} | {CARACT} | {CARACT2} | {QuotationMark} | {PUNTO} |
                 {WEIRD_QUOTATION_OP} | {WEIRD_QUOTATION_CL}

// Palabras reservadas
WHILE = "WHILE" | "While" | "while"
DO = "DO" | "Do" | "do"
CASE = "CASE" | "Case" | "case"
DEFAULT = "DEFAULT" | "Default" | "default"
ENDDO = "ENDDO" | "Enddo" | "enddo"
IF = "IF" | "If" | "if"
ELSE = "ELSE" | "Else" | "else"
WRITE = "WRITE" | "Write" | "write"
READ = "READ" | "Read" | "read"
OR = "OR" | "Or" | "or"
AND = "AND" | "And" | "and"
NOT = "NOT" | "Not" | "not"
init = "INIT" | "Init" | "init"
float = "FLOAT" | "Float" | "float"
int = "INT" | "Int" | "int"
string = "STRING" | "String" | "string"
ALLEQUAL = "AllEqual" | "ALLEQUAL" | "allequal"

WhiteSpace = {LineTerminator} | {Identation}
Identifier = {Letter} ({Letter}|{Digit})*
IntegerConstant = {Digit}+ | "-"?{Digit}+
FloatConstant = "-"?{PUNTO}{Digit}+|"-"?{Digit}+{PUNTO}{Digit}*
StringConstant = {QuotationMark} ({Letter}|{Digit}|{Space}|{AllowedSymbols})* {QuotationMark}
Comment = {Div}{Mult} ({Letter}|{Digit}|{Space}|{AllowedSymbols})* {Mult}{Div}


%%


/* keywords */

<YYINITIAL> {
  /* reserverd words */
    {IF}                                      { return symbol(ParserSym.IF); }
    {ELSE}                                    { return symbol(ParserSym.ELSE); }
    {WHILE}                                   { return symbol(ParserSym.WHILE); }
    {READ}                                    { return symbol(ParserSym.READ); }
    {WRITE}                                   { return symbol(ParserSym.WRITE); }
    {AND}                                     { return symbol(ParserSym.AND); }
    {OR}                                      { return symbol(ParserSym.OR); }
    {NOT}                                     { return symbol(ParserSym.NOT); }
    {init}                                    { return symbol(ParserSym.INIT); }
    {int}                                     { return symbol(ParserSym.INT); }
    {string}                                  { return symbol(ParserSym.STRING); }
    {float}                                   { return symbol(ParserSym.FLOAT); }
    {DO}                                      { return symbol(ParserSym.DO); }
    {CASE}                                    { return symbol(ParserSym.CASE); }
    {DEFAULT}                                 { return symbol(ParserSym.DEFAULT); }
    {ENDDO}                                   { return symbol(ParserSym.ENDDO); }
    {Comment}                                 { /* Ignore */ }
    {ALLEQUAL}                                { return symbol(ParserSym.ALLEQUAL); }


  /* operators */
  {Plus}                                    { return symbol(ParserSym.PLUS); }
  {Sub}                                     { return symbol(ParserSym.SUB); }
  {Mult}                                    { return symbol(ParserSym.MULT); }
  {Div}                                     { return symbol(ParserSym.DIV); }
  {Assig}                                   { return symbol(ParserSym.ASSIG); }
  {OpenBracket}                             { return symbol(ParserSym.OPEN_BRACKET); }
  {CloseBracket}                            { return symbol(ParserSym.CLOSE_BRACKET); }
  {COMA}                                    { return symbol(ParserSym.COMA); }
  {PUNTO_COMA}                              { return symbol(ParserSym.PUNTO_COMA); }
  {OPERACION_TIPO}                          { return symbol(ParserSym.OPERACION_TIPO); }
  {CORCH_ABIERTO}                           { return symbol(ParserSym.CORCH_ABIERTO); }
  {CORCH_CERRADO}                           { return symbol(ParserSym.CORCH_CERRADO); }
  {LLAVE_ABIERTA}                           { return symbol(ParserSym.LLAVE_ABIERTA); }
  {LLAVE_CERRADA}                           { return symbol(ParserSym.LLAVE_CERRADA); }
  {MENOR_IG}                                { return symbol(ParserSym.MENOR_IG); }
  {MENOR}                                   { return symbol(ParserSym.MENOR); }
  {MAYOR_IG}                                { return symbol(ParserSym.MAYOR_IG); }
  {MAYOR}                                   { return symbol(ParserSym.MAYOR); }
  {IGUAL}                                   { return symbol(ParserSym.IGUAL); }
  {DISTINTO}                                { return symbol(ParserSym.DISTINTO); }
  {PUNTO}                                   { return symbol(ParserSym.PUNTO); }

    /* identifiers */
    {Identifier}                              { if (yylength() > 40) throw new InvalidLengthException("Id max length is 40 characters");
                                                tabla.save("ID", yytext());
                                                return symbol(ParserSym.IDENTIFIER, yytext()); }
    /* Constants */
    {IntegerConstant}                         {
                                                try {

                                                    if (Integer.valueOf(yytext()) >= 32767 || Integer.valueOf(yytext()) <= -32768) throw new InvalidIntegerException("Integer should be between 32767 and -32767");
                                                    tabla.save("CTE_INTEGER", yytext());
                                                    return symbol(ParserSym.INTEGER_CONSTANT, yytext());
                                                } catch (Exception ex) {
                                                    throw new InvalidIntegerException("Integer should between 32767 and -32767");
                                                }
                                              }
    {StringConstant}                          { if (yylength() -2 > 40) throw new InvalidLengthException("String constats supports until 40 characters");
                                                tabla.save("CTE_STRING", yytext());
                                                return symbol(ParserSym.STRING_CONSTANT, yytext());
                                              }
    {FloatConstant}                           {
                                                try {
                                                    if (Float.valueOf(yytext()) <= Math.pow(1.18,-38) || Float.valueOf(yytext()) >= Math.pow(3.4,38)) throw new InvalidIntegerException("Float should be between " + Math.pow(1.18,-38) + "and " + Math.pow(3.4,38));
                                                    tabla.save("CTE_FLOAT", yytext());
                                                    return symbol(ParserSym.FLOAT_CONSTANT, yytext());
                                                } catch (Exception ex) {
                                                    throw new InvalidIntegerException("Float max value is:" + Float.MAX_VALUE);
                                                }
                                              }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}


/* error fallback */
[^]                              { throw new UnknownCharacterException(yytext()); }
