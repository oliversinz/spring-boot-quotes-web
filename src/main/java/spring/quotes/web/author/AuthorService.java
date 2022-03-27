package spring.quotes.web.author;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.quotes.web.exception.ResourceNotFoundException;
import spring.quotes.web.utils.JacksonUtils;

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
        return authorRepository.findById(id).get();
    }

    public Optional<Author> getAuthorByName(String name) {

        if (authorRepository.existsByAuthorName(name)) {
            return authorRepository.findByAuthorName(name);
        } else {
            try {
                return jsonToDatabase("https://api.quotable.io/search/authors?query=" + name);
            } catch (Exception e) {
                return Optional.empty();
            }
        }

    }

    public Optional<Author> getAuthorBySlug(String slug) {

        return authorRepository.findByAuthorSlug(slug);

    }

    private Optional<Author> jsonToDatabase(String urlString) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode treeNode = mapper.readTree(new URL(urlString));
        treeNode = treeNode.findValue("results");

        Author author = new Author();
        author.setAuthorName(treeNode.get("author").asText());
        author.setAuthorDescription(treeNode.get("author").asText());
        authorRepository.save(author);

        System.out.println("Records inserted.....");

        return Optional.of(author);

    }

}
