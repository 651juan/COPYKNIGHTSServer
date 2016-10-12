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
     * Main parsing function, given the raw json will return a QueryResult containing a list of Article objects
     * @param raw the raw json
     * @return QueryResult containing a list of Article Objects
     */
    public QueryResult parseResult(String raw) {
        Map<String,Object> myMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            myMap = objectMapper.readValue(raw, HashMap.class);
        } catch (IOException e) {
            return new QueryResult("Unable to construct map from JSON.");
        }

        String cmContinue = "";
        Map<String, Object> tmp = (LinkedHashMap<String, Object>) myMap.get(TokenType.TOK_HEAD_CONTINUE.getTokenValue());
        if(tmp != null) {
            try {
                cmContinue = tmp.get(TokenType.TOK_HEAD_CMCONTINUE.getTokenValue()).toString();
            }catch(Exception e) {
                cmContinue = "";
            }
        }

        myMap = (LinkedHashMap<String, Object>) myMap.get(TokenType.TOK_HEAD_QUERY.getTokenValue());
        if(myMap != null) {
            if (myMap.containsKey(TokenType.TOK_HEAD_PAGES.getTokenValue())) {
                return this.parsePages(myMap);
            } else if (myMap.containsKey(TokenType.TOK_HEAD_CATEGORY_MEMBERS.getTokenValue())) {
                QueryResult tmpResult = this.parseCategory(myMap);
                tmpResult.setCmcontinue(cmContinue);
                return tmpResult;
            }
        }

        return new QueryResult("Unknown JSON format.");
    }

    //Private Methods

    /**
     * Parses raw data when it contains page info and content
     * @param myMap the pages map
     * @return QueryResult containing a list of Article objects and their contents
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
                    return new QueryResult("Bad ID Unable to convert to Integer.");
                }

                //Assume its a short article
                Article myArticle = new Article(pageID, true);

                LinkedHashMap<String, Object> tmpMap = (LinkedHashMap<String, Object>) myMap.get(entry.getKey());
                if (tmpMap != null) {
                    String title = tmpMap.get(TokenType.TOK_HEAD_TITLE.getTokenValue()).toString();

                    if(tmpMap.containsKey(TokenType.TOK_HEAD_MISSING.getTokenValue())){
                        return new QueryResult("Missing Article Title: " + title);
                    }

                    String ns = tmpMap.get(TokenType.TOK_HEAD_NS.getTokenValue()).toString();
                    myArticle.setName(title);
                    tmpMap = (LinkedHashMap<String, Object>) ((ArrayList<Object>) tmpMap.get(TokenType.TOK_HEAD_REVISIONS.getTokenValue())).get(0);

                    if (tmpMap != null) {
                        String contentFormat = tmpMap.get(TokenType.TOK_CONTENT_FORMAT.getTokenValue()).toString();
                        String contentModel = tmpMap.get(TokenType.TOK_CONTENT_MODEL.getTokenValue()).toString();
                        String rawContent = tmpMap.get(TokenType.TOK_CONTENT.getTokenValue()).toString().replaceAll("\\n", "");
                        rawContent = rawContent.replaceAll("\\}\\}", "|}}");

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

        //Return Warning
        return new QueryResult("Token not found " + TokenType.TOK_HEAD_PAGES.getTokenValue());
    }

    /**
     * Parses raw data when its of the form of categories
     * @param myMap the category members map
     * @return QueryResult containing a list of Article objects which are short Articles
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

        //Return Warning
        return new QueryResult("Token not found " + TokenType.TOK_HEAD_CATEGORY_MEMBERS.getTokenValue());
    }

    /**
     * Parses raw data which must be assigned to the raw data attribute of the Article object
     * @param shortArticle the article object containing the raw data to parse
     * @return a parsed Article without the raw data
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
        if (this.checkDataset()) {
            Datasets myDatasets;
            String dataDescription = this.getData(TokenType.TOK_DAT_DESCRIPTION);
            String dataYear = this.getData(TokenType.TOK_DAT_YEAR);
            String dataType = this.getData(TokenType.TOK_DAT_TYPE);
            myDatasets = new Datasets(dataDescription, dataYear, dataType);
            myDatasets.setMethodOfCollection(this.getData(TokenType.TOK_DAT_MOC));
            myDatasets.setMethodOfAnalysis(this.getData(TokenType.TOK_DAT_MOA));
            myDatasets.setIndustry(this.getData(TokenType.TOK_DAT_INDUSTRY));
            myDatasets.setCountry(this.getData(TokenType.TOK_DAT_COUNTRY));
            myDatasets.setCrossCountry(this.getData(TokenType.TOK_DAT_CROSS_COUNTRY));
            myDatasets.setComparative(this.getData(TokenType.TOK_DAT_COMPARATIVE));
            myDatasets.setGovernmentOrPolicy(this.getData(TokenType.TOK_DAT_GOP));
            myDatasets.setLiteratureReview(this.getData(TokenType.TOK_DAT_LR));
            myDatasets.setFundedBy(this.getData(TokenType.TOK_DAT_FUNDED));

            myDatasets.setDatasets(this.getDatasets());
            shortArticle.setDatasets(myDatasets);
        }
        //Remove raw data
        shortArticle.setRawContent("");

        return shortArticle;
    }

    /**
     * Given a TokenType tries to find the token in the body of the raw data
     * @param type the token to look for
     * @return a string containing the requested token data or an empty string if the token is not found
     */
    private String getData(TokenType type) {
        int idx = this.pRawData.indexOf(type.getTokenValue()) + type.getValueLength()+1;
        if(idx >= 0) {
            return this.pRawData.substring(idx, this.pRawData.indexOf('|', idx)).trim();
        }
        return "";
    }

    /**
     * Splits the Authors string into an array of type String of Author names.
     * @return the Author String array
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
     * @return an array of References
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
    //TODO try to obtain other links that are not Authentic links
    private URL getLinks() {
        URL tmpResult = null;
        String tmp = this.getData(TokenType.TOK_LINK);


        if(!tmp.equalsIgnoreCase("")){
            tmpResult = this.normaliseURL(tmp);
        }

        return tmpResult;
    }

    /**
     * Returns an Array of Dataset objects which contain individual dataset data if any are available in the raw data.
     * @return an Array of Dataset objects
     */
    private Dataset[] getDatasets() {
        int idx = this.pRawData.indexOf(TokenType.TOK_DAT_DATASET.getTokenValue()) + TokenType.TOK_DAT_DATASET.getValueLength()+1;
        if(idx != -1) {
            int tmpIdx = this.pRawData.indexOf("|}}|}}", idx);
            if(tmpIdx >= 0) {
                String rawDatasetData = this.pRawData.substring(idx, tmpIdx + 2).trim();
                String[] rawDatasets = rawDatasetData.split("\\}\\}\\{\\{");
                Dataset[] result = new Dataset[rawDatasets.length];

                for (int i = 0; i < rawDatasets.length; i++) {
                    if (rawDatasets[i].startsWith("{{")) {
                        rawDatasets[i] = rawDatasets[i].substring(2);
                    }
                    if (rawDatasets[i].endsWith("|}}")) {
                        rawDatasets[i] = rawDatasets[i].substring(0, rawDatasets[i].length() - 3);
                    }

                    int datasetSize = -1;
                    if (this.checkDataset(rawDatasets[i])) {
                        try {
                            datasetSize = Integer.valueOf(this.getDatasetData(TokenType.TOK_DAT_SAMPLE_SIZE, rawDatasets[i]));
                        } catch (Exception e) {
                            datasetSize = -1;
                        }
                        String loa = this.getDatasetData(TokenType.TOK_DAT_LOG, rawDatasets[i]);
                        String dmy = this.getDatasetData(TokenType.TOK_DAT_DMY, rawDatasets[i]);

                        Dataset tmp = new Dataset(datasetSize, loa, dmy);

                        result[i] = tmp;
                    }
                }
                return result;
            }
        }


        return null;
    }

    /**
     * Obtains the data from the individual datasets
     * @param type the token type of the data to obtain
     * @param rawData the individual raw dataset data
     * @return a String containing the data requested or an empty String if the data is not found.
     */
    private String getDatasetData(TokenType type, String rawData) {
        int idx = rawData.indexOf(type.getTokenValue()) + type.getValueLength() + 1;
        if (idx != -1) {
            return rawData.substring(idx, rawData.indexOf('|', idx)).trim();
        }
        return "";
    }

    /**
     * Normalises URL strings to avoid malformed url exceptions
     * @param toNormalise the url string to normalise
     * @return the normalised url, null in case of exceptions
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

    /**
     * Checks the rawdata for inforamtion about the datasets
     * @return true if the raw data contains information about the datasets used, false otherwise.
     */
    private boolean checkDataset() {
        int idx = this.pRawData.indexOf(TokenType.TOK_DAT_DATASET.getTokenValue()) + TokenType.TOK_DAT_DATASET.getValueLength()+1;
        if(idx >= 0) {
            String checkDatasetContent = this.pRawData.substring(idx, this.pRawData.indexOf("|}}", idx)).trim();
            if(!checkDatasetContent.equalsIgnoreCase("")){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks individual dataset data and makes sure it is not blank.
     * @param toCheck individual dataset data to check
     * @return True if the individual dataset data is not blank, false otherwise.
     */
    private boolean checkDataset(String toCheck) {
        int idx = toCheck.indexOf(TokenType.TOK_DAT_DATASET.getTokenValue())+TokenType.TOK_DAT_DATASET.getValueLength();
        if(idx >= 0) {
            toCheck = toCheck.substring(idx,toCheck.length());
            idx = toCheck.indexOf("|}");
            if(idx >= 0) {
                toCheck = toCheck.substring(0, idx);
                if(toCheck.length() > 0) {
                    return true;
                }
            }
        }

        return false;
    }
}
