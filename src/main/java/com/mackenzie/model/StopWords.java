package com.mackenzie.model;

public class StopWords {
    private String[] stopWordsArray = {
        "a", "ao", "aos", "aquela", "aquelas", "aquele", "aqueles", "aquilo", "as", "à", 
        "às", "até", "com", "como", "da", "de", "dela", "delas", "dele", "deles", 
        "depois", "do", "dos", "e", "ela", "elas", "ele", "eles", "em", "entre", 
        "era", "essa", "essas", "esse", "esses", "esta", "estas", "está", "eu", 
        "foi", "fomos", "for", "foram", "fosse", "fosse", "fui", "há", "isso", 
        "isto", "já", "lhe", "lhes", "mais", "mas", "me", "menos", "meu", "meus", 
        "minha", "minhas", "na", "não", "nas", "nem", "no", "nos", "nossa", 
        "nossas", "nosso", "nossos", "num", "numa", "o", "os", "ou", "para", 
        "pela", "pelas", "pelo", "pelos", "por", "porque", "qual", "quando", "que", 
        "quem", "se", "sem", "ser", "sendo", "seu", "seus", "só", "sob", "sobre", 
        "sua", "suas", "tal", "também", "te", "tem", "tendo", "tenha", "ter", 
        "teu", "teus", "ti", "tinha", "toda", "todas", "todo", "todos", "tu", 
        "um", "uma", "uns", "você", "vocês", "vos"
    };

    public boolean isStopWord(String word) {
        for (int i = 0; i < stopWordsArray.length; i++) {
            if (stopWordsArray[i].equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }
}
