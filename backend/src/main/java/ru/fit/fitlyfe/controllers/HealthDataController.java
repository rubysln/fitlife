package ru.fit.fitlyfe.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fit.fitlyfe.models.HealthData;
import ru.fit.fitlyfe.services.impl.HealthDataServiceImpl;

@RestController
@RequestMapping("/api/healths")
public class HealthDataController {
	@Autowired
	private HealthDataServiceImpl healthDataService;

	@GetMapping("/{userId}")
	CollectionModel<EntityModel<HealthData>> getHealths(@PathVariable("userId") long userId){
		var healthsDataList =  healthDataService.getAllHealth(userId);
		if(healthsDataList == null) return null;
		List<EntityModel<HealthData>> healthModels = healthsDataList.stream()
				.map(healthData -> EntityModel.of(healthData,
						linkTo(methodOn(HealthDataController.class)
								.getHealth(userId, healthData.getId()))
								.withSelfRel()))
				.collect(Collectors.toList());

		Link collectionLink = linkTo(methodOn(HealthDataController.class)
				.getHealths(userId))
				.withSelfRel();

		return CollectionModel.of(healthModels, collectionLink);
	}

	@GetMapping("/{userId}/{healthId}")
	EntityModel<Optional<HealthData>> getHealth(@PathVariable("userId") long userId,
			@PathVariable("healthId") long healthId){
		var healthData = healthDataService.getOneHealth(userId, healthId);
		Link selfLink = linkTo(methodOn(HealthDataController.class)
				.getHealth(userId, healthId))
				.withSelfRel();
		return EntityModel.of(healthData, selfLink);
	}

	@PostMapping("/{userId}")
	EntityModel<HealthData> createHealth(@RequestBody HealthData healthData,
			@PathVariable("userId") long userId){
		var health =  healthDataService.createHealth(healthData, userId);

		Link selfLink = linkTo(methodOn(HealthDataController.class)
				.getHealth(userId, health.getId()))
				.withSelfRel();

		return EntityModel.of(health, selfLink);
	}

	@PatchMapping("/{userId}/{healthId}")
	EntityModel<Optional<HealthData>> patchHealth(@PathVariable("userId") long userId,
			@PathVariable("healthId") long healthId,
			@RequestBody HealthData healthData){
		var health =  healthDataService.patchHealth(healthData, healthId);

		Link selfLink = linkTo(methodOn(HealthDataController.class)
				.getHealth(userId, healthId))
				.withSelfRel();

		return EntityModel.of(health, selfLink);
	}

	@DeleteMapping("/{healthId}")
	ResponseEntity<Object> deleteHealth(@PathVariable("healthId") long healthId){
		healthDataService.deleteHealth(healthId);

		return ResponseEntity.noContent().build();
	}
}
