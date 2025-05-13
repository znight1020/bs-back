package com.bob.support.mysql;

import org.testcontainers.containers.MySQLContainer;

public class MySQLContainerProvider {

  public static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0")
      .withDatabaseName("test")
      .withUsername("test")
      .withPassword("test")
      .withReuse(true);

  static {
    MYSQL_CONTAINER.start();
  }
}

