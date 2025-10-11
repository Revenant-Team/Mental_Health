import { useState } from "react";
import Logo from "./Logo";

export default function Sidebar({ active, setActive }) {
  const navItems = [
    { id: "dashboard", label: "Dashboard", icon: "🏠" },
    { id: "schedule", label: "Schedule", icon: "📅" },
    { id: "clients", label: "Clients", icon: "👥" },
  ];

  return (
    <div className="w-64 bg-white shadow-lg min-h-screen flex flex-col">
      <div className="p-6 border-b">
        <Logo />
      </div>
      <nav className="flex-1 mt-6">
        {navItems.map((item) => (
          <button
            key={item.id}
            onClick={() => setActive(item.id)}
            className={`flex items-center gap-4 w-full px-6 py-3 text-gray-700 hover:bg-blue-50 transition rounded-l-full ${
              active === item.id ? "bg-blue-50 font-semibold text-blue-600" : ""
            }`}
          >
            <span className="text-lg">{item.icon}</span>
            <span className="text-sm">{item.label}</span>
          </button>
        ))}
      </nav>
    </div>
  );
}
