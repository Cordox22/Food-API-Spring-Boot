package dev.archety.food.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.archety.food.models.Food;
import dev.archety.food.models.FoodDTO;
import dev.archety.food.repositories.FoodRepositories;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class FoodServiceImpl implements FoodService{
	
	private final FoodRepositories foodRepo; 
	
	public FoodServiceImpl(FoodRepositories obj) {
		this.foodRepo = obj;
	}

	@Override
	public FoodDTO create(FoodDTO dto) {
		Food food = new Food();
		mapToEntity(dto, food);
		
		Food saved = foodRepo.save(food);
		
		return mapToDto(saved);
	}

	@Override
	public List<FoodDTO> findAll() {
		
		return foodRepo.findAll().stream().map(this::mapToDto).toList();
	}

	@Override
	public Optional<FoodDTO> findByUuid(String uuid) {
		return foodRepo.findByUuid(uuid).map(this::mapToDto);
	}

	@Override
	public boolean delete(String uuid) {
		boolean flag;
		Optional<Food> food = foodRepo.findByUuid(uuid);
		
		if(food.isEmpty()) {
			flag = false;
		}
		else {
			foodRepo.delete(food.get());
			flag = true;
		}
		
		return flag;
	}
	
	@Override
	public Optional<FoodDTO> update(String uuid, FoodDTO dto) {
		Optional<Food> foodOpt = foodRepo.findByUuid(uuid);
		
		if(foodOpt.isEmpty()) {
			return Optional.empty();
		}
		
		Food food = foodOpt.get();
		
		mapToEntity(dto, food);
		
		return Optional.of(mapToDto(food));
	}
	
	private FoodDTO mapToDto(Food food) {
		FoodDTO dto = new FoodDTO();
		dto.setUuid(food.getUuid());
		dto.setFood(food.getFoodName());
		dto.setAvailable(food.isAvailable());
		dto.setCalories(food.getCalories());
		dto.setCategory(food.getCategory());
		dto.setPrice(food.getPrice());
		dto.setDate(food.getDate());
		dto.setCreatedAt(food.getCreatedAt());
		
		return dto;
	}
	
	private void mapToEntity(FoodDTO dto, Food food) {
		food.setFoodName(dto.getFood());
		food.setCategory(dto.getCategory());
		food.setCalories(dto.getCalories());
		food.setPrice(dto.getPrice());
		food.setAvailable(dto.isAvailable());
		food.setDate(dto.getDate());
		//Created At non deve essere toccato
	}
}