package com.lab1;

public class Ranking implements Comparable{
    Document document;
    Double simIdf = 0.0;

    public Ranking(Document document, Double simIdf) {
        this.document = document;
        this.simIdf = simIdf;
    }

    @Override
    public int compareTo(Object o) {
        Ranking otherRanking = (Ranking) o;
        return otherRanking.simIdf.compareTo(this.simIdf);
    }
}
