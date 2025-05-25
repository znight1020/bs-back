package com.bob.domain.book.service.reader;

import com.bob.domain.book.entity.Book;
import com.bob.domain.book.repository.BookRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookReader {

  private final BookRepository bookRepository;

  public Optional<Book> readOptionalBookByIsbn(String isbn) {
    return bookRepository.findByIsbn13(isbn);
  }
}
