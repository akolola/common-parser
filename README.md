# Common Parser

**Common Parser** is a Java-based project that utilizes the Apache OpenNLP library to detect city names in text through custom tokenization and machine learning techniques. It includes two main programs:

## Features
1. **Trainer Program**:
    - Trains a custom city name detection model using the OpenNLP library.
    - Training data is supplied in a text file with labeled city names.

2. **Tokenizer Program**:
    - Uses the trained model to extract city names from text input.
    - The program tokenizes text and identifies multi-word city names while excluding non-city entities.

## Project Structure
- `TrainCityNameFinder.java`:
    - Trains the model using the custom-labeled training dataset from a text file (e.g., `training-city-name.train`).
    - Outputs a model file (e.g., `city-entity-model.bin`).

- `TestCityNameFinder.java`:
    - Loads the trained model and processes an input text file (e.g., `city-name-text.txt`) to detect and print city names.

## Training Data Example
The training data file (`city-name-training.train`) contains annotated sentences with city names, which are used by the trainer program to develop the model.

## How to Use

- **Train the Model**: Run `TrainCityNameFinder` to generate the model file (`city-entity-model.bin`).
- **Detect City Names**: Use `TestCityNameFinder` to analyze any text file (e.g., `city-name-text.txt`) and identify city names.

## Requirements

- Java 8+
- Apache OpenNLP (2.0.0)

## Extending the Project

This project can be easily extended to recognize other named entities (such as person names or organizations) by updating the training data and retraining the model.

