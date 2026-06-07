import { useState, useEffect } from "react";
import { getAllReports } from "../services/interview.api";

export const useReports = () => {
  const [reports, setReports] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchReports();
  }, []);

  const fetchReports = async () => {
    setIsLoading(true);
    try {
        const data = await getAllReports();
        console.log("Reports data:", data); 
      setReports(data);
    } catch (err) {
      setError("Failed to load reports.");
      console.log(err);
    } finally {
      setIsLoading(false);
    }
  };

  return { reports, isLoading, error, fetchReports };
};
