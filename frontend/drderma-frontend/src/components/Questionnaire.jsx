import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";

export default function Questionnaire({

  question,

  step,

  total,

  onNext,

  activeDiseases = []
}) {

  const [answer, setAnswer] =
    useState("");

  const progress =
    (step / total) * 100;
console.log(question);
  const renderInput = () => {
   const type =
  question?.type?.toUpperCase();

console.log(type);
    // =====================================
    // BOOLEAN
    // =====================================

    if (
      type === "BOOLEAN"
    ) {

      return (

        <div className="
          grid grid-cols-2 gap-4
        ">

          {["yes", "no"].map((option) => (

            <motion.button
              key={option}

              whileHover={{
                scale: 1.03
              }}

              whileTap={{
                scale: 0.97
              }}

              onClick={() =>
                setAnswer(option)
              }

              className={`
                p-5 rounded-2xl
                border transition-all
                text-lg capitalize
                backdrop-blur-md

                ${answer === option
                  ? "bg-yellow-400 text-black border-yellow-400"
                  : "bg-white/5 text-white border-white/10 hover:border-yellow-400/40"
                }
              `}
            >
              {option}
            </motion.button>
          ))}

        </div>
      );
    }

    // =====================================
    // ENUM
    // =====================================

    if (
      type === "ENUM"
    ) {

      return (

        <div className="
          flex flex-wrap gap-3
        ">

          {question.options?.map(
            (option) => (

              <motion.button
                key={option}

                whileHover={{
                  scale: 1.03
                }}

                whileTap={{
                  scale: 0.96
                }}

                onClick={() =>
                  setAnswer(option)
                }

                className={`
                  px-5 py-3
                  rounded-2xl
                  border transition-all
                  capitalize

                  ${answer === option
                    ? "bg-yellow-400 text-black border-yellow-400"
                    : "bg-white/5 text-white border-white/10 hover:border-yellow-400/30"
                  }
                `}
              >
                {option.replaceAll(
                  "_",
                  " "
                )}
              </motion.button>
            )
          )}

        </div>
      );
    }

    // =====================================
    // NUMBER
    // =====================================

    if (
      type === "NUMBER"
    ) {

      return (

        <input
          type="number"

          value={answer}

          onChange={(e) =>
            setAnswer(
              e.target.value
            )
          }

          placeholder="Enter value..."

          className="
            w-full p-5 rounded-2xl
            bg-white/5 border border-white/10
            text-white text-lg
            focus:outline-none
            focus:border-yellow-400
          "
        />
      );
    }

    // =====================================
    // FALLBACK TEXT
    // =====================================

    return (

      <textarea

        value={answer}

        onChange={(e) =>
          setAnswer(
            e.target.value
          )
        }

        placeholder="
          Describe symptoms...
        "

        className="
          w-full h-32 p-5 rounded-2xl
          bg-white/5 border border-white/10
          text-white resize-none
          focus:outline-none
          focus:border-yellow-400
        "
      />
    );
  };
  console.log(
  "QUESTION:",
  question
);

  return (

    <div className="
      min-h-screen
      flex items-center justify-center
      px-4 py-10
    ">

      <motion.div

        initial={{
          opacity: 0,
          y: 30
        }}

        animate={{
          opacity: 1,
          y: 0
        }}

        transition={{
          duration: 0.5
        }}

        className="
          w-full max-w-3xl
          bg-white/10
          border border-white/10
          backdrop-blur-2xl
          rounded-[32px]
          p-8 md:p-10
          shadow-2xl
        "
      >

        {/* HEADER */}

        <div className="
          flex items-center justify-between
          mb-6
        ">

          <div>

            <div className="
              text-yellow-400
              text-sm tracking-widest
              uppercase mb-2
            ">
              AI Clinical Analysis
            </div>

            <h2 className="
              text-3xl font-bold text-white
            ">
              Adaptive Dermatology Triage
            </h2>

          </div>

          <div className="
            text-white/50 text-sm
          ">
            {step} / {total}
          </div>

        </div>

        {/* PROGRESS */}

        <div className="
          w-full h-3
          bg-white/5
          rounded-full overflow-hidden
          mb-8
        ">

          <motion.div

            initial={{
              width: 0
            }}

            animate={{
              width: `${progress}%`
            }}

            transition={{
              duration: 0.5
            }}

            className="
              h-full bg-yellow-400
            "
          />

        </div>

        {/* ACTIVE DISEASES */}

        <div className="mb-8">

          <div className="
            text-white/40
            uppercase text-xs
            tracking-[3px]
            mb-4
          ">
            Active Differential Diagnosis
          </div>

          <div className="
            flex flex-wrap gap-3
          ">

            <AnimatePresence>

             {
  activeDiseases.map(
    (diseaseObj) => (

      <motion.div

        key={diseaseObj.disease}

        initial={{
          opacity: 0,
          scale: 0.8
        }}

        animate={{
          opacity: 1,
          scale: 1
        }}

        exit={{
          opacity: 0,
          scale: 0.7
        }}

        className="
          px-4 py-2 rounded-2xl
          bg-yellow-400/10
          border border-yellow-400/20
          text-yellow-300
          capitalize text-sm
          flex items-center gap-2
        "
      >

        <span>
          {
            diseaseObj.disease

              .replaceAll("_", " ")

              .replace(
                /\b\w/g,
                c => c.toUpperCase()
              )
          }
        </span>

        <span className="text-yellow-500 text-xs">
          {
            Math.round(
              diseaseObj.score
            )
          }%
        </span>

      </motion.div>
    )
  )
}

            </AnimatePresence>

          </div>

        </div>

        {/* QUESTION */}

        <AnimatePresence mode="wait">

          <motion.div

            key={question?.key}

            initial={{
              opacity: 0,
              y: 15
            }}

            animate={{
              opacity: 1,
              y: 0
            }}

            exit={{
              opacity: 0,
              y: -15
            }}

            transition={{
              duration: 0.35
            }}
          >

            <div className="
              text-white text-2xl
              leading-relaxed
              mb-8
            ">
              {question?.question}
            </div>

            {renderInput()}

          </motion.div>

        </AnimatePresence>

        {/* BUTTON */}

        <motion.button

          whileHover={{
            scale: 1.02
          }}

          whileTap={{
            scale: 0.98
          }}

          disabled={!answer}

          onClick={() => {

            onNext(answer);

            setAnswer("");
          }}

          className="
            w-full mt-10
            bg-yellow-400
            text-black font-semibold
            py-4 rounded-2xl
            text-lg
            disabled:opacity-40
          "
        >
          Continue Analysis
        </motion.button>

      </motion.div>

    </div>
  );
}