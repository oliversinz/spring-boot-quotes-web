package spring.quotes.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import okhttp3.HttpUrl;
import org.fastily.jwiki.core.Wiki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import spring.quotes.web.author.Author;
import spring.quotes.web.category.Category;
import spring.quotes.web.quote.Quote;
import spring.quotes.web.author.AuthorRepository;
import spring.quotes.web.category.CategoryRepository;
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

        String csvFilePath = "C:\\Users\\chakran\\Documents\\quotes.csv";

        if (quoteRepository.count() == 0) {

            try {
                BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
                String lineText = null;

                lineReader.readLine(); // skip header line

                while ((lineText = lineReader.readLine()) != null) {
                    String[] data = lineText.split(";");
                    String quoteContent = data[0];
                    String quoteAuthor = data[1];
                    String quoteCategory = data.length == 3 ? data[2] : "";

                    Quote quote = new Quote(quoteContent);
                    quoteRepository.save(quote);

                    if (!authorRepository.existsByAuthorName(quoteAuthor)) {
                        Author author = new Author(quoteAuthor);
                        authorRepository.save(author);
                        author.addQuote(quote);
                        quoteRepository.save(quote);
                    } else {
                        Author author = authorRepository.findByAuthorName(quoteAuthor);
                        authorRepository.save(author);
                        author.addQuote(quote);
                        quoteRepository.save(quote);
                    }

                    if (!categoryRepository.existsByCategoryName(quoteCategory)) {
                        Category category = new Category(quoteCategory);
                        categoryRepository.save(category);
                        quote.addCategory(category);
                        quoteRepository.save(quote);
                    } else {
                        Category category = categoryRepository.findByCategoryName(quoteCategory);
                        categoryRepository.save(category);
                        quote.addCategory(category);
                        quoteRepository.save(quote);
                    }

                }

                lineReader.close();

            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

//        Quote quote = new Quote("The only real valuable thing is intuition.");
//        quoteRepository.save(quote);
//
//        Author author = new Author("Albert Einstein", "Albert Einstein was a German-born theoretical physicist.");
//        authorRepository.save(author);
//
//        author.addQuote(quote);
//
//        quoteRepository.save(quote);
//
//        Category category = new Category("Wisdom", "All around wisdom");
//        categoryRepository.save(category);
//
//        quote.addCategory(category);
//
//        quoteRepository.save(quote);
    }

    public String setAuthorDescription(String authorName) {
        HttpUrl url = HttpUrl.get("https://en.wikiquote.org/w/api.php");

        Wiki wiki = new Wiki.Builder().withApiEndpoint(url).build();

        String response = wiki.getTextExtract(authorName);

        if (response != null) {
            int iend = response.indexOf("See also");

            if (iend != -1) {
                response = response.substring(0, iend);
            }

            return response;
        }

        return "";

    }

}
