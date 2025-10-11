import Header from "./Header";

export default function Clients() {
  const clients = [
    { name: "Onkar Hadadare", email: "onkar05@gmail.com", phone: "+91 234 234 2345", status: "Active" },
    { name: "Meet Oza", email: "meethr30@gmail.com", phone: "+91 234 234 2345", status: "Inactive" },
    { name: "Mahesh Rajput", email: "mahesh11@gmail.com", phone: "+91 234 234 2345", status: "Active" },
    { name: "Vishal Gailkwad", email: "vishalash11@gmail.com", phone: "+91 234 234 2345", status: "Active" },
    { name: "Virat Kohli", email: "virat18@gmail.com", phone: "+91 234 234 2345", status: "Inactive" },
  ];

  return (
    <div className="flex-1 p-6 bg-gray-50 min-h-screen">
      <Header />

      {/* Search / Filter */}
      <div className="flex flex-col md:flex-row gap-4 mb-6">
        <select className="border rounded px-4 py-2 text-sm bg-white shadow-sm">
          <option>All Status</option>
          <option>Active</option>
          <option>Inactive</option>
        </select>
        <input
          type="text"
          placeholder="Search client"
          className="border rounded px-4 py-2 flex-1 text-sm bg-white shadow-sm"
        />
      </div>

      {/* Client Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {clients.map((client, idx) => (
          <div
            key={idx}
            className="bg-white shadow-md rounded-lg p-5 flex flex-col gap-3 hover:shadow-xl transition"
          >
            <div className="flex justify-between items-center">
              <h3 className="text-lg font-semibold text-gray-800">{client.name}</h3>
              <span
                className={`px-2 py-1 rounded-full text-xs font-medium ${
                  client.status === "Active" ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-700"
                }`}
              >
                {client.status}
              </span>
            </div>
            <p className="text-gray-500 text-sm">{client.email}</p>
            <p className="text-gray-500 text-sm">{client.phone}</p>
            <div className="flex gap-2 mt-3">
              <button className="flex-1 px-3 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition">View</button>
              <button className="flex-1 px-3 py-2 bg-gray-200 rounded hover:bg-gray-300 transition">Message</button>
              <button className="flex-1 px-3 py-2 bg-green-500 text-white rounded hover:bg-green-600 transition">Schedule</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
