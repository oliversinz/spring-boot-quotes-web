package spring.quotes.web.author;

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
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public String listAuthors(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Page<Author> authorPage = authorService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("authorPage", authorPage);

        int totalPages = authorPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "author-list-view";
    }

    @PostMapping("/authors/search")
    public String getAuthorListFiltered(@RequestParam("authorName") String authorName, Model model) {
        List<Author> authorPage = authorService.getAuthorListFiltered(authorName);
        model.addAttribute("authorPage", authorPage);
        return "author-list-view";
    }

    @GetMapping("/authors/id/{id}")
    public String getAuthorById(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("author", authorService.getAuthorById(id));
        return "author-details";
    }

    @GetMapping("/authors/by/{slug}")
    public String getAuthorBySlug(@PathVariable(name = "slug") String slug, Model model) {
        model.addAttribute("author", authorService.getAuthorBySlug(slug));
        return "author-details";
    }

}
