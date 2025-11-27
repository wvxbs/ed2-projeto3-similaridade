/**
 * Projeto 2
 * Integrantes:
 * Gabriel Ferreira - RA: 10442043
 * Gian Lucca Campanha Ribeiro - RA: 10438361
 * Kaiki Bellini Barbosa - RA: 10402509
 */

package com.mackenzie.controller;

import com.mackenzie.model.Document;
import com.mackenzie.model.Word;

/**
 * Controlador responsável por operações relacionadas às palavras.
 */
public class WordController {

    /**
     * Retorna a frequência de uma palavra.
     * document Documento onde será feita a busca.
     * token Palavra a ser buscada.
     * Frequência da palavra ou 0 caso não exista.
     */
    public int getWordFrequency(Document document, String token) {
        Word word = document.getTable().get(token);
        return (word != null) ? word.getFrequency() : 0;
    }

    /**
     * Verifica se uma palavra existe dentro do doc.
     * document Documento alvo.
     * token Palavra procurada.
     * retorna true se a palavra existir, ou falso se não.
     */
    public boolean containsWord(Document document, String token) {
        return document.getTable().get(token) != null;
    }

    /**
     * Retorna o número total de palavras únicas do doc.
     * document Documento analisado.
     * returna Quantidade de termos únicos na tabela hash.
     */
    public int getVocabularySize(Document document) {
        return document.getTable().size();
    }

    /**
     * Imprime no console todas as palavras e suas frequências.
     * document Documento a ser exibido.
     */
    public void printAllWords(Document document) {
        Word[] words = document.getTable().getAllWords();
        System.out.println("=== Vocabulário do Documento: " + document.getName() + " ===");

        for (Word w : words) {
            System.out.println(w.getText() + " -> " + w.getFrequency());
        }
    }
}
