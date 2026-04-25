import { useRef, useState } from "react";
import Navbar from "./Navbar";
import Pricing from "./Pricing";
import Testimonials from "./Testimonials";
import Support from "./Support";
import FloatingGerms from "./FloatingGerms";
import uploadIcon from "../assets/icon.png";

export default function Landing({ onUpload }) {
  const fileRef = useRef();
  const [selectedFile, setSelectedFile] = useState(null);

  return (
    <div className="relative bg-gradient-to-br from-indigo-900 via-purple-900 to-black text-white">

      {/* Background Animation */}
      <FloatingGerms />

      <div className="relative z-10">

        <Navbar />

        {/* ================= HOME ================= */}
        <section
          id="home"
          className="min-h-screen flex flex-col items-center justify-center text-center px-4 pt-28"
        >
          <h1 className="text-4xl md:text-5xl font-bold mb-4 text-yellow-400">
            Get your diagnosis
          </h1>

          <p className="text-gray-300 mb-10 max-w-xl">
            Upload an image of the affected area and our AI will provide
            detailed insights.
          </p>

          {/* Upload Box */}
          <div
            onClick={() => fileRef.current.click()}
            className="w-[90%] max-w-[600px] h-[320px] bg-white/10 backdrop-blur-lg border border-white/20 rounded-3xl flex flex-col items-center justify-center gap-5 cursor-pointer hover:scale-105 transition shadow-xl overflow-hidden"
          >

            {!selectedFile ? (
              <>
                <img
                  src={uploadIcon}
                  alt="Upload"
                  className="w-14 h-14 object-contain"
                />

                <p className="text-xl font-semibold">
                  Drag & Drop your image
                </p>

                <p className="text-gray-400 text-sm">
                  or click to browse your files
                </p>
              </>
            ) : (
              <img
                src={URL.createObjectURL(selectedFile)}
                alt="preview"
                className="max-h-[200px] max-w-[80%] object-contain rounded-xl"
              />
            )}

            <input
              type="file"
              hidden
              ref={fileRef}
              onChange={(e) => {
                const file = e.target.files[0];
                if (file) {
                  console.log("Selected:", file);
                  setSelectedFile(file);
                }
              }}
            />
          </div>

          {selectedFile && (
            <button
              onClick={(e) => {
                e.stopPropagation(); // 🔥 VERY IMPORTANT
                console.log("BUTTON CLICKED");
                onUpload(selectedFile);
              }}
              className="mt-6 bg-yellow-400 text-black px-6 py-2 rounded-lg font-semibold hover:scale-105 transition"
            >
              Check
            </button>
          )}
        </section>

        {/* ================= ABOUT ================= */}
        <section id="about" className="py-24 px-6 text-center">
          <h2 className="text-3xl font-semibold mb-6 text-yellow-400">
            About DrDerma
          </h2>

          <p className="max-w-2xl mx-auto text-gray-300 leading-relaxed">
            DrDerma.ai is an intelligent skin analysis platform designed to make early understanding of skin conditions more accessible. By combining image-based analysis with guided questioning, we aim to provide users with a more informed and interactive diagnostic experience.
          </p>
          <p className="max-w-2xl mx-auto text-gray-300 leading-relaxed">
           We built DrDerma.ai with a focus on simplicity, accessibility, and practical usefulness. Whether someone is trying to understand a new skin concern or seeking preliminary guidance before consulting a professional, our goal is to offer a structured starting point.
          </p>
          <p className="max-w-2xl mx-auto text-gray-300 leading-relaxed">
            DrDerma.ai is an intelligent skin analysis platform designed to make early understanding of skin conditions more accessible. By combining image-based analysis with guided questioning, we aim to provide users with a more informed and interactive diagnostic experience.
          </p>
        </section>

        {/* ================= TESTIMONIALS ================= */}
        <section id="testimonials">
          <Testimonials />
        </section>

        {/* ================= PRICING ================= */}
        <section id="pricing" className="py-24">
          <Pricing />
        </section>

        {/* ================= SUPPORT ================= */}
        <section id="support">
          <Support />
        </section>

      </div>
    </div>
  );
}