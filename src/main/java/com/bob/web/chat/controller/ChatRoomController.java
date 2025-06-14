package com.bob.web.chat.controller;


import static org.springframework.http.HttpStatus.CREATED;

import com.bob.domain.chat.service.ChatRoomService;
import com.bob.domain.chat.service.dto.response.CreateChatRoomResponse;
import com.bob.web.chat.request.CreateChatRoomRequest;
import com.bob.web.common.AuthenticationId;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/chatrooms")
@RestController
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @PostMapping
  public ResponseEntity<CreateChatRoomResponse> handleCreateChatRoom(
      @Valid @RequestBody CreateChatRoomRequest request,
      @AuthenticationId UUID memberId
  ) {
    CreateChatRoomResponse response = chatRoomService.createChatRoomProcess(request.toCommand(memberId));
    return ResponseEntity.status(CREATED).body(response);
  }
}
