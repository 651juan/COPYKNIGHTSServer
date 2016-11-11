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

## ENUMS

### Fundamental Issue

    ISSUE_1("1. Relationship between protection (subject matter/term/scope) and supply/economic development/growth/welfare",0),
    ISSUE_2("2. Relationship between creative process and protection - what motivates creators (e.g. attribution; control; remuneration; time allocation)?",1),
    ISSUE_3("3. Harmony of interest assumption between authors and publishers (creators and producers/investors)",2),
    ISSUE_4("4. Effects of protection on industry structure (e.g. oligopolies; competition; economics of superstars; business models; technology adoption)",3),
    ISSUE_5("5. Understanding consumption/use (e.g. determinants of unlawful behaviour; user-generated content; social media)",4),
    UNKNOWN_ISSUE("UNKNOWN",5);

### EvidenceBasedPolicy

    POLICY_A("A. Nature and Scope of exclusive rights (hyperlinking/browsing; reproduction right)",0),
    POLICY_B("B. Exceptions (distinguish innovation and public policy purposes; open-ended/closed list; commercial/non-commercial distinction)",1),
    POLICY_C("C. Mass digitisation/orphan works (non-use; extended collective licensing)",2),
    POLICY_D("D. Licensing and Business models (collecting societies; meta data; exchanges/hubs; windowing; crossborder availability)",3),
    POLICY_E("E. Fair remuneration (levies; copyright contracts)",4),
    POLICY_F("F. Enforcement (quantifying infringement; criminal sanctions; intermediary liability; graduated response; litigation and court data; commercial/non-commercial distinction; education and awareness)",5),
    UNKNOWN_POLICY("UNKNOWN",5);


## Authors and Contributors
The Authors of this system are @Adrianapap @651juan & @xDanielle.

## Support or Contact
If you have any queries feel free to contact us.