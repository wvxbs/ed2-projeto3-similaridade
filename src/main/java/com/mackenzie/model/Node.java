/**
 * Projeto 2
 * Integrantes:
 * Gabriel Ferreira - RA: 10442043
 * Gian Lucca Campanha Ribeiro - RA: 10438361
 * Kaiki Bellini Barbosa - RA: 10402509
 */

package com.mackenzie.model;

import java.util.ArrayList;

public class Node {
    private double similarityScore;
    // Armazena uma lista para lidar com chaves de similaridade igual.
    private ArrayList<Result> results;
    private Node left;
    private Node right;
    private int height;

    /**
     * Construtor para um novo nó, aceitando o primeiro resultado de similaridade.
     */
    // CORREÇÃO 2: Construtor aceita Result, não Word
    public Node(double similarityScore, Result result) {
        this.similarityScore = similarityScore;
        // Inicializa a lista e adiciona o primeiro resultado
        this.results = new ArrayList<>();
        this.results.add(result);
        this.height = 1;
        this.left = null;
        this.right = null;
    }

    /**
     * Adiciona um resultado à lista existente. Chamado quando a chave é duplicada na AVL.
     */
    public void addResult(Result result) {
        this.results.add(result);
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    /**
     * Retorna a lista de resultados de comparação armazenada neste nó.
     */
    public ArrayList<Result> getResults() {
        return results;
    }


    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Representação do nó (adaptada para a lista de resultados).
     */
    @Override
    public String toString() {
        return String.format("Node{score=%.4f, results_count=%d, height=%d, left=%s, right=%s}",
                similarityScore,
                results.size(),
                height,
                (left != null ? "Yes" : "No"),
                (right != null ? "Yes" : "No"));
    }
}