# Bicycle Shop

This project is a bicycle customization platform where users can select different bicycle parts, view their choices, and see the real-time price of their customized bicycle.

## Project Overview

The platform includes:

- **Frontend**: A simple user interface that allows customers to choose from different parts of a bicycle (frame type, finish, wheels, etc.), and see the total price based on their selections.
- **Backend**: A RESTful API that serves the product catalog and calculates the total price based on user selections.
- **Data**: The bicycle parts and prices are stored in JSON format, allowing easy updates and extension of the catalog.

### Key Features

- Customizable bicycle parts (frame, wheels, finish, etc.).
- Dynamic pricing based on selected parts.
- Modular design to scale with new products (future-proof).
- Simple backend without external dependencies for lightweight use cases.

## Key Decisions and Trade-Offs

### 1. Backend and Frontend Separation

The project is designed with a **clear separation** between the backend (API) and the frontend (UI). The backend handles the business logic and data processing, while the frontend focuses on user interaction and presentation.

This separation:

- **Allows scalability**: New parts or categories can be added without modifying the frontend logic.
- **Enhances maintainability**: The backend can evolve independently of the frontend, making it easier to maintain or extend.

### 🔧 2. JSON-Based Data Storage

Initially, a single JSON file was considered for both product definitions and pricing rules. However, for **modularity and scalability**, the project uses two separate JSON files:

- **`products.json`**: Contains the structure of the available bicycle parts and their options.
- **`special_pricing.json`** (e.g., promotions, discounts) could be modularized further into their own files.

This approach allows for cleaner code and a more flexible architecture as the business grows and introduces new types of products or rules.

### 3. Manual JSON Parsing (No External Libraries)

To avoid unnecessary dependencies, the project uses manual JSON parsing with basic string operations. This avoids using heavy libraries which may be overkill for this small-scale use case. While not as robust as libraries, this decision keeps the application lightweight and simple.

### 4. Scalability Considerations

The current system is designed to scale both horizontally and vertically:

- **Modular data structure**: The use of separate files for parts and pricing logic ensures that the application can easily scale as more parts or product categories are added.
- **Easily extendable API**: The backend can easily be extended to support new endpoints for additional features, such as user accounts, order history, or detailed product information.

## System Architecture

The system consists of three main components:

### 1. **Frontend (UI)**

- **Description**: A simple form-based interface where users select different parts and options for their bicycle.
- **Key Features**:
  - Dynamically displays available parts and their options.
  - Sends user selections to the backend to calculate pricing.
  - Displays the total price based on the selected parts.

### 2. **Backend (API)**

- **Description**: A RESTful API that exposes endpoints for fetching product data and submitting bicycle customizations.
- **Key Features**:
  - `GET /products`: Returns the list of parts and their options with prices.
  - `POST /cart`: Accepts a selection of parts, calculates the price, and returns it in JSON format.
  - Logic to process user selections and calculate the total price dynamically based on the selected parts.

### 3. **Data (JSON Files)**

- **Description**: The product data (bicycle parts and options) is stored in JSON files. This modular structure ensures flexibility and scalability.
- **Structure**:
  - **`products.json`**: Contains information about the parts, options, and their prices.
  - **`special_pricing.json`**: Contains information about the special prices for specific parts.

## 💡 Future Improvements

### 1. Real-Time Bicycle Preview
A real-time visual preview of the bicycle could be displayed as users select different parts (frame, wheels, color). This would enhance the user experience by giving customers an immediate visual representation of their choices.

### 2. Expanded Product Range
As the business grows, the platform could easily expand to offer other sports-related products, such as skis, surfboards, or roller skates. The modular design of the backend and frontend allows for easy integration of new products without major changes.

### 3. Integration with Databases
Database Integration: Currently, the product catalog is stored in a JSON file. In a production environment, this could be replaced with a database (e.g., MySQL, PostgreSQL, MongoDB) for better scalability and performance.
Admin Interface: A future admin panel could be added to allow administrators to manage parts, prices, and inventory directly through a UI.

### 4. User Authentication and Orders
User Accounts: Implement user authentication and account management (e.g., login, registration).
Order Management: Add functionality to save users' custom bicycle configurations and allow them to place orders directly from the platform.

### 5. Admin Dashboard
An admin dashboard could be added to manage parts, prices, and inventory directly via a graphical interface. This would provide an easy way to update the catalog without needing to modify JSON files manually.