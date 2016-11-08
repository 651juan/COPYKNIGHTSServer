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
     * Main parsing function, given the raw json will return a ArticleList containing a list of Article objects
     * @param raw the raw json
     * @return ArticleList containing a list of Article Objects
     */
    public ArticleList parseResult(String raw) {
        Map<String,Object> myMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            myMap = objectMapper.readValue(raw, HashMap.class);
        } catch (IOException e) {
            return new ArticleList("Unable to construct map from JSON.");
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
                ArticleList tmpResult = this.parseCategory(myMap);
                tmpResult.setCmContinue(cmContinue);
                return tmpResult;
            }
        }

        return new ArticleList("Unknown JSON format.");
    }

    //Private Methods

    /**
     * Parses raw data when it contains page info and content
     * @param myMap the pages map
     * @return ArticleList containing a list of Article objects and their contents
     */
    private ArticleList parsePages(Map<String,Object> myMap){
        myMap = (LinkedHashMap<String, Object>) myMap.get(TokenType.TOK_HEAD_PAGES.getTokenValue());
        if(myMap != null) {
            List<Article> parsedResults = new ArrayList<>();
            for (Map.Entry<String, Object> entry : myMap.entrySet()) {
                int pageID = -1;
                try {
                    pageID = Integer.valueOf(entry.getKey());
                }catch(Exception e) {
                    return new ArticleList("Bad ID Unable to convert to Integer.");
                }

                //Assume its a short article
                Article myArticle = new Article(pageID, true);

                LinkedHashMap<String, Object> tmpMap = (LinkedHashMap<String, Object>) myMap.get(entry.getKey());
                if (tmpMap != null) {
                    String title = tmpMap.get(TokenType.TOK_HEAD_TITLE.getTokenValue()).toString();

                    if(tmpMap.containsKey(TokenType.TOK_HEAD_MISSING.getTokenValue())){
                        return new ArticleList("Missing Article Title: " + title);
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
            return new ArticleList(parsedResults);
        }

        //Return Warning
        return new ArticleList("Token not found " + TokenType.TOK_HEAD_PAGES.getTokenValue());
    }

    /**
     * Parses raw data when its of the form of categories
     * @param myMap the category members map
     * @return ArticleList containing a list of Article objects which are short Articles
     */
    private ArticleList parseCategory(Map<String,Object> myMap){
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

            return new ArticleList(parsedResults);
        }

        //Return Warning
        return new ArticleList("Token not found " + TokenType.TOK_HEAD_CATEGORY_MEMBERS.getTokenValue());
    }

    /**
     * Parses raw data which must be assigned to the raw data attribute of the Article object
     * @param shortArticle the article object containing the raw data to parse
     * @return a parsed Article without the raw data
     */
    private Article parse(Article shortArticle) {

        String tmp = "";

        this.pRawData = shortArticle.getRawContent();

        boolean[] tmpVector = new boolean[34];
        Arrays.fill(tmpVector, false);

        //Start parsing
        shortArticle.setYear(this.getData(TokenType.TOK_YEAR));
        shortArticle.setTitle(this.getData(TokenType.TOK_TITLE));
        shortArticle.setAuthors(this.getAuthors());
        shortArticle.setCitation(this.getData(TokenType.TOK_CITATION));
        shortArticle.setAbstract(this.getData(TokenType.TOK_ABSTRACT));
        shortArticle.setLinks(this.getLinks());
        shortArticle.setRefences(this.getReferences());
        FundamentalIssue[] tmpFI = this.getFundamentalIssues();
        shortArticle.setFundamentalIssues(tmpFI);
        EvidenceBasedPolicy[] tmpEVP = this.getEvidenceBasedPolicies();
        shortArticle.setEvidenceBasedPolicies(tmpEVP);
        shortArticle.setDiscipline(this.splitData(this.getData(TokenType.TOK_DISCIPLINE), ","));

        tmpVector = this.setFIVector(tmpVector, tmpFI, 0);
        tmpVector = this.setEBPVector(tmpVector, tmpEVP, 6);

        //Start parsing datasets
        if (this.checkDataset()) {
            Datasets myDatasets;
            String dataDescription = this.getData(TokenType.TOK_DAT_DESCRIPTION);
            String dataYear = this.getData(TokenType.TOK_DAT_YEAR);
            String dataType = this.getData(TokenType.TOK_DAT_TYPE);
            myDatasets = new Datasets(dataDescription, dataYear, dataType);
            myDatasets.setDataSources(this.splitData(this.getData(TokenType.TOK_DAT_SOURCES), ";"));
            myDatasets.setMethodOfCollection(this.getMOC(this.splitData(this.getData(TokenType.TOK_DAT_MOC), ",")));
            myDatasets.setMethodOfAnalysis(this.getMOA(this.splitData(this.getData(TokenType.TOK_DAT_MOA), ",")));
            myDatasets.setIndustry(this.getIndustries(this.splitData(this.getData(TokenType.TOK_DAT_INDUSTRY), ";")));
            myDatasets.setCountry(this.splitData(this.getData(TokenType.TOK_DAT_COUNTRY), ";"));
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
        int idx = this.pRawData.indexOf(type.getTokenValue());
        if(idx != -1) {
            idx += type.getValueLength();
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
            if(author.contains(" and ") && splitAuthors.length > 1) {
                String[] furtherSplit = author.split(" and ");
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
    private URL[] getLinks() {
        List<URL> tmpResult = new ArrayList<URL>();

        int idx = 0;
        int prevIdx = 0;
        while(idx >= 0) {
            prevIdx = idx;
            idx = this.pRawData.indexOf(TokenType.TOK_LINK.getTokenValue(), idx)+TokenType.TOK_LINK.getValueLength();

            if(idx <= prevIdx) {
                break;
            }

            String tmp = this.pRawData.substring(idx, this.pRawData.indexOf('|', idx));
            if(tmp.length() > 0) {
                tmpResult.add(this.normaliseURL(tmp));
            }
        }

        return tmpResult.toArray(new URL[tmpResult.size()]);
    }

    /**
     * Returns an array of type MethodOfCollection of all the Medthods of collection the article belongs to
     * @return A MethodOfCollection array of Methods of collection
     */
    private MethodOfCollection[] getMOC(String[] moc) {
        //TODO add sub methods of collection?
        //MethodOfCollection[] result = new MethodOfCollection[moc.length];
        MethodOfCollection[] result = new MethodOfCollection[1];

        if(Arrays.asList(moc).contains("Quantitative Collection Methods")){
            result[0] = MethodOfCollection.MOC_1;
        }else if(Arrays.asList(moc).contains("Qualitative Collection Methods")) {
            result[0] = MethodOfCollection.MOC_2;
        }else {
            result[0] = MethodOfCollection.UNKNOWN_MOC;
        }
        return result;
    }

    /**
     * Returns an array of type MethodOfAnalysis of all the Methods of analysis the article belongs to
     * @return A MethodOfAnalysis array of Industries
     */
    private MethodOfAnalysis[] getMOA(String[] moa) {
        //TODO add sub methods of analysis?
        //MethodOfAnalysis[] result = new MethodOfAnalysis[moa.length];
        MethodOfAnalysis[] result = new MethodOfAnalysis[1];

        if(Arrays.asList(moa).contains("Quantitative Analysis Methods")){
            result[0] = MethodOfAnalysis.MOA_1;
        }else if(Arrays.asList(moa).contains("Qualitative Collection Methods")) {
            result[0] = MethodOfAnalysis.MOA_2;
        }else {
            result[0] = MethodOfAnalysis.UNKNOWN_MOA;
        }
        return result;
    }

    /**
     * Returns an array of type Industry of all the Industries the article belongs to
     * @return An Industry array of Industries
     */
    private Industry[] getIndustries(String[] industries) {
        Industry[] result = new Industry[industries.length];

        for(int i = 0; i < industries.length; i++) {
            String tmpVal = industries[i];

            if(Industry.INDUSTRY_1.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_1;
            }else if(Industry.INDUSTRY_2.equals(tmpVal)) {
                result[i] = Industry.INDUSTRY_2;
            }else if(Industry.INDUSTRY_3.equals(tmpVal)) {
                result[i] = Industry.INDUSTRY_3;
            }else if(Industry.INDUSTRY_4.equals(tmpVal)) {
                result[i] = Industry.INDUSTRY_4;
            }else if(Industry.INDUSTRY_5.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_5;
            }else if(Industry.INDUSTRY_6.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_6;
            }else if(Industry.INDUSTRY_7.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_7;
            }else if(Industry.INDUSTRY_8.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_8;
            }else if(Industry.INDUSTRY_9.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_9;
            }else if(Industry.INDUSTRY_10.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_10;
            }else if(Industry.INDUSTRY_11.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_11;
            }else if(Industry.INDUSTRY_12.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_12;
            }else if(Industry.INDUSTRY_13.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_13;
            }else if(Industry.INDUSTRY_14.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_14;
            }else if(Industry.INDUSTRY_15.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_15;
            }else if(Industry.INDUSTRY_16.equals(tmpVal)){
                result[i] = Industry.INDUSTRY_16;
            }else{
                result[i] = Industry.UNKNOWN_INDUSTRY;
            }
        }

        return result;
    }

    /**
     * Returns an array of type FundamentalIssue of all the fundamental issues the article belongs to
     * @return A FundamentalIssue array of fundamental issues
     */
    private FundamentalIssue[] getFundamentalIssues() {
        String[] rawIssues = this.getData(TokenType.TOK_FI).split(",");
        FundamentalIssue[] result = new FundamentalIssue[rawIssues.length];

        for(byte i = 0; i < rawIssues.length; i++) {
            String tmpVal = rawIssues[i];

            if(FundamentalIssue.ISSUE_1.equals(tmpVal)){
                result[i] = FundamentalIssue.ISSUE_1;
            }else if(FundamentalIssue.ISSUE_2.equals(tmpVal)) {
                result[i] = FundamentalIssue.ISSUE_2;
            }else if(FundamentalIssue.ISSUE_3.equals(tmpVal)) {
                result[i] = FundamentalIssue.ISSUE_3;
            }else if(FundamentalIssue.ISSUE_4.equals(tmpVal)) {
                result[i] = FundamentalIssue.ISSUE_4;
            }else if(FundamentalIssue.ISSUE_5.equals(tmpVal)){
                result[i] = FundamentalIssue.ISSUE_5;
            }else{
                result[i] = FundamentalIssue.UNKNOWN_ISSUE;
            }
        }

        return result;
    }

    /**
     * Returns an array of type EvidenceBasedPolicy of all the evidence based policies the article belongs to
     * @return An EvidenceBasedPolicy array of evidence based policies
     */
    private EvidenceBasedPolicy[] getEvidenceBasedPolicies() {
        String[] rawPolicies = this.getData(TokenType.TOK_EBP).split(",");
        EvidenceBasedPolicy[] result = new EvidenceBasedPolicy[rawPolicies.length];

        for(byte i = 0; i < rawPolicies.length; i++) {
            String tmpVal = rawPolicies[i];
            if(EvidenceBasedPolicy.POLICY_A.equals(tmpVal)){
                result[i] = EvidenceBasedPolicy.POLICY_A;
            }else if(EvidenceBasedPolicy.POLICY_B.equals(tmpVal)) {
                result[i] = EvidenceBasedPolicy.POLICY_B;
            }else if(EvidenceBasedPolicy.POLICY_C.equals(tmpVal)) {
                result[i] = EvidenceBasedPolicy.POLICY_C;
            }else if(EvidenceBasedPolicy.POLICY_D.equals(tmpVal)){
                result[i] = EvidenceBasedPolicy.POLICY_D;
            }else if(EvidenceBasedPolicy.POLICY_E.equals(tmpVal)){
                result[i] = EvidenceBasedPolicy.POLICY_E;
            }else if(EvidenceBasedPolicy.POLICY_F.equals(tmpVal)) {
                result[i] = EvidenceBasedPolicy.POLICY_F;
            }else {
                result [i] = EvidenceBasedPolicy.UNKNOWN_POLICY;
            }
        }

        return result;
    }
    /**
     * Returns an Array of Dataset objects which contain individual dataset data if any are available in the raw data.
     * @return an Array of Dataset objects
     */
    private Dataset[] getDatasets() {
        int idx = this.pRawData.indexOf(TokenType.TOK_DAT_DATASET.getTokenValue()) + TokenType.TOK_DAT_DATASET.getValueLength();
        if(idx != -1) {
            int tmpIdx = this.pRawData.indexOf("|}}|}}", idx);
            if(tmpIdx >= 0) {
                String rawDatasetData = this.pRawData.substring(idx, tmpIdx + 3).trim();
                String[] rawDatasets = rawDatasetData.split("\\}\\}\\{\\{");
                Dataset[] result = new Dataset[rawDatasets.length];

                for (int i = 0; i < rawDatasets.length; i++) {
                    if (rawDatasets[i].startsWith("{{")) {
                        rawDatasets[i] = rawDatasets[i].substring(2);
                    }
                    if (rawDatasets[i].endsWith("|}}")) {
                        rawDatasets[i] = rawDatasets[i].substring(0, rawDatasets[i].length() - 2);
                    }

                    int datasetSize = -1;
                    if (this.checkDataset(rawDatasets[i])) {

                        try {
                            datasetSize = Integer.valueOf(this.getDatasetData(TokenType.TOK_DAT_SAMPLE_SIZE, rawDatasets[i]).replaceAll(",",""));
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
        int idx = rawData.indexOf(type.getTokenValue());
        if (idx != -1) {
            idx += type.getValueLength();
            return rawData.substring(idx, rawData.indexOf('|', idx)).trim();
        }
        return "";
    }

    /**
     * Splits a given Strong using the given delimeter and returns an array of strings
     * @param toSplit the String to split
     * @param delimeter the delimeter to look for in the String
     * @return A String array of the split result
     */
    private String[] splitData(String toSplit, String delimeter){
        return Arrays.stream(toSplit.split(delimeter)).map(String::trim).toArray(String[]::new);
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
        int idx = this.pRawData.indexOf(TokenType.TOK_DAT_DATASET.getTokenValue()) + TokenType.TOK_DAT_DATASET.getValueLength();
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
        int idx = toCheck.indexOf(TokenType.TOK_DAT_DATASET.getTokenValue())+TokenType.TOK_DAT_DATASET.getValueLength()+1;
        if(idx >= 0) {
            toCheck = toCheck.substring(idx,toCheck.length());
            idx = toCheck.lastIndexOf("|");
            if(idx >= 0) {
                toCheck = toCheck.substring(0, idx);
                if(toCheck.length() > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Updates the Article vector with the provided Fundamental Issues
     * @param currentVector The current state of the vector
     * @param fiArr The Fundamental Issues assigned to the current article
     * @param startIdx The start index of where the Fundamental Issues is located in the vector
     * @return An updated boolean[] vector
     */
    private boolean[] setFIVector(boolean[] currentVector, FundamentalIssue[] fiArr, int startIdx) {
        for(FundamentalIssue fi : fiArr) {
            if(fi == FundamentalIssue.ISSUE_1){
                currentVector[startIdx] = true;
            }else if(fi == FundamentalIssue.ISSUE_2) {
                currentVector[startIdx+1] = true;
            }else if(fi ==FundamentalIssue.ISSUE_3) {
                currentVector[startIdx+2] = true;
            }else if(fi == FundamentalIssue.ISSUE_4) {
                currentVector[startIdx+3] = true;
            }else if(fi == FundamentalIssue.ISSUE_5){
                currentVector[startIdx+4] = true;
            }else{
                currentVector[startIdx+5] = true;
            }
        }

        return currentVector;
    }

    /**
     * Updates the Article Vector with the provided Evidence Policies
     * @param currentVector The current state of the vector
     * @param ebpArr The Evidence Policies assigned to the current article
     * @param startIdx The start index of where the Evidence Based Policies is located in the vector
     * @return An updated boolean[] vector
     */
    private boolean[] setEBPVector(boolean[] currentVector, EvidenceBasedPolicy[] ebpArr, int startIdx) {
        for(EvidenceBasedPolicy ebp : ebpArr) {
            if(ebp == EvidenceBasedPolicy.POLICY_A){
                currentVector[startIdx] = true;
            }else if(ebp == EvidenceBasedPolicy.POLICY_B) {
                currentVector[startIdx+1] = true;
            }else if(ebp == EvidenceBasedPolicy.POLICY_C) {
                currentVector[startIdx+2] = true;
            }else if(ebp == EvidenceBasedPolicy.POLICY_D){
                currentVector[startIdx+3] = true;
            }else if(ebp == EvidenceBasedPolicy.POLICY_E){
                currentVector[startIdx+4] = true;
            }else if(ebp == EvidenceBasedPolicy.POLICY_F) {
                currentVector[startIdx+5] = true;
            }else {
                currentVector[startIdx+6] = true;
            }
        }
        return currentVector;
    }
}