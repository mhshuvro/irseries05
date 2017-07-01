package com.ir05;

/**
 * Created by shuvr on 7/1/2017.
 */

import java.io.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class AnalyzeIndex {

    private Directory ramDirectory;

    private Analyzer simpleAnalyzer;

    public String fieldName = "CACMTextField";

    private String CACMFilePath = "CACM";

    public AnalyzeIndex(){

        // For analyzing text
        simpleAnalyzer = new SimpleAnalyzer();

        // For storing index
        ramDirectory = new RAMDirectory();

    }

    public void indexCACMFiles() throws IOException{

        // Beginning the construction process of index
        IndexWriterConfig iwConfig = new IndexWriterConfig(simpleAnalyzer);

        iwConfig.setOpenMode(OpenMode.CREATE);	// Creating new index and removing any earlier indexed documents

        iwConfig.setRAMBufferSizeMB(256.0);		// Enhancing indexing performance

        IndexWriter iWriter = new IndexWriter(ramDirectory, iwConfig);

        Document doc = new Document();

        // Read from CACM corpus
        File folder = new File(CACMFilePath);

        File[] listOfFiles = folder.listFiles();

        org.jsoup.nodes.Document htmlDocument;

        Elements body;

        // Iterating through the files and parsing them
        for(int i=0; i < listOfFiles.length; i++) {

            htmlDocument = Jsoup.parse(listOfFiles[i], "UTF-8");

            body = htmlDocument.select("pre");

            doc.add(new Field(fieldName, body.text(), TextField.TYPE_STORED));

            iWriter.addDocument(doc);

            doc.clear();
        }

        iWriter.close();
    }

    public ScoreDoc[] search(String queryText, int topHits) throws IOException, ParseException{

        DirectoryReader dirReader = DirectoryReader.open(ramDirectory);

        IndexSearcher iSearcher = new IndexSearcher(dirReader);

        QueryParser qParser = new QueryParser(fieldName, simpleAnalyzer);

        Query query = qParser.parse(queryText);

        ScoreDoc[] hits = iSearcher.search(query, topHits).scoreDocs;

        return hits;
    }

}
