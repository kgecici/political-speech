How "/evaluation" API Works:
- The endpoint gets query parameter and expect them start with url.
It can be url1, url2 or urlSourceA, urlSourceB etc. 
- If no query param start with "url", returns http bad request
- Giving same url value, gives bad request to prevent wrong usage. for ex here both url1 and url2 has same value
/evaluation?url1=http://example.com/data1.csv&url2=http://example.com/data1.csv
- If given url s has same speaker, topic and date it compares number of words
  - If they are same, it picks one of them (if you have 2 url with same content, it works same as giving 1 url)
  - If word counts are not save gives error, because it is not clear which one to pick
- If given url does not have valid csv header("Speaker;Topic;Date;Words"), returns error. Header might have spaces but not more than 2 times size of total default header length. This helps us to prevent downloading whole content of the file
  - To prevent malicious url requests, it reads required number of bytes from http(2 * length of "Speaker;Topic;Date;Words") and if header not found returns error.
- All url s are loaded and combined in one list to be able to evaluate all of them

Response:
- mostSpeeches: Which politician gave the most speeches in 2013? Here all 2013 speeches are summed per politician"
- mostSecurity: "Which politician gave the most speeches on ""homeland security""? Here all speeches with ""homeland security"" in the topic are summed per politician
- leastWordy: "Which politician spoke the fewest words overall? Here all speeches are summed per politician"


Further Improvements:
- UrlContentReader does not cache any url requests. This part would be bottleneck since network requests are time consuming. Caching can be added here
- UrlContentReader does not have certain timeout. It can be added
- Api does not have any authentication. It can be added