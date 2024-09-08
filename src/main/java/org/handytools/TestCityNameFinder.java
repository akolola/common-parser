package org.handytools;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestCityNameFinder {

    public static void main(String[] args) {
        String modelPath = "src/main/resources/city-entity-model.bin";
        String analysisTextPath = "src/main/resources/city-name-text.txt";

        try {
            TokenNameFinderModel model = loadModel(modelPath);
            String content = readFileContent(analysisTextPath);
            findCityNames(model, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the TokenNameFinderModel from the model file
    private static TokenNameFinderModel loadModel(String modelPath) throws IOException {
        try (InputStream modelInputStream = new FileInputStream(new File(modelPath))) {
            return new TokenNameFinderModel(modelInputStream);
        }
    }

    // Read the content of the city-text.txt file
    private static String readFileContent(String filePath) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textBuilder.append(line).append(" ");
            }
        }
        return textBuilder.toString();
    }


    private static void findCityNames(TokenNameFinderModel model, String content) {
        NameFinderME nameFinder = new NameFinderME(model);
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(content);

        Span[] spans = nameFinder.find(tokens);

        // List of country names to exclude
        Set<String> countryNames = new HashSet<>(Arrays.asList("California", "Germany", "China", "France"));

        System.out.println("Found the following city names:");
        for (Span span : spans) {
            StringBuilder name = new StringBuilder();
            for (int i = span.getStart(); i < span.getEnd(); i++) {
                // Append token to city name and remove punctuation
                String cleanToken = tokens[i].replaceAll("[,\\.]", "");
                name.append(cleanToken).append(" ");
            }

            String cityName = name.toString().trim();
            // Split merged values but keep multi-word cities intact (remove punctuation after joining)
            if (!countryNames.contains(cityName)) {
                System.out.println(cityName);
            }
        }

        nameFinder.clearAdaptiveData();
    }






}