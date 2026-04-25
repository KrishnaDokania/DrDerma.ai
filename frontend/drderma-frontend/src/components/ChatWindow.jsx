import { motion } from "framer-motion";
import { useEffect, useRef } from "react";

export default function ChatWindow({ messages }) {

  const bottomRef = useRef(null);

  // 🔹 Auto-scroll to latest message
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  return (
    <div className="h-[400px] overflow-y-auto p-4 bg-gray-900 rounded-xl flex flex-col gap-3 shadow-lg">

      {messages.map((msg, index) => {

        // 🔹 LOADING MESSAGE
        if (msg.loading) {
          return (
            <div
              key={index}
              className="text-gray-400 animate-pulse text-sm"
            >
              {msg.text}
            </div>
          );
        }

        // 🔹 RESULT CARD
        if (msg.type === "result") {
          return (
            <div
              key={index}
              className="bg-green-500/10 border border-green-400 rounded-xl p-4 text-white max-w-[85%] self-start"
            >
              <h2 className="text-lg font-bold text-green-300 mb-2">
                🧠 {msg.disease}
              </h2>

              <p className="text-sm mb-2">
                📊 Confidence: {msg.confidence}%
              </p>

              {/* Uploaded Image */}
              {msg.image && (
                <img
                  src={msg.image}
                  alt="uploaded"
                  className="w-full max-h-48 object-contain rounded-lg mb-3"
                />
              )}

              {/* Similar Images */}
              {msg.similarImages && msg.similarImages.length > 0 && (
                <div className="flex gap-2 mb-3 overflow-x-auto">
                  {msg.similarImages.map((img, i) => (
                    <img
                      key={i}
                      src={img}
                      alt="similar"
                      className="w-20 h-20 object-cover rounded-lg"
                    />
                  ))}
                </div>
              )}

              {/* Explanation */}
              <p className="text-sm text-gray-300">
                {typeof msg.explanation === "string"
                  ? msg.explanation
                  : JSON.stringify(msg.explanation)}
              </p>
            </div>
          );
        }

        // 🔹 NORMAL CHAT MESSAGE
        return (
          <motion.div
            key={index}
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            className={`max-w-[75%] px-4 py-2 rounded-lg text-sm ${
              msg.type === "user"
                ? "bg-blue-500 self-end text-white"
                : "bg-gray-700 self-start text-white"
            }`}
          >
            {msg.text}
          </motion.div>
        );
      })}

      {/* 🔹 Scroll Anchor */}
      <div ref={bottomRef} />
    </div>
  );
}