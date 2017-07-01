package com.ir05;

/**
 * Created by shuvr on 7/1/2017.
 */

import java.io.*;
import java.util.*;
import javax.xml.stream.*;

public class FetchQueries {

    private XMLInputFactory xmlInputFactory;

    private XMLStreamReader xmlStreamReader;

    private String filePath = "cacm.query.xml";

    public FetchQueries() {

        xmlInputFactory = XMLInputFactory.newInstance();

    }

    public List<String> fetchQueriesFromXML() throws XMLStreamException, IOException {

        List<String> queries = new ArrayList<String>();

        FileInputStream fileInputStream = new FileInputStream(filePath);

        xmlStreamReader = xmlInputFactory.createXMLStreamReader(fileInputStream);

        int event;

        String tagContent = "";

        while(xmlStreamReader.hasNext()) {

            event = xmlStreamReader.next();

            switch(event){

                case XMLStreamConstants.CHARACTERS:
                    tagContent = xmlStreamReader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    if(xmlStreamReader.getLocalName().equals("text"))
                        queries.add(tagContent);
                    break;

            }

        }

        xmlStreamReader.close();

        fileInputStream.close();

        return queries;

    }

}
