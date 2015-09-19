package fwj.futures.resource.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fwj.futures.resource.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	Product findByCode(String code);
	
	@Query("select o from Product o where o.active='1'")
	List<Product> findAllActive();

}
