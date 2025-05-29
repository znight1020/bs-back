package com.bob.global.props;

import com.bob.global.utils.web.CookieUtils;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@ConfigurationProperties(prefix = "cookie")
@Component
public class CookieProps {
  private String sameSite;

  public void setSameSite(String sameSite) {
    this.sameSite = sameSite;
    CookieUtils.setSameSite(sameSite);
  }
}
