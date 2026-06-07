import React, { useState } from 'react';
import '../styles/interview.scss';
import { useNavigate, useLocation } from 'react-router-dom';
import api from '../../auth/services/axios';

const Interview = ({ reportData = null }) => {
  const { state } = useLocation();
    const navigate = useNavigate();
    const [isDownloading, setIsDownloading] = useState(false);

  const [expandedSections, setExpandedSections] = useState({
    technicalQuestions: true,
    behavioralQuestions: false,
    preparationPlan: false,
  });

    const finalData = state?.report || reportData;

  const {
    matchScore = finalData.matchScore,
    technicalQuestions = finalData.technicalQuestions,
    behavioralQuestions = finalData.behavioralQuestions,
    skillGaps = finalData.skillGaps,
    preparationPlan = finalData.preparationPlan,
  } = finalData;

  if (!finalData) {
    return (
      <main className="interview">
        <div className="interview-container">
          <div className="error-state">
            <p>No report found</p>
          </div>
        </div>
      </main>
    );
  }

    const handleDownloadResume = async () => {
      setIsDownloading(true)
    try {
      const response = await api.get(
        `/api/interview/download-resume/${finalData._id?.$oid || finalData.id}`,
        { responseType: "blob" },
      );

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "resume.pdf");
      document.body.appendChild(link);
      link.click();
      link.remove();
      setIsDownloading(false);
    } catch (err) {
      console.log("Download error:", err);
    } finally {
      setIsDownloading(false);
    }
  };

  const toggleSection = (section) => {
    setExpandedSections(prev => ({
      ...prev,
      [section]: !prev[section]
    }));
  };

  const getSeverityBadgeClass = (severity) => {
    switch (severity?.toLowerCase()) {
      case 'high':
        return 'badge-high';
      case 'medium':
        return 'badge-medium';
      case 'low':
        return 'badge-low';
      default:
        return 'badge-low';
    }
  };

  const getScoreColor = (score) => {
    if (score >= 70) return '#1D9E75';
    if (score >= 50) return '#FFA500';
    return '#FF6B7A';
  };

  const strokeDasharray = () => 201;

  const strokeDashoffset = (score) => {
    return 201 - (score / 100) * 201;
  };

  return (
    <main className="interview">
      <div className="interview-container">
        {/* Header with Actions */}
        <div className="report-header card">
          <div className="header-left">
            <h1 className="report-title">Interview preparation report</h1>
            <p className="report-subtitle">
              Senior Backend Developer · Generated just now
            </p>
          </div>
          <div className="header-right">
            <button
              className="btn-download"
              onClick={handleDownloadResume}
              disabled={isDownloading}
            >
              {isDownloading ? (
                <>
                  <span className="spinner-small"></span> Downloading...
                </>
              ) : (
                <>
                  <i className="ti ti-download" aria-hidden="true"></i> Download
                  Resume
                </>
              )}
            </button>
            <button className="btn-regenerate" onClick={() => navigate("/")}>
              <i className="ti ti-refresh" aria-hidden="true"></i> Regenerate
            </button>
          </div>
        </div>

        {/* Match Score - Primary Focus */}
        <div className="metric-card card score-card">
          <div className="score-ring">
            <div className="ring-wrap">
              <svg width="80" height="80" viewBox="0 0 80 80">
                <circle
                  cx="40"
                  cy="40"
                  r="32"
                  fill="none"
                  stroke="rgba(255,255,255,0.1)"
                  strokeWidth="7"
                />
                <circle
                  cx="40"
                  cy="40"
                  r="32"
                  fill="none"
                  stroke={getScoreColor(matchScore)}
                  strokeWidth="7"
                  strokeDasharray={strokeDasharray()}
                  strokeDashoffset={strokeDashoffset(matchScore)}
                  strokeLinecap="round"
                  style={{ transition: "stroke-dashoffset 0.6s ease" }}
                />
              </svg>
              <div className="ring-label">{matchScore}%</div>
            </div>
            <div>
              <p className="metric-label">Match score</p>
              <p className="metric-subtitle">
                {matchScore >= 70
                  ? "Good fit for this role"
                  : "Some gaps to address"}
              </p>
            </div>
          </div>
        </div>

        {/* Quick Stats Grid */}
        <div className="metrics-grid">
          <div className="metric-card card">
            <p className="metric-label">Technical questions</p>
            <p className="metric-value">{technicalQuestions.length}</p>
          </div>
          <div className="metric-card card">
            <p className="metric-label">Behavioral questions</p>
            <p className="metric-value">{behavioralQuestions.length}</p>
          </div>
          <div className="metric-card card">
            <p className="metric-label">Preparation days</p>
            <p className="metric-value">{preparationPlan.length}</p>
          </div>
        </div>

        {/* Skill Gaps */}
        <div className="card">
          <p className="section-title">
            <i className="ti ti-chart-bar" aria-hidden="true"></i> Skill gaps
          </p>
          <div className="skill-gaps-list">
            {skillGaps.map((gap, idx) => (
              <div key={idx} className="skill-row">
                <span className="skill-name">{gap.skill}</span>
                <span
                  className={`badge ${getSeverityBadgeClass(gap.severity)}`}
                >
                  {gap.severity}
                </span>
              </div>
            ))}
          </div>
        </div>

        {/* Technical Questions */}
        <div className="card">
          <div
            className="section-header"
            onClick={() => toggleSection("technicalQuestions")}
          >
            <p className="section-title">
              <i className="ti ti-code" aria-hidden="true"></i> Technical
              questions
            </p>
            <span className="toggle-icon">
              {expandedSections.technicalQuestions ? "−" : "+"}
            </span>
          </div>

          {expandedSections.technicalQuestions && (
            <div className="questions-list">
              {technicalQuestions.map((q, idx) => (
                <div key={idx} className="q-card">
                  <p className="q-label">Question</p>
                  <p className="q-text">{q.question}</p>
                  <hr className="q-divider" />
                  <p className="q-label">Why they ask this</p>
                  <p className="q-text secondary">{q.intention}</p>
                  <hr className="q-divider" />
                  <p className="q-label">Ideal answer</p>
                  <p className="q-text secondary">{q.answer}</p>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Behavioral Questions */}
        <div className="card">
          <div
            className="section-header"
            onClick={() => toggleSection("behavioralQuestions")}
          >
            <p className="section-title">
              <i className="ti ti-user" aria-hidden="true"></i> Behavioral
              questions
            </p>
            <span className="toggle-icon">
              {expandedSections.behavioralQuestions ? "−" : "+"}
            </span>
          </div>

          {expandedSections.behavioralQuestions && (
            <div className="questions-list">
              {behavioralQuestions.map((q, idx) => (
                <div key={idx} className="q-card">
                  <p className="q-label">Question</p>
                  <p className="q-text">{q.question}</p>
                  <hr className="q-divider" />
                  <p className="q-label">Why they ask this</p>
                  <p className="q-text secondary">{q.intention}</p>
                  <hr className="q-divider" />
                  <p className="q-label">Ideal answer (STAR method)</p>
                  <p className="q-text secondary">{q.answer}</p>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Preparation Plan */}
        <div className="card">
          <div
            className="section-header"
            onClick={() => toggleSection("preparationPlan")}
          >
            <p className="section-title">
              <i className="ti ti-calendar" aria-hidden="true"></i>{" "}
              {preparationPlan.length}-day preparation plan
            </p>
            <span className="toggle-icon">
              {expandedSections.preparationPlan ? "−" : "+"}
            </span>
          </div>

          {expandedSections.preparationPlan && (
            <div className="preparation-list">
              {preparationPlan.map((day, idx) => (
                <div key={idx} className="day-section">
                  <div className="day-header">
                    <div className="day-badge">Day {day.day}</div>
                    <span className="day-focus">{day.focus}</span>
                  </div>
                  <div className="tasks">
                    {day.tasks.map((task, taskIdx) => (
                      <div key={taskIdx} className="task-item">
                        <div className="task-dot"></div>
                        <span>{task}</span>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </main>
  );
};

export default Interview;