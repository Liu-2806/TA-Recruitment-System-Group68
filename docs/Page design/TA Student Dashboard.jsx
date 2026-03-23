import React, { useState } from 'react';
import { 
  User, 
  Bell, 
  ChevronDown, 
  FileText, 
  Download, 
  Plus, 
  Briefcase, 
  Clock, 
  ExternalLink,
  CheckCircle2,
  XCircle,
  AlertCircle,
  Edit3,
  Mail,
  Calendar,
  MessageSquare,
  BadgeCheck,
  ArrowRight
} from 'lucide-react';

const App = () => {
  // 模拟学生数据 (根据草图翻译)
  const userData = {
    name: "Zhang San",
    studentId: "2021001234",
    major: "Software Engineering",
    stats: { pending: 3, accepted: 1, rejected: 0 },
    resume: "resume_zhang_san.pdf",
    skills: ["Java", "Python", "Project Management", "Communication"],
    pendingActionCount: 2,
    latestJobs: [
      { id: 1, title: "Software Engineering TA", mo: "Prof. Wang" },
      { id: 2, title: "Data Structures TA", mo: "Prof. Li" },
      { id: 3, title: "Computer Networks TA", mo: "Prof. Zhang" }
    ],
    progress: [
      { id: 101, title: "Software Engineering TA", status: "Pending", action: "Details" },
      { id: 102, title: "Database Systems TA", status: "Accepted", action: "Details" }
    ],
    deadlines: [
      { date: "03/30", event: "Software Engineering Application Deadline" },
      { date: "04/02", event: "Data Structures Interview Session" }
    ],
    notifications: [
      { id: 1, text: "Your resume has passed the initial screening.", type: "success" },
      { id: 2, text: "3 new TA positions were posted this week.", type: "info" }
    ]
  };

  return (
    <div className="min-h-screen bg-slate-50 font-sans text-slate-900">
      {/* Header - 简洁风格，去除了搜索框 */}
      <header className="bg-white border-b border-slate-200 px-6 py-3 sticky top-0 z-20 shadow-sm">
        <div className="max-w-[1600px] mx-auto flex items-center justify-between gap-4">
          {/* Logo */}
          <div className="flex items-center gap-3 shrink-0">
            <div className="w-9 h-9 bg-blue-600 rounded-lg flex items-center justify-center text-white font-bold text-lg shadow-sm">
              T
            </div>
            <h1 className="text-lg font-bold tracking-tight text-slate-800">
              TA Recruitment Portal
            </h1>
          </div>

          {/* User Actions */}
          <div className="flex items-center gap-6">
            <button className="relative p-2 text-slate-500 hover:bg-slate-100 rounded-full transition-colors">
              <Bell size={20} />
              <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-red-500 rounded-full border-2 border-white"></span>
            </button>
            <div className="flex items-center gap-3 pl-4 border-l border-slate-200 cursor-pointer group">
              <div className="text-right hidden sm:block">
                <p className="text-sm font-bold text-slate-700 group-hover:text-blue-600 transition-colors">{userData.name}</p>
                <p className="text-[10px] font-semibold text-slate-400 uppercase tracking-wider">TA Applicant</p>
              </div>
              <div className="w-9 h-9 bg-blue-50 rounded-full flex items-center justify-center text-blue-600 overflow-hidden border border-blue-100 shadow-sm">
                <User size={20} />
              </div>
              <ChevronDown size={16} className="text-slate-400" />
            </div>
          </div>
        </div>
      </header>

      {/* Main Content Layout (30/70分栏) */}
      <main className="max-w-[1600px] mx-auto p-6 grid grid-cols-1 lg:grid-cols-12 gap-6">
        
        {/* LEFT COLUMN (约 30%) */}
        <div className="lg:col-span-4 xl:col-span-3 space-y-6">
          
          {/* Profile Card (从草图：个人资料卡片) */}
          <div className="bg-white rounded-[2rem] shadow-xl shadow-slate-200/50 border border-slate-100 p-6 overflow-hidden relative group">
            <div className="absolute top-0 left-0 w-full h-1.5 bg-blue-600"></div>
            <div className="flex flex-col items-center text-center mb-6">
              <div className="w-20 h-20 bg-slate-50 rounded-full flex items-center justify-center text-blue-600 mb-4 border border-slate-100 shadow-sm">
                <User size={40} />
              </div>
              <h2 className="text-xl font-black text-slate-800 tracking-tight">{userData.name}</h2>
              <p className="text-xs font-bold text-slate-400 uppercase tracking-widest mt-1">ID: {userData.studentId}</p>
              <p className="text-sm text-slate-500 font-medium mt-1 italic">{userData.major}</p>
            </div>
            <button className="w-full flex items-center justify-center gap-2 py-3 bg-slate-50 hover:bg-blue-50 hover:text-blue-600 text-slate-500 text-sm font-bold rounded-2xl border border-slate-100 transition-all">
              <Edit3 size={16} />
              View / Edit Profile
            </button>
          </div>

          {/* Application Stats (从草图：申请状态统计) */}
          <div className="bg-white rounded-[2rem] shadow-md border border-slate-100 p-6">
            <h3 className="font-bold text-slate-700 text-xs uppercase tracking-widest mb-4">Application Statistics</h3>
            <div className="grid grid-cols-3 gap-3">
              <div className="bg-amber-50 rounded-2xl p-3 text-center border border-amber-100">
                <p className="text-amber-600 font-black text-xl">{userData.stats.pending}</p>
                <p className="text-[10px] text-amber-700 font-bold uppercase tracking-tighter">Pending</p>
              </div>
              <div className="bg-emerald-50 rounded-2xl p-3 text-center border border-emerald-100">
                <p className="text-emerald-600 font-black text-xl">{userData.stats.accepted}</p>
                <p className="text-[10px] text-emerald-700 font-bold uppercase tracking-tighter">Accepted</p>
              </div>
              <div className="bg-slate-50 rounded-2xl p-3 text-center border border-slate-200">
                <p className="text-slate-400 font-black text-xl">{userData.stats.rejected}</p>
                <p className="text-[10px] text-slate-500 font-bold uppercase tracking-tighter">Rejected</p>
              </div>
            </div>
          </div>

          {/* Resume Status (从草图：我的简历状态) */}
          <div className="bg-white rounded-[2rem] shadow-sm border border-slate-100 p-6">
            <h3 className="font-bold text-slate-700 text-xs uppercase tracking-widest mb-4">Resume Status</h3>
            <div className="flex items-center gap-3 p-3 bg-slate-50 rounded-2xl border border-slate-100 mb-4">
              <div className="p-2 bg-white rounded-xl text-blue-600 shadow-sm">
                <FileText size={20} />
              </div>
              <div className="overflow-hidden">
                <p className="text-xs font-bold text-slate-700 truncate">{userData.resume}</p>
                <p className="text-[10px] text-slate-400 font-medium">Verified System PDF</p>
              </div>
            </div>
            <div className="grid grid-cols-2 gap-3">
              <button className="text-[10px] font-bold py-2 bg-white border border-slate-200 rounded-xl text-slate-600 hover:bg-slate-50 transition-colors">Update</button>
              <button className="text-[10px] font-bold py-2 bg-blue-50 border border-blue-100 rounded-xl text-blue-600 hover:bg-blue-100 transition-colors flex items-center justify-center gap-1">
                <Download size={12} /> Download
              </button>
            </div>
          </div>

          {/* Skill Tags (从草图：技能标签) */}
          <div className="bg-white rounded-[2rem] shadow-sm border border-slate-100 p-6">
            <div className="flex items-center justify-between mb-4">
              <h3 className="font-bold text-slate-700 text-xs uppercase tracking-widest">Expertise</h3>
              <button className="text-blue-600 hover:bg-blue-50 p-1 rounded-lg transition-colors">
                <Plus size={16} />
              </button>
            </div>
            <div className="flex flex-wrap gap-2">
              {userData.skills.map((skill, i) => (
                <span key={i} className="px-3 py-1 bg-blue-50 text-blue-700 text-[10px] font-black rounded-full border border-blue-100 uppercase tracking-tighter">
                  {skill}
                </span>
              ))}
            </div>
          </div>
        </div>

        {/* RIGHT COLUMN (约 70%) */}
        <div className="lg:col-span-8 xl:col-span-9 space-y-6">
          
          {/* Pending Action (从草图：待处理申请) */}
          <div className="bg-gradient-to-br from-blue-600 to-blue-700 rounded-[2.5rem] p-8 text-white shadow-xl shadow-blue-200 flex flex-col md:flex-row items-center justify-between gap-6 relative overflow-hidden group">
            <div className="absolute top-0 right-0 w-64 h-64 bg-white/10 rounded-full -mr-20 -mt-20 blur-3xl group-hover:scale-110 transition-transform duration-700"></div>
            <div className="relative z-10">
              <h2 className="text-2xl font-black mb-1 tracking-tight">Action Required</h2>
              <p className="text-blue-100 text-sm opacity-90 font-medium italic">You have <span className="underline font-bold">{userData.pendingActionCount} applications</span> currently waiting for Module Organizer (MO) review.</p>
            </div>
            <button className="relative z-10 px-8 py-3.5 bg-white text-blue-700 font-black rounded-2xl hover:bg-blue-50 transition-all flex items-center gap-2 shrink-0 shadow-lg active:scale-95">
              Track My Progress <ArrowRight size={18} />
            </button>
          </div>

          {/* Latest Job Postings (从草图：最新发布的岗位) */}
          <div className="bg-white rounded-[2.5rem] shadow-xl shadow-slate-200/50 border border-slate-100 p-8">
            <div className="flex items-center justify-between mb-8">
              <div className="flex items-center gap-3">
                <div className="p-2.5 bg-blue-50 text-blue-600 rounded-2xl">
                  <Briefcase size={20} />
                </div>
                <h3 className="text-lg font-black text-slate-800 tracking-tight leading-none uppercase">Latest Job Postings</h3>
              </div>
              <button className="text-xs font-black text-blue-600 hover:underline uppercase tracking-widest">Browse All →</button>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              {userData.latestJobs.map((job, i) => (
                <div key={i} className="group p-5 bg-slate-50 border border-slate-100 rounded-[2rem] hover:border-blue-200 hover:bg-blue-50/30 transition-all cursor-pointer">
                  <div className="w-12 h-12 bg-white rounded-2xl flex items-center justify-center text-blue-600 mb-4 shadow-sm group-hover:bg-blue-600 group-hover:text-white transition-all">
                    <Briefcase size={24} />
                  </div>
                  <h4 className="font-bold text-slate-800 text-sm mb-1 leading-tight group-hover:text-blue-700">{job.title}</h4>
                  <p className="text-[10px] text-slate-400 font-bold uppercase tracking-widest">{job.mo}</p>
                </div>
              ))}
            </div>
          </div>

          {/* Application Progress Table (从草图：我的申请进度) */}
          <div className="bg-white rounded-[2.5rem] shadow-sm border border-slate-100 overflow-hidden">
            <div className="p-6 border-b border-slate-100 flex items-center justify-between bg-slate-50/30">
              <h3 className="text-sm font-black text-slate-700 uppercase tracking-widest">Active Application Tracking</h3>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full text-left">
                <thead className="bg-slate-50/80 border-b border-slate-100">
                  <tr>
                    <th className="px-8 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest">Job Position</th>
                    <th className="px-8 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest text-center">Status</th>
                    <th className="px-8 py-4 text-[10px] font-black text-slate-400 uppercase tracking-widest text-right">Action</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-50">
                  {userData.progress.map((item) => (
                    <tr key={item.id} className="hover:bg-slate-50/50 transition-colors group">
                      <td className="px-8 py-5">
                        <p className="font-bold text-slate-800 text-sm">{item.title}</p>
                      </td>
                      <td className="px-8 py-5 text-center">
                        <span className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[10px] font-black uppercase border ${
                          item.status === 'Pending' ? 'bg-amber-50 text-amber-700 border-amber-100' : 'bg-emerald-50 text-emerald-700 border-emerald-100'
                        }`}>
                          {item.status === 'Pending' ? <Clock size={12} /> : <CheckCircle2 size={12} />}
                          {item.status}
                        </span>
                      </td>
                      <td className="px-8 py-5 text-right">
                        <button className="text-xs font-black text-blue-600 hover:underline uppercase tracking-tighter">
                          {item.action}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* Important Deadlines (从草图：重要截止日期) */}
            <div className="bg-white rounded-[2.5rem] shadow-sm border border-slate-100 p-8">
              <h3 className="text-xs font-black text-slate-400 uppercase tracking-widest mb-6 flex items-center gap-2">
                <AlertCircle size={18} className="text-rose-500" />
                Urgent Deadlines
              </h3>
              <div className="space-y-6">
                {userData.deadlines.map((item, i) => (
                  <div key={i} className="flex gap-4 items-center">
                    <div className="w-12 text-center shrink-0 border-r border-slate-100 pr-4">
                      <p className="text-lg font-black text-blue-600 leading-none">{item.date.split('/')[1]}</p>
                      <p className="text-[10px] font-bold text-slate-400 uppercase tracking-widest mt-1">Mar</p>
                    </div>
                    <p className="text-sm font-bold text-slate-600 leading-snug">{item.event}</p>
                  </div>
                ))}
              </div>
            </div>

            {/* System Notifications (从草图：系统通知 - 挪到右侧) */}
            <div className="bg-white rounded-[2.5rem] shadow-sm border border-slate-100 p-8">
              <h3 className="text-xs font-black text-slate-400 uppercase tracking-widest mb-6 flex items-center gap-2">
                <MessageSquare size={18} className="text-blue-600" />
                Portal Alerts
              </h3>
              <ul className="space-y-4">
                {userData.notifications.map((note) => (
                  <li key={note.id} className="flex gap-3 items-start p-3 bg-slate-50 rounded-2xl border border-slate-100">
                    <div className={`mt-1 w-2 h-2 rounded-full shrink-0 ${note.type === 'success' ? 'bg-emerald-500' : 'bg-blue-500'}`}></div>
                    <p className="text-xs text-slate-600 font-medium leading-relaxed italic">{note.text}</p>
                  </li>
                ))}
              </ul>
            </div>
          </div>

        </div>
      </main>

      {/* Footer Info */}
      <footer className="max-w-[1600px] mx-auto p-8 text-center border-t border-slate-200 mt-6">
        <p className="text-slate-400 text-[10px] font-bold uppercase tracking-[0.2em]">
          © 2025 University TA Recruitment Portal. Processing with academic integrity.
        </p>
      </footer>
    </div>
  );
};

export default App;