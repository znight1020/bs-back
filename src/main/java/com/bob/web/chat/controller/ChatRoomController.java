package com.bob.web.chat.controller;


import static com.bob.web.common.symbol.ResponseSymbol.CREATED;

import com.bob.domain.chat.service.ChatRoomService;
import com.bob.web.chat.request.CreateChatRoomRequest;
import com.bob.web.common.AuthenticationId;
import com.bob.web.common.CommonResponse;
import com.bob.web.common.symbol.ResponseSymbol;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/chatrooms")
@RestController
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public CommonResponse<ResponseSymbol> handleCreateChatRoom(
      @Valid @RequestBody CreateChatRoomRequest request,
      @AuthenticationId UUID memberId
  ) {
    chatRoomService.createChatRoomProcess(request.toCommand(memberId));
    return new CommonResponse<>(true, CREATED);
  }
}
