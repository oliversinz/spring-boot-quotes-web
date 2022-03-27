package spring.quotes.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import spring.quotes.web.author.Author;
import spring.quotes.web.author.AuthorRepository;
import spring.quotes.web.category.Category;
import spring.quotes.web.category.CategoryRepository;
import spring.quotes.web.quote.Quote;
import spring.quotes.web.quote.QuoteRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {

        if (quoteRepository.count() == 0) {

            Quote quote = new Quote("The only real valuable thing is intuition.");
            quoteRepository.save(quote);

            Author author = new Author("Albert Einstein", "Albert Einstein was a German-born theoretical physicist.");
            authorRepository.save(author);

            author.addQuote(quote);

            quoteRepository.save(quote);

            Category category = new Category("Wisdom", "All around wisdom");
            categoryRepository.save(category);

            quote.addCategory(category);

            quoteRepository.save(quote);

        }

    }

}
