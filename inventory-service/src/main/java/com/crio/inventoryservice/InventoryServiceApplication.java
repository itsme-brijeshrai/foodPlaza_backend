package com.crio.inventoryservice;

import com.crio.inventoryservice.model.Inventory;
import com.crio.inventoryservice.repository.IInventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(IInventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("iPhone 14");
			inventory.setQuantity(100);

			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("iPhone 13");
			inventory1.setQuantity(65);

			Inventory inventory2 = new Inventory();
			inventory2.setSkuCode("iPhone 12");
			inventory2.setQuantity(40);

			Inventory inventory3 = new Inventory();
			inventory3.setSkuCode("iPhone 11");
			inventory3.setQuantity(30);

			Inventory inventory4 = new Inventory();
			inventory4.setSkuCode("iPhone XR");
			inventory4.setQuantity(10);

			Inventory inventory5 = new Inventory();
			inventory5.setSkuCode("iPhone se");
			inventory5.setQuantity(25);

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
			inventoryRepository.save(inventory2);
			inventoryRepository.save(inventory3);
			inventoryRepository.save(inventory4);
			inventoryRepository.save(inventory5);
		};
    }


}
