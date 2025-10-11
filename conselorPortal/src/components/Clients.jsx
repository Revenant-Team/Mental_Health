import Header from "./Header";

export default function Clients() {
  const clients = [
    { name: "Tanmay Shah", email: "tanmayshah@gmail.com", phone: "+91 234 234 2345" },
    { name: "Anshul Shetty", email: "anshulshetty@gmail.com", phone: "+91 234 234 2345" },
    { name: "Satyna Samanta", email: "satynasamanta@gmail.com", phone: "+91 234 234 2345" },
  ];

  return (
    <div>
      <Header title="Clients" />

      {/* Search / Filter */}
      <div className="flex gap-3 mb-6">
        <select className="border rounded px-3 py-2 text-sm bg-white shadow-sm">
          <option>Filter</option>
          <option>Active</option>
          <option>Inactive</option>
        </select>
        <input
          type="text"
          placeholder="Search client"
          className="border rounded px-3 py-2 flex-1 text-sm bg-white shadow-sm"
        />
      </div>

      {/* Client Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {clients.map((client, idx) => (
          <div
            key={idx}
            className="bg-white shadow rounded-lg p-5 flex flex-col gap-3 hover:shadow-md transition"
          >
            <div>
              <h3 className="font-semibold text-gray-800">{client.name}</h3>
              <p className="text-sm text-gray-500">{client.email}</p>
              <p className="text-sm text-gray-500">{client.phone}</p>
            </div>
            <div className="flex gap-2 mt-2">
              <button className="flex-1 px-3 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition">
                View
              </button>
              <button className="flex-1 px-3 py-2 bg-gray-200 rounded hover:bg-gray-300 transition">
                Message
              </button>
              <button className="flex-1 px-3 py-2 bg-green-500 text-white rounded hover:bg-green-600 transition">
                Schedule
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
