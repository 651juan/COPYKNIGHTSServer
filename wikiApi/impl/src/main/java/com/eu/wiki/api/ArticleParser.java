package com.eu.wiki.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by Juan on 29/09/2016.
 */
public class ArticleParser {
    //Attributes
    private String pRawData;

    //Constructors
    /**
     * Initialises the ArticleParser and its attributes
     */
    public ArticleParser() {
        this.pRawData = "";
    }

    /**
     * Main parsing function checks checks the form of the raw data weather it is of the form of categories or pages
     * @param raw
     * @return QueryResult
     */
    public QueryResult parseResult(String raw) {
        Map<String,Object> myMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            myMap = objectMapper.readValue(raw, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        myMap = (LinkedHashMap<String, Object>) myMap.get(TokenType.TOK_HEAD_QUERY.getTokenValue());
        if(myMap != null) {
            if (myMap.containsKey(TokenType.TOK_HEAD_PAGES.getTokenValue())) {
                return this.parsePages(myMap);
            } else if (myMap.containsKey(TokenType.TOK_HEAD_CATEGORY_MEMBERS.getTokenValue())) {
                return this.parseCategory(myMap);
            }
        }

        return new QueryResult();
    }

    //Private Methods
    /**
     * Parses raw data when it contains page info and content
     * @param myMap
     * @return QueryResult
     */
    private QueryResult parsePages(Map<String,Object> myMap){
        myMap = (LinkedHashMap<String, Object>) myMap.get(TokenType.TOK_HEAD_PAGES.getTokenValue());
        if(myMap != null) {
            List<Article> parsedResults = new ArrayList<>();
            for (Map.Entry<String, Object> entry : myMap.entrySet()) {
                int pageID = -1;
                try {
                    pageID = Integer.valueOf(entry.getKey());
                }catch(Exception e) {
                    //TODO Generate bad id warning here
                    return new QueryResult();
                }

                //Assume its a short article
                Article myArticle = new Article(pageID, true);

                LinkedHashMap<String, Object> tmpMap = (LinkedHashMap<String, Object>) myMap.get(entry.getKey());
                if (tmpMap != null) {
                    String ns = tmpMap.get(TokenType.TOK_HEAD_NS.getTokenValue()).toString();
                    String title = tmpMap.get(TokenType.TOK_HEAD_TITLE.getTokenValue()).toString();
                    myArticle.setName(title);

                    tmpMap = (LinkedHashMap<String, Object>) ((ArrayList<Object>) tmpMap.get(TokenType.TOK_HEAD_REVISIONS.getTokenValue())).get(0);

                    if (tmpMap != null) {
                        String contentFormat = tmpMap.get(TokenType.TOK_CONTENT_FORMAT.getTokenValue()).toString();
                        String contentModel = tmpMap.get(TokenType.TOK_CONTENT_MODEL.getTokenValue()).toString();
                        String rawContent = tmpMap.get(TokenType.TOK_CONTENT.getTokenValue()).toString().replaceAll("\\n", "");

                        if (rawContent != null || rawContent.equalsIgnoreCase("")) {
                            ArticleParser myParser = new ArticleParser();
                            myArticle.setRawContent(rawContent);
                            myArticle = myParser.parse(myArticle);
                            myArticle.setShortArticle(false);
                        }

                        parsedResults.add(myArticle);
                    }
                }
            }
            return new QueryResult(parsedResults);
        }

        //Let parseRsult handle null and return warnings
        return null;
    }

    /**
     * Parses raw data when its of the form of categories
     * @param myMap
     * @return QueryResult
     */
    private QueryResult parseCategory(Map<String,Object> myMap){
        List<Object> results = (ArrayList<Object>) myMap.get(TokenType.TOK_HEAD_CATEGORY_MEMBERS.getTokenValue());
        if(results != null) {
            List<Article> parsedResults = new ArrayList<>();

            for(int i = 0; i < results.size(); i++) {
                myMap = (LinkedHashMap<String, Object>) results.get(i);
                int pageID = Integer.valueOf(myMap.get(TokenType.TOK_HEAD_PAGEID.getTokenValue()).toString());
                String ns = myMap.get(TokenType.TOK_HEAD_NS.getTokenValue()).toString();
                String title = myMap.get(TokenType.TOK_HEAD_TITLE.getTokenValue()).toString();
                parsedResults.add(new Article(pageID,title,true));
            }

            return new QueryResult(parsedResults);
        }

        //Let parseResult handle null and return warnings
        return null;
    }

    /**
     * Parses raw data which must be assigned to the raw data attribute of the Article object
     * @param shortArticle
     * @return Article
     */
    private Article parse(Article shortArticle) {

        String tmp = "";

        this.pRawData = shortArticle.getRawContent();

        //Start parsing
        shortArticle.setYear(this.getData(TokenType.TOK_YEAR));
        shortArticle.setTitle(this.getData(TokenType.TOK_TITLE));
        shortArticle.setAuthors(this.getAuthors());
        shortArticle.setCitation(this.getData(TokenType.TOK_CITATION));
        shortArticle.setAbstract(this.getData(TokenType.TOK_ABSTRACT));
        shortArticle.setLinks(this.getLinks());
        shortArticle.setRefences(this.getReferences());

        //Start parsing datasets
        if(this.checkDataset()) {
            Datasets myDatasets;
            String dataDescription = this.getData(TokenType.TOK_DAT_DESCRIPTION);
            String dataYear = this.getData(TokenType.TOK_DAT_YEAR);
            String dataType = this.getData(TokenType.TOK_DAT_TYPE);
            myDatasets = new Datasets(dataDescription, dataYear, dataType);
            myDatasets.setDatasets(this.getDatasets());
            shortArticle.setDatasets(myDatasets);
        }
        //Remove raw data
        shortArticle.setRawContent("");

        return shortArticle;
    }

    /**
     * Given a TokenType tries to find the token in the header of the raw data
     * @param type
     * @return String
     */
    private String getHeaderData(TokenType type) {
        int idx = this.pRawData.indexOf(type.getTokenValue()) + type.getValueLength()+1;
        if(idx != -1) {
            return this.pRawData.substring(idx, this.pRawData.indexOf(',', idx)).trim();
        }
        return "";
    }

    /**
     * Given a TokenType tries to find the token in the body of the raw data
     * @param type
     * @return String
     */
    private String getData(TokenType type) {
        int idx = this.pRawData.indexOf(type.getTokenValue()) + type.getValueLength()+1;
        if(idx != -1) {
            return this.pRawData.substring(idx, this.pRawData.indexOf('|', idx)).trim();
        }
        return "";
    }

    /**
     * Returns the Authors as an array of type String of the Article
     * @return String[]
     */
    private String[] getAuthors() {
        List<String> tmpResult = new ArrayList<>();
        String rawAuthors = this.getData(TokenType.TOK_AUTHOR);
        String[] splitAuthors = rawAuthors.split(";");

        for(String author:splitAuthors){
            if(author.contains("and")) {
                String[] furtherSplit = author.split("and");
                for(String subauthor:furtherSplit) {
                    tmpResult.add(subauthor.trim());
                }
            }else{
                tmpResult.add(author.trim());
            }
        }

        return tmpResult.toArray(new String[tmpResult.size()]);
    }

    /**
     * Returns the references of the Article as an Array of Reference objects
     * @return Reference[]
     */
    private Reference[] getReferences() {
        List<Reference> tmpResult = new ArrayList<>();
        String rawReferences = this.getData(TokenType.TOK_REFERENCES);
        String[] splitReferences = rawReferences.split(";");

        for(String reference:splitReferences) {
            tmpResult.add(new Reference(reference, null));
        }

        return tmpResult.toArray(new Reference[tmpResult.size()]);
    }

    /**
     * Returns all the links where the article can be found online as an Array of URLS
     * @return URL[]
     */
    private URL getLinks() {
        //TODO Normalise links to avoid Malformed URL
        URL tmpResult = null;
        String tmp = this.getData(TokenType.TOK_LINK);


        if(!tmp.equalsIgnoreCase("")){
            tmpResult = this.normaliseURL(tmp);
        }

        return tmpResult;
    }

    /**
     * Checks if the raw data contains dataset information
     * @return true if the dataset has dataset information false otherwise
     */
    private boolean checkDataset() {
        int idx = this.pRawData.indexOf(TokenType.TOK_DAT_DATASET.getTokenValue()) + TokenType.TOK_DAT_DATASET.getValueLength()+1;
        if(idx != -1) {
            String checkDatasetContent = this.pRawData.substring(idx, this.pRawData.indexOf("}}", idx)).trim();
            if(!checkDatasetContent.equalsIgnoreCase("")){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the dataset objects
     * @return
     */
    private Dataset[] getDatasets() {
        int idx = this.pRawData.indexOf(TokenType.TOK_DAT_DATASET.getTokenValue()) + TokenType.TOK_DAT_DATASET.getValueLength()+1;
        if(idx != -1) {
            String rawDatasetData = this.pRawData.substring(idx, this.pRawData.indexOf("}}}}", idx)+2).trim();
            String[] rawDatasets = rawDatasetData.split("\\}\\}\\{\\{");

            Dataset[] result = new Dataset[rawDatasets.length];

            for(int i = 0; i < rawDatasets.length; i++){
                if(rawDatasets[i].startsWith("{{")){
                    rawDatasets[i] = rawDatasets[i].substring(2);
                }
                if(rawDatasets[i].endsWith("}}")){
                    rawDatasets[i] = rawDatasets[i].substring(0,rawDatasets[i].length()-2);
                }

                int datasetSize = -1;

                try {
                    datasetSize = Integer.valueOf(this.getDatasetData(TokenType.TOK_DAT_SAMPLE_SIZE, rawDatasets[i]));
                }catch(Exception e) {
                    datasetSize = -1;
                }
                String loa = this.getDatasetData(TokenType.TOK_DAT_LOG, rawDatasets[i]);
                String dmy = this.getDatasetData(TokenType.TOK_DAT_DMY, rawDatasets[i]);

                Dataset tmp = new Dataset(datasetSize, loa, dmy);

                result[i] = tmp;
            }

            return result;
        }


        return null;
    }

    /**
     * Obtains the data from the short datasets
     * @param type the token type of the data to obtain
     * @param rawData the raw dataset data
     * @return String
     */
    private String getDatasetData(TokenType type, String rawData) {
        int idx = rawData.indexOf(type.getTokenValue()) + type.getValueLength() + 1;
        if (idx != -1) {
            if (type == TokenType.TOK_DAT_DMY) {
                return rawData.substring(idx, rawData.length()).trim();
            } else {
                return rawData.substring(idx, rawData.indexOf('|', idx)).trim();
            }

        }
        return "";
    }

    /**
     * Normalises URL strings to avoid malformed url exceptions
     * @param toNormalise the url string to normalise
     * @return the normalised url
     */
    private URL normaliseURL(String toNormalise) {
        if(!(toNormalise.startsWith("http://") || toNormalise.startsWith("https://"))){
            toNormalise = "http://" + toNormalise;
        }

        try {
            return new URL(toNormalise);
        } catch (MalformedURLException e) {
            System.err.println("Malformed URL: " + toNormalise);
            return null;
        }
    }
}
