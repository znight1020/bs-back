package com.bob.domain.post.repository;

import static com.bob.support.fixture.domain.ActivityAreaFixture.customActivityArea;
import static com.bob.support.fixture.domain.BookFixture.defaultBook;
import static com.bob.support.fixture.domain.EmdAreaFixture.defaultEmdArea;
import static com.bob.support.fixture.domain.MemberFixture.defaultMember;
import static com.bob.support.fixture.domain.PostFixture.defaultPost;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import com.bob.domain.book.entity.Book;
import com.bob.domain.book.repository.BookRepository;
import com.bob.domain.category.entity.Category;
import com.bob.domain.category.repository.CategoryRepository;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.domain.post.entity.Post;
import com.bob.support.config.TestConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(TestConfig.class)
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

  @PersistenceContext
  private EntityManager em;

  private Member member;
  private Book book;
  private Category category;
  private Post post;

  @BeforeEach
  void setUp() {
    member = defaultMember();
    book = defaultBook();
    category = Category.builder().name("TEST").build();

    memberRepository.save(member);
    bookRepository.save(book);
    categoryRepository.save(category);
    member.updateActivityArea(customActivityArea(member, defaultEmdArea()));

    post = defaultPost(book, member, category);
    memberRepository.save(member);
    postRepository.save(post);
  }

  @Test
  @DisplayName("판매자 ID로 게시글 목록 조회 - 반환 테스트")
  void 판매자_ID로_게시글을_조회할_수_있다() {
    // when
    List<Post> result = postRepository.findAllBySellerId(member.getId());

    // then
    assertThat(result).hasSize(1);
    assertThat(result).extracting(post -> post.getSeller().getId()).containsOnly(member.getId());
  }

  @Test
  @DisplayName("판매자 ID로 게시글 목록 조회 - empty 테스트")
  void 판매자_ID로_게시글이_없으면_빈_리스트를_반환한다() {
    // when
    List<Post> result = postRepository.findAllBySellerId(randomUUID());

    // then
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("게시글 조회수 증가 테스트")
  void 게시글_조회수를_증가시킬_수_있다() {
    // given
    Post foundedPost = postRepository.findById(post.getId()).get();
    Long postId = foundedPost.getId();
    int originalViewCount = foundedPost.getViewCount();

    // when
    postRepository.increaseViewCount(postId);
    em.flush();
    em.clear();

    Post updatedPost = postRepository.findById(postId).orElseThrow();

    // then
    assertThat(updatedPost.getViewCount()).isEqualTo(originalViewCount + 1);
  }
}
