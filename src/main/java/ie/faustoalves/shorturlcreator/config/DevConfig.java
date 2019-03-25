package ie.com.faustoalves.shorturlcreation.config;

import ie.com.faustoalves.shorturlcreation.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile("dev")
public class DevConfig {

    @Autowired
    private DBService dbService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String strategy;


    public boolean  instantiateDatabase() throws ParseException {
        if(!strategy.equals("create")) {
            return true;
        }
        dbService.instatiateTestDatabase();
        return true;
    }

}
