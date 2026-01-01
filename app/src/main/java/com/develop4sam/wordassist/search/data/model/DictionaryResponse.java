package com.develop4sam.wordassist.search.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DictionaryResponse {

    public String word;
    // Phonetics is an array, there can be multiple (e.g., US/UK)
    public List<Phonetic> phonetics;
    public List<Meaning> meanings;
    public static class Phonetic {
        public String text;  // e.g., "/kəˈlaɪdəˌskoʊp/"
        public String audio; // Can be null for some entries
    }

    public static class Meaning {
        @SerializedName("partOfSpeech")
        public String partOfSpeech;
        public List<Definition> definitions;
    }

    public static class Definition {
        public String definition;
        public String example;
        public List<String> synonyms;
        public List<String> antonyms;
    }
}