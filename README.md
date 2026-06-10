# Intervi — AI Interview Preparation Assistant

A full-stack AI-powered interview preparation platform that analyzes your resume and job description to generate personalized interview questions, skill gap analysis, and a preparation plan — powered by Google Gemini AI.

---

## Live Demo

🔗 [interview-preparation-plan.vercel.app]

---

## Features

- **AI-powered report generation** — paste a job description and your background, Gemini AI generates a complete interview preparation report
- **JWT authentication** — secure register, login, logout with token blacklisting
- **Resume PDF generation** — generates a professional resume PDF from your profile data using Flying Saucer
- **File upload** — upload your resume for context-aware AI analysis
- **Interview reports history** — view all your past generated reports
- **Match score** — AI calculates how well your profile matches the job
- **Skill gap analysis** — identifies missing skills with severity levels
- **Technical & behavioral questions** — tailored questions with ideal answers
- **7-day preparation plan** — structured daily study plan

---

## Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 4 | Backend framework |
| Spring Security | Authentication & authorization |
| Spring Data MongoDB | Database ORM |
| JWT (jjwt 0.12.3) | Token-based authentication |
| Google Gemini AI | AI report generation |
| Flying Saucer + iText | HTML to PDF generation |
| Maven | Dependency management |

### Frontend
| Technology | Purpose |
|---|---|
| React 18 | UI framework |
| Vite | Build tool |
| Axios | HTTP client |
| React Router | Client-side routing |
| SCSS | Styling |

### Database & Services
| Technology | Purpose |
|---|---|
| MongoDB Atlas | Cloud database |
| Google Gemini API | AI text generation |

---

## Project Structure

```
intervi/
├── src/main/java/com/intervi/resume/
│   ├── controllers/
│   │   ├── AuthController.java
│   │   └── InterviewReportController.java
│   ├── models/
│   │   ├── User.java
│   │   ├── InterviewReport.java
│   │   └── BlackListToken.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── InterviewReportRepository.java
│   │   └── BlacklistedTokenRepository.java
│   ├── security/
│   │   ├── JwtUtil.java
│   │   ├── JwtFilter.java
│   │   └── SecurityConfig.java
│   ├── services/
│   │   ├── AIservices.java
│   │   ├── PdfService.java
│   │   └── FileStorageService.java
│   └── InterviApplication.java
├── src/main/resources/
│   └── application.properties.example
└── pom.xml
```

---

## Getting Started

### Prerequisites

- Java 21+
- Maven
- MongoDB Atlas account
- Google Gemini API key
- Node.js 18+ (for frontend)

### Backend Setup

1. Clone the repository

```bash
git clone https://github.com/yourusername/intervi.git
cd intervi
```

2. Create `application.properties` from the example file

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

3. Fill in your credentials in `application.properties`

```properties
spring.application.name=intervi
spring.mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/intervi_db
app.jwt.secret=your-secret-key-minimum-32-characters
app.jwt.expiration=86400000
gemini.api.key=your-gemini-api-key
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent
file.upload.dir=uploads
```

4. Run the backend

```bash
./mvnw spring-boot:run
```

Server starts on `http://localhost:8080`

### Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend starts on `http://localhost:5173`

---

## API Endpoints

### Auth
| Method | Endpoint | Description | Auth required |
|---|---|---|---|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login and get token | No |
| POST | `/api/auth/logout` | Logout and blacklist token | Yes |
| GET | `/api/auth/me` | Get logged in user details | Yes |

### Interview Reports
| Method | Endpoint | Description | Auth required |
|---|---|---|---|
| POST | `/api/interview/generate` | Generate AI report | Yes |
| GET | `/api/interview/all` | Get all user reports | Yes |
| GET | `/api/interview/{id}` | Get single report | Yes |
| GET | `/api/interview/download-resume/{id}` | Download resume PDF | Yes |

---

## How It Works

```
User submits job description + self description + resume
                    ↓
Spring Boot receives multipart form data
                    ↓
Gemini AI analyzes the data
                    ↓
Returns structured JSON with:
  - Match score (0-100%)
  - Technical questions + answers
  - Behavioral questions + answers
  - Skill gaps with severity
  - 7-day preparation plan
                    ↓
Saved to MongoDB Atlas
                    ↓
Displayed in React frontend
```


## Environment Variables

| Variable | Description |
|---|---|
| `MONGODB_URI` | MongoDB Atlas connection string |
| `JWT_SECRET` | Secret key for JWT signing (min 32 chars) |
| `JWT_EXPIRATION` | Token expiry in milliseconds (86400000 = 24h) |
| `GEMINI_API_KEY` | Google Gemini API key |
| `GEMINI_API_URL` | Gemini model endpoint URL |

---

## Author

**Vishal Bhardwaj**
- GitHub: [@VishalBhardwaj-xe3](https://github.com/VishalBhardwaj-xe3)
- Email: vishalbhardwaj0571@gmail.com
