package com.radovan.spring.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.radovan.spring.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

	@Query(value= "select * from products where product_name ilike CONCAT('%', :keyword, '%') or product_brand ilike CONCAT('%' ,:keyword, '%') or product_model ilike CONCAT('%', :keyword, '%') or category ilike CONCAT('%', :keyword, '%');",nativeQuery=true)
	List<ProductEntity> findAllByKeyword(@Param ("keyword") String keyword);
}
