/**
 * Projeto 2
 * Integrantes:
 * Gabriel Ferreira - RA: 10442043
 * Gian Lucca Campanha Ribeiro - RA: 10438361
 * Kaiki Bellini Barbosa - RA: 10402509
 */

/**
 * Projeto 2 - Document.java
 * Classe Modelo/Entidade: Representa um único arquivo de texto e é responsável
 * por encapsular sua Tabela Hash de vocabulário e a lógica de processamento (normalização).
 */
package com.mackenzie.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class Document {

    private String name;
    private HashTable table; // Armazena palavras e suas frequências
    private StopWords stopWords = new StopWords();
    private int totalWordsCount = 0; // Contador de palavras (após stop words)

    /**
     * Construtor. Inicializa o nome e a Tabela Hash, recebendo o modo de hash.
     */
    public Document(String fileName, HashTable.HashMode hashMode) {
        this.name = fileName;
        //Permite escolher o modo de hash no construtor para comparação
        this.table = new HashTable(hashMode);
    }

    /**
     * Processa o arquivo: normaliza, remove stop words e popula a tabela hash.
     */
    public void process() {
        try {
            String content = Files.readString(Path.of(name), StandardCharsets.UTF_8);
            content = normalize(content);

            StringTokenizer tokenizer = new StringTokenizer(content);

            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();

                if (!stopWords.isStopWord(token)) {
                    //Contagem de frequência de palavras
                    Word existingWord = table.get(token);

                    if (existingWord != null) {
                        //Palavra já existe: incrementa a frequência
                        existingWord.incrementFrequency();
                    } else {
                        //Palavra nova: cria e insere na tabela hash
                        Word newWord = new Word(token, name);
                        table.put(token, newWord);
                    }
                    totalWordsCount++;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error processing document: " + name, e);
        }
    }

    /**
     * Normalização: minúsculas, remove pontuação e tokenização.
     */
    private String normalize(String text) {
        text = text.toLowerCase();
        text = text.replaceAll("[^a-z0-9à-ú\\s]", " ");
        text = text.replaceAll("\\s+", " ");
        return text.trim();
    }

    public String getName() {
        Path p = Path.of(name);
        return p.getFileName().toString();
    }

    public HashTable getTable() {
        return table;
    }

    public int getTotalWordsCount() {
        return totalWordsCount;
    }
}