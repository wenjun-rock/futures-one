package fwj.futures.resource.trade.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.trade.entity.Trade;
import fwj.futures.resource.trade.entity.TradeAction;

@RepositoryRestResource(exported = false)
public interface TradeActionRepos extends JpaRepository<TradeAction, Integer> {

	List<TradeAction> findByTradeAndConCodeOrderByDtAsc(Trade trade, String contractCode);

}
