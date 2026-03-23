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
  Mail, 
  Key, 
  School, 
  Hash, 
  User, 
  Phone,
  Calendar,
  ShieldCheck,
  ShieldAlert,
  Edit,
  ArrowLeft,
  MoreVertical,
  ExternalLink,
  LogIn,
  CheckCircle2,
  XCircle
} from 'lucide-react';

const App = () => {
  const [activeTab, setActiveTab] = useState('AllMOs');

  // 模拟当前 MO 的详细数据 (基于草图翻译)
  const moDetail = {
    name: "Prof. Wang",
    staffId: "M001",
    email: "wang@bupt.edu",
    department: "Software Engineering",
    phone: "123-4567-8901",
    createdAt: "2026-03-01",
    status: "Active",
    postings: [
      { id: 101, title: "Software Engineering", deadline: "2026-03-30", vacancies: 3, applied: 5, status: "Open" },
      { id: 102, title: "Computer Networks", deadline: "2026-04-10", vacancies: 2, applied: 0, status: "Open" },
      { id: 103, title: "Algorithms", deadline: "2026-04-15", vacancies: 1, applied: 2, status: "Open" },
    ]
  };

  const menuItems = [
    { id: 'Dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'CreateMO', label: 'Create MO Account', icon: UserPlus },
    { id: 'AllMOs', label: 'All MOs', icon: Users },
    { id: 'AllPostings', label: 'All Positions', icon: Briefcase },
    { id: 'Workload', label: 'All TA Workload', icon: Clock },
    { id: 'Settings', label: 'Settings (Optional)', icon: Settings },
  ];

  return (
    <div className="flex min-h-screen bg-slate-50 font-sans text-slate-900">
      {/* Sidebar - Consistent with Admin Dashboard */}
      <aside className="w-64 bg-slate-900 text-slate-400 flex flex-col sticky top-0 h-screen shrink-0 border-r border-slate-800">
        <div className="p-6 border-b border-slate-800 flex items-center gap-3">
          <div className="w-9 h-9 bg-blue-600 rounded-lg flex items-center justify-center text-white font-bold text-lg">
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
        {/* Top Header - No Search Bar as requested */}
        <header className="bg-white border-b border-slate-200 px-8 py-3 flex items-center justify-between sticky top-0 z-10">
          <div>
            <h2 className="text-sm font-bold text-slate-400 uppercase tracking-widest px-1">
              MO Profile Review
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
        <main className="p-8 space-y-6 animate-in fade-in duration-500 max-w-5xl mx-auto w-full pb-20">
          
          {/* Breadcrumb & Heading */}
          <div className="flex flex-col md:flex-row md:items-end justify-between gap-4">
            <div>
              <button className="flex items-center gap-2 text-sm font-bold text-slate-500 hover:text-blue-600 transition-colors group mb-1">
                <ArrowLeft size={16} className="group-hover:-translate-x-1 transition-transform" />
                Back to All MOs
              </button>
              <h2 className="text-3xl font-black text-slate-800 tracking-tight">MO Detailed Information</h2>
            </div>
            <div className={`px-4 py-2 rounded-xl flex items-center gap-2 border font-bold text-xs ${
              moDetail.status === 'Active' ? 'bg-emerald-50 border-emerald-100 text-emerald-700' : 'bg-rose-50 border-rose-100 text-rose-700'
            }`}>
              <div className={`w-2 h-2 rounded-full ${moDetail.status === 'Active' ? 'bg-emerald-500' : 'bg-rose-500'}`}></div>
              Account Status: {moDetail.status}
            </div>
          </div>

          {/* MO Basic Info Card (从草图：MO详细信息) */}
          <section className="bg-white rounded-[32px] shadow-xl shadow-slate-200/50 border border-slate-100 overflow-hidden">
             <div className="p-8 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                <div className="space-y-1">
                  <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <User size={12} className="text-blue-600" /> Full Name
                  </p>
                  <p className="text-lg font-bold text-slate-700">{moDetail.name}</p>
                </div>
                <div className="space-y-1">
                  <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <Hash size={12} className="text-blue-600" /> Staff ID
                  </p>
                  <p className="text-lg font-bold text-slate-700">{moDetail.staffId}</p>
                </div>
                <div className="space-y-1">
                  <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <Mail size={12} className="text-blue-600" /> Email Address
                  </p>
                  <p className="text-lg font-bold text-slate-700">{moDetail.email}</p>
                </div>
                <div className="space-y-1">
                  <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <School size={12} className="text-blue-600" /> Department
                  </p>
                  <p className="text-lg font-bold text-slate-700">{moDetail.department}</p>
                </div>
                <div className="space-y-1">
                  <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <Phone size={12} className="text-blue-600" /> Phone Number
                  </p>
                  <p className="text-lg font-bold text-slate-700">{moDetail.phone}</p>
                </div>
                <div className="space-y-1">
                  <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <Calendar size={12} className="text-blue-600" /> Created At
                  </p>
                  <p className="text-lg font-bold text-slate-700">{moDetail.createdAt}</p>
                </div>
             </div>

             {/* Action Buttons Group (从草图：重置密码 | 禁用账号 | 编辑信息) */}
             <div className="px-8 py-6 bg-slate-50 border-t border-slate-100 flex flex-wrap gap-4">
                <button className="flex items-center gap-2 px-6 py-2.5 bg-white border border-slate-200 rounded-xl text-sm font-bold text-slate-600 hover:bg-blue-50 hover:border-blue-200 hover:text-blue-600 transition-all shadow-sm">
                   <Key size={16} /> Reset Password
                </button>
                <button className="flex items-center gap-2 px-6 py-2.5 bg-white border border-rose-100 rounded-xl text-sm font-bold text-rose-600 hover:bg-rose-50 transition-all shadow-sm">
                   <ShieldAlert size={16} /> Disable Account
                </button>
                <button className="flex items-center gap-2 px-6 py-2.5 bg-blue-600 text-white rounded-xl text-sm font-bold hover:bg-blue-700 transition-all shadow-lg shadow-blue-100">
                   <Edit size={16} /> Edit Information
                </button>
             </div>
          </section>

          {/* MO Postings Table (从草图：该MO发布的岗位) */}
          <section className="space-y-4">
            <div className="flex items-center justify-between px-1">
               <h3 className="text-xs font-bold text-slate-400 uppercase tracking-widest flex items-center gap-2">
                 <Briefcase size={14} className="text-blue-600" /> Postings by this MO
               </h3>
               <span className="text-[10px] font-bold text-slate-400 bg-slate-200 px-2 py-0.5 rounded uppercase">Total: {moDetail.postings.length}</span>
            </div>

            <div className="bg-white rounded-[32px] border border-slate-100 shadow-xl shadow-slate-200/50 overflow-hidden">
               <div className="overflow-x-auto">
                  <table className="w-full text-left border-collapse">
                    <thead>
                      <tr className="bg-slate-50 border-b border-slate-100">
                        <th className="px-8 py-5 text-[10px] font-black text-slate-400 uppercase tracking-widest">Course Title</th>
                        <th className="px-8 py-5 text-[10px] font-black text-slate-400 uppercase tracking-widest text-center">Deadline</th>
                        <th className="px-8 py-5 text-[10px] font-black text-slate-400 uppercase tracking-widest text-center">Vacancies / Apps</th>
                        <th className="px-8 py-5 text-[10px] font-black text-slate-400 uppercase tracking-widest text-center">Status</th>
                        <th className="px-8 py-5 text-[10px] font-black text-slate-400 uppercase tracking-widest text-right">Actions</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-50">
                      {moDetail.postings.map((job) => (
                        <tr key={job.id} className="hover:bg-slate-50/50 transition-colors group">
                          <td className="px-8 py-6">
                            <p className="font-bold text-slate-800 group-hover:text-blue-600 transition-colors">{job.title}</p>
                          </td>
                          <td className="px-8 py-6 text-center text-sm font-semibold text-slate-500">{job.deadline}</td>
                          <td className="px-8 py-6 text-center">
                            <span className="text-lg font-black text-slate-800">{job.vacancies}</span>
                            <span className="text-slate-300 mx-1">/</span>
                            <span className={`text-sm font-bold ${job.applied >= job.vacancies ? 'text-emerald-600' : 'text-blue-600'}`}>{job.applied}</span>
                          </td>
                          <td className="px-8 py-6 text-center">
                             <span className="inline-flex items-center gap-1.5 px-3 py-1 bg-emerald-50 text-emerald-700 font-bold rounded-full text-[10px] border border-emerald-100 uppercase">
                                <CheckCircle2 size={12} /> {job.status}
                             </span>
                          </td>
                          <td className="px-8 py-6 text-right">
                             <button className="text-xs font-bold text-blue-600 hover:underline flex items-center gap-1 justify-end ml-auto">
                               Details <ExternalLink size={14} />
                             </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
               </div>
            </div>
          </section>

          {/* Impersonation Button (从草图：以该MO身份登录) */}
          <div className="flex justify-center pt-8">
             <button className="flex items-center gap-3 px-10 py-4 bg-slate-800 text-white font-black rounded-2xl shadow-xl shadow-slate-200 hover:bg-slate-900 transition-all transform active:scale-95 group">
                <LogIn size={20} className="group-hover:translate-x-1 transition-transform" />
                Login as this MO (Admin Privilege)
             </button>
          </div>

        </main>
      </div>
    </div>
  );
};

export default App;