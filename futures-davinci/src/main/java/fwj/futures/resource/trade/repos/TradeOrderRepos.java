package fwj.futures.resource.trade.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.trade.entity.TradeOrder;

@RepositoryRestResource(exported = false)
public interface TradeOrderRepos extends JpaRepository<TradeOrder, Integer> {

	TradeOrder findBySerialNo(String serialNo);

	@Query("select o from TradeOrder o order by o.tradeDt desc")
	List<TradeOrder> findOrderByTradeDtDesc();

	@Query("select o from TradeOrder o, TradeGroupOrder r where r.groupId=:groupId and r.orderId=o.id")
	List<TradeOrder> findByGroupId(@Param("groupId") Integer groupId);

}
