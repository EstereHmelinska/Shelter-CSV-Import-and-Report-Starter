package lv.bootcamp.shelter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReportExportService {

    public void writeReport(Path outputPath, ShelterReportData reportData) {

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {

            writer.write("Shelter Upload Report");
            writer.newLine();

            writer.write("Generated: " + LocalDate.now());
            writer.newLine();
            writer.newLine();

            writer.write("Imported: " + reportData.importResult().allAnimals().size());
            writer.newLine();

            writer.write("Skipped: " + reportData.importResult().skippedRows());
            writer.newLine();

            writer.write("Invalid rows: ");

            if (reportData.importResult().invalidRows().isEmpty()) {
                writer.write("None");
            } else {
                writer.write(
                        reportData.importResult()
                                .invalidRows()
                                .stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(", "))
                );
            }

            writer.newLine();
            writer.newLine();

            writer.write("Unique Species:");
            writer.newLine();

            for (String species : reportData.uniqueSpecies()) {
                writer.write("- " + species);
                writer.newLine();
            }

            writer.newLine();

            writer.write("Species Breakdown");
            writer.newLine();

            for (String species : reportData.uniqueSpecies()) {

                List<Animal> animals = reportData.animalsBySpecies().get(species);

                long total = animals.size();

                long vaccinated =
                        reportData.vaccinatedCounts().getOrDefault(species, 0L);

                writer.write(species);
                writer.newLine();

                writer.write("  Total: " + total);
                writer.newLine();

                writer.write("  Vaccinated: " + vaccinated);
                writer.newLine();

                Animal oldest = reportData.oldestAnimals().get(species);

                if (oldest != null) {
                    writer.write("  Oldest: "
                            + oldest.getName()
                            + " ("
                            + oldest.getAge()
                            + " years)");
                    writer.newLine();
                }

                writer.newLine();
            }

            writer.write("Needs Vet Input:");
            writer.newLine();
            writer.write(String.join(", ", reportData.animalsNeedingVetInput()));

            // JSON export
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();

            Path jsonPath = outputPath.resolveSibling(
                    outputPath.getFileName()
                            .toString()
                            .replace(".txt", ".json")
            );

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(jsonPath.toFile(), reportData);

        } catch (IOException e) {
            throw new RuntimeException("Failed to write report", e);
        }
    }
}