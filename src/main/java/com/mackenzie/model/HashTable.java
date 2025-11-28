/**
 * Projeto 2
 * Integrantes:
 * Gabriel Ferreira - RA: 10442043
 * Gian Lucca Campanha Ribeiro - RA: 10438361
 * Kaiki Bellini Barbosa - RA: 10402509
 */

package com.mackenzie.model;

public class HashTable {
    public enum HashMode {
        HASH_JAVA_DEFAULT, // Polinomial (similar ao hashCode() do Java)
        HASH_SIMPLE_ADDITION // Soma simples dos códigos ASCII
    }

    private Entry[] table;
    private int size;
    private HashMode mode;

    /**
     * Construtor da Tabela Hash.
     * Permite a escolha da função de dispersão\.
     * O tratamento de colisões é feito por encadeamento.
     */
    public HashTable(HashMode mode) {
        this.table = new Entry[1024]; // Tamanho fixo (1024)
        this.size = 0;
        this.mode = mode;
    }

    /**
     * Função Hash 1: Hash Padrão do Java (Polinomial).
     * Apresenta boa distribuição de chaves.
     */
    private int hashJavaDefault(String key) {
        if (key == null) return 0;
        int hash = 0;
        for (int i = 0; i < key.length(); i++) {
            // Equivalente a hash = 31 * hash + key.charAt(i);
            hash = (hash << 5) - hash + key.charAt(i);
            hash = hash | 0;
        }
        return Math.abs(hash) % table.length;
    }

    /**
     * Função Hash 2: Adição Simples (Soma dos códigos ASCII).
     * Usada para fins de comparação experimental.
     */
    private int hashSimpleAddition(String key) {
        if (key == null) return 0;

        long hash = 0;
        for (int i = 0; i < key.length(); i++) {
            hash += key.charAt(i);
        }

        return (int) (hash % table.length);
    }

    /**
     * Lógica principal de dispersão: delega para a função hash selecionada no modo.
     */
    private int hash(String key) {
        if (mode == HashMode.HASH_JAVA_DEFAULT) {
            return hashSimpleAddition(key);
        }
        return hashJavaDefault(key);
    }

    /**
     * Insere ou atualiza um par chave-valor (Key=String, Value=Word).
     * Implementa tratamento de colisão separate chaining.
     */
    public void put(String key, Word word) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int index = hash(key);
        Entry current = table[index];

        // 1. Busca: Se a chave já existe, atualiza o valor.
        while (current != null) {
            if (current.key.equals(key)) {
                current.word = word;
                return;
            }
            current = current.next;
        }

        // 2. Inserção: Chave nova, insere no início da lista encadeada (bucket).
        Entry newEntry = new Entry(key, word);
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;
    }

    /**
     * Retorna o valor (Word) associado à chave.
     */
    public Word get(String key) {
        if (key == null) {
            return null;
        }

        int index = hash(key);
        Entry current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.word;
            }
            current = current.next;
        }

        return null;
    }

    /**
     * Retorna a contagem de elementos em cada bucket (tamanho da lista encadeada).
     * Essencial para a Análise Experimental da distribuição de chaves (colisões).
     */
    public int[] getCollisionDistribution() {
        int[] distribution = new int[table.length];
        for (int i = 0; i < table.length; i++) {
            int count = 0;
            Entry current = table[i];
            while (current != null) {
                count++;
                current = current.next;
            }
            distribution[i] = count;
        }
        return distribution;
    }

    /**
     * Retorna um array de todas as palavras (Word) armazenadas na tabela.
     */

    public HashMode getMode() {
        return mode;
    }

    public boolean remove(String key) {
        if (key == null) {
            return false;
        }

        int index = hash(key);
        Entry current = table[index];
        Entry previous = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }

        return false;
    }

    public boolean containsKey(String key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        size = 0;
    }

    public Word[] getAllWords() {
        Word[] words = new Word[size];
        int index = 0;

        for (int i = 0; i < table.length; i++) {
            Entry current = table[i];
            while (current != null) {
                words[index++] = current.word;
                current = current.next;
            }
        }

        return words;
    }

    /**
     * Retorna uma string formatada com estatísticas de distribuição de colisões,
     * incluindo o tamanho do array, buckets ocupados, e o comprimento máximo da cadeia.
     */
    public String getStatistics() {
        int[] distribution = getCollisionDistribution();
        int occupiedBuckets = 0;
        int maxChainLength = 0;
        long totalElements = this.size; // Total de palavras únicas

        //Calcula o número de buckets ocupados e o comprimento máximo da cadeia
        for (int count : distribution) {
            if (count > 0) {
                occupiedBuckets++;
            }
            if (count > maxChainLength) {
                maxChainLength = count;
            }
        }

        //Calcula o comprimento médio
        //Usa occupiedBuckets no denominador para medir a média real por lista criada
        double avgChainLength = (occupiedBuckets == 0) ? 0.0 : (double) totalElements / occupiedBuckets;

        //Calcula o Load Factor
        double loadFactor = (double) totalElements / table.length;

        return String.format("\n\n=== ESTATÍSTICAS DA TABELA HASH ===\n" +
                        "  Função Hash: %s\n" +
                        "  Tamanho do Array (Buckets): %d\n" +
                        "  Vocabulário Total (Elementos): %d\n" +
                        "  Buckets Ocupados: %d\n" +
                        "  Fator de Carga (Load Factor): %.4f (Elementos/Buckets Totais)\n" +
                        "  Comprimento Máximo da Cadeia (Pior Colisão): %d\n" +
                        "  Comprimento Médio da Cadeia (Média por Bucket Ocupado): %.2f",
                this.mode.name(), table.length, totalElements, occupiedBuckets,
                loadFactor, maxChainLength, avgChainLength);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HashTable [size=").append(size).append("]\n");

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                sb.append("Bucket ").append(i).append(": ");
                Entry current = table[i];
                while (current != null) {
                    sb.append("[").append(current.key).append("=");
                    sb.append(current.word.toCompactString()).append("]");
                    if (current.next != null) {
                        sb.append(" -> ");
                    }
                    current = current.next;
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}