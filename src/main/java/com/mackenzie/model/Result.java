/**
 * Projeto 2
 * Integrantes:
 * Gabriel Ferreira - RA: 10442043
 * Gian Lucca Campanha Ribeiro - RA: 10438361
 * Kaiki Bellini Barbosa - RA: 10402509
 */

package com.mackenzie.model;

/**
 *Classe feita para armazenar o resultado de uma comparação de similaridade.
 * Lógica Principal: Armazenar a CHAVE (similaridade) e os VALORES (par de documentos) que serão usados pela AVL.
 */
public class Result {
    private String filenameA;
    private String filenameB;
    private double similarity;

    /**
     * CONSTRUTOR: Armazena o par de documentos e o score de similaridade.
     */
    public Result(String filenameA, String filenameB, double similarity) {
        // Garante que o par (doc1, doc2) seja armazenado de forma consistente
        if (filenameA.compareTo(filenameB) <= 0) {
            this.filenameA = filenameA;
            this.filenameB = filenameB;
        } else {
            this.filenameA = filenameB;
            this.filenameB = filenameA;
        }

        // Este valor é a CHAVE pela qual os nós serão ordenados e balanceados na AVL.
        this.similarity = similarity;
    }

    public String getFilenameA() {
        return filenameA;
    }

    public String getFilenameB() {
        return filenameB;
    }

    public double getSimilarity() {
        return similarity;
    }

    /**
     * Define o formato de exibição do resultado.
     */
    @Override
    public String toString() {
        return String.format("%s <-> %s = %.4f", filenameA, filenameB, similarity);
    }
}