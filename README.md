How "/evaluation" API Works:
- The endpoint gets query parameter and expect them start with url.
It can be url1, url2 or urlSourceA, urlSourceB etc. 
- If no query param start with url given, returns http bad request
- Giving same url value, gives bad request to prevent wrong usage. for ex here both url1 and url2 has same value
/evaluation?url1=http://example.com/data1.csv&url2=http://example.com/data1.csv
- If given url s has same speaker, topic and date it compares number of words
  - if they are same, it picks one of them (for example if you have 2 url with same content, it works same as giving 1 url)
  - If word counts are not save gives error
- If given url does not have valid header, returns error. 
  - To prevent malicious url requests it reads required number of bytes from http and if header not found returns error. This prevents also executable download
- I assumed this api will public, so there is no jwt auth or any other
- 