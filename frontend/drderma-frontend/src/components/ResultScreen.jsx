export default function ResultScreen({ data, image, onRestart }) {

  // ================= LOW CONFIDENCE =================
  if (!data || data.confidence < 0.6) {
    return (
      <div className="min-h-screen flex items-center justify-center px-4">

        <div className="bg-white/10 backdrop-blur-lg border border-yellow-400/40 rounded-2xl p-8 max-w-md text-center shadow-xl">

          <h2 className="text-yellow-400 text-xl font-semibold mb-3">
            Unable to Diagnose
          </h2>

          <p className="text-gray-300 mb-6">
            The system could not reach a confident conclusion.  
            Please upload a clearer image or consult a medical professional.
          </p>

          <button
            onClick={onRestart}
            className="bg-yellow-400 text-black px-6 py-2 rounded-lg font-medium hover:scale-105 transition"
          >
            Try Again
          </button>

        </div>
      </div>
    );
  }

  // ================= CONFIDENCE COLOR =================
  const confidence = data.confidence;

  let badgeColor = "text-green-400 border-green-400 bg-green-400/10";
  if (confidence < 0.75) {
    badgeColor = "text-yellow-400 border-yellow-400 bg-yellow-400/10";
  }

  // ================= MAIN RESULT =================
  return (
    <div className="min-h-screen px-6 py-10">

      <div className="max-w-6xl mx-auto grid md:grid-cols-2 gap-10">

        {/* ================= LEFT ================= */}
        <div>

          <div className="relative bg-white/10 border border-white/20 rounded-2xl overflow-hidden shadow-xl">

            <img
              src={image}
              alt="Uploaded"
              className="w-full h-[350px] object-cover"
            />

            {/* STATUS BADGE */}
            <span className="absolute top-3 left-3 bg-black/60 px-3 py-1 rounded-lg text-sm">
              Processed
            </span>

          </div>

          <button
            onClick={onRestart}
            className="mt-6 w-full bg-yellow-400 text-black py-2 rounded-lg font-medium hover:scale-105 transition"
          >
            Start New Analysis
          </button>

        </div>

        {/* ================= RIGHT ================= */}
        <div className="space-y-6">

          {/* HEADER */}
          <div className="bg-white/10 border border-white/20 rounded-2xl p-6 shadow-xl">

            <div className={`inline-block px-3 py-1 text-sm rounded-lg border mb-3 ${badgeColor}`}>
              {confidence >= 0.75 ? "High Confidence" : "Moderate Confidence"}
            </div>

            <h1 className="text-2xl font-bold text-white mb-2">
              {data.topDisease}
            </h1>

            <p className="text-gray-300">
              Confidence: {(confidence * 100).toFixed(1)}%
            </p>

          </div>

          {/* DESCRIPTION */}
          <div className="bg-white/10 border border-white/20 rounded-2xl p-5 shadow-xl">
            <h3 className="text-yellow-400 mb-2 font-semibold">
              Description
            </h3>
            <p className="text-gray-300 text-sm leading-relaxed">
              {data.explanation?.imageEvidence || "No description available."}
            </p>
          </div>

          {/* RECOMMENDATION */}
          <div className="bg-white/10 border border-white/20 rounded-2xl p-5 shadow-xl">
            <h3 className="text-yellow-400 mb-2 font-semibold">
              Recommendation
            </h3>
            <p className="text-gray-300 text-sm">
              Consult a dermatologist if symptoms persist. Maintain hygiene and avoid irritation.
            </p>
          </div>

          {/* CONTEXT */}
          <div className="bg-white/10 border border-white/20 rounded-2xl p-5 shadow-xl">
            <h3 className="text-yellow-400 mb-2 font-semibold">
              Context Provided
            </h3>
            <p className="text-gray-400 text-sm">
              {JSON.stringify(data.explanation?.questionEvidence || {}, null, 2)}
            </p>
          </div>

          {/* SIMILAR IMAGES */}
          {data.similarImages && data.similarImages.length > 0 && (
            <div className="bg-white/10 border border-white/20 rounded-2xl p-5 shadow-xl">

              <h3 className="text-yellow-400 mb-3 font-semibold">
                Similar Cases
              </h3>

              <div className="flex gap-3 overflow-x-auto">
                {data.similarImages.map((img, i) => (
                  <img
                    key={i}
                    src={img}
                    alt="similar"
                    className="w-24 h-24 object-cover rounded-lg border border-white/20"
                  />
                ))}
              </div>

            </div>
          )}

        </div>
      </div>
    </div>
  );
}