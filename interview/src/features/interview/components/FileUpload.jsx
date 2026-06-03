import React from "react";
import "../styles/file-upload.scss";

const FileUpload = ({ label, fileType, onFileSelect, fileName, icon }) => {
  return (
    <div className="file-upload-container">
      <label htmlFor={fileType} className="file-upload-label">
        <div className="file-upload-icon">
          {icon || "📄"}
        </div>
        <div className="file-upload-text">
          <p className="upload-label">{label}</p>
          <p className="upload-hint">Click to upload</p>
        </div>
      </label>
      <input
        hidden
        type="file"
        id={fileType}
        name={fileType}
        accept=".pdf,.doc,.docx"
        onChange={(e) => onFileSelect?.(e.target.files?.[0])}
      />
      {fileName && (
        <div className="file-selected">
          <span className="file-name">✓ {fileName}</span>
        </div>
      )}
    </div>
  );
};

export default FileUpload;
