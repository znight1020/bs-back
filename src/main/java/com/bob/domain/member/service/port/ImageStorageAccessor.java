package com.bob.domain.member.service.port;

public interface ImageStorageAccessor {

  String getImageUploadUrl(String imageName, String contentType);
}
