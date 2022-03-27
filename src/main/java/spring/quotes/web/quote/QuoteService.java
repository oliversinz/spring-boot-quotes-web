package spring.quotes.web.quote;

import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.quotes.web.exception.ResourceNotFoundException;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public Page<Quote> findPaginated(Pageable pageable) {

        List<Quote> quotes = quoteRepository.findAll();

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Quote> list;

        if (quotes.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, quotes.size());
            list = quotes.subList(startItem, toIndex);
        }

        Page<Quote> quotePage = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), quotes.size());

        return quotePage;

    }

    public Page<Quote> findPaginatedAuthor(String authorSlug, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return quoteRepository.findByAuthor_AuthorSlug(authorSlug, pageable);
    }

    public Page<Quote> findPaginatedCategory(String categorySlug, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return quoteRepository.findByCategories_CategorySlug(categorySlug, pageable);
    }

    public List<Quote> getQuoteList() {
        return quoteRepository.findAll();
    }

    public List<Quote> getQuoteListFilteredQuoteContent(String quoteContent) {
        return quoteRepository.findByQuoteContentContaining(quoteContent);
    }

    public Quote getQuoteById(Long id) {
        Quote quote = quoteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quote", "id", id));
        return quote;
    }

    public Quote getQuoteBySlug(String slug) {
        return quoteRepository.findByQuoteContentSlug(slug);
    }

}
