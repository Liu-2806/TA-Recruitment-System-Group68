import React, { useState } from 'react';
import { 
  Bell, 
  Search, 
  ChevronDown, 
  User, 
  ArrowLeft, 
  Mail, 
  Hash, 
  Briefcase, 
  Phone, 
  Lock,
  Save,
  X
} from 'lucide-react';

const App = () => {
  // 模拟用户信息状态
  const [profile, setProfile] = useState({
    name: 'Prof. Wang',
    staffId: 'M001',
    email: 'wang@bupt.edu',
    department: 'School of Software Engineering',
    phone: '123-4567-8901',
    description: 'Main research interests: Software Engineering, Agile Development, Human-Computer Interaction.'
  });

  const [activeTab, setActiveTab] = useState('profile');

  return (
    <div className="min-h-screen bg-slate-50 font-sans text-slate-700">
      {/* Top Navigation Bar - Matching Style from Image 2 */}
      <header className="bg-white border-b border-slate-200 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 h-16 flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="w-8 h-8 bg-blue-600 rounded flex items-center justify-center text-white font-bold">T</div>
            <span className="text-xl font-bold text-slate-800">TA Recruitment System</span>
          </div>

          <div className="flex-1 max-w-md mx-8">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400 w-4 h-4" />
              <input 
                type="text" 
                placeholder="Quick search..." 
                className="w-full bg-slate-100 border-none rounded-full py-2 pl-10 pr-4 focus:ring-2 focus:ring-blue-500 outline-none text-sm"
              />
            </div>
          </div>

          <div className="flex items-center space-x-6">
            <button className="relative text-slate-500 hover:text-blue-600 transition-colors">
              <Bell className="w-5 h-5" />
              <span className="absolute -top-1 -right-1 w-2 h-2 bg-red-500 rounded-full border-2 border-white"></span>
            </button>
            <div className="flex items-center space-x-3 border-l pl-6 border-slate-200">
              <div className="text-right">
                <p className="text-sm font-semibold text-slate-800 leading-tight">Prof. Wang</p>
                <p className="text-xs text-slate-500 uppercase tracking-wider">Module Organizer</p>
              </div>
              <div className="w-9 h-9 bg-blue-100 rounded-full flex items-center justify-center text-blue-600">
                <User className="w-5 h-5" />
              </div>
              <ChevronDown className="w-4 h-4 text-slate-400" />
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 py-8">
        {/* Breadcrumb / Back Link */}
        <div className="flex items-center justify-between mb-6">
          <button className="flex items-center text-blue-600 hover:text-blue-700 font-medium transition-colors">
            <ArrowLeft className="w-4 h-4 mr-2" />
            Back to Dashboard
          </button>
          <h1 className="text-2xl font-bold text-slate-800 uppercase tracking-tight">My Profile</h1>
        </div>

        {/* Main Content Area */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          
          {/* Left Column: Basic Information */}
          <div className="lg:col-span-1 space-y-6">
            <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
              <div className="p-6 border-b border-slate-50 flex items-center justify-between">
                <h2 className="font-bold text-slate-800 flex items-center tracking-wide">
                  <div className="w-1.5 h-4 bg-blue-600 rounded-full mr-2"></div>
                  BASIC INFORMATION
                </h2>
                <span className="text-xs text-blue-600 bg-blue-50 px-2 py-1 rounded font-medium">Editable</span>
              </div>
              
              <div className="p-6 space-y-5">
                <div>
                  <label className="block text-xs font-bold text-slate-400 mb-2 uppercase tracking-widest">* Full Name</label>
                  <input 
                    type="text" 
                    value={profile.name}
                    className="w-full border border-slate-200 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all"
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-400 mb-2 uppercase tracking-widest">* Staff ID</label>
                  <div className="flex items-center bg-slate-50 border border-slate-200 rounded-lg px-4 py-2.5 text-slate-500">
                    <Hash className="w-4 h-4 mr-2" />
                    <span>{profile.staffId}</span>
                    <span className="ml-auto text-[10px] bg-slate-200 px-1.5 py-0.5 rounded text-slate-400">READ ONLY</span>
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-400 mb-2 uppercase tracking-widest">* Email Address</label>
                  <div className="relative">
                    <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                    <input 
                      type="email" 
                      value={profile.email}
                      className="w-full border border-slate-200 rounded-lg pl-10 pr-4 py-2.5 focus:ring-2 focus:ring-blue-500 outline-none transition-all"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-400 mb-2 uppercase tracking-widest">Department</label>
                  <div className="relative">
                    <Briefcase className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                    <select className="w-full border border-slate-200 rounded-lg pl-10 pr-10 py-2.5 appearance-none focus:ring-2 focus:ring-blue-500 outline-none transition-all bg-white">
                      <option>{profile.department}</option>
                      <option>School of Computer Science</option>
                      <option>School of Artificial Intelligence</option>
                    </select>
                    <ChevronDown className="absolute right-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400 pointer-events-none" />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-400 mb-2 uppercase tracking-widest">Contact Phone</label>
                  <div className="relative">
                    <Phone className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                    <input 
                      type="text" 
                      value={profile.phone}
                      className="w-full border border-slate-200 rounded-lg pl-10 pr-4 py-2.5 focus:ring-2 focus:ring-blue-500 outline-none transition-all"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Right Column: Personal Description & Account Security */}
          <div className="lg:col-span-2 space-y-8">
            
            {/* Personal Description Card */}
            <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
              <div className="p-6 border-b border-slate-50">
                <h2 className="font-bold text-slate-800 flex items-center tracking-wide">
                  <div className="w-1.5 h-4 bg-blue-600 rounded-full mr-2"></div>
                  PERSONAL DESCRIPTION
                  <span className="ml-2 text-xs font-normal text-slate-400 uppercase italic">(Optional)</span>
                </h2>
              </div>
              <div className="p-6">
                <label className="block text-xs font-bold text-slate-400 mb-2 uppercase tracking-widest">Research Interests & Teaching Background</label>
                <textarea 
                  rows="6"
                  className="w-full border border-slate-200 rounded-xl p-4 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all resize-none text-slate-600"
                  placeholder="Tell students more about your research focus and what you look for in a TA..."
                  defaultValue={profile.description}
                ></textarea>
                <p className="mt-2 text-xs text-slate-400">
                  This description will be visible to potential TA applicants to help them understand the module's requirements.
                </p>
              </div>
            </div>

            {/* Account Security Card */}
            <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
              <div className="p-6 border-b border-slate-50">
                <h2 className="font-bold text-slate-800 flex items-center tracking-wide">
                  <div className="w-1.5 h-4 bg-blue-600 rounded-full mr-2"></div>
                  ACCOUNT SECURITY
                </h2>
              </div>
              <div className="p-6 flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div className="flex items-center space-x-4">
                  <div className="w-10 h-10 bg-orange-50 rounded-lg flex items-center justify-center text-orange-600">
                    <Lock className="w-5 h-5" />
                  </div>
                  <div>
                    <p className="text-sm font-semibold text-slate-800 tracking-tight">Login Password</p>
                    <p className="text-xs text-slate-500">Last login: 2026-03-18 14:20:05</p>
                  </div>
                </div>
                <button className="px-4 py-2 border border-slate-200 text-slate-700 text-sm font-semibold rounded-lg hover:bg-slate-50 transition-colors">
                  Change Password
                </button>
              </div>
            </div>

            {/* Bottom Actions */}
            <div className="flex items-center space-x-4 pt-4">
              <button className="flex-1 md:flex-none px-8 py-3 bg-blue-600 text-white rounded-xl font-bold shadow-lg shadow-blue-200 hover:bg-blue-700 transition-all flex items-center justify-center">
                <Save className="w-5 h-5 mr-2" />
                Save Changes
              </button>
              <button className="flex-1 md:flex-none px-8 py-3 bg-white border border-slate-200 text-slate-600 rounded-xl font-bold hover:bg-slate-50 transition-all flex items-center justify-center">
                <X className="w-5 h-5 mr-2" />
                Cancel
              </button>
            </div>

          </div>
        </div>
      </main>
      
      {/* Footer Decoration */}
      <footer className="mt-12 py-8 text-center text-slate-400 text-xs border-t border-slate-100">
        &copy; 2026 TA Recruitment System - BUPT Department HR
      </footer>
    </div>
  );
};

export default App;