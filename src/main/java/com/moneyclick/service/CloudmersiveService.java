package com.moneyclick.service;

import com.cloudmersive.client.ImageOcrApi;
import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.invoker.Configuration;
import com.cloudmersive.client.invoker.auth.ApiKeyAuth;
import com.cloudmersive.client.model.ImageToTextResponse;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CloudmersiveService {
    private ApiClient defaultClient;
    private ApiKeyAuth apiKey;
    private ImageOcrApi apiInstance;

    private final String language = "ENG";
    private final String preProcessing = "Auto";

    public CloudmersiveService() {
        defaultClient = Configuration.getDefaultApiClient();
        apiKey = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
        apiKey.setApiKey(System.getenv("API_KEY"));

        apiInstance = new ImageOcrApi();
    }

    public List<String> processImage(Path imagePath) {
        try {
            ImageToTextResponse response = apiInstance.imageOcrPost(imagePath.toFile(), language, preProcessing);
            List<String> textLines = Arrays.asList(response.getTextResult().split("\n"));

            return textLines;
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }
}
