/**
 * Projeto 2
 * Integrantes:
 * Gabriel Ferreira - RA: 10442043
 * Gian Lucca Campanha Ribeiro - RA: 10438361
 * Kaiki Bellini Barbosa - RA: 10402509
 */

package com.mackenzie;

import com.mackenzie.model.Document;
import com.mackenzie.model.Word;

import java.util.HashSet;
import java.util.Set;

/**
 * Classe responsável por calcular similaridade entre documentos.
 */
public class DocumentComparator {

    private final String metric;

    /**
     * Construtor que armazena a métrica escolhida.
     * metric Nome da métrica a ser utilizada.
     */
    public DocumentComparator(String metric) {
        this.metric = metric;
    }

    /**
     * Função principal para calcular a similaridade baseada na métrica selecionada.
     * docA Primeiro documento.
     * docB Segundo documento.
     * Score de similaridade entre 0 e 1.
     */
    public double calculateSimilarity(Document docA, Document docB) {
        if (metric.equalsIgnoreCase("Cosseno")) {
            return cosineSimilarity(docA, docB);
        }
        if (metric.equalsIgnoreCase("Jaccard")) {
            return jaccardSimilarity(docA, docB);
        }

        // Retorna 0.0 se a métrica não for reconhecida
        return 0.0;
    }

    /**
     * Calcula a Similaridade de Cosseno.
     * Utiliza o modelo de espaço vetorial (frequências).
     * docA Primeiro documento.
     * docB Segundo documento.
     * @return O valor da Similaridade do Cosseno.
     */
    private double cosineSimilarity(Document docA, Document docB) {

        Word[] wordsA = docA.getTable().getAllWords();

        double dotProduct = 0.0;
        double magnitudeA_sq = 0.0;
        double magnitudeB_sq = 0.0;

        //Produto escalar e Magnitude de A
        for (Word wordA : wordsA) {
            String token = wordA.getText();
            int freqA = wordA.getFrequency();

            //Busca token em B
            Word wordB = docB.getTable().get(token);

            if (wordB != null) {
                //soma do produto das frequências
                dotProduct += (double) freqA * wordB.getFrequency();
            }

            //Magnitude A ao quadrado
            magnitudeA_sq += (double) freqA * freqA;
        }

        //2. Magnitude de B (separada, para não recalcular o dot product)
        Word[] wordsB = docB.getTable().getAllWords();
        for (Word wordB : wordsB) {
            int freqB = wordB.getFrequency();
            magnitudeB_sq += (double) freqB * freqB;
        }

        double magnitudeA = Math.sqrt(magnitudeA_sq);
        double magnitudeB = Math.sqrt(magnitudeB_sq);

        double denominator = magnitudeA * magnitudeB;

        if (denominator == 0) {
            return 0.0;
        }

        return dotProduct / denominator;
    }

    /**
     * Calcula a Similaridade de Jaccard.
     * Baseia-se na interseção e união dos conjuntos de termos, ignorando a frequência.
     * docA Primeiro documento.
     * docB Segundo documento.
     * O valor da Similaridade de Jaccard.
     */
    private double jaccardSimilarity(Document docA, Document docB) {
        Word[] wordsA = docA.getTable().getAllWords();
        Word[] wordsB = docB.getTable().getAllWords();

        // 1. Cria conjuntos de termos únicos (Set)
        Set<String> setA = new HashSet<>();
        for (Word w : wordsA) {
            setA.add(w.getText());
        }

        Set<String> setB = new HashSet<>();
        for (Word w : wordsB) {
            setB.add(w.getText());
        }

        // 2. Interseção (Numerador)
        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        // 3. União (Denominador) = |A| + |B| - |A interseção B|
        int sizeUnion = setA.size() + setB.size() - intersection.size();

        if (sizeUnion == 0) {
            return 0.0;
        }

        return (double) intersection.size() / sizeUnion;
    }
}