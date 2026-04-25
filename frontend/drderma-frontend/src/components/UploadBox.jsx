export default function UploadBox({ onUpload }) {

  const handleChange = (e) => {
    const file = e.target.files[0];

    console.log("FILE SELECTED:", file); // ✅ now valid

    if (file) {
      onUpload(file);
    }
  };

  return (
    <div className="mb-4">
      <input
        type="file"
        accept="image/*"
        onChange={handleChange}
        className="p-2 border rounded"
      />
    </div>
  );
}