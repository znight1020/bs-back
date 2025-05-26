package com.bob.domain.book.service;

import com.bob.domain.book.entity.Book;
import com.bob.domain.book.repository.BookRepository;
import com.bob.domain.book.service.dto.BookCreateCommand;
import com.bob.domain.book.service.reader.BookReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookService {

  private final BookRepository bookRepository;
  private final BookReader bookReader;

  @Transactional
  public Book createBookProcess(BookCreateCommand command) {
    return bookReader.readOptionalBookByIsbn(command.isbn13())
        .orElseGet(() -> bookRepository.save(command.toBook()));
  }
}
