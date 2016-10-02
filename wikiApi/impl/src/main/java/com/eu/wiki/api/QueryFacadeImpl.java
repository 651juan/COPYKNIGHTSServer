package com.eu.wiki.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Juan on 01/10/2016.
 */
public class QueryFacadeImpl implements QueryFacade {

    private String BaseUrl;

    public QueryFacadeImpl(String BaseUrl) {
        this.BaseUrl = BaseUrl += "?action=query&";
    }

    @Override
    public QueryResult query(Query q) {
        String url = this.generateUrl(q);
        Client client = Client.create();
        ClientResponse response = null;
        try {
            WebResource webResource = client.resource(url.replace("|","%7C"));
            response = webResource.accept("application/json").get(ClientResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) {
            return parseResult(response.getEntity(String.class));
        } else {
            return null;
        }
    }

    private String generateUrl(Query q){
        StringBuilder workingUrl = new StringBuilder(BaseUrl);
        if (!q.getTitles().isEmpty()) {

            workingUrl.append(TITLE_ID);
            workingUrl.append(String.join("|", q.getTitles()));
            workingUrl.append('&');
        }
        if (!q.getPageids().isEmpty()) {
            workingUrl.append(PAGES_ID);
            workingUrl.append(q.getPageids().stream().map(Object::toString).collect(Collectors.joining("|")));
            workingUrl.append('&');
        }
        if (q.getList() != null) {
            workingUrl.append(LIST_ID);
            workingUrl.append(q.getLimit());
            workingUrl.append('&');
        }
        if (q.getCmtitle() != null) {
            workingUrl.append(CMTITLE_ID);
            workingUrl.append(q.getCmtitle());
            workingUrl.append('&');
        }
        if (q.getCmContinue() != null) {
            workingUrl.append(CMCONTINUE_ID);
            workingUrl.append(q.getCmContinue());
            workingUrl.append('&');
        }
        if (q.getLimit() != null) {
            workingUrl.append(LIMIT_ID);
            workingUrl.append(q.getLimit());
            workingUrl.append('&');
        }
        if (q.getCmSort() != null) {
            workingUrl.append(SORT_ID);
            workingUrl.append(q.getCmSort());
            workingUrl.append('&');
        }
        if (q.getCmDir() != null) {
            workingUrl.append(DIR_ID);
            workingUrl.append(q.getCmDir());
            workingUrl.append('&');
        }
        if (q.isNcontinue()) {
            workingUrl.append(CONTINUE_ID);
        }
        //Temp fix to get all content
        workingUrl.append("prop=revisions&rvprop=content&");
        workingUrl.append(FORMAT_ID);
        workingUrl.append(Query.FORMAT);
        return workingUrl.toString().endsWith("&") ? workingUrl.toString().substring(workingUrl.toString().length()-2) : workingUrl.toString();
    }

    private QueryResult parseResult(String raw) {
        Map<String,Object> myMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            myMap = objectMapper.readValue(raw, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        myMap = (LinkedHashMap<String, Object>) myMap.get(TokenType.TOK_HEAD_QUERY.getTokenValue());
        if(myMap != null) {
            myMap = (LinkedHashMap<String, Object>) myMap.get(TokenType.TOK_HEAD_PAGES.getTokenValue());
            if(myMap != null) {
                //Get first entry in linked hash map should be pageid (cant access it by .getKey() different for every query)
                String pageIDAsKey = myMap.entrySet().iterator().next().getKey();
                int pageID = Integer.valueOf(pageIDAsKey);
                if(pageID < 0) {
                    //Article is Missing
                    return new QueryResult();
                }
                //Assume its a short article
                Article myArticle = new Article(pageID, true);
                myMap = (LinkedHashMap<String, Object>) myMap.get(pageIDAsKey);
                if(myMap != null) {
                    String ns = myMap.get(TokenType.TOK_HEAD_NS.getTokenValue()).toString();
                    String title = myMap.get(TokenType.TOK_HEAD_TITLE.getTokenValue()).toString();
                    myArticle.setName(title);

                    myMap = (LinkedHashMap<String, Object>) ((ArrayList<Object>) myMap.get(TokenType.TOK_HEAD_REVISIONS.getTokenValue())).get(0);

                    if (myMap != null) {
                        List<Article> parsedResults = new ArrayList<>();

                        String contentFormat = myMap.get(TokenType.TOK_CONTENT_FORMAT.getTokenValue()).toString();
                        String contentModel = myMap.get(TokenType.TOK_CONTENT_MODEL.getTokenValue()).toString();
                        String rawContent = myMap.get(TokenType.TOK_CONTENT.getTokenValue()).toString().replaceAll("\\n", "");

                        if(rawContent != null || rawContent.equalsIgnoreCase("")) {
                            ArticleParser myParser = new ArticleParser();
                            myArticle.setRawContent(rawContent);
                            myArticle = myParser.parse(myArticle);
                            myArticle.setShortArticle(false);
                        }

                        parsedResults.add(myArticle);
                        return new QueryResult(parsedResults);
                    }
                }
            }
        }

       /* Old version directly pass full raw to parser
        List<Article> parsedResults = new ArrayList<>();
        ArticleParser myParser = new ArticleParser();

        parsedResults.add(myParser.parse(raw));

        return new QueryResult(parsedResults);*/
        return new QueryResult();
    }
}
