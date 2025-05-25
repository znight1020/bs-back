package com.bob.domain.book.repository;

import com.bob.domain.book.entity.Book;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

  Optional<Book> findByIsbn13(String isbn);
}
