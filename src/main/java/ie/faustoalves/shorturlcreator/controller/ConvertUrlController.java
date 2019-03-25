package ie.faustoalves.shorturlcreator.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ie.faustoalves.shorturlcreator.model.CreateUrl;
import ie.faustoalves.shorturlcreator.service.CreateUrlService;
import ie.faustoalves.shorturlcreator.validator.URLValidator;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/")
public class ConvertUrlController {
    private final static Logger LOGGER = Logger.getLogger(ConvertUrlController.class.getName());

    @Autowired
    private CreateUrlService createUrlService;

    @RequestMapping(value = "/shortener", method= RequestMethod.POST, consumes = {"application/json"})
    public JSONArray shortenUrl(@RequestBody @Valid final ShortenRequest shortenRequest, HttpServletRequest request) throws Exception {
        String longUrl = shortenRequest.getUrl();
        if (URLValidator.INSTANCE.validateURL(longUrl)) {
            String localURL = request.getRequestURL().toString();
            JSONArray shortenedUrl = createUrlService.shortenURL(localURL, shortenRequest.getUrl());
            return shortenedUrl;
        }
        throw new Exception("Please enter a valid URL");
    }

    @RequestMapping(value = "/{id}", method=RequestMethod.GET)
    public RedirectView redirectUrl(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException, Exception {
        LOGGER.info("Received shortened url to redirect: " + id);
        String redirectUrlString = createUrlService.getLongURLFromID(id);
        RedirectView redirectView = new RedirectView();
        if(!redirectUrlString.equals("")) {
            redirectView.setUrl("http://" + redirectUrlString);
        } else {
            redirectView.setUrl("");
            redirectView.setStatusCode(HttpStatus.NOT_FOUND);
        }
        return redirectView;
    }

    @RequestMapping(value = "/statistics/{shortUrl}", method=RequestMethod.GET)
    public JSONArray statistics(@PathVariable String shortUrl, HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException, Exception {
        String serverName = request.getServerName();
        String port = String.valueOf(request.getServerPort());
        String url = "http://"+serverName+":"+port + "/"+shortUrl;
        CreateUrl existedUrl = createUrlService.getCreateUrlByUrlKey(url);
        String jsonString;
        String[] result = null;
        JSONArray json;
        if(existedUrl != null) {
            ResponseEntity<String> responseStatistics = createUrlService.getStatistics(existedUrl.getIdKey());
            if(responseStatistics != null) {
                result = responseStatistics.getBody().split(",");
                jsonString = createUrlService.createObjectString(url, result);
            } else {
                jsonString = "[ {\"error\": \"Can't connect to Gathering Statistics Service !\" }]";
            }
        } else {
            jsonString = "[ {\"error\": \"Short url not found !\" }]";
        }
        json = createUrlService.convertStringToJSONArray(jsonString);
        return json;
    }
}

class ShortenRequest{
    private String url;

    @JsonCreator
    public ShortenRequest() {

    }

    @JsonCreator
    public ShortenRequest(@JsonProperty("url") String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}