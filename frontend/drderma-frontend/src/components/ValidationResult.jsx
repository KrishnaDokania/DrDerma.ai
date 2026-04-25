export default function ValidationResult({ valid, onRetry }) {

  if (valid) return null;

  return (
    <div className="min-h-screen flex items-center justify-center px-4">

      <div className="bg-red-500/10 backdrop-blur-lg border border-red-400/40 rounded-2xl p-8 max-w-md text-center shadow-xl">

        {/* ICON */}
        <div className="text-red-400 text-4xl mb-4">
          ⚠
        </div>

        {/* TITLE */}
        <h2 className="text-xl font-semibold text-white mb-3">
          Invalid Image Type
        </h2>

        {/* DESCRIPTION */}
        <p className="text-gray-300 text-sm leading-relaxed mb-6">
          Our model determined that the uploaded image does not contain clear
          skin structures. Please upload a focused image of the affected skin area.
        </p>

        {/* BUTTON */}
        <button
          onClick={onRetry}
          className="w-full bg-red-500/20 border border-red-400/40 text-white py-2 rounded-lg hover:bg-red-500/30 transition"
        >
          ↻ Try Another Image
        </button>

      </div>

    </div>
  );
}