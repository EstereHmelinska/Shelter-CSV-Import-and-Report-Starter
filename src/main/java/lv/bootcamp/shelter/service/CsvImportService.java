package lv.bootcamp.shelter.service;

import lombok.extern.slf4j.Slf4j;
import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.service.data.ImportResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvImportService {

    public ImportResult importAnimals(Path inputPath) {

        log.info("Starting import from {}", inputPath);

        List<Animal> allAnimals = new ArrayList<>();
        List<Integer> invalidRows = new ArrayList<>();
        int skippedRows = 0;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        try {

            List<String> lines = Files.readAllLines(inputPath, StandardCharsets.UTF_8);

            // Skip header
            for (int i = 1; i < lines.size(); i++) {

                String line = lines.get(i);
                String[] values = line.split(",");

                // Check correct number of columns
                if (values.length != 5) {
                    skippedRows++;
                    invalidRows.add(i + 1);
                    log.warn("Skipping malformed row {}: {}", i + 1, line);
                    continue;
                }

                String name = values[0].trim();
                String species = values[1].trim();
                String ageText = values[2].trim();
                String vaccinatedText = values[3].trim();
                String dateText = values[4].trim();

                // Parse age
                Integer age = null;

                if (!ageText.isBlank()) {
                    try {
                        age = Integer.parseInt(ageText);
                    } catch (NumberFormatException e) {
                        skippedRows++;
                        invalidRows.add(i + 1);
                        log.warn("Invalid age at row {}: {}", i + 1, ageText);
                        continue;
                    }
                }
                if (!vaccinatedText.equalsIgnoreCase("true")
                        && !vaccinatedText.equalsIgnoreCase("false")) {

                    skippedRows++;
                    invalidRows.add(i + 1);
                    log.warn("Invalid vaccinated value at row {}: {}", i + 1, vaccinatedText);
                    continue;
                }

                // Parse vaccination
                boolean vaccinated = Boolean.parseBoolean(vaccinatedText);

                // Parse date
                LocalDate intakeDate;

                try {
                    intakeDate = LocalDate.parse(dateText, formatter);
                } catch (DateTimeParseException e) {
                    skippedRows++;
                    invalidRows.add(i + 1);
                    log.warn("Invalid date at row {}: {}", i + 1, dateText);
                    continue;
                }

                Animal animal = new Animal(
                        name,
                        species,
                        age,
                        vaccinated,
                        intakeDate
                );

                allAnimals.add(animal);
            }

        } catch (IOException e) {
            log.error("Failed to read file {}", inputPath, e);
        }

        log.info("Imported {} animals. Skipped {} rows.", allAnimals.size(), skippedRows);

        return new ImportResult(allAnimals, skippedRows, invalidRows);
    }
}