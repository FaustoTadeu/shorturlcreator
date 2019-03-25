package ie.faustoalves.shorturlcreator.repository;

import ie.faustoalves.shorturlcreator.model.CreateUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateUrlRepository extends JpaRepository<CreateUrl, Long> {

    CreateUrl findFirstByOrderByIdKeyDesc();

    CreateUrl findByLongUrl(String longUrl);

    CreateUrl findByUrlKey(String urlKey);

}
