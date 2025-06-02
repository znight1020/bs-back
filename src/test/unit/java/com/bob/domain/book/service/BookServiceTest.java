package com.bob.domain.book.service;

import static com.bob.support.fixture.command.BookCreateCommandFixture.defaultBookCreateCommand;
import static com.bob.support.fixture.domain.BookFixture.defaultBook;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.bob.domain.book.entity.Book;
import com.bob.domain.book.repository.BookRepository;
import com.bob.domain.book.service.dto.BookCreateCommand;
import com.bob.domain.book.service.reader.BookReader;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("도서 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  @InjectMocks
  private BookService bookService;

  @Mock
  private BookReader bookReader;

  @Mock
  private BookRepository bookRepository;

  @Test
  @DisplayName("책 조회 및 생성 - 조회된 Book 반환")
  void 이미_존재하는_도서가_있으면_저장하지_않고_조회된_도서를_반환한다() {
    // given
    BookCreateCommand command = defaultBookCreateCommand();
    Book existingBook = defaultBook();
    given(bookReader.readOptionalBookByIsbn(command.isbn13())).willReturn(Optional.of(existingBook));

    // when
    Book result = bookService.createBookProcess(command);

    // then
    assertThat(result).isEqualTo(existingBook);
    verify(bookReader).readOptionalBookByIsbn(command.isbn13());
    verify(bookRepository, never()).save(any());
  }

  @Test
  @DisplayName("책 조회 및 생성 - 존재하지 않는 경우 새로 저장 후 반환")
  void 존재하지_않는_도서면_새로_저장하고_반환한다() {
    // given
    BookCreateCommand command = defaultBookCreateCommand();
    Book newBook = command.toBook();
    given(bookReader.readOptionalBookByIsbn(command.isbn13())).willReturn(Optional.empty());
    given(bookRepository.save(any(Book.class))).willReturn(newBook);

    // when
    Book result = bookService.createBookProcess(command);

    // then
    assertThat(result).isEqualTo(newBook);
    verify(bookReader).readOptionalBookByIsbn(command.isbn13());
    verify(bookRepository).save(any(Book.class));
  }
}