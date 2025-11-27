/**
 * Projeto 2
 * Integrantes:
 * Gabriel Ferreira - RA: 10442043
 * Gian Lucca Campanha Ribeiro - RA: 10438361
 * Kaiki Bellini Barbosa - RA: 10402509
 */

package com.mackenzie.model;

import java.util.ArrayList;
import java.util.List;

public class AVLTree {
    private Node root;

    private int llRotations = 0;
    private int rrRotations = 0;
    private int lrRotations = 0;
    private int rlRotations = 0;

    /**
     * Insere um novo resultado de similaridade na árvore.
     * O score de similaridade (double) é a chave de ordenação e balanceamento.
     */
    public void insert(double similarityScore, Result result) {
        root = insertRecursive(root, similarityScore, result);
    }

    private Node insertRecursive(Node node, double similarityScore, Result result) {
        if (node == null) {
            // Caso base: Chave não existe, cria novo nó com o resultado.
            return new Node(similarityScore, result);
        }

        // LÓGICA PRINCIPAL 1: TRATAMENTO DE CHAVES DUPLICADAS
        // Se a similaridade for igual (chave duplicada), adiciona o resultado à lista do nó.
        // Usa tolerância para comparação de doubles.
        if (Math.abs(similarityScore - node.getSimilarityScore()) < 0.0001) {
            node.addResult(result);
            return node;
        }

        // Lógica Padrão de Inserção BST
        if (similarityScore < node.getSimilarityScore()) {
            node.setLeft(insertRecursive(node.getLeft(), similarityScore, result));
        } else {
            node.setRight(insertRecursive(node.getRight(), similarityScore, result));
        }

        // LÓGICA PRINCIPAL 2: BALANCEAMENTO E ROTAÇÕES
        updateHeight(node);
        int balance = getBalance(node);

        // Rotação Simples à Direita (LL Case)
        if (balance > 1 && similarityScore < node.getLeft().getSimilarityScore()) {
            llRotations++;
            return rotateRight(node);
        }

        // Rotação Simples à Esquerda (RR Case)
        if (balance < -1 && similarityScore > node.getRight().getSimilarityScore()) {
            rrRotations++;
            return rotateLeft(node);
        }

        // Rotação Dupla Esquerda-Direita (LR Case)
        if (balance > 1 && similarityScore > node.getLeft().getSimilarityScore()) {
            lrRotations++;
            node.setLeft(rotateLeft(node.getLeft()));
            return rotateRight(node);
        }

        // Rotação Dupla Direita-Esquerda (RL Case)
        if (balance < -1 && similarityScore < node.getRight().getSimilarityScore()) {
            rlRotations++;
            node.setRight(rotateRight(node.getRight()));
            return rotateLeft(node);
        }

        return node;
    }

    /**
     * Realiza a Rotação Simples à Direita e atualiza as alturas.
     */
    private Node rotateRight(Node y) {
        Node x = y.getLeft();
        Node T2 = x.getRight();

        x.setRight(y);
        y.setLeft(T2);

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    /**
     * Realiza a Rotação Simples à Esquerda e atualiza as alturas.
     */
    private Node rotateLeft(Node x) {
        Node y = x.getRight();
        Node T2 = y.getLeft();

        y.setLeft(x);
        x.setRight(T2);

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    /**
     * Recalcula a altura do nó: 1 + max(altura_esquerda, altura_direita).
     */
    private void updateHeight(Node n) {
        int lh = (n.getLeft() == null ? 0 : n.getLeft().getHeight());
        int rh = (n.getRight() == null ? 0 : n.getRight().getHeight());
        n.setHeight(Math.max(lh, rh) + 1);
    }

    /**
     * Calcula o Fator de Balanceamento (Altura Esquerda - Altura Direita).
     */
    private int getBalance(Node n) {
        if (n == null) {
            return 0;
        }
        int lh = (n.getLeft() == null ? 0 : n.getLeft().getHeight());
        int rh = (n.getRight() == null ? 0 : n.getRight().getHeight());
        return lh - rh;
    }

    /**
     * Retorna os N resultados de similaridade mais altos.
     * Utiliza travessia In-Order Reversa para obter os maiores scores primeiro.
     */
    public List<Result> getTopN(int n) {
        List<Result> topResults = new ArrayList<>();
        getTopNRecursive(root, n, topResults);
        return topResults;
    }

    private void getTopNRecursive(Node node, int n, List<Result> results) {
        if (node == null || results.size() >= n) {
            return;
        }

        // 1. Visita a sub-árvore direita (maiores scores)
        getTopNRecursive(node.getRight(), n, results);

        // 2. Visita o nó atual, adicionando todos os resultados da lista
        if (results.size() < n) {
            for (Result result : node.getResults()) {
                if (results.size() < n) {
                    results.add(result);
                } else {
                    break;
                }
            }
        }

        // 3. Visita a sub-árvore esquerda (menores scores)
        if (results.size() < n) {
            getTopNRecursive(node.getLeft(), n, results);
        }
    }

    /**
     * Retorna todos os resultados ordenados por similaridade (In-Order Reversa).
     */
    public List<Result> getAllResultsSorted() {
        List<Result> allResults = new ArrayList<>();
        getAllResultsSortedRecursive(root, allResults);
        return allResults;
    }

    private void getAllResultsSortedRecursive(Node node, List<Result> results) {
        if (node == null) {
            return;
        }

        // 1. Visita a sub-árvore direita (maior score)
        getAllResultsSortedRecursive(node.getRight(), results);

        // 2. Adiciona todos os resultados do nó (scores iguais)
        results.addAll(node.getResults());

        // 3. Visita a sub-árvore esquerda (menor score)
        getAllResultsSortedRecursive(node.getLeft(), results);
    }

    /**
     * Retorna as estatísticas de rotação para o relatório técnico.
     */
    public String getRotationStatistics() {
        return String.format("Estatísticas de Rotação:\n" +
                        "  LL Rotations: %d\n" +
                        "  RR Rotations: %d\n" +
                        "  LR Rotations: %d\n" +
                        "  RL Rotations: %d\n" +
                        "  Total Rotations: %d",
                llRotations, rrRotations, lrRotations, rlRotations,
                llRotations + rrRotations + lrRotations + rlRotations);
    }

    // Outros métodos de debug e manutenção (isBalanced, clear, printTree)

    public Node getRoot() {
        return root;
    }

    public boolean isBalanced() {
        return isBalancedRecursive(root);
    }

    private boolean isBalancedRecursive(Node node) {
        if (node == null) {
            return true;
        }
        int balance = Math.abs(getBalance(node));
        return balance <= 1
                && isBalancedRecursive(node.getLeft())
                && isBalancedRecursive(node.getRight());
    }

    public void clear() {
        root = null;
        llRotations = 0;
        rrRotations = 0;
        lrRotations = 0;
        rlRotations = 0;
    }

    /**
     * Exibe a estrutura da árvore (útil para debug).
     */
    public void printTree() {
        printTreeRecursive(root, "", true);
    }

    private void printTreeRecursive(Node node, String prefix, boolean isTail) {
        if (node == null) {
            return;
        }

        System.out.println(prefix + (isTail ? "└── " : "├── ") +
                String.format("Score: %.4f | Results: %d (height=%d)",
                        node.getSimilarityScore(),
                        node.getResults().size(),
                        node.getHeight()));

        if (node.getLeft() != null || node.getRight() != null) {
            if (node.getRight() != null) {
                printTreeRecursive(node.getRight(), prefix + (isTail ? "    " : "│   "), false);
            }
            if (node.getLeft() != null) {
                printTreeRecursive(node.getLeft(), prefix + (isTail ? "    " : "│   "), true);
            }
        }
    }
}