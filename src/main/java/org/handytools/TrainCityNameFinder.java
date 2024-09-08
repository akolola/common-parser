package org.handytools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import opennlp.tools.namefind.*;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;

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
        ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);

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
