package com.bob.web.post.controller;

import static com.bob.web.common.symbol.ResponseSymbol.CREATED;

import com.bob.domain.post.service.PostService;
import com.bob.web.common.AuthenticationId;
import com.bob.web.common.CommonResponse;
import com.bob.web.common.symbol.ResponseSymbol;
import com.bob.web.post.request.CreatePostRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CommonResponse<ResponseSymbol> handleCreatePost(
      @RequestBody CreatePostRequest request,
      @AuthenticationId UUID memberId
  ) {
    postService.createPostProcess(request.toCommand(memberId));
    return new CommonResponse<>(true, CREATED);
  }
}
