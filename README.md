# Copy Knights API Server
The Copy Knights API Server connects to the media wiki APIs and parses the content of the articles. The Copy Knights API Server exposes it's own API to provide easier access to the content of the articles. The API allows for searching and counting by a certain criteria. This will allow any developer to connect to the API to help with better visualizations of the article data. The Server also cache's most information as to improve performance, this can be configured also by the API.

## The API
We have dedicated a whole page for the API below & we have also provided Javadoc in the '/docs' folder for more information regarding the process. THE API's are designed to follow mostly the REST API Ideology.

### Get Articles By A Page ID
This API uses the path '/article' with an attribute of 'pageids'.
Where pageids is a string of id's separated by a ','. Eg. '/article?pageids=2049,1530' or '/article?pageids=2049' for a single page id. This has a limit of around 50-page ids as there is a URL max length limit.

## Authors and Contributors
The Authors of this system are @Adrianapap @651juan & @xDanielle.

## Support or Contact
If you have any queries feel free to contact us.
