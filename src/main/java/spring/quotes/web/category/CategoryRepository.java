package spring.quotes.web.category;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Boolean existsByCategoryName(String categoryName);

    Boolean existsByCategorySlug(String categorySlug);

    Category findByCategoryName(String categoryName);

    Category findByCategorySlug(String categorySlug);

    List<Category> findByCategoryNameContaining(String search);

}
