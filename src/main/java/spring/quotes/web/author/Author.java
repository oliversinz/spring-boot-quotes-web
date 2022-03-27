package spring.quotes.web.author;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import spring.quotes.web.quote.Quote;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "author", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"author_name"})})
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", nullable = false)
    private Long id;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "author_slug", nullable = false)
    private String authorSlug;

    @Column(name = "author_description", nullable = true)
    private String authorDescription;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quote> quotes = new ArrayList<>();

    public Author() {
    }

    public Author(String authorName) {
        this.authorName = authorName;
        this.authorSlug = toPrettyURL(authorName);
    }

    public Author(String authorName, String authorDescription) {
        this.authorName = authorName;
        this.authorDescription = authorDescription;
        this.authorSlug = toPrettyURL(authorName);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorSlug() {
        return authorSlug;
    }

    public void setAuthorSlug(String authorSlug) {
        this.authorSlug = authorSlug;
    }

    public String getAuthorDescription() {
        return authorDescription;
    }

    public void setAuthorDescription(String authorDescription) {
        this.authorDescription = authorDescription;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }

    public void addQuote(Quote quote) {
        quotes.add(quote);
        quote.setAuthor(this);
    }

    public static String toPrettyURL(String string) {
        return Normalizer.normalize(string.toLowerCase(), Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\p{Alnum}]+", "-");
    }

    @Override
    public String toString() {
        return authorName;
    }

}
