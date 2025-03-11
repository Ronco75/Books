package ronco.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ronco.books.model.BookEntity;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, String> {
}
