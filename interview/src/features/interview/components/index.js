/**
 * Interview Feature - UI Layer Components
 * 
 * This folder contains the presentational components for the interview feature.
 * These are "dumb" components that receive all data via props and emit events.
 * They have NO business logic, state management, or API calls.
 * 
 * Architecture:
 * ┌─────────────────────────────────────┐
 * │         4-Layer React Architecture   │
 * ├─────────────────────────────────────┤
 * │ 1. UI Layer (THIS)                  │
 * │    - Pure presentational components  │
 * │    - Props-driven                    │
 * │    - No business logic               │
 * ├─────────────────────────────────────┤
 * │ 2. Hooks Layer                      │
 * │    - Custom React hooks              │
 * │    - Encapsulates component logic    │
 * │    - Composition pattern             │
 * ├─────────────────────────────────────┤
 * │ 3. State Layer                      │
 * │    - Context, Redux, Zustand, etc.  │
 * │    - Global state management        │
 * ├─────────────────────────────────────┤
 * │ 4. API Layer                        │
 * │    - Service/API calls              │
 * │    - Data fetching                  │
 * └─────────────────────────────────────┘
 * 
 * Components:
 * - Button.jsx: Reusable button with variants and states
 * - Card.jsx: Container component with styling options
 * - TextArea.jsx: Textarea input with character count and hints
 * - FileUpload.jsx: File upload input with visual feedback
 * 
 * Usage Example:
 * ```jsx
 * <Home
 *   jobDescription={state.jobDescription}
 *   onJobDescriptionChange={handleJobDescriptionChange}
 *   isLoading={isLoading}
 *   onGenerateReport={handleGenerateReport}
 * />
 * ```
 */

export { default as Button } from './Button';
export { default as Card } from './Card';
export { default as TextArea } from './TextArea';
export { default as FileUpload } from './FileUpload';
