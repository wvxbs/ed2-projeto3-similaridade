/**
 * Projeto 2
 * Integrantes:
 * Gabriel Ferreira - RA: 10442043
 * Gian Lucca Campanha Ribeiro - RA: 10438361
 * Kaiki Bellini Barbosa - RA: 10402509
 */

package com.mackenzie.controller;

import com.mackenzie.model.Document;
import com.mackenzie.model.AVLTree;
import com.mackenzie.model.Result;
import com.mackenzie.DocumentComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gerencia a lógica de negócios de alto nível para o Verificador de Similaridade de Textos.
 * Orquestra a AVLTree e a exibição dos resultados.
 */
public class SearchController {

    private AVLTree avlTree;
    private List<Document> documents;
    private DocumentComparator comparator;
    private String hashFunctionName;
    private String similarityMetric;

    /**
     * Recebe as estruturas principais do Main.
     */
    public SearchController(AVLTree avlTree, List<Document> documents, DocumentComparator comparator,
                            String hashFunctionName, String similarityMetric) {
        this.avlTree = avlTree;
        this.documents = documents;
        this.comparator = comparator;
        this.hashFunctionName = hashFunctionName;
        this.similarityMetric = similarityMetric;
    }

    public AVLTree getAvlTree() {
        return avlTree;
    }

    /**
     * Executa o modo 'lista': Retorna resultados com similaridade acima do limiar.
     */
    public List<Result> getResultsAboveThreshold(double threshold) {
        // Usa a travessia In-Order Reversa para obter resultados já ordenados.
        return avlTree.getAllResultsSorted().stream()
                .filter(r -> r.getSimilarity() >= threshold)
                .collect(Collectors.toList());
    }

    /**
     * Executa o modo 'topK': Retorna os K resultados de maior similaridade.
     */
    public List<Result> getTopKResults(int k) {
        return avlTree.getTopN(k);
    }

    /**
     * Executa o modo 'busca': Compara dois documentos específicos.
     */
    public Result compareSpecificDocuments(String docNameA, String docNameB) {
        Document docA = findDocumentByName(docNameA);
        Document docB = findDocumentByName(docNameB);

        if (docA == null || docB == null) {
            return null;
        }

        double similarity = comparator.calculateSimilarity(docA, docB);
        return new Result(docA.getName(), docB.getName(), similarity);
    }

    /**
     * Gera o relatório de saída completo (terminal e arquivo), excluindo o modo 'busca'.
     */
    public String generateReport(String mode, double threshold, List<Result> modeResults, int totalPairs) {
        StringBuilder sb = new StringBuilder();

        Document sampleDoc = documents.get(0);

        sb.append("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===\n");
        sb.append(String.format("Total de documentos processados: %d\n", documents.size()));
        sb.append(String.format("Total de pares comparados: %d\n", totalPairs));
        sb.append(String.format("Função hash utilizada: %s\n", hashFunctionName));
        sb.append(String.format("Métrica de similaridade: %s\n", similarityMetric));

        if (mode.equals("lista")) {
            sb.append(String.format("Pares com similaridade >= %.4f:\n", threshold));
            modeResults.forEach(r -> sb.append(r.toString()).append("\n"));
        } else if (mode.equals("topk")) {
            int k = modeResults.size();
            sb.append(String.format("Top %d Pares Mais Semelhantes:\n", k));
            modeResults.forEach(r -> sb.append(r.toString()).append("\n"));
        }

        // Resultados de menor similaridade (para o formato de saída)
        List<Result> allResults = avlTree.getAllResultsSorted();
        if (!allResults.isEmpty()) {
            Result minResult = allResults.get(allResults.size() - 1);
            sb.append("Pares com menor similaridade:\n");
            sb.append(minResult.toString()).append("\n");
        }

        // Inclui estatísticas de rotação para o relatório
        sb.append("\n").append(avlTree.getRotationStatistics());

        sb.append(sampleDoc.getTable().getStatistics());

        return sb.toString();
    }

    private Document findDocumentByName(String name) {
        // Encontra o documento na lista pelo nome do arquivo
        return documents.stream()
                .filter(d -> d.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}