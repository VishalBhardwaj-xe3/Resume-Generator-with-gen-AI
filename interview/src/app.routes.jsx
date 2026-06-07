import { createBrowserRouter } from "react-router";
import Login from "./features/auth/pages/Login";
import Register from "./features/auth/pages/Register";
import Protected from "./features/auth/components/Protected";
import Home from "./features/interview/pages/Home";
import Interview from "./features/interview/pages/Interview";
import ReportsPage from "./features/interview/pages/ReportPage";


export const router = createBrowserRouter([
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/register",
    element: <Register />,
  },
  {
    path: "/",
    element:
      <Protected>
        <Home />
      </Protected>
  },
  {
    path: "/interview/:interview",
    element: <Protected>
      <Interview />
    </Protected>
  },
  {
    path: "/reports",
    element: 
        <Protected>
            <ReportsPage />
      </Protected>
  }
]);