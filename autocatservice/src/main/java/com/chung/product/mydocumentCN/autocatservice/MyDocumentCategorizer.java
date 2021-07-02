package com.chung.product.mydocumentCN.autocatservice;


import opennlp.tools.doccat.*;
import opennlp.tools.ml.AbstractTrainer;
import opennlp.tools.ml.naivebayes.NaiveBayesTrainer;
import opennlp.tools.util.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class MyDocumentCategorizer {
    DoccatModel model;

    private String modelFileName;

    public void trainModel(String trainFileName){

        DoccatModel model = null;
        InputStream dataIn = null;
        File trainingFile = new File(trainFileName);
        try {
            FeatureGenerator[] featureGenerators = { new BagOfWordsFeatureGenerator() };

            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.ITERATIONS_PARAM, 1000+"");
            params.put(TrainingParameters.CUTOFF_PARAM, 0+"");
            params.put(AbstractTrainer.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);

            featureGenerators[0] = new BagOfWordsFeatureGenerator();
            DoccatFactory factory = new DoccatFactory(featureGenerators);

            InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(trainingFile);//"en-sentiment.train");
            ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8.name());
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            model = DocumentCategorizerME.train("en", sampleStream,params,factory);

            try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(this.getModelFileName()))) {
                model.serialize(modelOut);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public String categorize(String inputText) throws IOException {

        String[] inputWordsArray = inputText.replaceAll("[^A-Za-z]", " ").split(" ");
        InputStream is = new FileInputStream(new File(this.getModelFileName()));
        DoccatModel m = new DoccatModel(is);
        DocumentCategorizerME myCategorizer = new DocumentCategorizerME(m);

        double[] outcomes = myCategorizer.categorize(inputWordsArray);
        return myCategorizer.getBestCategory(outcomes);
    }


    public String getModelFileName() {
        return modelFileName;
    }

    public void setModelFileName(String modelFileName) {
        this.modelFileName = modelFileName;
    }

    public void categorize(){

    }
}
