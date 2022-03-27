package spring.quotes.web.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import spring.quotes.web.category.Category;
import spring.quotes.web.author.Author;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.apache.commons.lang3.StringUtils;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "quote", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"quote_content"})})
public class Quote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_id", nullable = false)
    private Long id;

    @Column(name = "quote_content", nullable = false)
    private String quoteContent;

    @Column(name = "quote_content_slug", nullable = false)
    private String quoteContentSlug;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "quote_category", joinColumns = @JoinColumn(name = "quote_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public Quote() {
    }

    public Quote(String quoteContent) {
        this.quoteContent = quoteContent;
        this.quoteContentSlug = toPrettyURL(quoteContent);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuoteContent() {
        return quoteContent;
    }

    public void setQuoteContent(String quoteContent) {
        this.quoteContent = quoteContent;
    }

    public String getQuoteContentSlug() {
        return quoteContentSlug;
    }

    public void setQuoteContentSlug(String quoteContentSlug) {
        this.quoteContentSlug = quoteContentSlug;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
        category.getQuotes().add(this);
    }

    public static String toPrettyURL(String string) {
        String normalized = Normalizer.normalize(string.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\p{Alnum}]+", "-");
        int index = StringUtils.ordinalIndexOf(normalized, "-", 8);
        return index == -1 ? normalized : normalized.substring(0, index);
    }

}
