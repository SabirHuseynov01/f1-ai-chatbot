# F1 AI Chatbot

A modern Spring Boot-based AI chatbot application designed to provide interactive conversations about Formula 1 racing. Built with Java 21 and Spring Boot 4.0.4, this project demonstrates clean architecture and best practices in web application development.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Usage](#usage)
- [Docker Support](#docker-support)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

F1 AI Chatbot is an intelligent conversational application built with Spring Boot that leverages modern web technologies to deliver a seamless user experience. The application integrates data persistence, dynamic web interfaces, and AI-powered interactions.

## ✨ Features

- **Interactive Chat Interface** - Real-time conversation with an AI assistant
- **Persistent Data Storage** - PostgreSQL integration for reliable data management
- **Dynamic Web UI** - Thymeleaf-powered responsive templates
- **RESTful API** - Clean API endpoints for chat interactions
- **Object-Relational Mapping** - Spring Data JPA for efficient database operations
- **JSON Processing** - Jackson for seamless data serialization
- **Container Support** - Docker and Docker Compose for easy deployment

## 🛠️ Tech Stack

### Backend
- **Java 21** - Latest LTS version with modern language features
- **Spring Boot 4.0.4** - Rapid application development framework
- **Spring Data JPA** - Object-relational mapping and persistence layer
- **Spring Web MVC** - RESTful web services framework

### Database
- **PostgreSQL** - Enterprise-grade relational database

### Frontend
- **Thymeleaf** - Server-side template engine for dynamic HTML

### Tools & Utilities
- **Gradle** - Build automation and dependency management
- **Lombok** - Reduces boilerplate code with annotations
- **Jackson** - JSON processing library
- **JUnit 5** - Testing framework

### Containerization
- **Docker** - Application containerization
- **Docker Compose** - Multi-container orchestration

## 📦 Prerequisites

Before running this application, ensure you have installed:

- **Java 21** or later
- **Gradle 8.0+** (or use the included Gradle wrapper)
- **Docker** and **Docker Compose** (optional, for containerized deployment)
- **PostgreSQL 14+** (if running locally without Docker)

## 🚀 Installation

### Option 1: Clone and Setup Locally

1. **Clone the repository**
   ```bash
   git clone https://github.com/SabirHuseynov01/f1-ai-chatbot.git
   cd f1-ai-chatbot
