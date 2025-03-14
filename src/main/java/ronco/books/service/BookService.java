package ronco.books.service;

import org.springframework.stereotype.Service;
import ronco.books.model.Book;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    Book save(Book book);

    Optional<Book> findById(String isbn);

    List<Book> listBooks();

    boolean isBookExist(Book book);

    void deleteBookById(String isbn);
}
