/**
 * Projeto 2
 * Integrantes:
 * Gabriel Ferreira - RA: 10442043
 * Gian Lucca Campanha Ribeiro - RA: 10438361
 * Kaiki Bellini Barbosa - RA: 10402509
 */

package com.mackenzie.model;

public class Word {
    private String text;
    private int frequency;
    private String documentName;

    public Word(String text, String documentName) {
        this.text = text;
        this.frequency = 1;
        this.documentName = documentName;
    }

    /**
     * Incrementa a frequência da palavra.
     */
    public void incrementFrequency() {
        this.frequency++;
    }

    public String getText() {
        return text;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getDocumentName() {
        return documentName;
    }


    public String toCompactString() {
        return String.format("%s:%d", text, frequency);
    }

    @Override
    public String toString() {
        return String.format("Word{text='%s', frequency=%d, document='%s'}",
                text, frequency, documentName);
    }
}