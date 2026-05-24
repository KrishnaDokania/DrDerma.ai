import { useState } from "react";
import axios from "axios";
import { AnimatePresence, motion } from "framer-motion";

import LoadingQuestions from "./components/LoadingQuestions";
import FloatingGerms from "./components/FloatingGerms";
import Landing from "./components/Landing";
import ValidationResult from "./components/ValidationResult";
import Questionnaire from "./components/Questionnaire";
import ProcessingScreen from "./components/ProcessingScreen";
import ResultScreen from "./components/ResultScreen";

const BASE_URL =
  "https://drderma-backend.onrender.com";

export default function App() {

  // =====================================================
  // STATE
  // =====================================================

  const [readyToContinue,
    setReadyToContinue] =
    useState(false);

  const [transitioning,
    setTransitioning] =
    useState(false);

  const [uploadedImage,
    setUploadedImage] =
    useState(null);

  const [validationResult,
    setValidationResult] =
    useState(null);

  const [stage,
    setStage] =
    useState("landing");

  const [step,
    setStep] =
    useState(1);

  const [currentQuestion,
    setCurrentQuestion] =
    useState(null);

  const [activeDiseases,
    setActiveDiseases] =
    useState([]);

  const [sessionId,
    setSessionId] =
    useState(null);

  const [loadingQuestion,
    setLoadingQuestion] =
    useState(false);

  const [finalResult,
    setFinalResult] =
    useState(null);

  // =====================================================
  // IMAGE UPLOAD
  // =====================================================

  const handleUpload =
    async (file) => {

      // IMAGE PREVIEW

      const imageBase64 =
        await new Promise(
          (resolve) => {

            const reader =
              new FileReader();

            reader.onload = () =>
              resolve(
                reader.result
              );

            reader.readAsDataURL(
              file
            );
          }
        );

      setUploadedImage(
        imageBase64
      );

      setValidationResult(
        null
      );

      setReadyToContinue(
        false
      );

      setStage(
        "processing"
      );

      const formData =
        new FormData();

      formData.append(
        "image",
        file
      );

      try {

        // =====================================
        // ANALYZE IMAGE
        // =====================================

        const res =
          await axios.post(

            `${BASE_URL}/analyze`,

            formData
          );

        console.log(
          "ANALYZE RESPONSE:",
          res.data
        );

        // =====================================
        // MOCK SKIN VALIDATION
        // =====================================

        setTimeout(() => {

          setValidationResult(
            true
          );

          setTimeout(() => {

            setCurrentQuestion(
              res.data.question
            );

            setSessionId(
              res.data.sessionId
            );

            setActiveDiseases(
              res.data
                .activeDiseases || []
            );

            setStep(1);

            setReadyToContinue(
              true
            );

          }, 1200);

        }, 2000);

      } catch (err) {

        console.error(err);

        setStage("landing");
      }
    };

  // =====================================================
  // CONTINUE
  // =====================================================

  const handleContinue =
    () => {

      setTransitioning(true);

      setTimeout(() => {

        setStage(
          "loadingQuestions"
        );

        setTransitioning(
          false
        );

        setTimeout(() => {

          setStage(
            "questionnaire"
          );

        }, 1200);

      }, 400);
    };

  // =====================================================
  // QUESTION FLOW
  // =====================================================

  const handleNext =
    async (answer) => {

      if (!currentQuestion)
        return;

      setLoadingQuestion(
        true
      );

      try {

        // =====================================
        // SEND ANSWER
        // =====================================

        const res =
          await axios.post(

            `${BASE_URL}/answer?sessionId=${sessionId}&signal=${currentQuestion.key}&answer=${answer}`
          );

        const data =
          res.data;

        console.log(
          "ANSWER RESPONSE:",
          data
        );

        setLoadingQuestion(
          false
        );

        // =====================================
        // FINAL RESULT
        // =====================================

        if (data.finished) {

          setFinalResult(
            data
          );

          setStage(
            "result"
          );

          return;
        }

        // =====================================
        // NEXT QUESTION
        // =====================================

        if (data.question) {

          setCurrentQuestion(
            data.question
          );

          setActiveDiseases(
            data
              .activeDiseases || []
          );

          setStep(
            (prev) =>
              prev + 1
          );

          return;
        }

      } catch (err) {

        console.error(err);

        setLoadingQuestion(
          false
        );
      }
    };

  // =====================================================
  // UI
  // =====================================================

  return (

    <div className="
      relative min-h-screen
      bg-gradient-to-br
      from-indigo-950
      via-purple-950
      to-black
      text-white
      overflow-hidden
    ">

      {/* BACKGROUND */}

      <FloatingGerms />

      {/* LOADING */}

      {loadingQuestion && (

        <div className="
          fixed inset-0 z-50
          bg-black/60
          backdrop-blur-md
          flex items-center justify-center
        ">

          <motion.div

            initial={{
              opacity: 0,
              scale: 0.95
            }}

            animate={{
              opacity: 1,
              scale: 1
            }}

            className="
              bg-white/10
              border border-white/10
              rounded-3xl
              px-10 py-8
              shadow-2xl
            "
          >

            <div className="
              text-2xl
              text-white
              animate-pulse
            ">
              AI analyzing
              clinical patterns...
            </div>

          </motion.div>

        </div>
      )}

      {/* MAIN */}

      <div className="
        relative z-10
      ">

        <AnimatePresence
          mode="wait"
        >

          {/* LANDING */}

          {stage ===
            "landing" && (

            <Landing
              key="landing"
              onUpload={
                handleUpload
              }
            />
          )}

          {/* PROCESSING */}

          {stage ===
            "processing" && (

            <ProcessingScreen

              key="processing"

              image={
                uploadedImage
              }

              result={
                validationResult
              }

              readyToContinue={
                readyToContinue
              }

              onContinue={
                handleContinue
              }

              transitioning={
                transitioning
              }
            />
          )}

          {/* VALIDATION */}

          {stage ===
            "validationResult" && (

            <ValidationResult

              key="validation"

              valid={
                validationResult
              }

              onRetry={() =>
                setStage(
                  "landing"
                )
              }
            />
          )}

          {/* LOADING QUESTIONS */}

          {stage ===
            "loadingQuestions" && (

            <LoadingQuestions
              key="loading"
            />
          )}

          {/* QUESTIONNAIRE */}

          {stage ===
            "questionnaire" && (

            <Questionnaire

              key="questionnaire"

              question={
                currentQuestion
              }

              step={step}

              total={10}

              activeDiseases={
                activeDiseases
              }

              onNext={
                handleNext
              }
            />
          )}

          {/* RESULT */}

          {stage ===
            "result" && (

            <ResultScreen

              key="result"

              result={
                finalResult
              }

              image={
                uploadedImage
              }

              onRestart={() => {

                setStage(
                  "landing"
                );

                setFinalResult(
                  null
                );

                setCurrentQuestion(
                  null
                );

                setStep(1);

                setUploadedImage(
                  null
                );

                setSessionId(
                  null
                );
              }}
            />
          )}

        </AnimatePresence>

      </div>

    </div>
  );
}