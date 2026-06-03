import React from "react";
import "../styles/text-area.scss";

const TextArea = ({ 
  label, 
  placeholder, 
  value, 
  onChange, 
  hint, 
  maxLength,
  characterCount,
  id,
  name 
}) => {
  return (
    <div className="textarea-container">
      <div className="textarea-header">
        <label htmlFor={id} className="textarea-label">
          {label}
        </label>
        {hint && <span className="textarea-hint">{hint}</span>}
      </div>
      <textarea
        id={id}
        name={name}
        placeholder={placeholder}
        value={value}
        onChange={(e) => onChange?.(e.target.value)}
        maxLength={maxLength}
        className="textarea-input"
      />
      {maxLength && (
        <div className="character-count">
          {characterCount || 0} / {maxLength}
        </div>
      )}
    </div>
  );
};

export default TextArea;
