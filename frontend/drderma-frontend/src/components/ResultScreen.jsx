import { motion } from "framer-motion";

const diseaseDescriptions = {
  tinea_corporis:
    "A superficial fungal infection that causes ring-shaped, itchy, red patches on the skin.",

  psoriasis:
    "A chronic autoimmune condition causing rapid skin cell buildup and scaling.",

  eczema:
    "An inflammatory skin condition associated with itching, redness, and irritation.",

  rosacea:
    "A chronic facial skin condition causing redness and visible blood vessels.",

  acne:
    "A common skin condition caused by clogged pores and inflammation.",
};

export default function ResultScreen({
  result,
  image,
  onRestart,
}) {

  if (!result) return null;

  const mostLikely = result.mostLikely;

  const confidence =
    Math.round(mostLikely.confidence);

  const diseaseName =
    mostLikely.disease
      .replaceAll("_", " ");

  const description =
    diseaseDescriptions[
      mostLikely.disease
    ] || "AI-generated clinical assessment.";
console.log("RESULT IMAGE:", image);
  return (

    <div className="
      min-h-screen
      flex items-center justify-center
      px-6 py-10
    ">

      <motion.div
        initial={{
          opacity: 0,
          y: 20,
        }}
        animate={{
          opacity: 1,
          y: 0,
        }}
        className="
          w-full max-w-5xl
          grid grid-cols-1 lg:grid-cols-2
          gap-8
        "
      >

        {/* LEFT PANEL */}

        <div className="
          bg-white/10
          backdrop-blur-xl
          border border-white/10
          rounded-3xl
          p-5
          shadow-2xl
        ">

          <div className="
            relative overflow-hidden
            rounded-2xl
          ">

           <img
  src={image || "/placeholder.png"}
              alt="uploaded"
              className="
                w-full
                h-[500px]
                object-contain bg-black/20              "
            />

            <div className="
              absolute top-4 right-4
              px-4 py-2
              rounded-full
              bg-black/50
              backdrop-blur-md
              text-yellow-400
              text-sm
              border border-white/10
            ">
              Processed
            </div>

          </div>

          <button
            onClick={onRestart}
            className="
              mt-6
              w-full
              py-4
              rounded-2xl
              border border-white/10
              bg-white/5
              hover:bg-white/10
              transition-all
              text-white
              text-lg
            "
          >
            Start New Analysis
          </button>

        </div>

        {/* RIGHT PANEL */}

        <div className="
          bg-white/10
          backdrop-blur-xl
          border border-white/10
          rounded-3xl
          p-6
          shadow-2xl
        ">

          {/* CONFIDENCE BADGE */}

          <div className="
            inline-flex
            items-center
            px-5 py-2
            rounded-full
            bg-emerald-500/20
            border border-emerald-400/20
            text-emerald-300
            text-sm
            font-medium
            mb-8
          ">
            High Confidence Result
          </div>

          {/* TITLE */}

          <h1 className="
            text-4xl
            font-bold
            text-white
            capitalize
            mb-4
          ">
            {diseaseName}
          </h1>

          {/* CONFIDENCE */}

          <div className="
            text-yellow-400
            text-2xl
            font-semibold
            mb-10
          ">
            Confidence: {confidence}%
          </div>

          {/* DESCRIPTION */}

          <div className="mb-10">

            <h2 className="
              text-white/50
              uppercase
              tracking-wider
              text-sm
              mb-4
            ">
              Description
            </h2>

            <p className="
              text-white/90
              leading-relaxed
              text-xl
            ">
              {description}
            </p>

          </div>

          {/* RECOMMENDATION */}

          <div className="
            bg-black/20
            border border-white/10
            rounded-2xl
            p-6
            mb-10
          ">

            <h2 className="
              text-white/50
              uppercase
              tracking-wider
              text-sm
              mb-4
            ">
              Clinical Recommendation
            </h2>

            <p className="
              text-white/90
              leading-relaxed
              text-xl
            ">
              A dermatologist consultation is recommended for proper clinical confirmation and treatment planning.
            </p>

          </div>

          {/* INDICATORS */}

          <div>

            <h2 className="
              text-white/50
              uppercase
              tracking-wider
              text-sm
              mb-5
            ">
              Clinical Indicators
            </h2>

            <div className="
              flex flex-wrap gap-3
            ">

              {mostLikely.why?.map(
                (item, index) => (

                  <div
                    key={index}
                    className="
                      px-4 py-3
                      rounded-2xl
                      bg-white/10
                      border border-white/10
                      text-white/90
                    "
                  >
                    {item
                      .replaceAll("_", " ")
                    }
                  </div>

                )
              )}

            </div>

          </div>

        </div>

      </motion.div>

    </div>
  );
}