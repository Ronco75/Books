package ronco.books.service;

import org.springframework.stereotype.Service;
import ronco.books.model.Book;

@Service
public interface BookService {

    Book create(Book book);
}
