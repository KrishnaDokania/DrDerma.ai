import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
export default function Questionnaire({ question, step, total, onNext }) {

  const progress = (step / total) * 100;

 return (
  <div className="min-h-screen flex items-center justify-center px-4">

    <motion.div
      initial={{ opacity: 0, y: 30 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5, ease: "easeOut" }}
      className="w-full max-w-2xl bg-white/10 backdrop-blur-lg border border-white/20 rounded-2xl p-8 shadow-xl"
    >

        {/* HEADER */}
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-yellow-400 font-semibold text-lg">
            Clinical Context
          </h2>

          <span className="text-gray-400 text-sm">
            {step} / {total}
          </span>
        </div>

        {/* PROGRESS BAR */}
        <div className="w-full h-2 bg-white/10 rounded-full mb-6 overflow-hidden">
          <motion.div
            className="h-full bg-yellow-400"
            initial={{ width: 0 }}
            animate={{ width: `${progress}%` }}
            transition={{ duration: 0.5 }}
          />
        </div>

        {/* QUESTION */}
        <motion.p
          key={question}
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-white text-lg leading-relaxed mb-6"
        >
          {question}
        </motion.p>

        {/* INPUT */}
        <textarea
          value={answer}
          onChange={(e) => setAnswer(e.target.value)}
          placeholder="Describe your symptoms in detail..."
          className="w-full h-28 p-4 rounded-xl bg-black/30 border border-white/20 focus:border-yellow-400 focus:outline-none text-white resize-none mb-6"
        />

        {/* BUTTON */}
        <button
          onClick={() => {
            if (!answer.trim()) return;
            onNext(answer);
            setAnswer("");
          }}
          className="w-full bg-yellow-400 text-black py-3 rounded-xl font-semibold hover:scale-[1.02] transition"
        >
          Next Step
        </button>

      </motion.div>
    </div>
  );
}