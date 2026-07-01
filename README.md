# Shelter CSV Import and Report

A Java application that imports animal shelter intake data from a CSV file, validates the data, stores it using Java collections, performs analytics with the Stream API, and generates a report.

## Features

- Import shelter intake data from CSV
- Validate imported data
- Skip invalid rows and log warnings using SLF4J
- Store data using `List`, `Set`, and `Map`
- Generate statistics using Java Streams
- Export a UTF-8 report

## Validation

- Accept blank ages as unknown (`null`)
- Reject non-numeric ages
- Validate vaccination values
- Parse dates using `dd.MM.yyyy`
- Track skipped rows and invalid row numbers

## Collections Used

- `List<Animal>` – imported animals
- `Set<String>` – unique species
- `Map<String, List<Animal>>` – animals grouped by species
- `List<String>` – animals requiring veterinary input

## Stretch Goals

- Alphabetically sorted species
- Used `Optional` for unknown ages
- Included invalid row numbers in the report
- Timestamped report filenames
- Support for processing multiple CSV files

## Author

Created by Estere Hmeļinska.
