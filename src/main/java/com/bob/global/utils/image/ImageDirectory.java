package com.bob.global.utils.image;

import lombok.Getter;

@Getter
public enum ImageDirectory {
  PROFILE("profile/"),
  BOOK("book/"),
  CHAT("chat/");

  private final String prefix;

  ImageDirectory(String prefix) {
    this.prefix = prefix;
  }
}
