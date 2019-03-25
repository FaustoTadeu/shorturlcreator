package ie.faustoalves.shorturlcreator.service;

import ie.com.faustoalves.shorturlcreation.model.CreateUrl;
import ie.com.faustoalves.shorturlcreation.repository.CreateUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Arrays;

@Service
public class DBService {

    @Autowired
    private CreateUrlRepository createUrlRepository;

    private String contextPath = "http://localhost:8100";

    @Bean
    public void instatiateTestDatabase() throws ParseException {
        CreateUrl createUrlTest1 = new CreateUrl("www.stackoverflow.com", contextPath +"/b", 0 );
        CreateUrl createUrlTest2 = new CreateUrl("www.github.com", contextPath + "/c", 0);

        createUrlRepository.saveAll(Arrays.asList(createUrlTest1, createUrlTest2));
    }
}