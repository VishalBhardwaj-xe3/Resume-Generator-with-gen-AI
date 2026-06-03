import React from "react";
import "../styles/card.scss";

const Card = ({ children, className, shadow = true, padding = true }) => {
  return (
    <div className={`card ${className || ""} ${shadow ? "shadow" : ""} ${padding ? "padded" : ""}`}>
      {children}
    </div>
  );
};

export default Card;
