package spring.quotes.web;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spring.quotes.web.author.Author;
import spring.quotes.web.author.AuthorRepository;
import spring.quotes.web.quote.Quote;
import spring.quotes.web.quote.QuoteRepository;
import spring.quotes.web.utils.JacksonUtils;

@Controller
public class MainController {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @GetMapping("/")
    public String getIndex(Model model) {

        try {
            URL jsonUrl = new URL("https://api.quotable.io/random");
            JsonNode node = JacksonUtils.parseJson(jsonUrl);

            Quote quote = new Quote(node.get("content").asText());
            quoteRepository.save(quote);

            Author author = new Author(node.get("author").asText());
            authorRepository.save(author);

            author.addQuote(quote);

            quoteRepository.save(quote);
            
            model.addAttribute("quote", quote);

        } catch (Exception e) {

        }
        return "index";

    }

}
