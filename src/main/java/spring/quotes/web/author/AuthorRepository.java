package spring.quotes.web.author;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Boolean existsByAuthorName(String authorName);

    Boolean existsByAuthorSlug(String authorSlug);

    Optional<Author> findByAuthorName(String authorName);

    Optional<Author> findByAuthorSlug(String authorSlug);

    List<Author> findByAuthorNameContaining(String search);

}
