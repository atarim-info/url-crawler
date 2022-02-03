## url-crawler
#**Create a web server that crawls url for inside links**

- Create a web server that exposes the following endpoint: /crawl?url=x&number=y
- When this endpoint is accessed, the URL (x value) is crawled and a total of “number” (y value) of unique URLs are retrieved.
- Take into account that the following are different and unique urls:
	- https://www.linkedin.com
	- https://www.linkedin.com/feed/
	- https://www.linkedin.com/jobs/

- The crawling operation continues to operate on the retrieved URLs until the number of results (y value) is achieved or there are no more links that can be used for crawling.

- Create a new DB table urlToTitleMap.
- For each URL in the result, access urlToTitleMap table:
	- If the URL exists in the table, return the title matching that URL as part of the result.
	- If the URL does not exist in the table, save it to the table and return it where the title should derive from the url, example, for url = https://www.facebook.com/, the title should be facebook.

- The result should be rendered as a JSON array of objects, where each object should be in the following format:

```json
{url: 'http://www.example.com/example.html', title: 'the title'}
```

Crawling scenario example:
- Input is /crawl?url=’https://www.linkedin.com/feed/’&number=10
- The page contains 3 other unique urls (www.url1.com, www.url2.com, www.url3.com)
- None of the crawled URLs contain any new unique urls themselves
- The expected result:

```json
[
	{url: ‘www.url1.com’, title: ‘url1’},
	{url: ‘www.url2.com’, title: ‘url2’},
	{url: ‘www.url3.com’, title: ‘url3’}
]
```
- In this example even though number=10, since there are only 3 URLs available,
the result will contain only these 3 URLs


See: [How to make a simple web crawler in Java] (http://www.netinstructions.com/how-to-make-a-simple-web-crawler-in-java/)