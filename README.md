# Shelter CSV Import & Report

A Java application that imports animal shelter intake data from CSV files, validates the data, stores it using Java collections, performs analytics with Stream API, and generates a detailed report.

---

## Features

### CSV Import
- Reads shelter intake data from CSV files
- Uses UTF-8 encoding
- Skips the header row
- Validates every record before importing
- Logs invalid rows using SLF4J

### Validation
- Accepts blank ages as unknown (`null`)
- Rejects non-numeric age values
- Validates boolean vaccination values
- Parses dates using `dd.MM.yyyy`
- Tracks skipped rows and invalid row numbers

### Collections
Uses Java collections to organize imported data:

- `List<Animal>` – all imported animals
- `Set<String>` – unique species
- `Map<String, List<Animal>>` – animals grouped by species
- `List<String>` – animals requiring veterinary input

### Stream API Analytics
Generates statistics using Java Streams:

- Total imported animals
- Total skipped rows
- Unique species list
- Animals grouped by species
- Vaccinated animals per species
- Oldest animal per species
- Animals with unknown age

### Report Export
Generates a UTF-8 report containing:

- Report generation date
- Import statistics
- Invalid row numbers
- Unique species
- Per-species summary
- Vaccination counts
- Oldest animal per species
- Animals requiring veterinary input

---

## Stretch Goals Completed

- Alphabetically sorted species
- Used `Optional` for unknown animal ages
- Included invalid row numbers in the report
- Timestamped report filenames
- Support for processing multiple CSV files
---

## Author

Created by Estere Hmeļinska
