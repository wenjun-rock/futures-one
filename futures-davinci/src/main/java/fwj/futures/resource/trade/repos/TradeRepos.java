package fwj.futures.resource.trade.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fwj.futures.resource.trade.entity.Trade;

@RepositoryRestResource(exported = false)
public interface TradeRepos extends JpaRepository<Trade, Integer> {

}
