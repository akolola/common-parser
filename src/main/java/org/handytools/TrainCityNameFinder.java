package org.handytools;

import opennlp.tools.namefind.*;
import opennlp.tools.util.*;
import java.io.*;
import java.util.Arrays;

public class TrainCityNameFinder {

    public static void main(String[] args) {
        String trainingDataPath = "src/main/resources/city-name-training.train";
        String modelPath = "src/main/resources/city-entity-model.bin";

        try {
            // Train the model and save it to a file
            TokenNameFinderModel model = trainModel(trainingDataPath);
            saveModel(model, modelPath);
            System.out.println("Model training completed and saved to " + modelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to create and train the model
    private static TokenNameFinderModel trainModel(String trainingDataPath) throws IOException {
        InputStreamFactory inputStreamFactory = createInputStreamFactory(trainingDataPath);
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, "UTF-8");
        ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream) {
            @Override
            public NameSample read() throws IOException {
                NameSample sample = super.read();
                if (sample != null) {
                    // Convert sentence tokens to lowercase
                    String[] lowerCasedTokens = Arrays.stream(sample.getSentence())
                            .map(String::toLowerCase)
                            .toArray(String[]::new);
                    return new NameSample(lowerCasedTokens, sample.getNames(), sample.isClearAdaptiveDataSet());
                }
                return null;
            }
        };

        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, 100);
        params.put(TrainingParameters.CUTOFF_PARAM, 1);

        return NameFinderME.train(
                "en",
                "city",
                sampleStream,
                params,
                new TokenNameFinderFactory()
        );
    }

    // Method to save the trained model
    private static void saveModel(TokenNameFinderModel model, String modelPath) throws IOException {
        try (BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream(new File(modelPath)))) {
            model.serialize(modelOut);
        }
    }

    // Method to create InputStreamFactory for reading the training data
    private static InputStreamFactory createInputStreamFactory(String trainingDataPath) {
        return () -> new FileInputStream(trainingDataPath);
    }
}

