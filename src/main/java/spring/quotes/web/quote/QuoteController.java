package spring.quotes.web.quote;

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
import spring.quotes.web.author.AuthorService;
import spring.quotes.web.category.CategoryService;

@Controller
public class QuoteController {

    private final QuoteService quoteService;

    private final AuthorService authorService;

    private final CategoryService categoryService;

    public QuoteController(QuoteService quoteService, AuthorService authorService, CategoryService categoryService) {
        this.quoteService = quoteService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @GetMapping("/quotes")
    public String listQuotes(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Quote> quotePage = quoteService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("quotePage", quotePage);

        int totalPages = quotePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "quote-list-view";
    }

    @PostMapping("/quotes/search")
    public String getQuotesListFilteredQuoteContent(@RequestParam("quoteContent") String quoteContent, Model model) {
        model.addAttribute("quotePage", quoteService.getQuoteListFilteredQuoteContent(quoteContent));
        return "quote-list-view";
    }

    @GetMapping("/quotes/author/{authorSlug}")
    public String listQuotesByAuthor(@PathVariable(name = "authorSlug") String authorSlug, Model model) {
        return listQuotesByAuthor(authorSlug, 1, model);
    }

    @GetMapping("/quotes/author/{authorSlug}/{pageNo}")
    public String listQuotesByAuthor(@PathVariable(name = "authorSlug") String authorSlug, @PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 5;

        Page<Quote> page = quoteService.findPaginatedAuthor(authorSlug, pageNo, pageSize);
        List<Quote> quotePage = page.getContent();

        if (!quotePage.isEmpty()) {

            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("quotePage", quotePage);

            model.addAttribute("authorName", authorService.getAuthorBySlug(authorSlug).get().getAuthorName());

            return "quote-list-view-author";

        }

        return "redirect:/error-404";

    }

    @GetMapping("/quotes/category/{categorySlug}")
    public String listQuotesByCategory(@PathVariable(name = "categorySlug") String categorySlug, Model model) {
        return listQuotesByCategory(categorySlug, 1, model);
    }

    @GetMapping("/quotes/category/{categorySlug}/{pageNo}")
    public String listQuotesByCategory(@PathVariable(name = "categorySlug") String categorySlug, @PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 10;

        Page<Quote> page = quoteService.findPaginatedCategory(categorySlug, pageNo, pageSize);
        List<Quote> quotePage = page.getContent();

        if (!quotePage.isEmpty()) {

            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("quotePage", quotePage);

            model.addAttribute("categoryName", categoryService.getCategoryBySlug(categorySlug).getCategoryName());

            return "quote-list-view-category";

        }

        return "redirect:/error-404";

    }

    @GetMapping("/quotes/{id}")
    public String getQuoteById(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("quote", quoteService.getQuoteById(id));
        return "quote-details";
    }

    @GetMapping("/quotes/by/{slug}")
    public String getQuoteBySlug(@PathVariable(name = "slug") String slug, Model model) {
        model.addAttribute("quote", quoteService.getQuoteBySlug(slug));
        return "quote-details";
    }

}
