export default function AnalyzingScreen() {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center text-white bg-black">

      <div className="text-xl mb-6 animate-pulse">
        🔍 Analyzing image...
      </div>

      <div className="text-gray-400 animate-pulse">
        Checking patterns...
      </div>

      <div className="text-gray-500 mt-2 animate-pulse">
        Matching diseases...
      </div>

    </div>
  );
}