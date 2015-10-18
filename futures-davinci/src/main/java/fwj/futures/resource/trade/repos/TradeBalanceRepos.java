package fwj.futures.resource.trade.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.trade.entity.Trade;
import fwj.futures.resource.trade.entity.TradeBalance;

@RepositoryRestResource(exported = false)
public interface TradeBalanceRepos extends JpaRepository<TradeBalance, Integer> {
	
	List<TradeBalance> findByTrade(Trade trade);
	
	TradeBalance findByTradeAndConCode(Trade trade, String conCode);
	

}
