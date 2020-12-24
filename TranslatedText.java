package com.example.azure;

public class TranslatedText {

    class Result {
        String text, to;
    }

    Result[] translations;

    @Override
    public String toString() {
        String str = " ";
        for (Result result : translations) {
            str += result.text + "\n";
        }
        return str;
    }
}
