package com.bob.domain.post.repository;

import static com.bob.support.fixture.domain.BookFixture.defaultBook;
import static com.bob.support.fixture.domain.MemberFixture.defaultMember;
import static com.bob.support.fixture.domain.PostFixture.defaultPost;
import static org.assertj.core.api.Assertions.assertThat;

import com.bob.domain.book.entity.Book;
import com.bob.domain.book.repository.BookRepository;
import com.bob.domain.category.entity.Category;
import com.bob.domain.category.repository.CategoryRepository;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.domain.post.entity.Post;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("PostRepository 테스트")
class PostRepositoryTest {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  private Member savedMember;

  @BeforeEach
  void setUp() {
    Member member = defaultMember();
    Book book = defaultBook();
    Category category = Category.builder().name("TEST").build();

    savedMember = memberRepository.save(member);
    Book savedBook = bookRepository.save(book);
    Category savedCategory = categoryRepository.save(category);

    Post post = defaultPost(savedBook, savedMember, savedCategory);
    postRepository.save(post);
  }

  @Test
  @DisplayName("판매자 ID로 게시글 목록 조회 - 반환 테스트")
  void 판매자_ID로_게시글을_조회할_수_있다() {
    // when
    List<Post> result = postRepository.findAllBySellerId(savedMember.getId());

    // then
    assertThat(result).hasSize(1);
    assertThat(result).extracting(post -> post.getSeller().getId()).containsOnly(savedMember.getId());
  }

  @Test
  @DisplayName("판매자 ID로 게시글 목록 조회 - empty 테스트")
  void 판매자_ID로_게시글이_없으면_빈_리스트를_반환한다() {
    // when
    List<Post> result = postRepository.findAllBySellerId(-1L);

    // then
    assertThat(result).isEmpty();
  }
}
