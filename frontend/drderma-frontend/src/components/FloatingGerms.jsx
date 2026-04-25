export default function FloatingGerms() {
  const images = ["/germ.png", "/germ2.webp", "/germ3.png"];

  const layers = [
    { count: 10, size: [20, 35], speed: [40, 60], opacity: [0.1, 0.2] }, // far
    { count: 8, size: [35, 55], speed: [30, 45], opacity: [0.2, 0.35] }, // mid
    { count: 6,  size: [55, 80], speed: [20, 30], opacity: [0.35, 0.5] }, // near
  ];

  const animations = ["drift1", "drift2", "drift3", "drift4"];

  return (
    <div className="fixed inset-0 z-0 pointer-events-none overflow-hidden">

      {layers.map((layer, layerIndex) =>
        [...Array(layer.count)].map((_, i) => {
          const img = images[Math.floor(Math.random() * images.length)];
          const animation =
            animations[Math.floor(Math.random() * animations.length)];

          const duration =
            Math.random() * (layer.speed[1] - layer.speed[0]) + layer.speed[0];

          const size =
            Math.random() * (layer.size[1] - layer.size[0]) + layer.size[0];

          const opacity =
            Math.random() * (layer.opacity[1] - layer.opacity[0]) +
            layer.opacity[0];

          return (
            <img
              key={`${layerIndex}-${i}`}
              src={img}
              alt=""
             style={{
  position: "absolute",
  width: `${Math.random() * 50 + 40}px`,
  opacity: Math.random() * 0.4 + 0.2,

  left:
    animation === "drift1"
      ? "-10%"
      : animation === "drift2"
      ? "110%"
      : `${Math.random() * 100}%`,

  top:
    animation === "drift3"
      ? "-10%"
      : animation === "drift4"
      ? "110%"
      : `${Math.random() * 100}%`,

  animationName: animation,
  animationDuration: `${duration}s`,
  animationTimingFunction: "linear",
  animationIterationCount: "infinite",
  animationDelay: `-${(i / 20) * duration}s`,
}}
            />
          );
        })
      )}

    </div>
  );
}