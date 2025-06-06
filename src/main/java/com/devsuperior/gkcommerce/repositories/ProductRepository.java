package com.devsuperior.gkcommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.gkcommerce.entities.Product;


public interface ProductRepository extends JpaRepository<Product, Long>{

}
