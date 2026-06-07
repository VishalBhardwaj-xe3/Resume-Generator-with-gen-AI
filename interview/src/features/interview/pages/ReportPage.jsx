import React from "react";
import { useNavigate } from "react-router";
import { useReports } from "../hooks/useReport";
import "../styles/reports.scss";

const ReportsPage = () => {
  const { reports, isLoading, error } = useReports();
  const navigate = useNavigate();

  const calculateStats = () => {
    if (!reports || reports.length === 0) {
      return { totalReports: 0, avgScore: 0, totalQuestions: 0 };
    }

    const totalReports = reports.length;
    const avgScore = Math.round(
      reports.reduce((sum, r) => sum + (r.matchScore ?? 0), 0) / totalReports
    );
    const totalQuestions = reports.reduce(
      (sum, r) =>
        sum + (r.technicalQuestions?.length ?? 0) + (r.behavioralQuestions?.length ?? 0),
      0
    );

    return { totalReports, avgScore, totalQuestions };
  };

  const stats = calculateStats();

  const getScoreBadgeColor = (score) => {
    if (score >= 70) return "score-excellent";
    if (score >= 50) return "score-good";
    return "score-fair";
  };

  if (isLoading) {
    return (
      <main className="reports-page">
        <div className="reports-container">
          <div className="loading-state">
            <div className="spinner"></div>
            <p>Loading your reports...</p>
          </div>
        </div>
      </main>
    );
  }

  if (error) {
    return (
      <main className="reports-page">
        <div className="reports-container">
          <div className="error-state">
            <span className="error-icon">⚠️</span>
            <p>{error}</p>
            <button className="btn-retry" onClick={() => window.location.reload()}>
              Try Again
            </button>
          </div>
        </div>
      </main>
    );
  }

  return (
    <main className="reports-page">
      <div className="reports-container">
        {/* Header */}
        <section className="reports-header">
          <div>
            <h1 className="reports-title">Interview Reports</h1>
            <p className="reports-subtitle">
              {reports?.length || 0} {reports?.length === 1 ? "report" : "reports"} generated
            </p>
          </div>
          <button
            className="btn-new-report"
            onClick={() => navigate("/")}
          >
            <span className="btn-icon">+</span>
            New Report
          </button>
        </section>

        {/* Stats Overview */}
        {reports && reports.length > 0 && (
          <section className="stats-overview">
            <div className="stat-card">
              <div className="stat-icon">📊</div>
              <div className="stat-info">
                <p className="stat-label">Total Reports</p>
                <p className="stat-value">{stats.totalReports}</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon">🎯</div>
              <div className="stat-info">
                <p className="stat-label">Average Score</p>
                <p className="stat-value">{stats.avgScore}%</p>
              </div>
            </div>
            <div className="stat-card">
              <div className="stat-icon">❓</div>
              <div className="stat-info">
                <p className="stat-label">Questions Studied</p>
                <p className="stat-value">{stats.totalQuestions}</p>
              </div>
            </div>
          </section>
        )}

        {/* Empty state */}
        {!reports || reports.length === 0 ? (
          <section className="empty-state">
            <div className="empty-icon">📋</div>
            <h2>No reports yet</h2>
            <p>Generate your first interview report to get started with personalized preparation insights.</p>
            <button
              className="btn-get-started"
              onClick={() => navigate("/")}
            >
              Generate First Report
            </button>
          </section>
        ) : (
          /* Reports Grid */
          <section className="reports-grid">
            {reports.map((report) => (
              <div
                key={report._id?.$oid || report.id}
                className="report-card"
                onClick={() =>
                  navigate(`/interview/${report._id?.$oid || report.id}`, { state: { report } })
                }
              >
                {/* Score Badge */}
                <div className={`report-score ${getScoreBadgeColor(report.matchScore ?? 0)}`}>
                  <div className="score-circle">
                    <span className="score-number">{report.matchScore ?? 0}</span>
                    <span className="score-label">%</span>
                  </div>
                </div>

                {/* Report Content */}
                <div className="report-content">
                  {/* Title */}
                  <div className="report-header-info">
                    <h3 className="report-title-card">
                      {report.jobDescription
                        ? report.jobDescription.substring(0, 50) + "..."
                        : "Interview Report"}
                    </h3>
                    <span className="report-date">
                      {report.createdAt
                        ? new Date(report.createdAt).toLocaleDateString("en-US", {
                            month: "short",
                            day: "numeric",
                            year: "numeric"
                          })
                        : "Unknown date"}
                    </span>
                  </div>

                  {/* Stats */}
                  <div className="report-stats">
                    <div className="stat-item">
                      <span className="stat-icon">🎯</span>
                      <span className="stat-text">{report.technicalQuestions?.length ?? 0} Technical</span>
                    </div>
                    <div className="stat-item">
                      <span className="stat-icon">💬</span>
                      <span className="stat-text">{report.behavioralQuestions?.length ?? 0} Behavioral</span>
                    </div>
                    <div className="stat-item">
                      <span className="stat-icon">📅</span>
                      <span className="stat-text">{report.preparationPlan?.length ?? 0} Days</span>
                    </div>
                  </div>

                  {/* Skill Gaps Preview */}
                  {report.skillGaps && report.skillGaps.length > 0 && (
                    <div className="report-gaps">
                      {report.skillGaps.slice(0, 2).map((gap, i) => (
                        <span
                          key={i}
                          className={`skill-badge skill-badge-${gap.severity?.toLowerCase()}`}
                        >
                          {gap.skill}
                        </span>
                      ))}
                      {report.skillGaps.length > 2 && (
                        <span className="skill-badge-more">
                          +{report.skillGaps.length - 2} more
                        </span>
                      )}
                    </div>
                  )}
                </div>

                {/* Card Footer Arrow */}
                <div className="report-arrow">
                  <span>→</span>
                </div>
              </div>
            ))}
          </section>
        )}
      </div>
    </main>
  );
};

export default ReportsPage;
