import React from "react";
import "../styles/home.scss";
import Card from "../components/Card";
import TextArea from "../components/TextArea";
import FileUpload from "../components/FileUpload";
import Button from "../components/Button";

/**
 * Home Page - UI Layer Component
 * Pure presentational component that receives all data and handlers via props
 * Follows 4-layer React architecture: UI (this layer) -> Hooks -> State -> API
 */
const Home = ({ 
  // Form data
  jobDescription = "",
  selfDescription = "",
  resumeFile = null,
  
  // Form handlers
  onJobDescriptionChange = () => {},
  onSelfDescriptionChange = () => {},
  onResumeFileSelect = () => {},
  onGenerateReport = () => {},
  
  // UI state
  isLoading = false,
  error = null
}) => {
  return (
    <main className="home">
      <div className="home-container">
        {/* Header */}
        <section className="home-header">
          <h1 className="home-title">Interview Preparation Assistant</h1>
          <p className="home-subtitle">
            Upload your resume and share the job description to get tailored interview insights
          </p>
        </section>

        {/* Error Message */}
        {error && (
          <div className="error-banner">
            <span className="error-icon">⚠️</span>
            <p>{error}</p>
          </div>
        )}

        {/* Main Content */}
        <div className="interview-input-group">
          {/* Left Panel */}
          <Card className="input-panel left-panel">
            <div className="panel-content">
              <TextArea
                id="jobDescription"
                name="jobDescription"
                label="Job Description"
                placeholder="Paste the job description here..."
                value={jobDescription}
                onChange={onJobDescriptionChange}
                hint="Required for best results"
                maxLength={5000}
                characterCount={jobDescription.length}
              />
            </div>
          </Card>

          {/* Right Panel */}
          <Card className="input-panel right-panel">
            <div className="panel-content">
              {/* Resume Section */}
              <div className="section">
                <h3 className="section-title">Resume</h3>
                <p className="section-hint">
                  Use resume and self description together for best results
                </p>
                <FileUpload
                  label="Upload Resume"
                  fileType="resume"
                  onFileSelect={onResumeFileSelect}
                  fileName={resumeFile?.name}
                  icon="📄"
                />
              </div>

              {/* Divider */}
              <div className="divider"></div>

              {/* Self Description Section */}
              <div className="section">
                <h3 className="section-title">Cover Letter / Self Description</h3>
                <TextArea
                  id="selfDescription"
                  name="selfDescription"
                  label="About You"
                  placeholder="Describe your skills, experience, and why you're interested in this role..."
                  value={selfDescription}
                  onChange={onSelfDescriptionChange}
                  maxLength={2000}
                  characterCount={selfDescription.length}
                />
              </div>

              {/* Action Button */}
              <div className="action-section">
                <Button
                  variant="primary"
                  size="large"
                  onClick={onGenerateReport}
                  disabled={!jobDescription.trim()}
                  loading={isLoading}
                  icon={isLoading ? "⏳" : "🚀"}
                >
                  {isLoading ? "Generating..." : "Generate Interview Report"}
                </Button>
              </div>
            </div>
          </Card>
        </div>
      </div>
    </main>
  );
};

export default Home;
