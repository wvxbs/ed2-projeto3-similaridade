/**
 * Projeto 2
 * Integrantes:
 * Gabriel Ferreira - RA: 10442043
 * Gian Lucca Campanha Ribeiro - RA: 10438361
 * Kaiki Bellini Barbosa - RA: 10402509
 */

package com.mackenzie.view;

import com.mackenzie.controller.SearchController;
import com.mackenzie.model.Result;
import java.util.List;

public class SearchView {

    private SearchController controller;

    public SearchView(SearchController controller) {
        this.controller = controller;
    }

    /**
     * Exibe o relatório completo do sistema (Saída final padrão e arquivo).
     * O controller deve gerar e salvar a string do relatório.
     */
    public void displayReport(String report) {
        System.out.println(report);
    }

    // --- Funções de Exibição de Resultados do Projeto ---

    /**
     * Exibe o resultado de uma comparação única (Modo Busca).
     * @param result O resultado da comparação entre dois arquivos.
     * @param metric A métrica utilizada.
     */
    public void displaySingleComparison(Result result, String metric) {
        System.out.println("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===");
        System.out.println("Comparando: " + result.getFilenameA() + " <-> " + result.getFilenameB());
        System.out.println(String.format("Similaridade calculada: %.2f", result.getSimilarity()));
        System.out.println("Métrica utilizada: " + metric);
        System.out.println();
    }

    /**
     * Exibe a lista de pares de documentos com similaridade acima de um limiar (Modo Lista).
     * @param results Lista de resultados ordenados.
     * @param threshold O limiar mínimo de similaridade.
     */
    public void displayResultsList(List<Result> results, double threshold) {
        System.out.println(String.format("\nPares com similaridade >= %.2f:", threshold));

        boolean found = false;
        for (Result result : results) {
            if (result.getSimilarity() >= threshold) {
                System.out.println(result.toString());
                found = true;
            } else {
                // A lista está ordenada, podemos parar de buscar
                break;
            }
        }

        if (!found) {
            System.out.println("Nenhum par encontrado acima do limiar.");
        }
        System.out.println();
    }

    /**
     * Exibe os K pares de documentos mais semelhantes (Modo TopK).
     * @param topKResults Lista contendo apenas os K resultados mais altos.
     * @param k O número de resultados exibidos.
     */
    public void displayTopK(List<Result> topKResults, int k) {
        System.out.println(String.format("\n=== Top %d Pares Mais Semelhantes ===", k));
        if (topKResults.isEmpty()) {
            System.out.println("Nenhum resultado encontrado.");
            return;
        }

        for (Result result : topKResults) {
            System.out.println(result.toString());
        }
        System.out.println();
    }

    // --- Métodos de Debug e Estatísticas (Mantidos e Adaptados) ---

    /**
     * Exibe a estrutura da árvore AVL (Adaptado para o novo Node/Result)
     */
    public void displayTreeStructure() {
        System.out.println("=== AVL Tree Structure ===");
        // O método printTree() no AVLTree.java foi adaptado para a lista de Results
        controller.getAvlTree().printTree();
        System.out.println();
    }

    /**
     * Exibe estatísticas detalhadas de rotações
     */
    public void displayRotationStatistics() {
        System.out.println("=== Rotation Statistics ===");
        System.out.println(controller.getAvlTree().getRotationStatistics());
        System.out.println();
    }

    /**
     * Exibe o cabeçalho inicial da aplicação
     */
    public void displayHeader() {
        System.out.println("=================================");
        System.out.println("   VERIFICADOR DE SIMILARIDADE");
        System.out.println("=================================");
        System.out.println();
    }

    // --- Métodos de Suporte (Mantidos) ---

    /**
     * Exibe mensagem de erro
     */
    public void displayError(String message) {
        System.out.println("✗ Error: " + message);
        System.out.println();
    }

    /**
     * Exibe mensagem de uso (necessário para o Main)
     */
    public void displayUsage() {
        System.out.println("Usage: java Main <directory> <threshold> <mode> [k or files]");
        System.out.println("Modes: lista | topK <K_value> | busca <file1> <file2>");
    }

    // --- Métodos Originais Obsoletos para o Foco do Projeto (Removidos/Comentados) ---
    /*
    public void displayTopWords(int n) { ... }
    public void displayAllWords() { ... }
    public void displaySearchByText(String text) { ... }
    public void displaySearchBySimilarity(double score) { ... } // Nova lógica no Main/Controller
    public void displaySummary() { ... } // Deve ser integrado ao Relatório Final
    public void displayMenu() { ... } // Aplicação via linha de comando não usa menu interativo
    public void displaySuccess(String message) { ... }
    public void displaySeparator() { ... }
    */
}