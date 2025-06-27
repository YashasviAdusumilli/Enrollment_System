# 📚 Elective Course Registration System

This project is a **Java + Vert.x + MongoDB** based Elective Course Registration System with:

- 🧑‍🎓 Student registration with **SMTP email confirmation**
- 🔐 Login using email and password
- 📘 View available elective courses
- ✅ Register for one course only (seat count reduces)
- 🌐 Simple & clean HTML frontend with interactive UI

---

## ⚙️ Technologies Used

| Layer       | Stack                          |
|-------------|---------------------------------|
| Backend     | Java, Vert.x Web, Vert.x Mail, MongoDB Java Driver |
| Frontend    | HTML, CSS, JavaScript          |
| Database    | MongoDB (NoSQL)                |
| Email       | Gmail SMTP via Vert.x Mail     |

---

## 🚀 How to Run

### 1. Backend Setup
```bash
# Clone the repo
https://github.com/your-username/ElectiveCourseRegistration.git

# Open in IntelliJ and run MainApp.java

# Ensure MongoDB is running
```

### 2. Frontend Setup
```bash
# Go to frontEnd folder
cd frontEnd

# Open index.html in browser
```

---

## 📬 SMTP Email Setup
Use Gmail SMTP:
- Go to https://myaccount.google.com/apppasswords
- Enable 2FA and generate an **App Password**
- Replace in `EmailService.java`:
```java
.setUsername("your-email@gmail.com")
.setPassword("your-app-password")
```

---

## 🔗 API Endpoints
| Endpoint           | Method | Description                    |
|--------------------|--------|--------------------------------|
| `/register`        | POST   | Register student and send email|
| `/login`           | POST   | Login with email and password  |
| `/courses`         | GET    | Fetch available courses        |
| `/enroll`          | POST   | Register for an elective       |

---

## 👨‍💻 Author
[Yashasvi Adusumilli]
https://www.linkedin.com/in/yashasvi-adusumilli-756877292/

---

## ⭐ Star the repo if you like it!
