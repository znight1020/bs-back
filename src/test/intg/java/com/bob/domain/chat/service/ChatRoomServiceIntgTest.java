package com.bob.domain.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bob.domain.chat.entity.ChatRoom;
import com.bob.domain.chat.entity.ChatRoomMember;
import com.bob.domain.chat.repository.ChatRoomMemberRepository;
import com.bob.domain.chat.repository.ChatRoomRepository;
import com.bob.domain.chat.service.dto.command.CreateChatRoomCommand;
import com.bob.domain.chat.service.dto.response.CreateChatRoomResponse;
import com.bob.domain.member.entity.Member;
import com.bob.domain.member.repository.MemberRepository;
import com.bob.domain.post.entity.Post;
import com.bob.domain.post.repository.PostRepository;
import com.bob.domain.trade.entity.Trade;
import com.bob.domain.trade.repository.TradeRepository;
import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import com.bob.support.TestContainerSupport;
import com.bob.support.fixture.command.CreateChatRoomCommandFixture;
import com.bob.support.fixture.domain.MemberFixture;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("채팅방 서비스 통합 테스트")
@Transactional
@SpringBootTest
class ChatRoomServiceIntgTest extends TestContainerSupport {

  @Autowired
  private ChatRoomService chatRoomService;

  @Autowired
  private ChatRoomRepository chatRoomRepository;

  @Autowired
  private ChatRoomMemberRepository chatRoomMemberRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private TradeRepository tradeRepository;

  @Test
  @DisplayName("채팅방 생성 - 성공 테스트")
  void 채팅방을_생성할_수_있다() {
    // given
    Member seller = memberRepository.findById(UUID.fromString("0197365f-8074-7d24-a332-95c9ebd1f5c0")).get();
    Member buyer = memberRepository.save(MemberFixture.otherMember());
    Post post = postRepository.findAllBySellerId(seller.getId()).get(0);
    CreateChatRoomCommand command = CreateChatRoomCommandFixture.of(post.getId(), buyer.getId());

    // when
    CreateChatRoomResponse response = chatRoomService.createChatRoomProcess(command);

    // then
    ChatRoom chatRoom = chatRoomRepository.findById(response.chatRoomId()).orElseThrow();
    List<ChatRoomMember> members = chatRoomMemberRepository.findByChatRoomId(chatRoom.getId());

    assertThat(chatRoom.getPostId()).isEqualTo(post.getId());
    assertThat(chatRoom.getTitleSuffix()).contains("자바의 정석");
    assertThat(members).extracting(ChatRoomMember::getMemberId)
        .containsExactlyInAnyOrder(seller.getId(), buyer.getId());

    Trade trade = tradeRepository.findById(chatRoom.getTradeId()).orElseThrow();
    assertThat(trade.getSellerId()).isEqualTo(seller.getId());
    assertThat(trade.getBuyerId()).isEqualTo(buyer.getId());
  }

  @Test
  @DisplayName("채팅방 생성 - 실패 테스트 (본인 게시글 요청)")
  void 본인_게시글에는_채팅방을_생성할_수_없다() {
    // given
    Member seller = memberRepository.findById(UUID.fromString("0197365f-8074-7d24-a332-95c9ebd1f5c0")).get();
    Post post = postRepository.findAllBySellerId(seller.getId()).get(0);
    CreateChatRoomCommand command = CreateChatRoomCommandFixture.of(post.getId(), seller.getId());

    // when & then
    assertThatThrownBy(() -> chatRoomService.createChatRoomProcess(command))
        .isInstanceOf(ApplicationException.class)
        .hasMessage(ApplicationError.IS_SAME_CHAT_MEMBER.getMessage());
  }

  @Test
  @DisplayName("채팅방 생성 - 이미 존재하는 경우 기존 ID 반환")
  void 이미_존재하는_채팅방이_있다면_ID를_반환한다() {
    // given
    Member seller = memberRepository.findById(UUID.fromString("0197365f-8074-7d24-a332-95c9ebd1f5c0")).get();
    Member buyer = memberRepository.save(MemberFixture.otherMember());
    Post post = postRepository.findAllBySellerId(seller.getId()).get(0);

    CreateChatRoomCommand command = CreateChatRoomCommandFixture.of(post.getId(), buyer.getId()); // 최초 생성
    CreateChatRoomResponse created = chatRoomService.createChatRoomProcess(command);

    // when
    CreateChatRoomResponse result = chatRoomService.createChatRoomProcess(command);

    // then
    assertThat(result.chatRoomId()).isEqualTo(created.chatRoomId());
    assertThat(chatRoomRepository.findById(result.chatRoomId())).isNotNull();
    assertThat(chatRoomMemberRepository.findByChatRoomId(result.chatRoomId())).hasSize(2);
  }
}
