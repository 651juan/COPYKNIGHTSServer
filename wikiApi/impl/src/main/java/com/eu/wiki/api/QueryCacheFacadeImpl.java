package com.eu.wiki.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Juan on 08/10/2016.
 */
public class QueryCacheFacadeImpl implements QueryCacheFacade {
    private HashMap<Integer, Article> cache;
    private HashMap<String, Integer> yearCache;
    private HashMap<String, Integer> authorCache;
    private HashMap<String, Integer> industryCache;
    private HashMap<String, Integer> fundamentalCache;
    private HashMap<String, Integer> evidenceCache;
    private HashMap<String, Integer> countryCache;
    private LocalDateTime  lastUpdated;
    private LocalDateTime expiryPeriod;
    private QueryFacade queryFacade;

    public QueryCacheFacadeImpl(LocalDateTime expiryPeriod, String url) {
        lastUpdated = LocalDateTime.MIN;
        this.expiryPeriod = expiryPeriod;
        this.queryFacade = new QueryFacadeImpl(url);
        this.cache = new HashMap<>();
        this.yearCache = new HashMap<>();
        this.authorCache = new HashMap<>();
        this.industryCache = new HashMap<>();
        this.fundamentalCache = new HashMap<>();
        this.evidenceCache = new HashMap<>();
        this.countryCache = new HashMap<>();
        initialiseCache();
    }

    @Override
    public ArticleList query(Query q) {
        if (checkExpiry())  {
            initialiseCache();
        }
        ArticleList list = new ArticleList();
        if (!q.getPageids().isEmpty()) {
            list.setArticles(q.getPageids().stream()
                    .map(x -> cache.get(x))
                    .collect(Collectors.toList())
            );
        } else if (q.isAll()) {
            list.setArticles(
                    cache.entrySet()
                            .stream()
                            .map(Map.Entry::getValue)
                            .collect(Collectors.toList())
            );
        } else if (!q.getTitles().isEmpty()) {
            list.setArticles(
                    cache.entrySet()
                            .stream()
                            .map(Map.Entry::getValue)
                            .filter(x -> q.getTitles().contains(x.getName()))
                            .collect(Collectors.toList())
            );
        }
        return list;
    }

    /**
     * Checks if the cache has expired by adding the current time with the expiry period.
     * @return true if expired, false otherwise.
     */
    private boolean checkExpiry() {
        LocalDateTime addition = lastUpdated.plusSeconds(expiryPeriod.getSecond());
        addition = addition.plusMinutes(expiryPeriod.getMinute());
        addition = addition.plusHours(expiryPeriod.getHour());
        addition = addition.plusDays(expiryPeriod.getDayOfYear() - addition.getDayOfYear());
        addition = addition.plusYears(expiryPeriod.getYear() - addition.getYear());
        return LocalDateTime.now().isAfter(addition);
    }

    /**
     * Gets all articles from the wiki and stores them in the different caches to reduce processing on subsequent requests
     */
    private void initialiseCache() {
        lastUpdated = LocalDateTime.now();
        Query query = generateInitialQuery("");
        ArticleList initialList = queryFacade.query(query);
        ArticleList output = queryFacade.query(generateFullArticleQuery(initialList));
        output.setCmContinue(initialList.getCmContinue());
        while(output.getCmContinue() != null && !output.getCmContinue().equals("")) {
            ArticleList categoryResult = queryFacade.query(generateInitialQuery(output.getCmContinue()));
            ArticleList FullResult = queryFacade.query(generateFullArticleQuery(categoryResult));
            output.getArticles().addAll(FullResult.getArticles());
            output.setCmContinue(categoryResult.getCmContinue());
        }
        cache.putAll(output.getArticles().stream()
                .collect(Collectors.toMap(Article::getPageid, Function.identity())));

        for(Article article : output.getArticles()) {
            String tmpYear = article.getYear();
            List<String> allMatches = new ArrayList<String>();
            Pattern p = Pattern.compile("[0-9]{4}");
            Matcher m = p.matcher(tmpYear);
            while (m.find()) {
                allMatches.add(m.group());
            }
            if (allMatches.size() == 2) {
                for (int i = Integer.valueOf(allMatches.get(0)); i <= Integer.valueOf(allMatches.get(1)); i++) {
                    incrementCountToMap(yearCache, String.valueOf(i));
                }
            } else if (allMatches.size() == 1) {
                incrementCountToMap(yearCache, allMatches.get(0));
            } else {
                incrementCountToMap(yearCache, tmpYear);
            }
        }

        for(Article article : output.getArticles()) {
            String[] tmpAuthor = article.getAuthors();
            for(String author : tmpAuthor) {
                incrementCountToMap(authorCache, author);
            }
        }

        for(Article article : output.getArticles()) {
            if (article.getDatasets() != null) {
                Industry[] tmpIndustry = article.getDatasets().getIndustry();
                for (Industry industry : tmpIndustry) {
                    incrementCountToMap(industryCache, industry.getValue());
                }
            } else {
                incrementCountToMap(industryCache, "");
            }
        }

        for (Article article : output.getArticles()) {
            for (FundamentalIssue fund : article.getFundamentalIssues()) {
                if (fund == null) {
                    incrementCountToMap(fundamentalCache, "");
                } else {
                    incrementCountToMap(fundamentalCache, fund.toString());
                }
            }
        }

        for (Article article : output.getArticles()) {
            for (EvidenceBasedPolicy evi : article.getEvidenceBasedPolicies()) {
                if (evi == null) {
                    incrementCountToMap(evidenceCache, "");
                } else {
                    incrementCountToMap(evidenceCache, evi.toString());
                }
            }
        }

        for (Article article : output.getArticles()) {
            if (article.getDatasets() != null) {
                String[] tmpCountry = article.getDatasets().getCountries();
                for (String country: tmpCountry) {
                    incrementCountToMap(countryCache, country);
                }
            } else {
                incrementCountToMap(countryCache, "");
            }
        }
     }

    private void incrementCountToMap(Map<String, Integer> map, String tmpValue) {
        if(map.containsKey(tmpValue)) {
            int tmp = map.get(tmpValue);
            tmp ++;
            map.put(tmpValue, tmp);
        }else{
            map.put(tmpValue,1);
        }
    }


    /**
     * Generates a query to be used to obtain all studies.
     * @param cmcontinue the continue string to be added to the query.
     * @return the Query with the continue string.
     */
    private Query generateInitialQuery(String cmcontinue) {
        Query query = new Query();
        query.setList("categorymembers");
        query.setCmtitle("Category:Studies");
        query.setLimit(Integer.toString(50));
        query.setNcontinue(true);
        query.setCmContinue(cmcontinue);
        return query;
    }

    /**
     * Generates a query with the page ids from the article list to get the full article from the short article obtained.
     * @param articleList the short articles obtained.
     * @return the query with the full @{@link Article}(s)
     */
    private Query generateFullArticleQuery(ArticleList articleList) {
        Query q = new Query();
        q.setPageids(articleList.getArticles().stream()
                .map(Article::getPageid)
                .collect(Collectors.toList()));
        return q;
    }

    @Override
    public Map<String, Integer> getYearCount() {
        if (checkExpiry())  {
            initialiseCache();
        }
        return yearCache;
    }

    @Override
    public Map<String, Integer> getAuthorCount() {
        if (checkExpiry())  {
            initialiseCache();
        }
        return authorCache;
    }

    @Override
    public Map<String, Integer> getIndustryCount() {
        if (checkExpiry())  {
            initialiseCache();
        }
        return industryCache;
    }

    @Override
    public Map<String, Integer> getFundamentalIssueCount() {
        if (checkExpiry())  {
            initialiseCache();
        }
        return fundamentalCache;
    }

    @Override
    public Map<String, Integer> getEvidenceBasedPolicyCount() {
        if (checkExpiry())  {
            initialiseCache();
        }
        return evidenceCache;
    }

    @Override
    public Map<String, Integer> getCountryCount() {
        if (checkExpiry())  {
            initialiseCache();
        }
        return countryCache;
    }

}
