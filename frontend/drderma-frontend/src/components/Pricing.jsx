export default function Pricing() {
  return (
    <div className="mt-16 flex flex-col items-center bg-transparent relative z-10">

      <h2 className="text-3xl font-semibold text-yellow-400 mb-10">
        Choose Your Plan
      </h2>

      <div className="flex gap-8 flex-wrap justify-center">

        {/* BASIC */}
        <div className="bg-white/10 backdrop-blur-md border border-white/20 rounded-2xl p-6 w-[260px] text-center hover:scale-105 transition shadow-lg">
          <h3 className="text-xl font-semibold text-yellow-400">Basic</h3>
          <p className="text-3xl font-bold mt-2">$0.3</p>
          <p className="text-gray-300 text-sm mb-4">per scan</p>

          <ul className="text-sm text-gray-300 space-y-2">
            <li>✔ Single image analysis</li>
            <li>✔ Basic AI diagnosis</li>
            <li>✔ Limited question flow</li>
            <li>✔ No history saved</li>
          </ul>

          <button className="mt-6 w-full bg-yellow-500 text-black py-2 rounded-lg font-medium hover:scale-105 transition">
            Use Now
          </button>
        </div>

        {/* PRO (HIGHLIGHTED) */}
        <div className="bg-gradient-to-b from-purple-600/30 to-indigo-700/30 backdrop-blur-md border border-yellow-400 rounded-2xl p-6 w-[280px] text-center scale-105 shadow-2xl relative">

          {/* Badge */}
          <div className="absolute -top-3 left-1/2 -translate-x-1/2 bg-yellow-400 text-black text-xs px-3 py-1 rounded-full font-semibold">
            MOST POPULAR
          </div>

          <h3 className="text-xl font-semibold text-yellow-300">Pro</h3>
          <p className="text-3xl font-bold mt-2">$50</p>
          <p className="text-gray-200 text-sm mb-4">per month</p>

          <ul className="text-sm text-gray-200 space-y-2">
            <li>✔ Unlimited scans</li>
            <li>✔ Advanced AI accuracy</li>
            <li>✔ Full question engine</li>
            <li>✔ History tracking</li>
            <li>✔ Priority processing</li>
          </ul>

          <button className="mt-6 w-full bg-yellow-400 text-black py-2 rounded-lg font-semibold hover:scale-105 transition">
            Get Pro
          </button>
        </div>

        {/* ENTERPRISE */}
        <div className="bg-white/10 backdrop-blur-md border border-white/20 rounded-2xl p-6 w-[260px] text-center hover:scale-105 transition shadow-lg">
          <h3 className="text-xl font-semibold text-yellow-400">Enterprise</h3>
          <p className="text-3xl font-bold mt-2">$500</p>
          <p className="text-gray-300 text-sm mb-4">per year</p>

          <ul className="text-sm text-gray-300 space-y-2">
            <li>✔ Everything in Pro</li>
            <li>✔ Faster response time</li>
            <li>✔ API access</li>
            <li>✔ Priority support</li>
            <li>✔ Cost-effective pricing</li>
          </ul>

          <button className="mt-6 w-full bg-yellow-500 text-black py-2 rounded-lg font-medium hover:scale-105 transition">
            Go Yearly
          </button>
        </div>

      </div>
    </div>
  );
}