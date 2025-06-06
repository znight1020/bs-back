package com.bob.domain.post.service;

import static com.bob.global.exception.response.ApplicationError.NOT_POST_OWNER;
import static com.bob.global.exception.response.ApplicationError.NOT_VERIFIED_MEMBER;
import static com.bob.support.fixture.command.ChangePostCommandFixture.DEFAULT_CHANGE_POST_COMMAND;
import static com.bob.support.fixture.command.CreatePostCommandFixture.defaultCreatePostCommand;
import static com.bob.support.fixture.domain.ActivityAreaFixture.customActivityArea;
import static com.bob.support.fixture.domain.CategoryFixture.defaultCategory;
import static com.bob.support.fixture.domain.MemberFixture.defaultMember;
import static com.bob.support.fixture.query.PostQueryFixture.searchAuthorQuery;
import static com.bob.support.fixture.query.PostQueryFixture.searchBookStatusQuery;
import static com.bob.support.fixture.query.PostQueryFixture.searchCategoryQuery;
import static com.bob.support.fixture.query.PostQueryFixture.searchHighPriceQuery;
import static com.bob.support.fixture.query.PostQueryFixture.searchLowPriceQuery;
import static com.bob.support.fixture.query.PostQueryFixture.searchNewestQuery;
import static com.bob.support.fixture.query.PostQueryFixture.searchOldestQuery;
import static com.bob.support.fixture.query.PostQueryFixture.searchTitleQuery;
import static com.bob.support.fixture.query.PostQueryFixture.searchTradeStatusQuery;
import static com.bob.support.fixture.query.PostQueryFixture.searchUnderPriceQuery;
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
import com.bob.domain.post.service.dto.command.ChangePostCommand;
import com.bob.domain.post.service.dto.command.CreatePostCommand;
import com.bob.domain.post.service.dto.query.ReadFilteredPostsQuery;
import com.bob.domain.post.service.dto.query.ReadPostDetailQuery;
import com.bob.domain.post.service.dto.response.PostDetailResponse;
import com.bob.domain.post.service.dto.response.PostSummary;
import com.bob.domain.post.service.dto.response.PostsResponse;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.support.TestContainerSupport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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

  @PersistenceContext
  private EntityManager em;

  private EmdArea emdArea;

  private final PageRequest pageable = PageRequest.of(0, 10);

  @BeforeEach
  void setUp() {
    emdArea = areaRepository.findById(213).get();
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
  }

  @Test
  @DisplayName("제목 검색 - '오브젝트' 포함 게시글 조회")
  void 제목으로_게시글을_검색할_수_있다() {
    ReadFilteredPostsQuery query = searchTitleQuery();
    PostsResponse result = postService.readFilteredPostsProcess(query, pageable);

    assertThat(result.posts())
        .extracting(PostSummary::postTitle)
        .allMatch(title -> title.contains("오브젝트"));
  }

  @Test
  @DisplayName("저자 검색 - '김영한' 포함 게시글 조회")
  void 저자로_게시글을_검색할_수_있다() {
    ReadFilteredPostsQuery query = searchAuthorQuery();
    PostsResponse result = postService.readFilteredPostsProcess(query, pageable);

    assertThat(result.posts())
        .extracting(PostSummary::postTitle)
        .contains("자바 ORM 표준 JPA 프로그래밍");
  }

  @Test
  @DisplayName("가격 필터 - 5000원 이하 게시글 조회")
  void 가격이_5000원이하인_게시글만_조회할_수_있다() {
    ReadFilteredPostsQuery query = searchUnderPriceQuery();
    PostsResponse result = postService.readFilteredPostsProcess(query, pageable);

    assertThat(result.posts())
        .extracting(PostSummary::sellPrice)
        .allMatch(price -> price <= 5000);
  }

  @Test
  @DisplayName("카테고리 필터 - categoryId = 1")
  void 카테고리별_게시글을_조회할_수_있다() {
    ReadFilteredPostsQuery query = searchCategoryQuery();
    PostsResponse result = postService.readFilteredPostsProcess(query, pageable);

    assertThat(result.posts())
        .extracting(PostSummary::categoryId)
        .containsOnly(1);
  }

  @Test
  @DisplayName("거래 상태 필터 - 거래 완료")
  void 거래상태로_게시글을_조회할_수_있다() {
    ReadFilteredPostsQuery query = searchTradeStatusQuery();
    PostsResponse result = postService.readFilteredPostsProcess(query, pageable);

    assertThat(result.posts())
        .extracting(PostSummary::postStatus)
        .containsOnly("COMPLETED");
  }

  @Test
  @DisplayName("책 상태 필터 - 최상")
  void 책상태로_게시글을_조회할_수_있다() {
    ReadFilteredPostsQuery query = searchBookStatusQuery();
    PostsResponse result = postService.readFilteredPostsProcess(query, pageable);

    assertThat(result.posts())
        .extracting(PostSummary::bookStatus)
        .contains("BEST");
  }

  @Test
  @DisplayName("정렬 - 최신순 (createdAt DESC)")
  void 최신순으로_게시글을_정렬할_수_있다() {
    ReadFilteredPostsQuery query = searchNewestQuery();
    PostsResponse result = postService.readFilteredPostsProcess(query, pageable);

    List<LocalDateTime> createdAtList = result.posts().stream()
        .map(PostSummary::createdAt)
        .toList();

    assertThat(createdAtList).isSortedAccordingTo(Comparator.reverseOrder());
  }

  @Test
  @DisplayName("정렬 - 오래된 순 (createdAt ASC)")
  void 오래된순으로_게시글을_정렬할_수_있다() {
    ReadFilteredPostsQuery query = searchOldestQuery();
    PostsResponse result = postService.readFilteredPostsProcess(query, pageable);

    List<LocalDateTime> createdAtList = result.posts().stream()
        .map(PostSummary::createdAt)
        .toList();

    assertThat(createdAtList).isSorted();
  }

  @Test
  @DisplayName("정렬 - 낮은 가격순")
  void 가격이_낮은_순으로_게시글을_정렬할_수_있다() {
    ReadFilteredPostsQuery query = searchLowPriceQuery();
    PostsResponse result = postService.readFilteredPostsProcess(query, pageable);

    List<Integer> prices = result.posts().stream()
        .map(PostSummary::sellPrice)
        .toList();

    assertThat(prices).isSorted();
  }

  @Test
  @DisplayName("정렬 - 높은 가격순")
  void 가격이_높은_순으로_게시글을_정렬할_수_있다() {
    ReadFilteredPostsQuery query = searchHighPriceQuery();
    PostsResponse result = postService.readFilteredPostsProcess(query, pageable);

    List<Integer> prices = result.posts().stream()
        .map(PostSummary::sellPrice)
        .toList();

    assertThat(prices).isSortedAccordingTo(Comparator.reverseOrder());
  }

  @Test
  @DisplayName("게시글 상세 조회 - 작성자 본인, 조회수 증가")
  void 게시글_상세_조회_작성자일_경우_isOwner_true_조회수_증가() {
    // given
    UUID writerId = UUID.fromString("0197365f-8074-7d24-a332-95c9ebd1f5c0");
    Post post = postRepository.findAllBySellerId(writerId).get(0); // 더미 데이터의 첫 번째 게시글
    int beforeViewCount = post.getViewCount();

    // when
    PostDetailResponse result = postService.readPostDetailProcess(new ReadPostDetailQuery(writerId, post.getId()));
    em.flush();
    em.clear(); // @Modifying @Query 작성으로 인한 영속성 컨텍스트 무시 -> Dirty checking X

    // then
    Post updated = postRepository.findById(post.getId()).orElseThrow();
    assertThat(result.isOwner()).isTrue();
    assertThat(updated.getViewCount()).isEqualTo(beforeViewCount + 1);
  }

  @Test
  @DisplayName("게시글 상세 조회 - 요청자가 작성자가 아닐 경우 isOwner=false")
  void 게시글_상세_조회_작성자가_아닌_경우_isOwner_false() {
    // given
    UUID viewerId = UUID.randomUUID();
    UUID writerId = UUID.fromString("0197365f-8074-7d24-a332-95c9ebd1f5c0");
    Post post = postRepository.findAllBySellerId(writerId).get(0);

    // when
    PostDetailResponse result = postService.readPostDetailProcess(new ReadPostDetailQuery(viewerId, post.getId()));

    // then
    assertThat(result.isOwner()).isFalse();
  }

  @Test
  @DisplayName("게시글 수정 - 성공 케이스")
  void 게시글_정보를_성공적으로_수정할_수_있다() {
    // given
    UUID writerId = UUID.fromString("0197365f-8074-7d24-a332-95c9ebd1f5c0");
    Post post = postRepository.findAll().iterator().next();
    ChangePostCommand command = DEFAULT_CHANGE_POST_COMMAND(writerId, post.getId());

    // when
    postService.changePostProcess(command);

    // then
    Post updated = postRepository.findById(post.getId()).orElseThrow();
    assertThat(updated.getSellPrice()).isEqualTo(command.sellPrice());
    assertThat(updated.getBookStatus().getStatus()).isEqualTo(command.bookStatus());
    assertThat(updated.getDescription()).isEqualTo(command.description());
  }

  @Test
  @DisplayName("게시글 수정 - 작성자 불일치 예외")
  void 게시글_작성자가_아니면_예외가_발생한다() {
    // given
    Member viewer = memberRepository.save(defaultMember());
    Post post = postRepository.findAll().iterator().next();
    ChangePostCommand command = DEFAULT_CHANGE_POST_COMMAND(viewer.getId(), post.getId());

    // when & then
    assertThatThrownBy(() -> postService.changePostProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessageContaining(NOT_POST_OWNER.getMessage());
  }
}
