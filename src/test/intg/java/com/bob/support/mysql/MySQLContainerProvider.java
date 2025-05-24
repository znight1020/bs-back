package com.bob.support.mysql;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class MySQLContainerProvider {

  @Container
  public static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0")
      .withDatabaseName("tc_test")
      .withUsername("root")
      .withPassword("1234")
      .withInitScript("sql/schema.sql");

  static {
    MYSQL_CONTAINER.start();
  }
}

