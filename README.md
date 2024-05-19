# Word to PDF and PNG Converter

This project provides a web-based service for converting Word documents to PDF and PNG formats. It includes RESTful APIs for uploading Word files, converting them to PDF, and then converting the PDFs to PNG images. Additionally, it offers a file download endpoint to retrieve the converted files.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Contributing](#contributing)
- [License](#license)

## Introduction

The Word to PDF and PNG Converter allows users to upload Word documents and receive converted PDF and PNG files. This can be useful for scenarios where users need to share documents in multiple formats or visualize them as images.

## Features

- Upload Word documents
- Convert Word documents to PDF
- Convert PDF files to PNG images
- Download converted files
- Error handling for file conversion failures

## Technologies Used

- Java
- Spring Boot
- Apache PDFBox
- Apache POI
- Maven

## Installation

To run the project locally, follow these steps:

1. Clone the repository to your local machine.
2. Make sure you have Java and Maven installed.
3. Navigate to the project directory.
4. Run `mvn spring-boot:run` to start the Spring Boot application.
5. The application will be accessible at `http://localhost:8080`.

## Usage

1. Use any HTTP client (e.g., Postman) or web browser to interact with the provided RESTful endpoints.
2. Upload a Word document using the `/api/files/upload` 
3. Retrieve the converted PDF or PNG file using the `/api/files/download/{filename}` endpoint.
4. Optionally, integrate the file conversion endpoints into your own applications or workflows.

## Endpoints

- `POST /api/files/upload`: Upload a Word document.
- in header section add "key" = content-type and "value" = multipart/form-data (for upload)
- body type = form-data, key = "file" value = select file from the system. 
- `GET /api/files/download/{filename}`: Download the converted file by specifying its filename.


## Contributing

Contributions to this project are welcome! If you have any ideas for improvements, feature requests, or bug reports, please open an issue or submit a pull request on GitHub.

## License

