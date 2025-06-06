package com.devsuperior.gkcommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.gkcommerce.dtos.ProductDTO;
import com.devsuperior.gkcommerce.entities.Product;
import com.devsuperior.gkcommerce.repositories.ProductRepository;
import com.devsuperior.gkcommerce.services.exceptions.DatabaseIntegrityException;
import com.devsuperior.gkcommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

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
		Product product = prodRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
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
		try {
			Product entity = prodRepository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = prodRepository.save(entity);
			return new ProductDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Update failed! Id not found");
		}
		
	}
	
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		try {
			prodRepository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Delete failed! Id not found");
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseIntegrityException("Delete failed! Referential integrity constraint violation.");
		}
		
	}
	
	
	
	
	private Product copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		return entity;
	}

}
