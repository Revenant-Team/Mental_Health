export default function Navbar({ active, setActive }) {
  const navItems = [
    { id: "home", label: "Home", icon: "🏠" },
    { id: "schedule", label: "Schedule", icon: "📅" },
    { id: "clients", label: "Clients", icon: "👥" },
  ];

  return (
    <nav className="bg-white shadow-md flex justify-center gap-10 py-4 sticky top-0 z-10">
      {navItems.map((item) => (
        <button
          key={item.id}
          onClick={() => setActive(item.id)}
          className={`flex items-center gap-2 text-sm font-medium transition-colors ${
            active === item.id
              ? "text-blue-600 border-b-2 border-blue-600 pb-1"
              : "text-gray-500 hover:text-blue-500"
          }`}
        >
          <span className="text-lg">{item.icon}</span>
          {item.label}
        </button>
      ))}
    </nav>
  );
}
