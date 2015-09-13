package fwj.futures.resource.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fwj.futures.resource.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	Product findByCode(String code);

}
