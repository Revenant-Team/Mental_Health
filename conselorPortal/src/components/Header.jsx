export default function Header() {
  return (
    <div className="flex justify-between items-center p-4 bg-white shadow-sm">
      <h1 className="text-2xl font-semibold text-gray-800">Counselor Portal</h1>
      <div className="flex items-center gap-4">
        <button className="relative p-2 hover:bg-gray-100 rounded-full transition">
          🔔
          <span className="absolute -top-1 -right-1 w-2 h-2 bg-red-500 rounded-full"></span>
        </button>
        <div className="flex items-center gap-2 bg-gray-100 px-3 py-1 rounded-full">
          <span className="text-sm font-medium">Admin</span>
          <div className="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center text-white">A</div>
        </div>
      </div>
    </div>
  );
}
