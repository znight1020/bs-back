package com.bob.domain.trade.repository;

import com.bob.domain.trade.entity.Trade;
import org.springframework.data.repository.CrudRepository;

public interface TradeRepository extends CrudRepository<Trade, Long> {

}
