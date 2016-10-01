package com.eu.wiki.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan on 01/10/2016.
 */
public class Query {
    private List<String> titles;
    private List<Integer> pageids;
    private String list;
    private String cmtitle;
    private String cmContinue;
    private String limit;
    private String cmSort;
    private String cmDir;
    private boolean ncontinue;
    public static final String FORMAT = "json";

    public Query() {
        titles = new ArrayList<>();
        pageids = new ArrayList<>();
        ncontinue = false;
    }

    public static String getFORMAT() {
        return FORMAT;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getCmContinue() {
        return cmContinue;
    }

    public void setCmContinue(String cmContinue) {
        this.cmContinue = cmContinue;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<Integer> getPageids() {
        return pageids;
    }

    public void setPageids(List<Integer> pageids) {
        this.pageids = pageids;
    }

    public String getCmtitle() {
        return cmtitle;
    }

    public void setCmtitle(String cmtitle) {
        this.cmtitle = cmtitle;
    }

    public String getCmSort() {
        return cmSort;
    }

    public void setCmSort(String cmSort) {
        this.cmSort = cmSort;
    }

    public String getCmDir() {
        return cmDir;
    }

    public void setCmDir(String cmDir) {
        this.cmDir = cmDir;
    }

    public boolean isNcontinue() {
        return ncontinue;
    }

    public void setNcontinue(boolean ncontinue) {
        this.ncontinue = ncontinue;
    }
}
