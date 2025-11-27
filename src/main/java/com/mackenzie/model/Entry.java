/**
 * Projeto 2
 * Integrantes:
 * Gabriel Ferreira - RA: 10442043
 * Gian Lucca Campanha Ribeiro - RA: 10438361
 * Kaiki Bellini Barbosa - RA: 10402509
 */

package com.mackenzie.model;

public class Entry {
    String key;
    Word word;
    Entry next;

    /**
     * Construtor de uma nova entrada.
     */
    public Entry(String k, Word w) {
        this.key = k;
        this.word = w;
        this.next = null;
    }
}