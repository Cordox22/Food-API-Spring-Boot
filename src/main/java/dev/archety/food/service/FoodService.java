package dev.archety.food.service; 

import java.util.List;
import java.util.Optional;

import dev.archety.food.models.FoodDTO;

public interface FoodService {
	
	FoodDTO create(FoodDTO sto);
	List<FoodDTO> findAll();
	Optional<FoodDTO> findByUuid(String uuid);
	Optional<FoodDTO> update(String uuid, FoodDTO dto);
	boolean delete(String uuid);
}