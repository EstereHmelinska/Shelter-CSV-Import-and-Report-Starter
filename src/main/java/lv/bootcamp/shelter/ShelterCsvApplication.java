package lv.bootcamp.shelter;

import lv.bootcamp.shelter.service.CsvImportService;
import lv.bootcamp.shelter.service.data.ImportResult;
import lv.bootcamp.shelter.service.ReportExportService;
import lv.bootcamp.shelter.service.ShelterAnalyticsService;
import lv.bootcamp.shelter.service.data.ShelterReportData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.nio.file.Path;

public class ShelterCsvApplication {

    public static void main(String[] args) {
        Path inputFolder = Path.of(
                "src",
                "main",
                "resources",
                "data"
        );
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

        Path outputPath = Path.of(
                "output",
                "upload-report-" + LocalDateTime.now().format(formatter) + ".txt"
        );

        CsvImportService importService = new CsvImportService();
        ShelterAnalyticsService analyticsService = new ShelterAnalyticsService();
        ReportExportService reportExportService = new ReportExportService();

        try {

            java.nio.file.Files.walk(inputFolder)
                    .filter(java.nio.file.Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".csv"))
                    .forEach(path -> {

                        ImportResult importResult =
                                importService.importAnimals(path);

                        ShelterReportData reportData =
                                analyticsService.buildReportData(importResult);

                        reportExportService.writeReport(outputPath, reportData);

                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
