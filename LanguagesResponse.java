package com.example.azure;

import java.util.ArrayList;
import java.util.Map;

public class LanguagesResponse {
    Map<String, Language> translation;

    public ArrayList<String> toText() {

        ArrayList<String> languages = new ArrayList<String>();

        for (String l : translation.keySet()) {
            Language value = translation.get(l);
            String lang = l + " / " + value.name + " " + value.nativeName;
            languages.add(lang);
        }
        return languages;
    }
}
