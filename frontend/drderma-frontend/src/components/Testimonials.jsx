export default function Testimonials() {
  const users = [
    {
      name: "Rahul Sharma",
      img: "https://randomuser.me/api/portraits/men/32.jpg",
      text: "This AI helped me identify my skin issue quickly. Very accurate and easy to use.",
    },
    {
      name: "Ananya Gupta",
      img: "https://randomuser.me/api/portraits/women/44.jpg",
      text: "The questioning system is smart and adaptive. Feels like a real doctor interaction.",
    },
    {
      name: "Amit Verma",
      img: "https://randomuser.me/api/portraits/men/65.jpg",
      text: "Saved me time and money. I got clarity before even visiting a clinic.",
    },
    {
      name: "Sneha Kapoor",
      img: "https://randomuser.me/api/portraits/women/68.jpg",
      text: "Beautiful UI and powerful AI. Definitely feels like a premium product.",
    },
  ];

  return (
    <div className="py-28 text-center bg-transparent relative z-10">

      {/* HEADING */}
      <h2 className="text-3xl font-semibold text-yellow-400 mb-12">
        Testimonials
      </h2>

      {/* SCROLL CONTAINER */}
      <div className="flex gap-7 overflow-x-auto px-10 scrollbar-hide">

        {users.map((user, i) => (
          <div
            key={i}
            className="
              min-w-[280px] h-[260px]
              bg-white/10 backdrop-blur-md
              border border-white/20
              rounded-2xl
              p-6
              flex flex-col justify-between
              hover:scale-105 transition
              shadow-xl
            "
          >

            {/* USER */}
            <div className="flex items-center gap-4">
              <img
                src={user.img}
                alt=""
                className="w-14 h-14 rounded-full border border-white/30"
              />
              <h3 className="font-semibold text-white">
                {user.name}
              </h3>
            </div>

            {/* TEXT */}
            <p className="text-gray-300 text-sm leading-relaxed mt-4">
              "{user.text}"
            </p>

            {/* RATING */}
            <div className="text-yellow-400 text-sm mt-4">
              ⭐⭐⭐⭐⭐
            </div>

          </div>
        ))}

      </div>
    </div>
  );
}