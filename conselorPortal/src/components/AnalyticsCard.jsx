export default function AnalyticsCard({ title, value, icon, color }) {
  return (
    <div className="bg-white shadow-md rounded-lg p-5 flex items-center gap-4 hover:shadow-xl transition">
      <div className={`p-3 rounded-full bg-${color}-100 text-${color}-600 text-2xl`}>{icon}</div>
      <div>
        <p className="text-sm text-gray-500">{title}</p>
        <p className="text-2xl font-semibold text-gray-800">{value}</p>
      </div>
    </div>
  );
}
