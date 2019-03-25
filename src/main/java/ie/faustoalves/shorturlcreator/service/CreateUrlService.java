package ie.faustoalves.shorturlcreator.service;

import ie.faustoalves.shorturlcreator.config.IdConverter;
import ie.faustoalves.shorturlcreator.model.CreateUrl;
import ie.faustoalves.shorturlcreator.repository.CreateUrlRepository;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;


@Service
public class CreateUrlService {

    @Autowired
    private CreateUrlRepository createUrlRepository;

    private CreateUrl createUrl;

    private String shortenedURL;

    private Integer acessed;

    private String objectJson;

    private CreateUrl existedCreateUrl;

    ResponseEntity<String> response;

    private static final String URL_GATHERING_STATISTICS_SERVICE = "http://172.21.0.3:8080/statistics";

    public JSONArray shortenURL(String localURL, String longUrl) {
        shortenedURL = "";
        String[] result = null;
        Long id;
        if(createUrlRepository.count() > 0) {
            id = createUrlRepository.findFirstByOrderByIdKeyDesc().getIdKey() + 1;
            existedCreateUrl = createUrlRepository.findByLongUrl(longUrl);
            if(existedCreateUrl != null) {
                shortenedURL = existedCreateUrl.getUrlKey();
                response = getStatistics(existedCreateUrl.getIdKey());
                if(response != null) {
                    result = response.getBody().split(",");
                }
            }
        } else {
            id = 1L;
        }
        if(shortenedURL.equals("")) {
            createURL(id, localURL, longUrl);
        }
        String resultObjectString = createObjectString(shortenedURL, result);
        return  convertStringToJSONArray(resultObjectString);
    }

    public String getLongURLFromID(String uniqueID) throws Exception {
        String longUrl = "";
        Long dictionaryKey = IdConverter.INSTANCE.getDictionaryKeyFromUniqueID(uniqueID);
        Optional<CreateUrl> created = createUrlRepository.findById(dictionaryKey);
        if(created.isPresent()) {
            longUrl = created.get().getLongUrl();
            acessed = created.get().getAcessed();
            created.get().setAcessed(acessed + 1);
            createUrlRepository.save(created.get());
        }
        return longUrl;
    }

    public CreateUrl getCreateUrlByUrlKey(String urlKey) {
        return createUrlRepository.findByUrlKey(urlKey);
    }

    public ResponseEntity<String> getStatistics(Long selectedId) {
        Integer count = 1;
        List<CreateUrl> list = createUrlRepository.findAll();
        objectJson = "[ ";
        for (CreateUrl crtUrl: list) {
            objectJson += "{ ";
            objectJson +=     "\"idKey\": " + crtUrl.getIdKey() + ", ";
            objectJson +=     "\"acessed\": " + crtUrl.getAcessed() + ", ";
            objectJson +=     "\"idSelected\": " + selectedId;
            objectJson += "}";

            if(count < list.size()) {
                objectJson += ",";
            }
        }
        objectJson += "]";

        JSONArray json;
        json = convertStringToJSONArray(objectJson);

        return getDataFromService(json);
    }

    public JSONArray convertStringToJSONArray(String objectJson) {
        JSONParser parser = new JSONParser();
        JSONArray json = new JSONArray();
        try {
            json = (JSONArray) parser.parse(objectJson);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return json;
    }

    public String createObjectString(String shortenedURL, String[] result) {
        String resultObject;
        if(result != null) {
            resultObject = "[ ";
            resultObject += "{ ";
            resultObject += "\"URL\": " + "\"" + shortenedURL + "\", ";
            resultObject += "\"AllAcess\": " +  result[0].replace("[", "") + ", ";
            resultObject += "\"SumRegistries\": " + result[1] + ", ";
            resultObject += "\"URLAllAcess\": " + result[2] + ", ";
            resultObject += "\"PercentAcess\": " + "\"" + result[3] + "%\" , ";
            resultObject += "\"PercentMemoryUsed\": " + "\"" + result[4].replace("]", "") + "%\" ";
            resultObject += " }";
            resultObject += "]";
        } else {
            resultObject = "[ ";
            resultObject += "{ ";
            resultObject += "\"url\": " + "\"" + shortenedURL + "\", ";
            resultObject += "\"error\": \"Not exists Statistics for this URL or Gathering Statistics service is unavailable\"";
            resultObject += " }";
            resultObject += " ]";
        }
        return resultObject;
    }

    private ResponseEntity<String> getDataFromService (JSONArray jsonData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<JSONArray> entity = new HttpEntity<>(jsonData, headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(URL_GATHERING_STATISTICS_SERVICE, entity, String.class);
        }catch (Exception e) {
            response = null;

        }
        return response;

    }

    private String formatLocalURLFromShortener(String localURL) {
        String[] addressComponents = localURL.split("/");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < addressComponents.length - 1; ++i) {
            sb.append(addressComponents[i]);
            if(i == 0) {
                sb.append("//");
            }
        }
        sb.append('/');
        return sb.toString();
    }

    private void createURL(Long id, String localURL, String longUrl) {
        String uniqueID = IdConverter.INSTANCE.createUniqueID(id);
        String baseString = formatLocalURLFromShortener(localURL);
        shortenedURL = baseString + uniqueID;
        createUrl = new CreateUrl(longUrl, shortenedURL, 0);
        createUrlRepository.save(createUrl);
    }
}