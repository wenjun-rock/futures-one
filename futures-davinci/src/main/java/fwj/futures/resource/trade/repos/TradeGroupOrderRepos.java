package fwj.futures.resource.trade.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.trade.entity.TradeGroupOrder;

@RepositoryRestResource(exported = false)
public interface TradeGroupOrderRepos extends JpaRepository<TradeGroupOrder, Integer> {

	void deleteByGroupId(Integer groupId);

	void deleteByOrderId(Integer orderId);

}
