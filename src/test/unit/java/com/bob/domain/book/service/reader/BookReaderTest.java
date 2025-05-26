package com.bob.domain.book.service.reader;

import static com.bob.support.fixture.domain.BookFixture.DEFAULT_ISBN;
import static com.bob.support.fixture.domain.BookFixture.defaultBook;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.bob.domain.book.entity.Book;
import com.bob.domain.book.repository.BookRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookReaderTest {

  @InjectMocks
  private BookReader bookReader;

  @Mock
  private BookRepository bookRepository;

  @Test
  @DisplayName("ISBN Book 조회 - 존재하는 경우")
  void isbn으로_책을_조회_할_수_있다() {
    // given
    Book book = defaultBook();
    given(bookRepository.findByIsbn13(DEFAULT_ISBN)).willReturn(Optional.of(book));

    // when
    Optional<Book> result = bookReader.readOptionalBookByIsbn(DEFAULT_ISBN);

    // then
    assertThat(result).isPresent();
    assertThat(result.get().getIsbn13()).isEqualTo(DEFAULT_ISBN);
    verify(bookRepository).findByIsbn13(DEFAULT_ISBN);
  }

  @Test
  @DisplayName("ISBN Book 조회 - 존재하지 않는 경우")
  void isbn으로_책_조회_시_존재하지_않으면_empty를_반환한다() {
    // given
    String isbn = "0000000000000";
    given(bookRepository.findByIsbn13(isbn)).willReturn(Optional.empty());

    // when
    Optional<Book> result = bookReader.readOptionalBookByIsbn(isbn);

    // then
    assertThat(result).isEmpty();
    verify(bookRepository).findByIsbn13(isbn);
  }
}
