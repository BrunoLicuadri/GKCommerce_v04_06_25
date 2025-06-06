package com.devsuperior.gkcommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.gkcommerce.dtos.ProductDTO;
import com.devsuperior.gkcommerce.entities.Product;
import com.devsuperior.gkcommerce.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	public ProductRepository prodRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable) {
		Page<Product> result = prodRepository.findAll(pageable);
		return result.map(x -> new ProductDTO(x));
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product product = prodRepository.findById(id).get();
		ProductDTO dto = new ProductDTO(product);
		return dto;
	}
	
	@Transactional()
	public ProductDTO insert (ProductDTO dto) {
		Product entity = new Product(); 
		copyDtoToEntity(dto, entity);
		entity = prodRepository.save(entity);
		return new ProductDTO(entity);
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		Product entity = prodRepository.getReferenceById(id);
		copyDtoToEntity(dto, entity);
		entity = prodRepository.save(entity);
		return new ProductDTO(entity);
	}
	
	
	@Transactional
	public void delete(Long id) {
		prodRepository.deleteById(id);
	}
	
	
	
	
	private Product copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		return entity;
	}

}
