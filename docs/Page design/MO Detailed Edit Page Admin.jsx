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
  ShieldCheck,
  ArrowLeft,
  Save,
  X,
  FileText,
  RefreshCw
} from 'lucide-react';

const App = () => {
  const [activeTab, setActiveTab] = useState('AllMOs');

  // 模拟待编辑的 MO 数据
  const [formData, setFormData] = useState({
    name: "Prof. James Smith",
    staffId: "MO-12345",
    email: "james.smith@university.edu",
    department: "Software Engineering",
    phone: "123-4567-8901",
    bio: "Professor James Smith is a senior faculty member in the School of Software Engineering, specializing in Object-Oriented Design and Agile Methodologies.",
    status: "Active"
  });

  const menuItems = [
    { id: 'Dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'CreateMO', label: 'Create MO Account', icon: UserPlus },
    { id: 'AllMOs', label: 'All MOs', icon: Users },
    { id: 'AllPostings', label: 'All Positions', icon: Briefcase },
    { id: 'Workload', label: 'All TA Workload', icon: Clock },
    { id: 'Settings', label: 'Settings (Optional)', icon: Settings },
  ];

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

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
        {/* Top Header - No Search Bar */}
        <header className="bg-white border-b border-slate-200 px-8 py-3 flex items-center justify-between sticky top-0 z-10">
          <div>
            <h2 className="text-sm font-bold text-slate-400 uppercase tracking-widest">
              MO Profile Management
            </h2>
          </div>

          <div className="flex items-center gap-6">
            <button className="relative p-2 text-slate-500 hover:bg-slate-100 rounded-full transition-colors">
              <Bell size={20} />
              <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-red-500 rounded-full border-2 border-white"></span>
            </button>
            <div className="flex items-center gap-3 pl-4 border-l border-slate-200 cursor-pointer group">
              <div className="text-right hidden sm:block">
                <p className="text-sm font-bold text-slate-700">Super Admin</p>
                <p className="text-[10px] font-semibold text-slate-400 uppercase tracking-wider text-right">System Control</p>
              </div>
              <div className="w-9 h-9 bg-slate-800 rounded-full flex items-center justify-center text-white border border-slate-700">
                <UserCog size={20} />
              </div>
              <ChevronDown size={16} className="text-slate-400" />
            </div>
          </div>
        </header>

        {/* Content Body */}
        <main className="p-8 space-y-6 animate-in fade-in duration-500 max-w-4xl mx-auto w-full pb-20">
          
          {/* Breadcrumb & Title */}
          <div className="flex flex-col md:flex-row md:items-end justify-between gap-4">
            <div>
              <button className="flex items-center gap-2 text-sm font-bold text-slate-500 hover:text-blue-600 transition-colors group mb-1">
                <ArrowLeft size={16} className="group-hover:-translate-x-1 transition-transform" />
                Back to All MOs
              </button>
              <h2 className="text-3xl font-black text-slate-800 tracking-tight">Edit MO Information</h2>
            </div>
          </div>

          {/* Form Card */}
          <section className="bg-white rounded-[32px] shadow-xl shadow-slate-200/50 border border-slate-100 overflow-hidden">
            <div className="p-8 space-y-8">
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6">
                {/* Name */}
                <div className="space-y-2">
                  <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <User size={12} className="text-blue-600" /> * Full Name
                  </label>
                  <input
                    type="text"
                    name="name"
                    className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all text-sm font-bold text-slate-700"
                    value={formData.name}
                    onChange={handleInputChange}
                  />
                </div>

                {/* Staff ID */}
                <div className="space-y-2">
                  <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <Hash size={12} className="text-blue-600" /> * Staff ID
                  </label>
                  <input
                    type="text"
                    name="staffId"
                    className="w-full px-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all text-sm font-bold text-slate-700"
                    value={formData.staffId}
                    onChange={handleInputChange}
                  />
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

                {/* Department */}
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
                      <option>Data Science</option>
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

                {/* Account Status */}
                <div className="space-y-2">
                  <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <ShieldCheck size={12} className="text-blue-600" /> Account Status
                  </label>
                  <div className="relative">
                    <select
                      name="status"
                      className="w-full pl-4 pr-10 py-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none text-sm font-bold text-slate-700 appearance-none cursor-pointer"
                      value={formData.status}
                      onChange={handleInputChange}
                    >
                      <option value="Active">Active</option>
                      <option value="Disabled">Disabled</option>
                    </select>
                    <ChevronDown size={16} className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
                  </div>
                </div>

                {/* Bio */}
                <div className="md:col-span-2 space-y-2">
                  <label className="text-[10px] font-bold text-slate-400 uppercase tracking-widest flex items-center gap-1.5">
                    <FileText size={12} className="text-blue-600" /> Personal Biography
                  </label>
                  <textarea
                    name="bio"
                    rows="3"
                    className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-blue-500 outline-none transition-all text-sm text-slate-600 leading-relaxed resize-none"
                    value={formData.bio}
                    onChange={handleInputChange}
                  ></textarea>
                </div>
              </div>

              {/* Password Management Card */}
              <div className="p-6 bg-slate-50 rounded-[24px] border border-slate-200 space-y-4">
                <div className="flex items-center gap-2">
                  <Key size={16} className="text-blue-600" />
                  <h4 className="font-bold text-slate-700 text-sm uppercase tracking-widest">Password Management</h4>
                </div>
                <div className="flex flex-wrap gap-3">
                  <button className="flex items-center gap-2 px-5 py-2 bg-white border border-slate-200 rounded-xl text-xs font-bold text-slate-600 hover:bg-blue-50 hover:border-blue-200 hover:text-blue-600 transition-all shadow-sm">
                    <RefreshCw size={14} /> Reset Password
                  </button>
                </div>
                <p className="text-[10px] text-slate-400 font-medium italic">Clicking "Reset Password" will generate a temporary initial password for the user.</p>
              </div>
            </div>

            {/* Action Buttons */}
            <div className="px-8 py-6 bg-slate-100/50 border-t border-slate-200 flex items-center gap-4">
              <button className="flex-1 max-w-[200px] flex items-center justify-center gap-2 py-3.5 bg-blue-600 text-white font-black rounded-2xl shadow-xl shadow-blue-100 hover:bg-blue-700 transform transition-all active:scale-95 group">
                <Save size={18} className="group-hover:animate-pulse" />
                Save Changes
              </button>
              <button className="flex-1 max-w-[200px] flex items-center justify-center gap-2 py-3.5 bg-white border-2 border-slate-200 text-slate-500 font-bold rounded-2xl hover:bg-slate-50 hover:text-slate-700 transition-all">
                <X size={18} />
                Cancel
              </button>
            </div>
          </section>

        </main>
      </div>
    </div>
  );
};

export default App;