# Interview Feature - 4-Layer React Architecture (UI Layer)

## Overview
This implementation follows the **4-Layer React Architecture** pattern, which separates concerns into distinct layers. Currently, only the **UI Layer** is implemented as requested.

## Architecture Layers

### 1. **UI Layer** ✅ (Implemented)
**Location:** `components/` folder and `pages/Home.jsx`

**Responsibility:** Pure presentational components that receive all data and callbacks via props.

**Characteristics:**
- ❌ No state management (useState, useReducer)
- ❌ No business logic
- ❌ No API calls
- ✅ Props-driven (all data comes from props)
- ✅ Event callbacks (all actions go through props)
- ✅ Fully reusable and testable
- ✅ Easy to style and maintain

**Components:**
- `Home.jsx` - Main page container (UI only)
- `Button.jsx` - Reusable button component with variants
- `Card.jsx` - Reusable card container
- `TextArea.jsx` - Textarea input with character count
- `FileUpload.jsx` - File upload input component

**Example:**
```jsx
<Home
  jobDescription={jobDescription}
  onJobDescriptionChange={setJobDescription}
  isLoading={isLoading}
  onGenerateReport={handleGenerateReport}
/>
```

---

### 2. **Hooks Layer** ⏳ (To be implemented)
**Location:** `hooks/` folder (create next)

**Responsibility:** Custom React hooks that encapsulate component logic and state management.

**Characteristics:**
- ✅ React hooks (useState, useEffect, useReducer)
- ✅ Component-level logic
- ❌ No direct API calls (delegated to API Layer)
- ✅ Reusable across components
- ✅ Custom composition patterns

**Example Structure:**
```
hooks/
├── useInterviewForm.js        // Form state and validation
├── useResumeUpload.js         // File upload logic
├── useApiCall.js              // Generic API hook
└── useDebounce.js             // Utility hooks
```

**Example Implementation:**
```jsx
// useInterviewForm.js
function useInterviewForm() {
  const [formData, setFormData] = useState({
    jobDescription: '',
    selfDescription: '',
    resumeFile: null
  });

  const updateField = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  return { formData, updateField };
}
```

---

### 3. **State Layer** ⏳ (To be implemented)
**Location:** `context/` or `store/` folder

**Responsibility:** Global state management using Context API, Redux, Zustand, etc.

**Characteristics:**
- ✅ Global application state
- ✅ Shared across multiple components
- ✅ Actions and reducers
- ❌ No API calls (delegated to API Layer)
- ✅ Time-travel debugging (if using Redux)

**Example Structure:**
```
context/
├── InterviewContext.js        // Global state provider
├── AuthContext.js             // Auth state
└── AppContext.js              // App-level state
```

**Example Implementation:**
```jsx
// context/InterviewContext.js
const InterviewContext = createContext();

export function InterviewProvider({ children }) {
  const [state, dispatch] = useReducer(interviewReducer, initialState);

  return (
    <InterviewContext.Provider value={{ state, dispatch }}>
      {children}
    </InterviewContext.Provider>
  );
}
```

---

### 4. **API Layer** ⏳ (To be implemented)
**Location:** `services/` folder

**Responsibility:** Handle all API/backend communication.

**Characteristics:**
- ✅ HTTP requests (fetch, axios)
- ✅ Data transformation
- ✅ Error handling
- ✅ Caching logic
- ✅ Request/response interceptors

**Example Structure:**
```
services/
├── api.js                     // HTTP client setup
├── interviewService.js        // Interview API calls
├── authService.js             // Auth API calls
└── fileService.js             // File upload API calls
```

**Example Implementation:**
```jsx
// services/interviewService.js
import { apiClient } from './api';

export const generateInterviewReport = async (data) => {
  const formData = new FormData();
  formData.append('jobDescription', data.jobDescription);
  formData.append('selfDescription', data.selfDescription);
  formData.append('resumeFile', data.resumeFile);

  const response = await apiClient.post('/api/interview/generate', formData);
  return response.data;
};
```

---

## Data Flow

```
User Interaction
      ↓
   UI Layer (Home.jsx)
      ↓ (via callback props)
   Hooks Layer (useInterviewForm)
      ↓ (useState, actions)
   State Layer (Context/Redux)
      ↓ (dispatch actions)
   API Layer (Service calls)
      ↓ (HTTP request)
   Backend API
      ↓ (response)
   API Layer (transform data)
      ↓
   State Layer (update state)
      ↓
   Hooks Layer (derived state)
      ↓
   UI Layer (re-render)
      ↓
   User sees updated UI
```

---

## File Structure

```
interview/
├── src/
│   └── features/
│       └── interview/
│           ├── components/         ✅ UI Layer (Implemented)
│           │   ├── Button.jsx
│           │   ├── Card.jsx
│           │   ├── TextArea.jsx
│           │   ├── FileUpload.jsx
│           │   └── index.js
│           ├── pages/              ✅ UI Layer (Implemented)
│           │   └── Home.jsx
│           ├── hooks/              ⏳ To implement
│           │   ├── useInterviewForm.js
│           │   ├── useResumeUpload.js
│           │   └── useApiCall.js
│           ├── context/            ⏳ To implement
│           │   └── InterviewContext.js
│           ├── services/           ⏳ To implement
│           │   ├── api.js
│           │   ├── interviewService.js
│           │   └── fileService.js
│           └── styles/             ✅ UI Layer (Implemented)
│               ├── button.scss
│               ├── card.scss
│               ├── textarea.scss
│               ├── file-upload.scss
│               └── home.scss
```

---

## Next Steps

### Step 1: Implement Hooks Layer
Create custom hooks to manage form state, file uploads, and API calls.

**Files to create:**
- `hooks/useInterviewForm.js` - Form state management
- `hooks/useResumeUpload.js` - File upload logic
- `hooks/useApiCall.js` - Generic hook for API calls

### Step 2: Implement State Layer
Set up global state management using Context API or Redux.

**Files to create:**
- `context/InterviewContext.js` - Global interview state
- `context/InterviewProvider.jsx` - Context provider wrapper

### Step 3: Implement API Layer
Create service functions for backend communication.

**Files to create:**
- `services/api.js` - HTTP client configuration
- `services/interviewService.js` - Interview API calls
- `services/fileService.js` - File upload API calls

### Step 4: Connect Everything
Update pages and components to use hooks, state, and API services.

---

## Usage Guidelines

### For UI Components
```jsx
// Always receive data via props
// Always emit events via callbacks
<Home
  jobDescription={data.jobDescription}
  selfDescription={data.selfDescription}
  resumeFile={data.resumeFile}
  onJobDescriptionChange={(value) => {}}
  onSelfDescriptionChange={(value) => {}}
  onResumeFileSelect={(file) => {}}
  onGenerateReport={() => {}}
  isLoading={false}
  error={null}
/>
```

### For Testing
Pure UI components are easy to test:
```jsx
test('Button renders correctly', () => {
  render(<Button onClick={mockFn}>Click me</Button>);
  expect(screen.getByText('Click me')).toBeInTheDocument();
});
```

---

## Benefits of 4-Layer Architecture

| Benefit | Explanation |
|---------|------------|
| **Testability** | Each layer can be tested independently |
| **Reusability** | Components are truly reusable |
| **Maintainability** | Clear separation of concerns |
| **Scalability** | Easy to add new features |
| **Performance** | Better optimization opportunities |
| **Team Collaboration** | Multiple developers can work on different layers |

---

## References

- [React Composition Pattern](https://react.dev/learn/passing-data-deeply-with-context)
- [Custom Hooks](https://react.dev/learn/reusing-logic-with-custom-hooks)
- [Context API](https://react.dev/reference/react/useContext)
- [React Best Practices](https://react.dev/learn)
