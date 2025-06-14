package com.bob.support.fixture.command;

import com.bob.domain.trade.service.dto.command.CreateTradeCommand;
import java.util.UUID;

public class CreateTradeCommandFixture {

  public static CreateTradeCommand DEFAULT_CREATE_TRADE_COMMAND() {
    return new CreateTradeCommand(1L, UUID.randomUUID(), UUID.randomUUID());
  }
}
