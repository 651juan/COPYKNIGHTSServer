package com.eu.wiki.api;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Juan on 08/10/2016.
 */
public class QueryCacheFacadeImpl implements QueryCacheFacade {
    private final String[] STOPWORDS = {"a", "about", "above", "across", "after", "again", "against", "all", "almost", "alone", "along", "already", "also", "although", "always", "among", "an", "and", "another", "any", "anybody", "anyone", "anything", "anywhere", "are", "area", "areas", "around", "as", "ask", "asked", "asking", "asks", "at", "away", "b", "back", "backed", "backing", "backs", "be", "became", "because", "become", "becomes", "been", "before", "began", "behind", "being", "beings", "best", "better", "between", "big", "both", "but", "by", "c", "came", "can", "cannot", "case", "cases", "certain", "certainly", "clear", "clearly", "come", "could", "d", "did", "differ", "different", "differently", "do", "does", "done", "down", "down", "downed", "downing", "downs", "during", "e", "each", "early", "either", "end", "ended", "ending", "ends", "enough", "even", "evenly", "ever", "every", "everybody", "everyone", "everything", "everywhere", "f", "face", "faces", "fact", "facts", "far", "felt", "few", "find", "finds", "first", "for", "four", "from", "full", "fully", "further", "furthered", "furthering", "furthers", "g", "gave", "general", "generally", "get", "gets", "give", "given", "gives", "go", "going", "good", "goods", "got", "great", "greater", "greatest", "group", "grouped", "grouping", "groups", "h", "had", "has", "have", "having", "he", "her", "here", "herself", "high", "high", "high", "higher", "highest", "him", "himself", "his", "how", "however", "i", "if", "important", "in", "interest", "interested", "interesting", "interests", "into", "is", "it", "its", "itself", "j", "just", "k", "keep", "keeps", "kind", "knew", "know", "known", "knows", "l", "large", "largely", "last", "later", "latest", "least", "less", "let", "lets", "like", "likely", "long", "longer", "longest", "m", "made", "make", "making", "man", "many", "may", "me", "member", "members", "men", "might", "more", "most", "mostly", "mr", "mrs", "much", "must", "my", "myself", "n", "necessary", "need", "needed", "needing", "needs", "never", "new", "new", "newer", "newest", "next", "no", "nobody", "non", "noone", "not", "nothing", "now", "nowhere", "number", "numbers", "o", "of", "off", "often", "old", "older", "oldest", "on", "once", "one", "only", "open", "opened", "opening", "opens", "or", "order", "ordered", "ordering", "orders", "other", "others", "our", "out", "over", "p", "part", "parted", "parting", "parts", "per", "perhaps", "place", "places", "point", "pointed", "pointing", "points", "possible", "present", "presented", "presenting", "presents", "problem", "problems", "put", "puts", "q", "quite", "r", "rather", "really", "right", "right", "room", "rooms", "s", "said", "same", "saw", "say", "says", "second", "seconds", "see", "seem", "seemed", "seeming", "seems", "sees", "several", "shall", "she", "should", "show", "showed", "showing", "shows", "side", "sides", "since", "small", "smaller", "smallest", "so", "some", "somebody", "someone", "something", "somewhere", "state", "states", "still", "still", "such", "sure", "t", "take", "taken", "than", "that", "the", "their", "them", "then", "there", "therefore", "these", "they", "thing", "things", "think", "thinks", "this", "those", "though", "thought", "thoughts", "three", "through", "thus", "to", "today", "together", "too", "took", "toward", "turn", "turned", "turning", "turns", "two", "u", "under", "until", "up", "upon", "us", "use", "used", "uses", "v", "very", "w", "want", "wanted", "wanting", "wants", "was", "way", "ways", "we", "well", "wells", "went", "were", "what", "when", "where", "whether", "which", "while", "who", "whole", "whose", "why", "will", "with", "within", "without", "work", "worked", "working", "works", "would", "x", "y", "year", "years", "yet", "you", "young", "younger", "youngest", "your", "yours", "z"};
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


        //Generate WordClouds
        //Get IDF
        Map<String, Double> idFreq = new HashMap<>();
        for(Article article : output.getArticles()) {
            String aAbstract = article.getAbstract().toLowerCase();
            aAbstract = aAbstract.replaceAll("\\d",""); //Replace all digits with empty string
            aAbstract = aAbstract.replaceAll("[^\\p{L}\\p{Z}]", ""); //Unicode replace all non letters and non digits with empty string
            String[] aAbstractWords = aAbstract.split(" ");
            String[] unique = new HashSet<String>(Arrays.asList(aAbstractWords)).toArray(new String[0]);

            for(String word : unique) {
                if(!word.equals("") && !this.isIn(word, this.STOPWORDS) ) {
                    if (idFreq.containsKey(word)) {
                        Double tmp = idFreq.get(word);
                        idFreq.put(word, tmp + 1);
                    } else {
                        idFreq.put(word, 1.0);
                    }
                }
            }
        }

        for(Map.Entry<String,Double> entry : idFreq.entrySet()) {
            Double docF = Math.log10(output.getArticles().size()/(double)idFreq.get(entry.getKey()));
            idFreq.put(entry.getKey(),docF);
        }

        //Get TF and work out tf.idf
        for(Article article : output.getArticles()) {
            Map<String, Double> tFreq = new HashMap<>();
            Map<String, Double> tfidf = new HashMap<>();

            String aAbstract = article.getAbstract().toLowerCase();
            aAbstract = aAbstract.replaceAll("\\d",""); //Replace all digits with empty string
            aAbstract = aAbstract.replaceAll("[^\\p{L}\\p{Z}]", ""); //Unicode replace all non letters and non digits with empty string
            String[] aAbstractWords = aAbstract.split(" ");

            double max = 0;

            for(String word : aAbstractWords) {
                if(!word.equals("") && !this.isIn(word, this.STOPWORDS) ) {
                    if (tFreq.containsKey(word)) {
                        Double tmpVal = tFreq.get(word);
                        if (tmpVal > max) {
                            max = tmpVal;
                        }
                        tFreq.put(word, tmpVal + 1);
                    } else {
                        tFreq.put(word, 1.0);
                    }
                }
            }

            //Normalise tf
            for(Map.Entry<String,Double> entry : tFreq.entrySet()) {
                tFreq.put(entry.getKey(), entry.getValue()/max);
            }

            double maxTFIDF = 0;
            for(Map.Entry<String,Double> entry : tFreq.entrySet()) {
                String tmpKey = entry.getKey();
                double tf = tFreq.get(tmpKey);
                double idf = idFreq.get(tmpKey);
                double tmpTFIDF = tf * idf;
                if(tmpTFIDF > maxTFIDF) {
                    maxTFIDF = tmpTFIDF;
                }
                tfidf.put(tmpKey, tmpTFIDF);
            }

            //Normalise TFIDF
            for(Map.Entry<String,Double> entry : tfidf.entrySet()) {
                tfidf.put(entry.getKey(),entry.getValue()/maxTFIDF);
            }

            article.setWordCloud(tfidf);
        }

    }

    private boolean isIn(String toCheck, String[] toCheckIn) {
        for(int i = 0; i < toCheckIn.length; i++) {
            if(toCheckIn[i].equalsIgnoreCase(toCheck)){
                return true;
            }
        }
        return false;
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
