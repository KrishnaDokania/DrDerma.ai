export default function Support() {
  return (
    <div className="mt-20">

      {/* ================= CTA BANNER ================= */}
      <div className="mx-auto max-w-5xl rounded-2xl bg-gradient-to-r from-purple-600 to-blue-500 p-6 flex flex-col md:flex-row justify-between items-center gap-4 shadow-xl">

        <div className="text-left">
          <h3 className="text-xl font-semibold text-white">
            Start your skin diagnosis today
          </h3>
          <p className="text-sm text-gray-200 mt-1">
            Get accurate AI-powered results in seconds
          </p>
        </div>

        <div className="flex gap-3">
          <button className="bg-black text-white px-4 py-2 rounded-lg hover:scale-105 transition">
            Try Now →
          </button>

          <button className="border border-white text-white px-4 py-2 rounded-lg hover:bg-white hover:text-black transition">
            Contact us →
          </button>
        </div>
      </div>

      {/* ================= FOOTER ================= */}
      <div className="bg-[#0b1120] mt-16 px-10 py-16 text-gray-400">

        <div className="max-w-6xl mx-auto grid grid-cols-2 md:grid-cols-5 gap-8 text-sm">

          {/* BRAND */}
          <div>
            <h3 className="text-white font-semibold mb-4">DrDerma</h3>
            <p>AI-powered skin diagnosis assistant.</p>
          </div>

          {/* PRODUCT */}
          <div>
            <h4 className="text-white mb-3">Product</h4>
            <ul className="space-y-2">
              <li>Diagnosis</li>
              <li>Pricing</li>
              <li>Features</li>
            </ul>
          </div>

          {/* RESOURCES */}
          <div>
            <h4 className="text-white mb-3">Resources</h4>
            <ul className="space-y-2">
              <li>Docs</li>
              <li>Support</li>
              <li>FAQ</li>
            </ul>
          </div>

          {/* COMPANY */}
          <div>
            <h4 className="text-white mb-3">Company</h4>
            <ul className="space-y-2">
              <li>About</li>
              <li>Careers</li>
              <li>Blog</li>
            </ul>
          </div>

          {/* CONTACT */}
          <div>
            <h4 className="text-white mb-3">Contact</h4>
            <ul className="space-y-2">
              <li>📧 support@drderma.ai</li>
              <li>📱 +91 9876543210</li>
              <li>📱 +91 9123456780</li>
            </ul>
          </div>

        </div>

        {/* BOTTOM BAR */}
        <div className="max-w-6xl mx-auto mt-12 flex flex-col md:flex-row justify-between items-center text-xs text-gray-500 border-t border-white/10 pt-6">

          <p>© 2026 DrDerma. All rights reserved.</p>

          <div className="flex gap-6 mt-4 md:mt-0">
            <span className="hover:text-white cursor-pointer">Facebook</span>
            <span className="hover:text-white cursor-pointer">Instagram</span>
            <span className="hover:text-white cursor-pointer">LinkedIn</span>
          </div>

        </div>

      </div>
    </div>
  );
}