package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.service.data.ImportResult;
import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.util.Comparator;

import java.util.*;
import java.util.stream.Collectors;

public class ShelterAnalyticsService {

    public ShelterReportData buildReportData(ImportResult importResult) {

        List<Animal> allAnimals = importResult.allAnimals();

        // Unique species (alphabetically sorted)
        Set<String> uniqueSpecies = allAnimals.stream()
                .map(Animal::getSpecies)
                .collect(Collectors.toCollection(TreeSet::new));

        // Group animals by species
        Map<String, List<Animal>> animalsBySpecies = allAnimals.stream()
                .collect(Collectors.groupingBy(Animal::getSpecies));

        // Animals needing vet input (unknown age)
        List<String> animalsNeedingVetInput = allAnimals.stream()
                .filter(a -> a.getAge() == null)
                .map(a -> a.getName() + "(" + a.getSpecies() + ")")
                .toList();
        Map<String, Long> vaccinatedCounts = allAnimals.stream()
                .filter(Animal::isVaccinated)
                .collect(Collectors.groupingBy(
                        Animal::getSpecies,
                        Collectors.counting()
                ));

        Map<String, Animal> oldestAnimals = allAnimals.stream()
                .filter(a -> a.getOptionalAge().isPresent())
                .collect(Collectors.groupingBy(
                        Animal::getSpecies,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(
                                        Comparator.comparing(a -> a.getOptionalAge().orElse(0))
                                ),
                                Optional::get
                        )
                ));

        return new ShelterReportData(
                importResult,
                uniqueSpecies,
                animalsBySpecies,
                animalsNeedingVetInput,
                vaccinatedCounts,
                oldestAnimals
        );
    }
}