/* components/BottomNav.jsx */
export default function BottomNav({ active, setActive }) {
  const navItems = [
    { id: "home", label: "Home", icon: "🏠" },
    { id: "schedule", label: "Schedule", icon: "📅" },
    { id: "clients", label: "Clients", icon: "👥" },
  ];

  return (
    <nav className="bg-white shadow-md border-t flex justify-around py-2">
      {navItems.map((item) => (
        <button
          key={item.id}
          onClick={() => setActive(item.id)}
          className={`flex flex-col items-center text-sm ${
            active === item.id ? "text-blue-600 font-semibold" : "text-gray-500"
          }`}
        >
          <span className="text-lg">{item.icon}</span>
          {item.label}
        </button>
      ))}
    </nav>
  );
}
