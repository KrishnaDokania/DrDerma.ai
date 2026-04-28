import { useState } from "react";
import axios from "axios";
import { AnimatePresence } from "framer-motion";
import LoadingQuestions from "./components/LoadingQuestions";

import FloatingGerms from "./components/FloatingGerms";
import Landing from "./components/Landing";
import ValidationScreen from "./components/ValidationScreen";
import ValidationResult from "./components/ValidationResult";
import Questionnaire from "./components/Questionnaire";
import ProcessingScreen from "./components/ProcessingScreen";
import ResultScreen from "./components/ResultScreen";

const BASE_URL = "http://localhost:8080/api/image";

export default function App() {
  const [readyToContinue, setReadyToContinue] = useState(false);
  const [transitioning, setTransitioning] = useState(false);

  const [uploadedImage, setUploadedImage] = useState(null);
  const [uploadedImageFile, setUploadedImageFile] = useState(null);

  const [isSkin, setIsSkin] = useState(null);

  const [step, setStep] = useState(1);
  const [currentQuestion, setCurrentQuestion] = useState(null);
  const [validationResult, setValidationResult] = useState(null);
const [stage, setStage] = useState("landing");
  const [answers, setAnswers] = useState([]);
  const [askedQuestions, setAskedQuestions] = useState([]);
  const [candidates, setCandidates] = useState([]);

  const [finalResult, setFinalResult] = useState(null);

  // =====================================================
  // 🔹 Upload → Validate
  // =====================================================
const handleUpload = async (file) => {
  const url = URL.createObjectURL(file);

  setUploadedImage(url);
  setUploadedImageFile(file);

  setValidationResult(null);
  setReadyToContinue(false); // 🔥 reset
  setStage("processing");

  const formData = new FormData();
  formData.append("image", file);

  const startTime = Date.now();

  try {
    const res = await axios.post(`${BASE_URL}/analyze`, formData);

    console.log("FULL RESPONSE:", res.data);

    const elapsed = Date.now() - startTime;
    const minScanTime = 2000 + Math.random() * 1500;
    const remaining = Math.max(0, minScanTime - elapsed);

    setTimeout(() => {

      // 🔥 SHOW RESULT OVERLAY
      setValidationResult(res.data.isSkin);

      // 🔥 WAIT BEFORE NEXT ACTION
      setTimeout(() => {

        if (res.data?.isSkin === false) {
          setStage("validationResult");
          return;
        }

        // ❌ REMOVE auto navigation
        // setStage("questionnaire");

        // 🔥 STORE DATA BUT DON'T MOVE
        setCandidates(res.data.candidates || []);
        setCurrentQuestion(res.data.nextQuestion);

        setStep(1);
        setAnswers([]);
        setAskedQuestions([]);

        // ✅ SHOW CONTINUE BUTTON
        setReadyToContinue(true);

      }, 1500);

    }, remaining);

  } catch (err) {
    console.error(err);
    setStage("landing");
  }
};
  // =====================================================
  // 🔹 After validation → Analyze
  // =====================================================
 const handleContinue = () => {
  setTransitioning(true);

  // first fade out current screen
  setTimeout(() => {
    setStage("loadingQuestions");
    setTransitioning(false);

    // show loader for realistic delay
    setTimeout(() => {
      setStage("questionnaire");
    }, 1200); // adjust 1–2s
  }, 400);
};

  // =====================================================
  // 🔹 Question Loop
  // =====================================================
  const handleNext = async (answer) => {

    const updatedAnswers = [...answers, answer];
    setAnswers(updatedAnswers);

    const payload = {
      question: currentQuestion,
      answer: answer,
      askedQuestions: askedQuestions,
      candidates: candidates
    };

    try {
      const res = await axios.post(`${BASE_URL}/answer`, payload);

      setCandidates(res.data.candidates || []);
      setAskedQuestions([...askedQuestions, currentQuestion]);

      // ================= FINAL RESULT =================
      if (res.data.stage === "final_result") {
        setFinalResult(res.data);
        setStage("result");
        return;
      }

      // ================= MAX QUESTIONS FAILSAFE =================
      if (updatedAnswers.length >= 10) {
        setFinalResult(null); // triggers "unable to diagnose"
        setStage("result");
        return;
      }

      // ================= CONTINUE =================
      setCurrentQuestion(res.data.nextQuestion);
      setStep((prev) => prev + 1);

    } catch (err) {
      console.error(err);
    }
  };

  // =====================================================
  // UI
  // =====================================================
  return (
    <div className="relative min-h-screen bg-gradient-to-br from-indigo-900 via-purple-900 to-black text-white">

      {/* Background */}
      <FloatingGerms />

      {/* Foreground */}
      <div className="relative z-10">

   <AnimatePresence mode="wait">
  {stage === "landing" && (
    <Landing key="landing" onUpload={handleUpload} />
  )}

  {stage === "processing" && (
    <ProcessingScreen
      key="processing"
      image={uploadedImage}
      result={validationResult}
      readyToContinue={readyToContinue}
      onContinue={handleContinue}
      transitioning={transitioning}
    />
  )}

  {stage === "validationResult" && (
    <ValidationResult
      key="validation"
      valid={isSkin}
      onRetry={() => setStage("landing")}
    />
  )}

  {stage === "questionnaire" && (
    <Questionnaire
      key="questionnaire"
      question={currentQuestion}
      step={step}
      total={10}
      onNext={handleNext}
    />
  )}

  {stage === "result" && (
    <ResultScreen
      key="result"
      data={finalResult}
      image={uploadedImage}
      onRestart={() => setStage("landing")}
    />
  )}
  {stage === "loadingQuestions" && (
  <LoadingQuestions key="loading" />
)}
</AnimatePresence>
      </div>
    </div>
  );
}