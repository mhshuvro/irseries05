package com.ir05;

import java.io.*;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;

public class Main {

    private static final int topHits = 4000;

    public static void main(String[] args) {

        // parsing the queries
        FetchQueries parser = new FetchQueries();

        try {

            List<String> queryList = parser.fetchQueriesFromXML();

            System.out.println("Building index in memory...");

            AnalyzeIndex anIndex = new AnalyzeIndex();

            anIndex.indexCACMFiles();

            System.out.println("Index building complete.\n");

            System.out.println("Searching through index with top hits = " + topHits + "\n");

            // searching the queries and calculate results for each query
            ScoreDoc[] queryHits;

            int results = 0;

            int querySequence = 1;

            for(int i=0; i < queryList.size(); i++) {

                queryHits = anIndex.search(queryList.get(i), topHits);

                results += queryHits.length;

                System.out.println("Query" + querySequence + ": " + queryList.get(i) + ",\n Number of hits: " + queryHits.length + "\n");

                querySequence++;
            }

            System.out.println("The total no. of results: " + results + "\n");

        } catch (FileNotFoundException | XMLStreamException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (ParseException e) {

            e.printStackTrace();

        }

    }
}
