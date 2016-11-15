# Copy Knights API Server
The Copy Knights API Server connects to the media wiki APIs and parses the content of the articles. The Copy Knights API Server exposes it's own API to provide easier access to the content of the articles. The API allows for searching and counting by a certain criteria. This will allow any developer to connect to the API to help with better visualizations of the article data. The Server also cache's most information as to improve performance, this can be configured also by the API.

## The API
We have dedicated a whole page for the API below & we have also provided Javadoc in the '/docs' folder for more information regarding the process. THE API's are designed to follow mostly the REST API Ideology.

### Get Articles By Page ID's
This API uses the path '/article' with an attribute of 'pageids'.
Where the pageids variable is a string of id's separated by a ','. Eg. '/article?pageids=2049,1530' or '/article?pageids=2049' for a single page id. This has a limit of around 50-page ids as there is a URL max length limit.

### Get Articles By Titles
This API uses the path '/article' with an attribute of 'title'.
Where the title variable is a string of titles separated by a ','. Eg. '/article?title=Arcade(1900),Copy(2015)' or '/article?pageids=Arcade(1900)' for a single title. This has a limit of around 15 titles as there is a URL max length limit.

### Get Articles By Author
This API uses the path '/author/:author' with the path variable ':author'.
The ':author' variable is a string of the name of the author. Eg. '/article/Juan Buhagiar'

### Get Articles by Country
This API uses the path '/country/:country' with the path variable ':country'.
The ':country' variable is a string of the name of the country. Eg. '/article/Iceland'

### Get Articles by Year
This API uses the path '/year/:year' with the path variable ':year'.
The ':year' variable is a string of the year. Eg. '/year/1990'

### Get Articles by Fundamental Issue 
This API uses the path '/fundamental/:issue' with the path variable ':issue'.
The ':issue' variable is a string of the key of the FundamentalIssue ENUM which is described below. Eg. '/fundamental/ISSUE_1'

### Get Articles by Evidence Based Policy
This API uses the path '/evidence/:policy' with the path variable ':policy'.
The ':policy' variable is a string of the key of the EvidenceBasedPolicy ENUM which is described below. Eg. '/evidence/Policy_A'

### Get Articles by Keyword
This API uses the path '/wordcloud/:keyword' with the path variable ':keyword'.
The ':keyword' variable is the keyword from the article which you would like to search for of. Eg. '/keyword/innovation'

### Get Author Count
This API uses the path '/author'. It will return a list of authors followed by their respective count.

### Get Country Count
This API uses the path '/country'. It will return a list of countries followed by their respective count.

### Get Year Count
This API uses the path '/year'. It will return a list of years followed by their respective count.

### Get Fundamental Issue Count
This API uses the path '/fundamental'. It will return a list of FundamentalIssue ENUM Keys followed by their respective count.

### Get Evidence Based Policy Count 
This API uses the path '/evidence'. It will return a list of EvidenceBasedPolicy ENUM Keys followed by their respective count.

### Get Industry Count
This API uses the path '/industry'. It will return a list of Industry ENUM Keys followed by their respective count.

### Get Similar Articles
This API uses the path '/similar/:similar' with the path variable ':similar'.
The ':similar' variable is the pageid of the article which you would like to get similar articles of. Eg. '/similar/1305'

## ENUMS
    
### Industry
    INDUSTRY_1("Advertising",0),
    INDUSTRY_2("Architectural",1),
    INDUSTRY_3("Computer consultancy",2),
    INDUSTRY_4("Computer programming",3),
    INDUSTRY_5("Creative, arts and entertainment",4),
    INDUSTRY_6("Cultural education",5),
    INDUSTRY_7("Film and motion pictures",6),
    INDUSTRY_8("PR and communication",7),
    INDUSTRY_9("Photographic activities",8),
    INDUSTRY_10("Programming and broadcasting",9),
    INDUSTRY_11("Publishing of books, periodicals and other publishing",10),
    INDUSTRY_12("Software publishing (including video games)",11),
    INDUSTRY_13("Sound recording and music publishing",12),
    INDUSTRY_14("Specialised design",13),
    INDUSTRY_15("Television programmes",14),
    INDUSTRY_16("Translation and interpretation",15),
    UNKNOWN_INDUSTRY("UNKNOWN",16);


## Authors and Contributors
The Authors of this system are [Adrianapap](https://github.com/adrianapap) [651juan](https://github.com/651juan) & [xDanielle](https://github.com/xDanielle).

## Support or Contact
If you have any queries feel free to contact us.
