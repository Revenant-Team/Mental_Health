import Header from "./Header";
import AnalyticsCard from "./AnalyticsCard";

export default function Dashboard() {
  return (
    <div className="flex-1 p-6 bg-gray-50 min-h-screen">
      <Header />
      <section className="mt-6 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <AnalyticsCard title="Today Appointments" value="5" icon="📅" color="blue" />
        <AnalyticsCard title="Pending Alerts" value="3" icon="⚠️" color="yellow" />
        <AnalyticsCard title="Active Clients" value="12" icon="👥" color="green" />
        <AnalyticsCard title="Completed Sessions" value="14" icon="✅" color="purple" />
      </section>
      <section className="mt-10">
        <h2 className="text-xl font-semibold text-gray-700 mb-4">Recent Activities</h2>
        <div className="bg-white shadow-md rounded-lg p-6">
          <p className="text-gray-500">No recent activities yet.</p>
        </div>
      </section>
    </div>
  );
}
