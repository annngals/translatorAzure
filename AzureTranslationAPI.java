package com.example.azure;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AzureTranslationAPI {
    String API_URL = "https://api.cognitive.microsofttranslator.com";
    String key = "68f92da1247445f9ad6ff6fc4cd3ca5c";
    String region = "eastasia";

    @GET("/languages?api-version=3.0&scope=translation")
    Call<LanguagesResponse> getLanguages();

    @POST("/translate?api-version=3.0&")
    @Headers({
            "Content-Type: application/json",
            "Ocp-Apim-Subscription-Key: " + key,
            "Ocp-Apim-Subscription-Region: " + region
    })
    Call<TranslatedText[]> translate(@Body MainActivity.Message[] text, @Query("to") String language);
}
