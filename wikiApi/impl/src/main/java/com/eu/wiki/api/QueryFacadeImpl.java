package com.eu.wiki.api;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.util.stream.Collectors;

/**
 * Created by Juan on 01/10/2016.
 */
public class QueryFacadeImpl implements QueryFacade {

    private String BaseUrl;
    private ArticleParser parser;

    public QueryFacadeImpl(String BaseUrl) {
        this.BaseUrl = BaseUrl += "?action=query&";
        parser = new ArticleParser();
    }

    @Override
    public QueryResult query(Query q) {
        String url = this.generateUrl(q);
        Client client = Client.create();
        ClientResponse response = null;
        try {
            WebResource webResource = client.resource(url.replace("|","%7C").replace(" ","%20"));
            response = webResource.accept("application/json").get(ClientResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) {
            return parser.parseResult(response.getEntity(String.class));
        } else {
            return null;
        }
    }

    public String generateUrl(Query q){
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
            workingUrl.append(q.getList());
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
            workingUrl.append('&');
        }
        //Temp fix to get all content
        workingUrl.append("prop=revisions&rvprop=content&");
        workingUrl.append(FORMAT_ID);
        workingUrl.append(Query.FORMAT);
        return workingUrl.toString().endsWith("&") ? workingUrl.toString().substring(workingUrl.toString().length()-2) : workingUrl.toString();
    }

}
