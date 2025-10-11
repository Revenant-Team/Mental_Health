import Header from "./Header";

export default function Home() {
  return (
    <div>
      <Header title="Dashboard" />

      {/* Alerts */}
      <section className="mb-6">
        <h2 className="text-lg font-semibold text-gray-700 mb-3">Critical Alerts</h2>
        <div className="bg-red-50 border border-red-200 rounded-lg p-5 flex justify-between items-center">
          <p className="text-red-600 font-medium">
            You have unresponded and unread messages
          </p>
          <button className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 transition">
            View
          </button>
        </div>
      </section>

      {/* Appointments */}
      <section>
        <h2 className="text-lg font-semibold text-gray-700 mb-4">Appointments</h2>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="bg-white shadow rounded-lg p-5 text-center hover:shadow-md transition">
            <p className="text-3xl font-bold text-gray-800">3</p>
            <p className="text-sm text-gray-500">Today</p>
          </div>
          <div className="bg-white shadow rounded-lg p-5 text-center hover:shadow-md transition">
            <p className="text-3xl font-bold text-gray-800">4</p>
            <p className="text-sm text-gray-500">Tomorrow</p>
          </div>
          <div className="bg-yellow-50 shadow rounded-lg p-5 text-center hover:shadow-md transition">
            <p className="text-3xl font-bold text-yellow-600">12</p>
            <p className="text-sm">Pending</p>
          </div>
          <div className="bg-green-50 shadow rounded-lg p-5 text-center hover:shadow-md transition">
            <p className="text-3xl font-bold text-green-600">14</p>
            <p className="text-sm">Completed</p>
          </div>
        </div>
      </section>
    </div>
  );
}
