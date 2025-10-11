import React, { useState } from "react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  Tooltip,
  Legend,
} from "recharts";
import {
  BarChart3,
  Users,
  Brain,
  FileText,
  Bell,
  Settings,
  Shield,
  Monitor,
  Download,
  User,
  ChevronDown,
  AlertTriangle,
  Calendar,
  Info,
} from "lucide-react";
import { motion } from "framer-motion";

const StudentPortalDashboard = () => {
  const [activeTab, setActiveTab] = useState("Analytics Dashboard");
  const [currentSem] = useState("Current Sem...");
  const [, setSelectedAlert] = useState(null);

  // Data
  const wellnessData = [
    { name: "Aug", "PHQ-9": 8, "GAD-7": 6 },
    { name: "Sep", "PHQ-9": 7, "GAD-7": 5 },
    { name: "Oct", "PHQ-9": 6, "GAD-7": 4 },
    { name: "Nov", "PHQ-9": 5, "GAD-7": 4 },
    { name: "Dec", "PHQ-9": 4, "GAD-7": 3 },
  ];

  const stressData = [
    { name: "Event Stress", value: 35, color: "#ef4444" },
    { name: "Sleep", value: 25, color: "#10b981" },
    { name: "Social", value: 20, color: "#3b82f6" },
    { name: "Personal", value: 15, color: "#f59e0b" },
    { name: "Other", value: 5, color: "#8b5cf6" },
  ];

  const departmentData = [
    { name: "Computer Science", score: 6.8, color: "#3b82f6", avgText: "Avg PHQ-9" },
    { name: "Mechanical", score: 5.9, color: "#10b981", avgText: "Avg PHQ-9" },
    { name: "Electronics", score: 6.3, color: "#8b5cf6", avgText: "Avg PHQ-9" },
    { name: "Civil", score: 5.7, color: "#f97316", avgText: "Avg PHQ-9" },
  ];

  const menuItems = [
    { icon: BarChart3, label: "Analytics Dashboard", active: true },
    { icon: Users, label: "Peer Platform Insights" },
    { icon: Brain, label: "AI Recommendations" },
    { icon: FileText, label: "Reports & Exports" },
  ];

  const quickAccess = [
    { icon: Bell, label: "Alert Center" },
    { icon: Settings, label: "Privacy Settings" },
    { icon: Monitor, label: "System Config" },
  ];

  const recentAlerts = [
    {
      type: "danger",
      icon: AlertTriangle,
      title: "High-Risk Keywords Detected",
      subtitle: "3 users escalated in last 24h",
      bgColor: "bg-red-50",
      iconColor: "text-red-500",
    },
    {
      type: "warning",
      icon: Calendar,
      title: "Exam Stress Spike",
      subtitle: "45% increase in Engineering",
      bgColor: "bg-yellow-50",
      iconColor: "text-yellow-600",
    },
    {
      type: "info",
      icon: Info,
      title: "Low Engagement",
      subtitle: "Batch C needs attention",
      bgColor: "bg-blue-50",
      iconColor: "text-blue-500",
    },
  ];

  // Utility: StatCard component
  const StatCard = ({ icon: Icon, value, label, trend, trendColor }) => (
    <motion.div
      whileHover={{ scale: 1.02 }}
      className="bg-white p-5 rounded-2xl shadow-sm cursor-pointer border border-gray-100"
    >
      <div className="flex items-center justify-between">
        <div className="w-10 h-10 flex items-center justify-center rounded-full bg-gray-50">
          <Icon className="w-5 h-5 text-blue-600" />
        </div>
        <span className={`text-xs font-medium ${trendColor}`}>{trend}</span>
      </div>
      <div className="mt-3 text-3xl font-bold text-gray-900">{value}</div>
      <p className="text-sm text-gray-600">{label}</p>
    </motion.div>
  );

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Sidebar */}
      <div className="w-64 bg-white shadow-sm border-r border-gray-100 flex flex-col">
        <div className="p-4">
          <h1 className="text-lg font-semibold text-gray-900">Institute Portal</h1>
          <p className="text-sm text-gray-500">Student Wellness Analytics</p>
        </div>

        <nav className="mt-4 flex-1">
          {menuItems.map((item, index) => (
            <button
              key={index}
              onClick={() => setActiveTab(item.label)}
              className={`w-full flex items-center px-4 py-3 text-sm transition-colors ${
                activeTab === item.label
                  ? "text-blue-600 border-l-4 border-blue-600 bg-blue-50"
                  : "text-gray-600 hover:bg-gray-50"
              }`}
            >
              <item.icon className="w-4 h-4 mr-3" />
              {item.label}
            </button>
          ))}
        </nav>

        <div className="p-4">
          <h3 className="text-xs font-medium text-gray-500 uppercase mb-2">Quick Access</h3>
          {quickAccess.map((item, index) => {
            const Icon = item.icon;
            return (
              <button
                key={index}
                className="w-full flex items-center px-4 py-2 text-sm text-gray-600 hover:bg-gray-50 rounded"
              >
                <Icon className="w-4 h-4 mr-3" /> {item.label}
              </button>
            );
          })}
        </div>

        <div className="p-4 border-t border-gray-100 text-xs text-gray-500">
          <Shield className="w-4 h-4 inline-block text-green-500 mr-1" />
          Privacy Compliant • AI Safe • GDPR
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1">
        {/* Header */}
        <div className="bg-white border-b border-gray-100 px-6 py-4 flex items-center justify-between">
          <div className="flex items-center">
            <h2 className="text-xl font-semibold text-gray-900">{activeTab}</h2>
            <span className="ml-4 text-sm text-gray-500">Updated: 1 mins ago</span>
          </div>
          <div className="flex items-center space-x-3">
            <button className="flex items-center px-3 py-1 bg-gray-100 rounded text-sm">
              📊 {currentSem}
              <ChevronDown className="w-4 h-4 ml-1" />
            </button>
            <button className="flex items-center px-4 py-1.5 bg-blue-600 text-white text-sm rounded hover:bg-blue-700 transition">
              <Download className="w-4 h-4 mr-2" /> Export
            </button>
            <div className="flex items-center">
              <div className="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center mr-2">
                <User className="w-4 h-4 text-white" />
              </div>
              <span className="text-sm text-gray-700">Mr. Suraj Shingade</span>
            </div>
          </div>
        </div>

        {/* Dashboard Content */}
        <div className="p-6 space-y-6">
          {/* Stat Cards */}
          <div className="grid grid-cols-4 gap-4">
            <StatCard icon={Users} value="87.3%" label="Students Screened" trend="+12%" trendColor="text-green-600" />
            <StatCard icon={BarChart3} value="6.2" label="Avg PHQ-9 Score" trend="-8%" trendColor="text-red-600" />
            <StatCard icon={Brain} value="34" label="Active Sessions" trend="+23%" trendColor="text-green-600" />
            <StatCard icon={FileText} value="73.8%" label="Engagement Rate" trend="+15%" trendColor="text-green-600" />
          </div>

          {/* Charts */}
          <div className="grid grid-cols-2 gap-6">
            {/* Wellness Trends */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.4 }}
              className="bg-white p-5 rounded-2xl shadow-sm"
            >
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Wellness Trends</h3>
              <ResponsiveContainer width="100%" height={220}>
                <LineChart data={wellnessData}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                  <XAxis dataKey="name" tick={{ fontSize: 12, fill: "#6b7280" }} />
                  <YAxis domain={[0, 10]} tick={{ fontSize: 12, fill: "#6b7280" }} />
                  <Tooltip />
                  <Line type="monotone" dataKey="PHQ-9" stroke="#3b82f6" strokeWidth={2} />
                  <Line type="monotone" dataKey="GAD-7" stroke="#10b981" strokeWidth={2} />
                  <Legend />
                </LineChart>
              </ResponsiveContainer>
            </motion.div>

            {/* Stress Distribution */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.4, delay: 0.1 }}
              className="bg-white p-5 rounded-2xl shadow-sm"
            >
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Stress Distribution</h3>
              <div className="flex items-center">
                <ResponsiveContainer width="100%" height={220}>
                  <PieChart>
                    <Pie
                      data={stressData}
                      cx="50%"
                      cy="50%"
                      innerRadius={40}
                      outerRadius={80}
                      paddingAngle={2}
                      dataKey="value"
                    >
                      {stressData.map((entry, index) => (
                        <Cell key={index} fill={entry.color} />
                      ))}
                    </Pie>
                    <Tooltip formatter={(value) => `${value}%`} />
                  </PieChart>
                </ResponsiveContainer>
                <div className="w-32 space-y-2 ml-4">
                  {stressData.map((item, index) => (
                    <div key={index} className="flex items-center text-xs">
                      <div className="w-3 h-3 rounded-sm mr-2" style={{ backgroundColor: item.color }}></div>
                      <span className="text-gray-700">{item.name}</span>
                    </div>
                  ))}
                </div>
              </div>
            </motion.div>
          </div>

          {/* Department Breakdown + Alerts */}
          <div className="grid grid-cols-2 gap-6">
            {/* Department */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.4, delay: 0.2 }}
              className="bg-white p-5 rounded-2xl shadow-sm"
            >
              <div className="flex items-center justify-between mb-4">
                <h3 className="text-lg font-semibold text-gray-900">Department Breakdown</h3>
                <button className="text-blue-600 text-sm font-medium">View All</button>
              </div>
              <div className="space-y-3">
                {departmentData.map((dept, index) => (
                  <div
                    key={index}
                    className="flex items-center justify-between py-2 hover:bg-gray-50 rounded px-2 cursor-pointer transition-colors"
                  >
                    <div className="flex items-center">
                      <div className="w-3 h-3 rounded-full mr-3" style={{ backgroundColor: dept.color }}></div>
                      <span className="text-sm font-medium text-gray-900">{dept.name}</span>
                    </div>
                    <div className="text-right">
                      <div className="text-sm font-semibold text-gray-900">{dept.score}</div>
                      <div className="text-xs text-gray-500">{dept.avgText}</div>
                    </div>
                  </div>
                ))}
              </div>
            </motion.div>

            {/* Alerts */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.4, delay: 0.3 }}
              className="bg-white p-5 rounded-2xl shadow-sm"
            >
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Recent Alerts</h3>
              <div className="space-y-3">
                {recentAlerts.map((alert, index) => (
                  <div
                    key={index}
                    className={`flex items-start p-3 rounded-lg cursor-pointer hover:shadow-sm transition-all ${alert.bgColor}`}
                    onClick={() => setSelectedAlert(alert)}
                  >
                    <alert.icon className={`w-4 h-4 mr-3 ${alert.iconColor}`} />
                    <div>
                      <p className="text-sm font-medium text-gray-900">{alert.title}</p>
                      <p className="text-xs text-gray-600">{alert.subtitle}</p>
                    </div>
                  </div>
                ))}
              </div>
            </motion.div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default StudentPortalDashboard;
