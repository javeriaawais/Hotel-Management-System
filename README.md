Hotel Management System 🏨

A robust and scalable desktop application developed as a core project for my Object-Oriented Programming (OOP) course. This system demonstrates a professional implementation of the Model-View-Controller (MVC) architectural pattern using Java 21 and JavaFX.

📌 Project Overview

The primary goal of this project was to bridge the gap between theoretical academic concepts and practical software engineering. By building a digitized solution for hotel operations, I focused on creating a user-friendly interface for managing room bookings, real-time status tracking, and guest information.

✨ Key Features

Real-Time Room Tracking: Monitor and update room statuses (Vacant/Occupied) instantly.

MVC Architecture: Strict separation between data logic (Model), UI (View), and flow control (Controller).

Flexible DAO Pattern: Supports swappable data sources. Currently includes:

Mock DAO: For immediate testing without database dependency.

MySQL DAO: For persistent storage in a production-like environment.

Modern UI/UX: Styled with custom CSS for a professional look and feel.

Reactive Data Binding: Uses JavaFX ObservableList and properties to ensure the UI stays synchronized with the data model.

🛠️ Technical Stack

Language: Java 21 (JDK 21)

GUI Framework: JavaFX (FXML + CSS)

Design Patterns: MVC, DAO, Dependency Injection

Build Tool: IntelliJ IDEA project structure

🚀 Getting Started

Prerequisites

Java Development Kit (JDK) 21

JavaFX SDK 21+

Running the Application

Clone the repository:

git clone [https://github.com/javeriaawais/Hotel-Management-System.git](https://github.com/javeriaawais/Hotel-Management-System.git)


Open in IntelliJ IDEA.

Configure Libraries:

Go to Project Structure > Libraries.

Add the lib folder of your JavaFX SDK.

Setup VM Options:
Add the following to your Run/Debug configuration to link the JavaFX modules:

--module-path "path/to/your/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml


Run HotelApp.java.

📂 Project Structure

com.hotel.app: Application entry point.

com.hotel.app.controller: UI event handling logic.

com.hotel.app.model: Data objects and business logic.

com.hotel.app.model.database: DAO interfaces and implementations (Mock & MySQL).

resources: FXML layouts and CSS stylesheets.

Developed with ❤️ as part of my computer engineering journey.
