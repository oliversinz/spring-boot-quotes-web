package spring.quotes.web.category;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String listCategories(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Category> categoryPage = categoryService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("categoryPage", categoryPage);

        int totalPages = categoryPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "category-list-view";
    }

    @PostMapping("/categories/search")
    public String getAuthorListFiltered(@RequestParam("categoryName") String categoryName, Model model) {
        model.addAttribute("categoryPage", categoryService.getCategoryListFiltered(categoryName));
        return "category-list-view";
    }

    @GetMapping("/categories/{id}")
    public String getCategoryById(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "category-details";
    }

    @GetMapping("/categories/by/{slug}")
    public String getCategoryBySlug(@PathVariable(name = "slug") String slug, Model model) {
        model.addAttribute("category", categoryService.getCategoryBySlug(slug));
        return "category-details";
    }

}
