package ronco.books.service;

import org.springframework.stereotype.Service;
import ronco.books.model.Book;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    Book create(Book book);

    Optional<Book> findById(String isbn);

    List<Book> listBooks();
}
