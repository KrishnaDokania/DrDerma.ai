export default function Navbar() {
  return (
    <div className="fixed top-0 w-full flex justify-between items-center px-10 py-4 bg-black/30 backdrop-blur-md z-50">

      <div className="flex items-center gap-2">
        <div className="bg-yellow-400 text-black px-2 py-1 rounded font-bold">
          AI
        </div>
        <span className="font-semibold">DrDerma</span>
      </div>

      <div className="flex gap-8 text-sm text-gray-300">
        <a href="#home" className="hover:text-yellow-400">Home</a>
        <a href="#about" className="hover:text-yellow-400">About</a>
        <a href="#pricing" className="hover:text-yellow-400">Pricing</a>
      </div>
    </div>
  );
}