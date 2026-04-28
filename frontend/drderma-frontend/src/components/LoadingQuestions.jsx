import { motion } from "framer-motion";

export default function LoadingQuestions() {
  return (
    <div className="min-h-screen flex items-center justify-center px-4">

      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
        className="text-center"
      >

        {/* 🔥 SPINNER */}
        <motion.div
          className="w-16 h-16 border-2 border-yellow-400 border-t-transparent rounded-full mx-auto mb-6"
          animate={{ rotate: 360 }}
          transition={{ repeat: Infinity, duration: 1, ease: "linear" }}
        />

        {/* 🔥 TEXT */}
        <motion.p
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-yellow-400 text-lg font-semibold"
        >
          Generating Questions...
        </motion.p>

        <p className="text-gray-400 text-sm mt-2">
          Building a personalized diagnostic flow
        </p>

      </motion.div>

    </div>
  );
}