import api from "../../auth/services/axios";

export const generateReport = async (
  selfDescriptions,
  jobDescription,
  resumeFile,
) => {
 try {
   const formData = new FormData();
   formData.append("jobDescription", jobDescription);
   formData.append("selfDescription", selfDescription);
   if (resumeFile) {
     formData.append("resume", resumeFile);
   }

   const response = await api.post("/api/interview/generate", formData, {
     headers: {
       "Content-Type": "multipart/form-data",
     },
   });

   console.log("API response:", response.data); // ← add this
   return response.data;
 } catch (error) {
   console.log("API error:", error.response?.data); // ← add this
   throw error; // ← must rethrow so useInterview catches it
 }

  return response.data;
};

export const getAllReports = async () => {
  const response = await api.get("/api/interview/all");
  return response.data;
};

export const getReportById = async (id) => {
  const response = await api.get(`/api/interview/${id}`);
  return response.data;
};