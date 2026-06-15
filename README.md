---

# 🚀 Centralized File Sharing Point

A lightweight, secure, and fast RESTful API built with **Spring Boot** that allows users to upload files and share them via highly secure, unpredictable custom download links.

---

## ✨ Features

* **Secure Code Generation:** Uses `SecureRandom` to generate unique, unguessable 10-character alphanumeric sharing keys (e.g., `aBcD1234eF`) to prevent URL manipulation attacks.
* **Collision Prevention:** Automatically renames physical files on disk to prevent files with identical names from overwriting each other.
* **Dynamic URL Building:** Generates ready-to-share URLs on the fly based on the active server environment.
* **Native File Handling:** Leverages Java NIO and Spring's `UrlResource` for smooth file streaming and downloads.

---

## 🛠️ Tech Stack

* **Backend:** Java 17+ / Spring Boot 3.x (Spring Web, Spring Data JPA)
* **Database:** H2 / MySQL / PostgreSQL (Any JPA compatible DB)
* **Boilerplate Reduction:** Lombok

---

## 🚀 Getting Started

### 1. Prerequisites

* Java 17 or 21 installed.
* Maven 3.6+ installed.

### 2. Configuration (`application.properties`)

Open `src/main/resources/application.properties` and configure your storage folder and optional file upload limits:

```properties
# App Storage Config
file.upload-dir=uploads

# Optional: Configure maximum file upload limits (Default is 1MB/10MB)
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

```

### 3. Run the Application

Navigate to the root directory of the project and execute:

```bash
mvn spring-boot:run

```

The server will boot up by default on `http://localhost:8080`.

---

## 📋 API Documentation

### 1. Upload a File

Takes a multipart file request, saves it physically to the storage directory, registers the metadata in the database, and returns a secure sharing link.

* **URL:** `/api/files/upload`
* **Method:** `POST`
* **Content-Type:** `multipart/form-data`
* **Body Parameter:**
* `file` (Type: File)



#### 📥 Example Response:

```json
{
  "status": "success",
  "fileName": "project_presentation.pptx",
  "downloadUrl": "http://localhost:8080/api/files/download/K8sD3aG7pQ",
  "type": "application/vnd.openxmlformats-officedocument.presentationml.presentation",
  "size": 4194304
}

```

---

### 2. Download a File

Fetches the file securely using the unique code generated during the upload phase. This endpoint forces a browser download attachment while preserving the original filename and content type.

* **URL:** `/api/files/download/{code}`
* **Method:** `GET`
* **URL Params:** `code=[string]` (e.g., `K8sD3aG7pQ`)

#### 📤 Example Usage:

Simply paste the `downloadUrl` received from the upload response directly into any browser search bar or a standard HTTP client to trigger the file download.

---

## 📁 Project Structure

```text
src/main/java/com/example/filesharing/
│
├── controller/
│   └── FileController.java        # Exposes Upload & Download endpoints
│
├── model/
│   └── FileEntity.java            # Database metadata architecture
│
├── repository/
│   └── FileRepository.java        # DB operations & lookup methods
│
├── service/
│   ├── FileStorageService.java    # Service interface
│   └── FileStorageServiceImpl.java# Disk operations & business logic
│
└── util/
    └── RandomCodeGenerator.java   # Cryptographically secure random URL key generator

```

---

## 🛡️ Security Considerations Included

> 1. **Path Traversal Protection:** The application cleans input paths (`StringUtils.cleanPath`) and validates against relative paths (`..`) to ensure users cannot maliciously access files outside the designated `uploads` folder.
> 2. **Unguessable IDs:** Database primary keys (`id`) are fully hidden from the frontend, replacing them with a sequence space of $62^{10}$ combinations to stop enumeration scripts.
> 
>
