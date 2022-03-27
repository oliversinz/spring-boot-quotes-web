package spring.quotes.web.category;

import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.quotes.web.exception.ResourceNotFoundException;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<Category> findPaginated(Pageable pageable) {

        List<Category> categories = categoryRepository.findAll();

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Category> list;

        if (categories.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, categories.size());
            list = categories.subList(startItem, toIndex);
        }

        Page<Category> categoryPage = new PageImpl<>(list, PageRequest.of(currentPage, pageSize), categories.size());

        return categoryPage;

    }

    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoryListFiltered(String search) {
        return categoryRepository.findByCategoryNameContaining(search);
    }

    public Category getCategoryById(Long id) {
        Category category = checkCategoryExists(id);
        return category;
    }

    public Category getCategoryBySlug(String slug) {
        return categoryRepository.findByCategorySlug(slug);
    }

    private Category checkCategoryExists(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

}
