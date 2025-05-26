package com.bob.domain.post.service;

import static com.bob.global.exception.response.ApplicationError.NOT_VERIFIED_MEMBER;
import static com.bob.support.fixture.command.CreatePostCommandFixture.defaultCreatePostCommand;
import static com.bob.support.fixture.domain.ActivityAreaFixture.customActivityArea;
import static com.bob.support.fixture.domain.CategoryFixture.defaultCategory;
import static com.bob.support.fixture.domain.MemberFixture.defaultMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bob.domain.area.entity.EmdArea;
import com.bob.domain.area.repository.AreaRepository;
import com.bob.domain.book.entity.Book;
import com.bob.domain.book.repository.BookRepository;
import com.bob.domain.category.entity.Category;
import com.bob.domain.category.repository.CategoryRepository;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.domain.post.entity.Post;
import com.bob.domain.post.repository.PostRepository;
import com.bob.domain.post.service.dto.command.CreatePostCommand;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.support.TestContainerSupport;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("게시글 서비스 통합 테스트")
@Transactional
@SpringBootTest
class PostServiceIntgTest extends TestContainerSupport {

  @Autowired
  private PostService postService;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private AreaRepository areaRepository;

  private EmdArea emdArea;

  @BeforeEach
  void setUp() {
    emdArea = areaRepository.findById(213).get();
    postRepository.deleteAll();
    memberRepository.deleteAll();
    bookRepository.deleteAll();
  }

  @Test
  @DisplayName("게시글 등록 - 성공 테스트")
  void 위치_인증이_된_사용자는_게시글을_등록할_수_있다() {
    // given
    Member seller = defaultMember();
    memberRepository.save(seller);
    seller.updateActivityArea(customActivityArea(seller, emdArea));
    seller.getActivityArea().updateAuthenticationAt(LocalDate.now());

    Category category = categoryRepository.save(defaultCategory());
    CreatePostCommand command = defaultCreatePostCommand(seller.getId(), category.getId());

    // when
    postService.createPostProcess(command);

    // then
    List<Post> posts = postRepository.findAllBySellerId(seller.getId());
    assertThat(posts).hasSize(1);

    Post saved = posts.get(0);
    assertThat(saved.getSeller().getId()).isEqualTo(seller.getId());
    assertThat(saved.getCategory().getId()).isEqualTo(category.getId());
    assertThat(saved.getBook().getIsbn13()).isEqualTo(command.bookIsbn());

    Book book = bookRepository.findByIsbn13(command.bookIsbn()).get();
    assertThat(book).isNotNull();
    assertThat(book.getTitle()).isEqualTo(command.bookTitle());
  }

  @Test
  @DisplayName("게시글 등록 - 실패 테스트(위치 인증 X)")
  void 위치_인증이_안된_사용자는_게시글을_등록할_수_없다() {
    // given
    Member seller = defaultMember();
    memberRepository.save(seller);
    seller.updateActivityArea(customActivityArea(seller, emdArea));
    seller.getActivityArea().updateAuthenticationAt(LocalDate.now().minusMonths(2));

    Category category = categoryRepository.save(defaultCategory());
    CreatePostCommand command = defaultCreatePostCommand(seller.getId(), category.getId());

    // when & then
    assertThatThrownBy(() -> postService.createPostProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(NOT_VERIFIED_MEMBER.getMessage());

    assertThat(postRepository.findAll()).isEmpty();
  }
}
