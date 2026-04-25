import { useEffect, useState } from "react";
import { motion } from "framer-motion";

export default function ValidationScreen({ image, onComplete }) {
  const [status, setStatus] = useState("scanning"); // scanning | approved | denied

  useEffect(() => {
    // simulate validation delay (replace with backend later)
    const timer = setTimeout(() => {
      const isSkin = true; // replace with real result
      setStatus(isSkin ? "approved" : "denied");

      // move forward after showing result
      setTimeout(() => {
        onComplete(isSkin);
      }, 1500);

    }, 2500);

    return () => clearTimeout(timer);
  }, []);

  return (
    <div className="min-h-screen flex items-center justify-center px-4">

      <div className="w-full max-w-md bg-white/10 backdrop-blur-lg border border-white/20 rounded-2xl p-6 shadow-xl text-center">

        {/* TITLE */}
        <h2 className="text-yellow-400 mb-4 text-lg font-semibold">
          {status === "scanning" && "Validating Image..."}
          {status === "approved" && "Skin Structure Detected"}
          {status === "denied" && "Invalid Image"}
        </h2>

        {/* IMAGE CONTAINER */}
        <div className="relative overflow-hidden rounded-xl">

          <img
            src={image}
            alt="uploaded"
            className="w-full h-[250px] object-cover"
          />

          {/* SCAN LINE */}
          {status === "scanning" && (
            <motion.div
              className="absolute left-0 w-full h-[2px] bg-yellow-400"
              initial={{ top: "0%" }}
              animate={{ top: "100%" }}
              transition={{
                duration: 1.5,
                repeat: Infinity,
                ease: "linear",
              }}
            />
          )}

          {/* DARK OVERLAY DURING SCAN */}
          {status === "scanning" && (
            <div className="absolute inset-0 bg-black/30" />
          )}

          {/* RESULT OVERLAY */}
          {status !== "scanning" && (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className={`absolute inset-0 flex flex-col items-center justify-center ${
                status === "approved"
                  ? "bg-green-500/30"
                  : "bg-red-500/30"
              }`}
            >
              <div className="text-3xl mb-2">
                {status === "approved" ? "✔️" : "❌"}
              </div>

              <p className="font-semibold text-white">
                {status === "approved" ? "Approved" : "Denied"}
              </p>
            </motion.div>
          )}

        </div>

        {/* SUBTEXT */}
        <p className="text-gray-400 text-sm mt-4">
          {status === "scanning" && "Scanning skin patterns..."}
          {status === "approved" && "Image validated successfully"}
          {status === "denied" && "Please upload a valid skin image"}
        </p>

      </div>
    </div>
  );
}