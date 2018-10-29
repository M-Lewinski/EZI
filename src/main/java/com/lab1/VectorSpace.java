package com.lab1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class VectorSpace {

    public List<Document> documents = new ArrayList<>();
    public Set<String> keywords = new HashSet<>();
    public Map<String,Double> idfTerms = new HashMap<>();
    private Stemmer stemmer = new Stemmer();
    CommandParameters commandParameters;
    private double alpha = 1.0;
    private double beta = 0.75;
    private double gamma  = 0.25;

    public VectorSpace(CommandParameters commandParameters) {
        this.commandParameters = commandParameters;
        init();
    }

    private void init(){
        readKeywords(commandParameters.keywordsFileName);
        System.out.println(keywords.size());
        readDocuments(commandParameters.documentFileName);
        createIdfVectorTerm();
    }

    private void readKeywords(String keywordFileName){
        try (BufferedReader br = new BufferedReader(new FileReader(keywordFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String term = wordToTerm(line);
                this.keywords.add(term);
            }
        }
        catch (Exception e){
            System.out.println("Couldn't read file containing key words");
            System.exit(1);
        }

    }

    private void readDocuments(String documentFileName){
        try (BufferedReader br = new BufferedReader(new FileReader(documentFileName))) {
            String line;
            Document document = new Document();
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i == 0){
                    document.title = line;
                }
                if (line.equals("\n") || line.equals("")){
                    this.documents.add(document);
                    i = 0;
                    document = new Document();
                    continue;
                }
                calculateBagOfWordsAndTf(line, document);
                document.stemmedText.append("\n");
                i++;
            }
            this.documents.add(document);

            for (Document doc: this.documents) {
                if (commandParameters.verbose){
                    System.out.println(doc.title);
                    System.out.printf(doc.stemmedText.toString());
                    doc.bagOfWords.termsValue.forEach((k,v) -> System.out.printf("%s: %g, ",k,v));
                    System.out.println("\n");
                }

                doc.tf.normalize();
            }
        }
        catch (Exception e){
            System.out.println("Couldn't read file containing documents");
            System.exit(1);
        }
    }

    private void calculateBagOfWordsAndTf(String line, TfIdf tfIdf) {
        String[] words = line.split(" ");
        for (String word: words) {
            String term = wordToTerm(word);
            if (this.keywords.contains(term)){
                double value = tfIdf.tf.get(term);
                value++;
                tfIdf.tf.put(term,value);
                tfIdf.bagOfWords.put(term,value);
            }
            tfIdf.stemmedText.append(term).append(" ");
        }
    }

    private String wordToTerm(String word){
        String term = word.toLowerCase();
//        term = term.replaceAll("\t\n\\\\><#%\\$\\^&\\*_\\+=\\|]\\[\\)\\(}\\{;,.?!\'\"`-","");
        term = term.replaceAll("[^A-Za-z0-9]","");
        char[] termChars =  term.toCharArray();
        stemmer.add(termChars,termChars.length);
        stemmer.stem();
        term = stemmer.toString();
        return term;
    }

    private void createIdfVectorTerm(){
        int documentCount = this.documents.size();
        for (String term: this.keywords) {
            int occuranceCount = 0;
            for (Document doc: this.documents) {
                double value = doc.tf.get(term);
                if (value > 0.0) occuranceCount++;
            }
            double idfValue = 0.0;
            if (occuranceCount > documentCount){
                System.out.println("SOMETHING WENT WRONG");
            }
            if (occuranceCount != 0){
                idfValue = Math.log10(documentCount/(double)occuranceCount);
            }
            idfTerms.put(term,idfValue);
        }

        for (Document doc: this.documents) {
            calculateIdf(doc);
        }
    }

    private void calculateIdf(TfIdf tfIdf) {
        for (Map.Entry<String, Double> termTf: tfIdf.tf.termsValue.entrySet()) {
            tfIdf.idf.put(termTf.getKey(),termTf.getValue()*this.idfTerms.get(termTf.getKey()));
        }
        tfIdf.idf.calculateMagnitude();
    }


    private double calculateSim(VectorTerms querry,VectorTerms document){
        if (querry.magnitude == 0.0 || document.magnitude == 0.0){
            return 0.0;
        }
        double termSum = 0.0;
        for (Map.Entry<String, Double> termEntry: querry.termsValue.entrySet()) {
            termSum += termEntry.getValue()*document.get(termEntry.getKey());
        }
        return termSum/(querry.magnitude*document.magnitude);
    }


    public List<Ranking> querryExecution(Querry querry){
        querry.tf.normalize();
        calculateIdf(querry);
        List<Ranking> rankingList = new ArrayList<>();
        for (Document doc: this.documents) {
            double sim = calculateSim(querry.idf,doc.idf);
            if (!(sim > 0.0)) continue;
            Ranking ranking = new Ranking(doc,sim);
            rankingList.add(ranking);
        }

        rankingList.sort(Ranking::compareTo);
        return rankingList;
    }

    public Querry convertStringToQuerry(String querryString) {
        Querry querry = new Querry();
        calculateBagOfWordsAndTf(querryString,querry);
        if (commandParameters.verbose){
            System.out.println("com.lab1.Querry stemmed");
            System.out.println(querry.stemmedText.toString());
            querry.tf.termsValue.forEach((k,v) -> System.out.printf("%s: %g, ",k,v));
        }
        System.out.println();
        return querry;
    }


    public VectorTerms rochio(VectorTerms query, Map<Document,Boolean> feedback) {
        VectorTerms result = new VectorTerms();
        for (String term: keywords) {
            double queryValue = query.get(term);
            double sumRelevant = 0.0;
            double relevantCount = 0.0;
            double nonRelevantCount = 0.0;
            double sumNonRelevant = 0.0;
            double finalValue;
            for (Map.Entry<Document, Boolean> docEntry:  feedback.entrySet()) {
                Document doc = docEntry.getKey();
                Boolean feed = docEntry.getValue();
                if (feed){
                    sumRelevant += doc.bagOfWords.get(term);
                    relevantCount++;
                }
                else{
                    sumNonRelevant += doc.bagOfWords.get(term);
                    nonRelevantCount++;
                }
            }
            sumRelevant = 0.0;
            if (relevantCount > 0.0){
                sumRelevant /= relevantCount;
            }
            nonRelevantCount = 0.0;
            if (nonRelevantCount > 0.0){
                sumNonRelevant /= nonRelevantCount;
            }
            finalValue = alpha*queryValue + beta*sumRelevant - gamma*sumNonRelevant;
            result.put(term,finalValue);
        }
        if (commandParameters.verbose){
            System.out.println("Feedback q':");
            result.termsValue.forEach((k,v) ->  {
                if (v > 0.0) System.out.printf("%s: %g, ",k,v);
            });
            System.out.println();
        }
        return result;
    }
}

