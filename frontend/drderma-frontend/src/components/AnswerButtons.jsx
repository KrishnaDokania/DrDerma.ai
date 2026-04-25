export default function AnswerButtons({ onAnswer, loading }) {
  return (
    <div className="flex gap-4 mt-4 justify-center">
     <button
  disabled={loading}
  className="px-5 py-2 bg-green-500 text-white rounded-lg transition transform hover:scale-105 active:scale-95 disabled:opacity-50"
      >
        Yes
      </button>

     <button
  disabled={loading}
  className="px-5 py-2 bg-green-500 text-white rounded-lg transition transform hover:scale-105 active:scale-95 disabled:opacity-50"
>
        No
      </button>
    </div>
  );
}