package com.lodoss.parser;

/**
 * Node representation
 */
public class Node {
    private String symbol;
    private Node leftChild;
    private Node rightChild;
    private Node parent;

    public Node(String symbol, Node leftChild, Node rightChild, Node parent) {
        this.symbol = symbol;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.parent = parent;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
