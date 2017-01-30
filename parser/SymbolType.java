package com.lodoss.parser;

import java.util.Stack;

/**
 * Representation of item in expression
 */
public enum SymbolType {
    NUMBER{

        @Override
        int getPriority() {
            return -1;
        }

        @Override
        void handleNewSymbol(StringBuilder resultString, Stack<SymbolType> stackSymbol,  String value) {
            resultString.append(value)
            .append(" ");
        }

        @Override
        boolean isOperation() {
            return false;
        }
    },
    OPRATOR_PLUS{
        @Override
        int getPriority() {
            return 6;
        }

        @Override
        public String toString() {
            return "+";
        }

        @Override
        boolean isOperation() {
            return true;
        }
    },
    OPRATOR_MINUS{
        @Override
        int getPriority() {
            return 6;
        }

        @Override
        public String toString() {
            return "-";
        }

        @Override
        boolean isOperation() {
            return true;
        }
    },
    OPRATOR_DIVIDE{
        @Override
        int getPriority() {
            return 7;
        }

        @Override
        public String toString() {
            return "/";
        }

        @Override
        boolean isOperation() {
            return true;
        }
    },
    OPERATOR_MULTIPLY {
        @Override
        int getPriority() {
            return 7;
        }

        @Override
        public String toString() {
            return "*";
        }

        @Override
        boolean isOperation() {
            return true;
        }
    },
    OPRATOR_POWER{
        @Override
        int getPriority() {
            return 8;
        }

        @Override
        public String toString() {
            return "^";
        }

        @Override
        boolean isOperation() {
            return true;
        }
    },
    OPEN_BRACKET{
        @Override
        int getPriority() {
            return 0;
        }

        @Override
        public String toString() {
            return "(";
        }

        @Override
        void handleNewSymbol(StringBuilder resultString, Stack<SymbolType> stackSymbol, String val) {
            stackSymbol.push(this);
        }

        @Override
        boolean isOperation() {
            return false;
        }
    },
    CLOSE_BRACKET{
        @Override
        int getPriority() {
            return 1;
        }

        @Override
        public String toString() {
            return ")";
        }

        @Override
        void handleNewSymbol(StringBuilder resultString, Stack<SymbolType> stackSymbol, String val) {
            SymbolType pop = stackSymbol.pop();
            while (!stackSymbol.isEmpty() && pop != SymbolType.OPEN_BRACKET){
                resultString.append(pop.toString())
                .append(" ");
                pop = stackSymbol.pop();
            }
        }

        @Override
        boolean isOperation() {
            return false;
        }
    };

    /**
     * Provides priority of certain item
     * @return
     */
    abstract int getPriority();

    void handleNewSymbol(StringBuilder resultString, Stack<SymbolType> stackSymbol, String val){
        if(stackSymbol.isEmpty()){
            stackSymbol.push(this);
        } else if(stackSymbol.peek().getPriority() <= this.getPriority()){
            stackSymbol.push(this);
        } else {
            while (!stackSymbol.isEmpty() &&
                    stackSymbol.peek().isOperation() &&
                    stackSymbol.peek().getPriority() > getPriority()){
                SymbolType pop = stackSymbol.pop();
                resultString.append(pop.toString())
                    .append(" ");
            }
            stackSymbol.push(this);
        }
    }

    abstract boolean isOperation();
}
