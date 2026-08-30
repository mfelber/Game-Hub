# GameHub 🎮

GameHub is a full-stack web application designed as a central hub for discovering, managing, and building a personal game collection.

Users can browse games, add them to their library or wishlist, customize their profiles, and manage their accounts. The application also includes administration and moderation tools for managing users and platform content.

<p align="center">
  <img width="100%" alt="GameHub Home Page" src="./assets/store.png" />
</p>

---

## ✨ Features

### 👤 User Features

* User registration and authentication
* Browse and search for games
* Detailed game pages
* "Purchase" games and add them to a personal library
* Wishlist management
* Profile customization, including banners, profile pictures, and card colors
* Password reset functionality
* Email notifications upon registration, account bans, and suspensions
* And much more

### 🛠️ Administration & Moderation

* Administrative dashboard
* User management
* Report management
* User moderation tools
* Account bans and suspensions
* Moderation history and account status tracking

---

## 📸 GameHub in Action

### 🎮 Game details in Store

<p align="center">
<img width="100%" alt="Game details in the store" src="./assets/game-details.png" />
</p>

### 📚 Game Library

<p align="center">
<img width="100%" alt="User game library" src="./assets/library.png" />
</p>

### 🎨 Profile Customization

<p align="center">
<img width="100%" alt="User game library" src="./assets/Profile-customization.png" />
</p>

---

## 🛠️ Administration

### 📈 Admin Dashboard

<p align="center">
<img width="100%" alt="Dashboard" src="./assets/dashboard.png" />
</p>

### 🚨 Reports

<p align="center">
<img width="100%" alt="Reports" src="./assets/reports-page.png" />
</p>

---

## 🧩 Technical Features

* Persistent data storage using PostgreSQL
* REST API for communication between the frontend and backend
* Authentication and authorization
* Database migrations
* Email service integration
* Containerized development environment using Docker

---

## 🛠 Technologies Used

### Backend

* **Java 21**
* **Spring Boot**
* **Spring Data JPA**
* **PostgreSQL**
* **Maven**
* **Lombok**

### Frontend

* **Angular**
* **Tailwind CSS**

### Infrastructure

* **Docker**
* **Docker Compose**

---

## ▶️ Running the Application

### Prerequisites

Before running the application, make sure you have the following installed:

* Docker
* Docker Compose

### 🐳 Running with Docker Compose

1. Clone the repository and navigate to the Docker directory:

```bash
cd Game-Hub/game-Hub-be/docker
```

2. Start the application and its services:

```bash
docker-compose up
```

Once the containers are running, the application will be available at:

* **Frontend:** `http://localhost:4200/login`
* **Backend API:** `http://localhost:8088`

---

## 👥 Demo Accounts

Demo accounts are automatically created for development and testing purposes.

| Role              | Email               | Password   |
| ----------------- |---------------------|------------|
| 👤 User           | `marcel@marcel.com` | `heslo123` |
| 🛠️ Administrator | `admin@admin.com`   | `admin`    |



---

## 🚀 Planned Features

* 💬 Chat between system
* 👥 User-created groups
* 📱 Improved mobile responsive design
* 🚫 Blocking users
* 🔔 In-app notifications
* 📧💸 New Email notifications for example when games go on sale, banned reported user
* And much more 🚀
