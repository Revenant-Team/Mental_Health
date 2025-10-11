import { useState } from "react";
import Sidebar from "./components/Sidebar";
import Dashboard from "./components/Dashboard";
import Schedule from "./components/Schedule";
import Clients from "./components/Clients";

export default function App() {
  const [activeScreen, setActiveScreen] = useState("dashboard");

  const renderScreen = () => {
    switch (activeScreen) {
      case "dashboard": return <Dashboard />;
      case "schedule": return <Schedule />;
      case "clients": return <Clients />;
      default: return <Dashboard />;
    }
  };

  return (
    <div className="flex">
      <Sidebar active={activeScreen} setActive={setActiveScreen} />
      <div className="flex-1">{renderScreen()}</div>
    </div>
  );
}
