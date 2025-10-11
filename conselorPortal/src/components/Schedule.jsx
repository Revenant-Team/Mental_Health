import { useState } from "react";
import Header from "./Header";

export default function Schedule() {
  const [tab, setTab] = useState("today");

  const tabs = [
    { id: "today", label: "Today" },
    { id: "upcoming", label: "Upcoming" },
    { id: "completed", label: "Completed" },
  ];

  const sessions = {
    today: [
      { name: "Aahana Tipnis", time: "11:00 AM", status: "Chatting", type: "🟣" },
      { name: "Aditi Wagle", time: "12:00 PM", status: "In-person", type: "🟡" },
    ],
    upcoming: [
      { name: "Virat Desai", time: "3:00 PM", status: "In-person", type: "🟡" },
      { name: "Adi Patel", time: "4:00 PM", status: "Video Call", type: "🔵" },
    ],
    completed: [
      { name: "Priya Mehta", time: "10:00 AM", status: "Completed", type: "🟢" },
    ],
  };

  return (
    <div className="flex-1 p-6 bg-gray-50 min-h-screen">
      <Header />
      
      {/* Tabs */}
      <div className="flex gap-4 border-b mb-6">
        {tabs.map((t) => (
          <button
            key={t.id}
            onClick={() => setTab(t.id)}
            className={`pb-2 text-sm font-medium transition-colors ${
              tab === t.id
                ? "border-b-2 border-blue-600 text-blue-600"
                : "text-gray-500 hover:text-blue-500"
            }`}
          >
            {t.label}
          </button>
        ))}
      </div>

      {/* Session Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {sessions[tab].map((s, idx) => (
          <div
            key={idx}
            className="bg-white shadow-md rounded-lg p-5 flex flex-col gap-3 hover:shadow-xl transition"
          >
            <div className="flex justify-between items-center">
              <h3 className="text-lg font-semibold text-gray-800">{s.name}</h3>
              <span
                className={`px-2 py-1 rounded-full text-xs font-medium ${
                  s.status === "Completed"
                    ? "bg-green-100 text-green-700"
                    : s.status === "Chatting"
                    ? "bg-purple-100 text-purple-700"
                    : "bg-yellow-100 text-yellow-700"
                }`}
              >
                {s.status}
              </span>
            </div>
            <p className="text-gray-500 flex items-center gap-2">
              <span>{s.type}</span> {s.status} session
            </p>
            <p className="text-sm font-semibold text-gray-700">{s.time}</p>
            <button className="mt-2 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition">
              View Details
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
