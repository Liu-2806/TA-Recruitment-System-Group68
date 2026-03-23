import React, { useState } from 'react';
import { 
  User, 
  Mail, 
  Phone, 
  School, 
  Hash, 
  Lock, 
  ArrowLeft, 
  Save, 
  X, 
  Bell, 
  ChevronDown, 
  Edit3, 
  Clock, 
  ShieldCheck,
  CheckCircle2,
  FileText
} from 'lucide-react';

const App = () => {
  // 模拟当前登录的 MO 老师数据
  const [formData, setFormData] = useState({
    fullName: "Prof. James Wang",
    staffId: "M001",
    email: "wang@university.edu",
    department: "Software Engineering",
    phone: "123-4567-8901",
    bio: "Main research areas include Software Engineering, Agile Development, and Human-Computer Interaction. Interested in mentoring TAs with strong Java/Python backgrounds.",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  return (
    <div className="min-h-screen bg-slate-50 font-sans text-slate-900">
      {/* Header - 严格遵循之前 MO 仪表盘的风格且移除了搜索栏 */}
      <header className="bg-white border-b border-slate-200 px-6 py-3 sticky top-0 z-20 shadow-sm">
        <div className="max-w-[1400px] mx-auto flex items-center justify-between gap-4">
          {/* Logo */}
          <div className="flex items-center gap-3 shrink-0">
            <div className="w-9 h-9 bg-blue-600 rounded-lg flex items-center justify-center text-white font-bold text-lg shadow-sm">
              T
            </div>
            <h1 className="text-lg font-bold tracking-tight text-slate-800">
              TA Recruitment System
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
                <p className="text-sm font-bold text-slate-700 group-hover:text-blue-600 transition-colors">{formData.fullName}</p>
                <p className="text-[10px] font-semibold text-slate-400 uppercase tracking-wider">Module Organizer</p>
              </div>
              <div className="w-9 h-9 bg-blue-50 rounded-full flex items-center justify-center text-blue-600 overflow-hidden border border-blue-100 shadow-sm">
                <User size={20} />
              </div>
              <ChevronDown size={16} className="text-slate-400" />
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-[1200px] mx-auto p-6 space-y-6">
        
        {/* Breadcrumb & Title (从草图：返回仪表盘 / 我的个人资料) */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <button className="flex items-center gap-2 text-sm font-bold text-slate-500 hover:text-blue-600 transition-colors group mb-1">
              <ArrowLeft size={18} className="group-hover:-translate-x-1 transition-transform" />
              Back to Dashboard
            </button>
            <h2 className="text-2xl font-black text-slate-800 tracking-tight">My Profile Settings</h2>
          </div>
        </div>

        {/* Main Content Grid (从草图：左右分栏结构) */}
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
          
          {/* LEFT COLUMN: Basic Information (从草图：基本信息 约 45%) */}
          <div className="lg:col-span-5 space-y-6">
            <section className="bg-white rounded-3xl shadow-xl shadow-slate-200/50 border border-slate-100 overflow-hidden h-full">
              <div className="bg-slate-50 border-b border-slate-100 px-8 py-5 flex items-center gap-2">
                <Edit3 size={18} className="text-blue-600" />
                <h3 className="font-bold text-slate-700 uppercase tracking-widest text-xs">Basic Information (Editable)</h3>
              </div>
              
              <div className="p-8 space-y-6">
                {/* Full Name */}
                <div className="space-y-2">
                  <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <User size={12} className="text-blue-600" /> * Full Name
                  </label>
                  <input
                    type="text"
                    name="fullName"
                    className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all text-sm font-bold text-slate-700"
                    value={formData.fullName}
                    onChange={handleInputChange}
                  />
                </div>

                {/* Staff ID - Read Only (从草图：工号不可修改) */}
                <div className="space-y-2">
                  <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <Hash size={12} className="text-slate-300" /> * Staff ID (Non-modifiable)
                  </label>
                  <div className="w-full px-4 py-2.5 bg-slate-100 border border-slate-200 rounded-xl text-sm font-bold text-slate-400 flex items-center justify-between cursor-not-allowed">
                    {formData.staffId}
                    <Lock size={14} className="opacity-50" />
                  </div>
                </div>

                {/* Email */}
                <div className="space-y-2">
                  <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <Mail size={12} className="text-blue-600" /> * Email Address
                  </label>
                  <input
                    type="email"
                    name="email"
                    className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all text-sm font-bold text-slate-700"
                    value={formData.email}
                    onChange={handleInputChange}
                  />
                </div>

                {/* Department (Dropdown) */}
                <div className="space-y-2">
                  <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <School size={12} className="text-blue-600" /> Department
                  </label>
                  <div className="relative">
                    <select
                      name="department"
                      className="w-full pl-4 pr-10 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none text-sm font-bold text-slate-700 appearance-none cursor-pointer"
                      value={formData.department}
                      onChange={handleInputChange}
                    >
                      <option>Software Engineering</option>
                      <option>Computer Science</option>
                      <option>Artificial Intelligence</option>
                    </select>
                    <ChevronDown size={16} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
                  </div>
                </div>

                {/* Phone */}
                <div className="space-y-2">
                  <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <Phone size={12} className="text-blue-600" /> Phone Number
                  </label>
                  <input
                    type="text"
                    name="phone"
                    className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all text-sm font-bold text-slate-700"
                    value={formData.phone}
                    onChange={handleInputChange}
                  />
                </div>
              </div>
            </section>
          </div>

          {/* RIGHT COLUMN: Bio & Security (从草图：个人描述与安全 约 55%) */}
          <div className="lg:col-span-7 space-y-6">
            
            {/* Personal Description (从草图：个人描述 可选) */}
            <section className="bg-white rounded-3xl shadow-xl shadow-slate-200/50 border border-slate-100 overflow-hidden">
              <div className="bg-slate-50 border-b border-slate-100 px-8 py-5 flex items-center gap-2">
                 <FileText size={18} className="text-blue-600" />
                 <h3 className="font-bold text-slate-700 uppercase tracking-widest text-xs">Personal Description (Optional)</h3>
              </div>
              <div className="p-8">
                <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest block mb-3">Research Areas, Teaching Interests, etc.</label>
                <textarea
                  name="bio"
                  rows="9"
                  placeholder="Tell students more about your background and what you're looking for in a TA..."
                  className="w-full px-5 py-4 bg-slate-50 border border-slate-200 rounded-[20px] focus:ring-2 focus:ring-blue-500 outline-none transition-all text-sm text-slate-600 leading-relaxed resize-none font-medium italic"
                  value={formData.bio}
                  onChange={handleInputChange}
                ></textarea>
              </div>
            </section>

            {/* Account Security (从草图：账号安全) */}
            <section className="bg-white rounded-3xl shadow-md border border-slate-100 overflow-hidden">
              <div className="bg-slate-50 border-b border-slate-100 px-8 py-5">
                <h3 className="font-bold text-slate-700 uppercase tracking-widest text-xs">Account Security</h3>
              </div>
              <div className="p-8 flex flex-col md:flex-row items-center justify-between gap-6">
                <div className="flex items-center gap-4">
                   <div className="p-3 bg-blue-50 text-blue-600 rounded-2xl shadow-sm">
                      <ShieldCheck size={28} />
                   </div>
                   <div>
                      <p className="text-sm font-bold text-slate-700 leading-none">Password Management</p>
                      <p className="text-[11px] text-slate-400 mt-2 flex items-center gap-1.5 font-medium uppercase tracking-tighter">
                        <Clock size={12} /> Last Login: 2026-03-18 (Beijing Time)
                      </p>
                   </div>
                </div>
                <button className="w-full md:w-auto px-8 py-3 bg-white border-2 border-slate-200 rounded-2xl text-xs font-bold text-slate-600 hover:bg-slate-50 hover:border-blue-300 hover:text-blue-600 transition-all shadow-sm active:scale-95">
                   Change Password
                </button>
              </div>
            </section>
          </div>
        </div>

        {/* Footer Actions (从草图：保存修改 / 取消) */}
        <div className="pt-6 border-t border-slate-200 flex flex-col sm:flex-row items-center gap-4 justify-end">
          <button className="w-full sm:w-auto px-10 py-4 bg-white border-2 border-slate-200 rounded-2xl text-sm font-bold text-slate-500 hover:bg-slate-100 hover:text-slate-700 transition-all flex items-center justify-center gap-2">
            <X size={20} /> Cancel Changes
          </button>
          <button className="w-full sm:w-auto px-12 py-4 bg-blue-600 text-white rounded-2xl text-sm font-black shadow-xl shadow-blue-200 hover:bg-blue-700 transform transition-all active:scale-95 flex items-center justify-center gap-2 group">
            <CheckCircle2 size={20} className="group-hover:scale-110 transition-transform" /> Save Profile
          </button>
        </div>

      </main>

      {/* Footer info */}
      <footer className="max-w-[1200px] mx-auto p-8 text-center text-slate-400 text-xs">
        © 2025 University TA Recruitment Portal. Dedicated to efficient academic collaboration.
      </footer>
    </div>
  );
};

export default App;