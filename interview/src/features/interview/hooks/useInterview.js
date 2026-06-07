import { useNavigate } from "react-router"
import { useState } from "react";
import { generateReport } from "../services/interview.api";


export const useInterview = () => {
    const navigate = useNavigate()

    const [jobDescription, setJobDescription] = useState("");
    const [selfDescription, setSelfDescription] = useState("");
    const [resumeFile, setResumeFile] = useState(null);
    
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleJobDescriptionChange = (e) => {
        setJobDescription(e.target.value)
    }

    const handleSelfDescriptionChange = (e) => {
        setSelfDescription(e.target.value)
    }

    const handleResumeFileSelect = (file) => {
        setResumeFile(file)
    }

    const handleGenerateReport = async () => {
        setIsLoading(true)
        setError(null)

        try {
            const report = await generateReport(
              selfDescription,
              jobDescription,
              resumeFile,
            );

            console.log("Report:", report);

            if (report && report.id) {
              navigate(`/interview/${report.id}`, { state: { report } });
            } else {
              setError("Report generated but missing ID. Check console.");
            }

        } catch (error) {
            setError("Failed to generate report. Please try again.");
            console.log("Error:", error);

            // check if rate limit error
    if (error.response?.data?.message?.includes("429")) {
        setError("Gemini AI is busy. Please wait 15 seconds and try again.");
    } else {
        setError("Failed to generate report. Please try again.");
    }
        }

        finally {
            setIsLoading(false)
        }
    }

    return {
        jobDescription,
        selfDescription,
        resumeFile,


        //handlers
        onJobDescriptionChange: handleJobDescriptionChange,
        onSelfDescriptionChange: handleSelfDescriptionChange,
        onResumeFileSelect: handleResumeFileSelect,
        onGenerateReport: handleGenerateReport,

        //ui
        isLoading,
        error
    }

}