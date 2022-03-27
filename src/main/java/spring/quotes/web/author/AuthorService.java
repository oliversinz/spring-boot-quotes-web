package spring.quotes.web.author;

import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.quotes.web.exception.ResourceNotFoundException;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Page<Author> findPaginated(Pageable pageable) {

        List<Author> authors = authorRepository.findAll();

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Author> list;

        if (authors.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, authors.size());
            list = authors.subList(startItem, toIndex);
        }

        Page<Author> authorPage = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), authors.size());

        return authorPage;

    }

    public List<Author> getAuthorList() {
        return authorRepository.findAll();
    }

    public List<Author> getAuthorListFiltered(String search) {
        return authorRepository.findByAuthorNameContaining(search);
    }

    public Author getAuthorById(Long id) {
        Author author = checkAuthorExists(id);
        return author;
    }

    public Author getAuthorBySlug(String slug) {
        return authorRepository.findByAuthorSlug(slug);
    }

    private Author checkAuthorExists(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author", "id", id));
    }

}
