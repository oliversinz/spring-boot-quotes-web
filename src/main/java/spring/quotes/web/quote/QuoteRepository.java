package spring.quotes.web.quote;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    List<Quote> findByQuoteContentContaining(String search);

    // List<Quote> findByAuthor_AuthorName(String author);
    // List<Quote> findByAuthor_AuthorName(String author, Pageable pageable);
    // List<Quote> findByCategories_CategoryName(String category);
    // List<Quote> findByCategories_CategoryName(String category, Pageable pageable);

    Page<Quote> findByAuthor_AuthorSlug(String authorSlug, Pageable pageable);

    Page<Quote> findByCategories_CategorySlug(String categorySlug, Pageable pageable);

    Quote findByQuoteContentSlug(String slug);

}
