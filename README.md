# 🐷 PiggyPilot – Financial Data Tracker & Dashboard

### A full-stack budget monitoring system designed to promote financial discipline through data tracking and visualization.

**PiggyPilot** helps users manage their personal finances by allowing them to set monthly budget caps, log transaction data, and visualize their spending habits through a real-time dashboard. Built with a robust **relational database (MySQL)** and a Spring Boot backend.

---

## 🏛️ System Architecture (MVC)
The application follows a strict **Model-View-Controller** pattern to ensure separation of concerns:
* **Model:** Handles data logic for `Users`, `Budgets`, and `Transactions`.
* **View:** Renders dynamic interfaces using **Thymeleaf**.
* **Controller:** Manages data flow between the MySQL database and the frontend.

---

## 🛠️ Tech Stack
<p align="left">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-003B57?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white" />
  <img src="https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white" />
<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white" />
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black" />
</p>

---
## 📊 Core Features (Business Logic)

### 1. Financial Data Management
* **Income & Budget Allocation:** Users define a "Total Amount" (Income) which serves as the baseline for the month.
* **Expense Logging:** Transaction data is captured and linked to specific budget categories (e.g., Food, Transport).

### 2. Real-Time Analytics Dashboard
* **Automated Calculations:**
    * **Savings:** `Total Amount - Sum of Budgets`
    * **Budget Left:** `Budget Amount - Sum of Transactions`
* **Status Indicators:** Visual cues alert users when expenses approach the set budget limits.

### 3. Data Integrity & Constraints (Validation)
* **Temporal Locking:** To preserve historical data accuracy, users can **only add, update, or delete records for the current month**. [cite_start]Past months are locked for editing.
***One-Time Baseline:** The "Total Amount" (Income) can only be set once per profile to ensure consistency.
***Secure Authentication:** Users must re-authenticate with new credentials immediately after updating their email or password.

---

## 📸 Application Preview

<p align="center">
<img width="48%" alt="Dashboard View" src="https://github.com/user-attachments/assets/5495daa4-bc60-41e5-9e91-294b1e385933" />
<img width="48%" alt="Transaction Log" src="https://github.com/user-attachments/assets/5f64194f-3b3d-43bc-ba89-eed5986edf6a" />
</p>

---

## 📂 Database & Project Structure

The project is structured to ensure scalability and clean code management:

```bash
/src
  ├── /main/java/com/piggypilot
  │     ├── /controller    # Handles API requests & Page routing
  │     ├── /model         # Entity classes (User, Budget, Transaction)
  │     ├── /repository    # JPA interfaces for MySQL interaction
  │     └── /service       # Business logic & Calculations
  ├── /resources
  │     ├── /templates     # Thymeleaf HTML views
  │     └── /static        # CSS & JavaScript assets
