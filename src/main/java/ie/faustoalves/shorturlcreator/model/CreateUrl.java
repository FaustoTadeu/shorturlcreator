package ie.faustoalves.shorturlcreator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "urlCreated")
public class CreateUrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idKey;

    private String longUrl;

    private String urlKey;

    public Integer acessed;


    public CreateUrl() {
    }

    public CreateUrl(String longUrl, String urlKey, Integer acessed) {
        this.longUrl = longUrl;
        this.urlKey = urlKey;
        this.acessed =  acessed;
    }

    public Long getIdKey() {
        return idKey;
    }

    public void setIdKey(Long idKey) {
        this.idKey = idKey;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Integer getAcessed() {
        return acessed;
    }

    public void setAcessed(Integer acessed) {
        this.acessed = acessed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateUrl createUrl = (CreateUrl) o;
        return Objects.equals(idKey, createUrl.idKey) &&
                Objects.equals(longUrl, createUrl.longUrl) &&
                Objects.equals(urlKey, createUrl.urlKey) &&
                Objects.equals(acessed, createUrl.acessed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idKey, longUrl, urlKey, acessed);
    }
}