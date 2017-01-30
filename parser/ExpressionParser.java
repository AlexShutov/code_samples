package com.lodoss.parser;

import java.util.Stack;

/**
 * Created by Boris on 30/01/17.
 */
public class ExpressionParser extends AbstractParser {

    @Override
    public Node parse(String expression) {
        String resultString = processExpression(expression.replaceAll(" ", ""));
        System.out.println(resultString);
        return null;
    }

    private String processExpression(String expression){
        Stack<SymbolType> symbolStack = new Stack<>();
        StringBuilder result = new StringBuilder();
        StringBuilder localSymbol = new StringBuilder();
        for(int i = 0; i < expression.length(); i++){
            char character = expression.charAt(i);
            if(isNumber(character) || isDot(character)){
                localSymbol.append(character);
            } else if(!(isNumber(character) && isDot(character)) && localSymbol.length() > 0){
                handleNextSymbol(localSymbol.toString(), result, symbolStack);
                localSymbol = new StringBuilder();
            }
            if(!isNumber(character) && !isDot(character)){
                handleNextSymbol(String.valueOf(character), result, symbolStack);
            }
        }
        if(localSymbol.length() > 0){
            handleNextSymbol(localSymbol.toString(), result, symbolStack);
            localSymbol = new StringBuilder();
        }

        while (!symbolStack.isEmpty()){
            SymbolType pop = symbolStack.pop();
            result.append(pop.toString())
                .append(" ");
        }

        return result.toString();
    }

    public void handleNextSymbol(String nextSymbol, StringBuilder resultString, Stack<SymbolType> stack){
        SymbolType symbolType = getSymbolTypeFromString(nextSymbol);
        symbolType.handleNewSymbol(resultString, stack, nextSymbol);
    }

    public boolean isNumber(char character){
        if(character >= '0' && character <= '9'){
            return true;
        } else {
            return false;
        }
    }

    public boolean isDot(char character){
        if(character == '.' || character == ','){
            return true;
        } else {
            return false;
        }
    }

    public boolean isOpenBracket(char character){
        if(character == '('){
            return true;
        } else {
            return false;
        }
    }

    public boolean isCloseBracket(char character){
        if(character == ')'){
            return true;
        } else {
            return false;
        }
    }

    public boolean isOperator(char character){
        if(character == '+' || character == '-' || character == '/' || character == '*' || character == '^'){
            return true;
        } else {
            return false;
        }
    }

    public SymbolType getSymbolTypeFromString(String string){
        SymbolType result;
        try{
            double d = Double.parseDouble(string);
            return SymbolType.NUMBER;
        }catch (Exception e){}
        if(string.equals("(")){
            return SymbolType.OPEN_BRACKET;
        } else if (string.equals(")")) {
            return SymbolType.CLOSE_BRACKET;
        } else if(string.equals("+")){
            return SymbolType.OPRATOR_PLUS;
        } else if(string.equals("-")){
            return SymbolType.OPRATOR_MINUS;
        } else if (string.equals("*")) {
            return SymbolType.OPERATOR_MULTIPLY;
        } else if(string.equals("/")){
            return SymbolType.OPRATOR_DIVIDE;
        } else if(string.equals("^")){
            return SymbolType.OPRATOR_POWER;
        } else {
            throw new IllegalArgumentException("This symbol is not supported");
        }
    }
}
