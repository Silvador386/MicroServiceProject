package com.Silvador386.inventory;

import com.Silvador386.inventory.model.Inventory;
import com.Silvador386.inventory.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory firstInventory = new Inventory();
			firstInventory.setSkuCode("Samsung Galaxy");
			firstInventory.setQuantity(128);

			Inventory secondInventory = new Inventory();
			secondInventory.setSkuCode("Samsung Universe");
			secondInventory.setQuantity(0);

			inventoryRepository.save(firstInventory);
			inventoryRepository.save(secondInventory);
		};
	}

}
