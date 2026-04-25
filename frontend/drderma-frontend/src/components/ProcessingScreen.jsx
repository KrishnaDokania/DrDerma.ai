import { motion } from "framer-motion";

export default function ProcessingScreen({ image, result, readyToContinue, onContinue }) {
  // result: null = scanning, true = approved, false = rejected

  return (
    <div className="min-h-screen flex items-center justify-center px-4">

      <div className="text-center">

        {/* 🔥 IMAGE BOX */}
        <div className="relative w-[340px] h-[340px] mx-auto mb-8 rounded-2xl overflow-hidden border border-white/20 shadow-xl">

          {/* IMAGE */}
          <img
            src={image}
            alt="uploaded"
            className="w-full h-full object-cover"
          />

          {/* DARK BASE OVERLAY */}
          <div className="absolute inset-0 bg-black/30" />

          {/* 🔥 GRID OVERLAY (KEEP THIS — IMPORTANT) */}
          <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.05)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.05)_1px,transparent_1px)] bg-[size:24px_24px]" />

          {/* 🔥 SCANNING LINE */}
          {result === null && (
            <motion.div
              className="absolute left-0 w-full h-[2px] bg-yellow-400 shadow-[0_0_12px_rgba(255,255,0,0.7)]"
              initial={{ top: "0%" }}
              animate={{ top: "100%" }}
              transition={{
                duration: 5,
                repeat: Infinity,
                ease: "linear",
              }}
            />
          )}

          {result !== null && (
  <motion.div
    initial={{ opacity: 0 }}
    animate={{ opacity: 1 }}
    className={`absolute inset-0 flex flex-col items-center justify-center ${
      result ? "bg-green-500/20" : "bg-red-500/20"
    }`}
  >

    {result ? (
      <>
        {/* ICON */}
       <motion.img
  src="/approved-icon.png"
  alt="approved"
  initial={{ scale: 0.7, opacity: 0 }}
  animate={{
    scale: [1, 1.08, 1], // 👈 pulse
    opacity: 1
  }}
  transition={{
    duration: 1.5,
    repeat: Infinity,
    ease: "easeInOut"
  }}
  className="w-24 mb-3 drop-shadow-[0_0_25px_rgba(0,255,150,0.4)]"
/>

        {/* TEXT */}
        <p className="text-green-300 text-lg font-semibold tracking-wide">
          APPROVED
        </p>
     
      </>
    ) : (
      <>
        <p className="text-red-300 text-lg font-semibold">
          REJECTED
        </p>
        <p className="text-sm text-gray-300 mt-2">
          No valid skin structure detected
        </p>
      </>
    )}

  </motion.div>
)}

        </div>

        {/* 🔥 TITLE */}
        <h2 className="text-xl text-yellow-400 font-semibold mb-2">
          {result === null
            ? "Scanning image..."
            : result
            ? "Skin Structure Detected"
            : "Invalid Image"}
        </h2>

        <p className="text-gray-400 text-sm">
          {result === null
            ? "Analyzing visual patterns"
            : result
            ? "Validation successful"
            : "Please upload a clearer image"}
        </p>


{result && readyToContinue && (
  <motion.div
    initial={{ opacity: 0, y: 20, scale: 0.95 }}
    animate={{ opacity: 1, y: 0, scale: 1 }}
    transition={{
      duration: 0.4,
      ease: "easeOut",
      delay: 0.3
    }}
  >
    <button
      onClick={onContinue}
      className="mt-4 px-8 py-3 bg-green-500/20 border border-green-400/40 rounded-lg text-green-300 hover:bg-green-500/30 transition"
    >
      Continue
    </button>
  </motion.div>
)}
      </div>
    </div>
  );
}