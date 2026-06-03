import React from "react";
import "../styles/button.scss";

const Button = ({ 
  children, 
  onClick, 
  variant = "primary", 
  size = "medium",
  disabled = false,
  loading = false,
  icon,
  type = "button"
}) => {
  return (
    <button
      type={type}
      className={`button button-${variant} button-${size}`}
      onClick={onClick}
      disabled={disabled || loading}
    >
      <span className="button-content">
        {loading && <span className="spinner"></span>}
        {icon && <span className="button-icon">{icon}</span>}
        {children}
      </span>
    </button>
  );
};

export default Button;
