import React, { useState } from 'react';
import { 
  LayoutDashboard, 
  UserPlus, 
  Users, 
  Briefcase, 
  Clock, 
  Settings, 
  Bell, 
  ChevronDown, 
  UserCog, 
  Filter, 
  RotateCcw, 
  BarChart3, 
  PieChart, 
  Download, 
  AlertTriangle, 
  CheckCircle2, 
  ChevronLeft, 
  ChevronRight,
  ExternalLink,
  ArrowLeft,
  GraduationCap
} from 'lucide-react';

const App = () => {
  const [activeTab, setActiveTab] = useState('Workload');
  const [nameSearch, setNameSearch] = useState('');
  const [majorFilter, setMajorFilter] = useState('All Majors');

  // 模拟 TA 数据 (基于草图内容翻译)
  const workloadData = [
    { id: 1, name: "Zhang San", studentId: "2021001234", major: "Software", positions: 2, hours: 8, status: "Normal" },
    { id: 2, name: "Li Si", studentId: "2021002345", major: "Computer Science", positions: 3, hours: 12, status: "High" },
    { id: 3, name: "Wang Wu", studentId: "2021003456", major: "Software", positions: 1, hours: 4, status: "Normal" },
    { id: 4, name: "Zhao Liu", studentId: "2021004567", major: "Comm. Engineering", positions: 0, hours: 0, status: "Not Applied" },
  ];

  const menuItems = [
    { id: 'Dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'CreateMO', label: 'Create MO Account', icon: UserPlus },
    { id: 'AllMOs', label: 'All MOs', icon: Users },
    { id: 'AllPostings', label: 'All Positions', icon: Briefcase },
    { id: 'Workload', label: 'All TA Workload', icon: Clock },
    { id: 'Settings', label: 'Settings', icon: Settings },
  ];

  return (
    <div className="flex min-h-screen bg-slate-50 font-sans text-slate-900">
      {/* Sidebar - Consistent with Admin Dashboard */}
      <aside className="w-64 bg-slate-900 text-slate-400 flex flex-col sticky top-0 h-screen shrink-0 border-r border-slate-800">
        <div className="p-6 border-b border-slate-800 flex items-center gap-3">
          <div className="w-9 h-9 bg-blue-600 rounded-lg flex items-center justify-center text-white font-bold text-lg shadow-sm">
            T
          </div>
          <span className="font-bold text-white tracking-tight">Admin Portal</span>
        </div>
        
        <nav className="flex-1 p-4 space-y-1 overflow-y-auto">
          {menuItems.map((item) => (
            <button
              key={item.id}
              onClick={() => setActiveTab(item.id)}
              className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-bold transition-all ${
                activeTab === item.id 
                ? 'bg-blue-600 text-white shadow-lg shadow-blue-900/20' 
                : 'hover:bg-slate-800 hover:text-white'
              }`}
            >
              <item.icon size={18} />
              {item.label}
            </button>
          ))}
        </nav>
      </aside>

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col min-w-0">
        {/* Top Header - Removed the Search Bar as requested */}
        <header className="bg-white border-b border-slate-200 px-8 py-3 flex items-center justify-between sticky top-0 z-10">
          <div>
            <h2 className="text-sm font-bold text-slate-400 uppercase tracking-widest">
              Performance & Workload Analysis
            </h2>
          </div>

          <div className="flex items-center gap-6">
            <button className="relative p-2 text-slate-500 hover:bg-slate-100 rounded-full transition-colors">
              <Bell size={20} />
              <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-red-500 rounded-full border-2 border-white"></span>
            </button>
            <div className="flex items-center gap-3 pl-4 border-l border-slate-200 cursor-pointer group">
              <div className="text-right hidden sm:block">
                <p className="text-sm font-bold text-slate-700 group-hover:text-blue-600 transition-colors">Super Admin</p>
                <p className="text-[10px] font-semibold text-slate-400 uppercase tracking-wider text-right">Online</p>
              </div>
              <div className="w-9 h-9 bg-slate-800 rounded-full flex items-center justify-center text-white border border-slate-700">
                <UserCog size={20} />
              </div>
              <ChevronDown size={16} className="text-slate-400" />
            </div>
          </div>
        </header>

        {/* Content Body */}
        <main className="p-8 space-y-6 animate-in fade-in duration-500 max-w-[1400px] mx-auto w-full">
          {/* Breadcrumb & Title */}
          <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
            <div>
              <button className="flex items-center gap-2 text-sm font-bold text-slate-500 hover:text-blue-600 transition-colors group mb-1">
                <ArrowLeft size={16} className="group-hover:-translate-x-1 transition-transform" />
                Back to Dashboard
              </button>
              <h2 className="text-2xl font-black text-slate-800 tracking-tight">TA Workload Management</h2>
            </div>
          </div>

          {/* Filter Section (从草图：筛选：姓名/学号 / 专业 / 工作量状态) */}
          <section className="bg-white rounded-3xl shadow-sm border border-slate-200 p-6">
            <div className="grid grid-cols-1 md:grid-cols-12 gap-4 items-end">
              {/* Search Name/ID */}
              <div className="md:col-span-4 space-y-2">
                <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest px-1">Name / Student ID</label>
                <div className="relative">
                  <Filter className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-300" size={16} />
                  <input
                    type="text"
                    placeholder="Enter name or ID..."
                    className="w-full pl-10 pr-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all text-sm"
                    value={nameSearch}
                    onChange={(e) => setNameSearch(e.target.value)}
                  />
                </div>
              </div>

              {/* Major Filter */}
              <div className="md:col-span-3 space-y-2">
                <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest px-1">Major Filter</label>
                <div className="relative">
                  <select 
                    className="w-full pl-4 pr-10 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none text-sm appearance-none cursor-pointer"
                    value={majorFilter}
                    onChange={(e) => setMajorFilter(e.target.value)}
                  >
                    <option>All Majors</option>
                    <option>Software</option>
                    <option>Computer Science</option>
                    <option>Comm. Engineering</option>
                  </select>
                  <ChevronDown className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" size={16} />
                </div>
              </div>

              {/* Status Filter */}
              <div className="md:col-span-3 space-y-2">
                <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest px-1">Workload Status</label>
                <div className="relative">
                  <select className="w-full pl-4 pr-10 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none text-sm appearance-none cursor-pointer">
                    <option>All Status</option>
                    <option>Normal</option>
                    <option>High Alert</option>
                    <option>Not Applied</option>
                  </select>
                  <ChevronDown className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" size={16} />
                </div>
              </div>

              {/* Filter Buttons */}
              <div className="md:col-span-2 flex gap-2">
                <button className="flex-1 bg-blue-600 text-white font-bold py-2.5 rounded-xl hover:bg-blue-700 transition-all shadow-md shadow-blue-100 text-sm">
                  Search
                </button>
                <button className="px-3 bg-slate-100 text-slate-500 rounded-xl hover:bg-slate-200 transition-all border border-slate-200">
                  <RotateCcw size={18} />
                </button>
              </div>
            </div>
          </section>

          {/* Workload Table (从草图：数据表格) */}
          <div className="bg-white rounded-[32px] shadow-xl shadow-slate-200/50 border border-slate-100 overflow-hidden">
            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-100">
                    <th className="px-8 py-5 text-[10px] font-black text-slate-400 uppercase tracking-widest">TA Name</th>
                    <th className="px-8 py-5 text-[10px] font-black text-slate-400 uppercase tracking-widest">Student ID</th>
                    <th className="px-8 py-5 text-[10px] font-black text-slate-400 uppercase tracking-widest">Major</th>
                    <th className="px-8 py-5 text-[10px] font-black text-slate-400 uppercase tracking-widest text-center">Positions</th>
                    <th className="px-8 py-5 text-[10px] font-black text-slate-400 uppercase tracking-widest text-center">Hours</th>
                    <th className="px-8 py-5 text-[10px] font-black text-slate-400 uppercase tracking-widest text-center">Status</th>
                    <th className="px-8 py-5 text-[10px] font-black text-slate-400 uppercase tracking-widest text-right">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-50">
                  {workloadData.map((ta) => (
                    <tr key={ta.id} className="hover:bg-slate-50/50 transition-colors group">
                      <td className="px-8 py-6 font-bold text-slate-800">{ta.name}</td>
                      <td className="px-8 py-6">
                        <span className="text-xs font-bold text-slate-400 bg-slate-50 px-2 py-1 rounded border border-slate-100 font-mono">{ta.studentId}</span>
                      </td>
                      <td className="px-8 py-6">
                        <div className="flex items-center gap-2 text-xs font-bold text-slate-600">
                           <GraduationCap size={14} className="text-blue-400" />
                           {ta.major}
                        </div>
                      </td>
                      <td className="px-8 py-6 text-center font-black text-slate-700">{ta.positions}</td>
                      <td className="px-8 py-6 text-center">
                        <span className={`font-black ${ta.status === 'High' ? 'text-amber-600' : 'text-slate-800'}`}>{ta.hours}h</span>
                      </td>
                      <td className="px-8 py-6 text-center">
                        {ta.status === 'High' ? (
                          <span className="inline-flex items-center gap-1.5 px-3 py-1 bg-amber-50 text-amber-700 font-bold rounded-full text-[10px] border border-amber-100">
                            <AlertTriangle size={12} /> High Alert
                          </span>
                        ) : ta.status === 'Not Applied' ? (
                          <span className="inline-flex items-center gap-1.5 px-3 py-1 bg-slate-100 text-slate-400 font-bold rounded-full text-[10px] border border-slate-200">
                            Not Applied
                          </span>
                        ) : (
                          <span className="inline-flex items-center gap-1.5 px-3 py-1 bg-emerald-50 text-emerald-700 font-bold rounded-full text-[10px] border border-emerald-100">
                            <CheckCircle2 size={12} /> Normal
                          </span>
                        )}
                      </td>
                      <td className="px-8 py-6 text-right">
                        <button className="text-xs font-bold text-blue-600 hover:underline">Details</button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* Workload Visualization (从草图：工作量分布图表) */}
          <section className="bg-white rounded-[32px] shadow-lg border border-slate-200 p-8">
            <div className="flex items-center justify-between mb-8">
              <div className="flex items-center gap-3">
                <div className="p-2.5 bg-blue-50 text-blue-600 rounded-xl">
                  <BarChart3 size={20} />
                </div>
                <div>
                  <h3 className="text-lg font-black text-slate-800 tracking-tight leading-none">Workload Distribution Analysis</h3>
                  <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest mt-1">Real-time Statistical Report</p>
                </div>
              </div>
              <div className="flex gap-2">
                 <button className="px-3 py-1.5 bg-slate-50 border border-slate-200 rounded-lg text-[10px] font-bold text-slate-500 uppercase tracking-widest hover:bg-white transition-all">Major-based</button>
                 <button className="px-3 py-1.5 bg-blue-600 border border-blue-600 rounded-lg text-[10px] font-bold text-white uppercase tracking-widest shadow-sm">Hour-based</button>
              </div>
            </div>

            {/* Simple SVG Bar Chart Placeholder */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
               <div className="md:col-span-3 h-48 flex items-end justify-between gap-4 px-4 pb-4 border-b border-l border-slate-100">
                  <div className="w-full bg-blue-50 rounded-t-lg h-[20%] group relative">
                     <div className="absolute -top-6 left-1/2 -translate-x-1/2 text-[10px] font-bold text-blue-400">0-4h</div>
                  </div>
                  <div className="w-full bg-blue-200 rounded-t-lg h-[45%]">
                     <div className="absolute -top-6 left-1/2 -translate-x-1/2 text-[10px] font-bold text-blue-500">4-8h</div>
                  </div>
                  <div className="w-full bg-blue-600 rounded-t-lg h-[85%]">
                     <div className="absolute -top-6 left-1/2 -translate-x-1/2 text-[10px] font-bold text-blue-700">8-12h</div>
                  </div>
                  <div className="w-full bg-amber-500 rounded-t-lg h-[30%]">
                     <div className="absolute -top-6 left-1/2 -translate-x-1/2 text-[10px] font-bold text-amber-600">{'>'}12h</div>
                  </div>
               </div>
               <div className="flex flex-col justify-center space-y-4">
                  <div className="p-4 bg-slate-50 rounded-2xl border border-slate-100">
                     <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest mb-1">Peak Workload</p>
                     <p className="text-xl font-black text-slate-800 tracking-tight">Software Dept</p>
                  </div>
                  <div className="p-4 bg-amber-50 rounded-2xl border border-amber-100">
                     <p className="text-[10px] font-bold text-amber-400 uppercase tracking-widest mb-1">Critical Alerts</p>
                     <p className="text-xl font-black text-amber-600 tracking-tight">2 Students</p>
                  </div>
               </div>
            </div>
          </section>

          {/* Export Action (从草图：导出报表) */}
          <div className="flex justify-start">
            <button className="flex items-center gap-2 px-8 py-4 bg-white border-2 border-slate-200 text-slate-700 font-black rounded-2xl shadow-sm hover:border-blue-300 hover:text-blue-600 hover:bg-blue-50 transition-all active:scale-95 group">
              <Download size={20} className="group-hover:animate-bounce" />
              Export Full Workload Report
            </button>
          </div>

        </main>
      </div>
    </div>
  );
};

export default App;