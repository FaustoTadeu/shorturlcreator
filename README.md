<h1>Short Url Creator Project</h1>
<br />
<h3>Donwload and install </h3>
<p>1 -  Clone this Repository <p>
<p>2 -  Run command: mvn install </p>
<p>3 -  Run command: mvn update<p>
<br />
<h3>Run </h3>
<br />
<p>After doing the steps above run this command: mvn springboot:run<p>
<br />
<h3>Use the Short Url Creator</h3>
<h4>1 - Creating new short Url </h4>
<p>Using Postman access the POST URL "http://YOUR_LOCAL_URL:8100/shortener" passing the following json string in body request: </p>
<p>  {  "url": "longUrl" }  ---- Example: {  "url": "www.twitter.com" } <p>
<br />
<p> If the long url is still not created you will receive the json array as: </p>
<p>[  { "error": "Not exists Statistics for this URL or Gathering Statistics service is unavailable",  "url": "http://YOUR_LOCAL_URL:8100/e"  } ] <p>
<br />
<p> If the long url already exists you will receive the json array as: </p>
 [  { "URLAllAcess": 0, "SumRegistries": 4, "AllAcess": 0, "PercentMemoryUsed": "25.0%",  "PercentAcess": "0.0%", "URL": "http://YOUR_LOCAL_URL:8100/e"  }  ] </p>
<br />
<h4>2 -Using the created short URL </h4>
<p>Open any browse and set "http://YOUR_LOCAL_URL:8100/'shorturl'" ---    Example: http://YOUR_LOCAL_URL:8100/d and you will be redirect to longUrl created before </p>
<br />
<h4>Acessing Statistics </h4>
<p> Open any browse and set "http://YOUR_LOCAL_URL:8100/statistics/'shorturl'" --- Example: "http://YOUR_LOCAL_URL:8100/d" and you will see the statistics about this short URL </p>
<br />
<h4>This application use the Statistics Gathering Web Service to show statistics about the URLs created.</h4>
<p>Please, access the link below and follow the steps there to use it. </p>
<p>Link to statistics: https://github.com/FaustoTadeu/gatheringstatitics </p>
<br />
<p align="right">Developed by Fausto Alves - fausto.tna@hotmail.com </p> 
