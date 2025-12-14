# **PiggyPilot – Budget Monitoring Web Application**

PiggyPilot is a full-stack personal finance application built with Spring Boot, Java, and MySQL, that enables users to set monthly budgets, log transactions, and track spending through a clean, data-driven dashboard.

---

## **Tech Stack**

* Backend: Java, Spring Boot (MVC)
* Frontend: HTML, CSS, JS, Thymeleaf
* Database: MySQL
* Tools: Figma, GitHub, Maven

---

## **Key Features**

* **User Authentication** – Account creation, login, profile updates
* **Budget Management** – Create/edit/delete categories with monthly limits
* **Transaction Tracking** – Filterable logs, remaining-budget calculations
* **Dashboard Overview** – Summaries of income, expenses, and category status
* **Data Validation** – Prevent overspending + duplicate categories per month

<img width="1165" height="900" alt="image" src="https://github.com/user-attachments/assets/5495daa4-bc60-41e5-9e91-294b1e385933" />

<img width="850" height="921" alt="image" src="https://github.com/user-attachments/assets/5f64194f-3b3d-43bc-ba89-eed5986edf6a" />
  
---

## Architecture

Built using a clear **Model–View–Controller** structure:

* **Model:** User, Budget, Transaction entities
* **View:** Thymeleaf templates + static assets
* **Controller:** Login + User controllers handling app flows

---

## Project Structure

```
/src
  /main
    /java/... (Controllers, Models)
    /resources
      /templates (HTML)
      /static (CSS/JS)
documentation/ (Report, Slides)

```

## Running the App

1. Clone the repo
2. Configure MySQL credentials in `application.properties`
3. Run via IDE or `mvn spring-boot:run`
4. Visit:

```
http://localhost:8080
```

## **Demo**

https://drive.google.com/drive/folders/1uWtxYySg_3qVUeslyRPF6yiNXlyYsyXG?usp=sharing

---

