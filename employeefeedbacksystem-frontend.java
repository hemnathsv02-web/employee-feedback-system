import React, { useState } from "react";
import axios from "axios";

export default function FeedbackForm({ employeeId }) {
  const [form, setForm] = useState({ reviewer: "", comments: "", rating: 0 });

  const submitFeedback = async () => {
    if (!form.reviewer || !form.rating) {
      alert("Please fill all fields!");
      return;
    }
    try {
      await axios.post("/api/feedback", { employeeId, ...form });
      alert("Feedback submitted successfully!");
    } catch (err) {
      alert("Error submitting feedback. Please try again.");
    }
  };

  return (
    <div className="feedback-form">
      <h2>Employee Feedback</h2>
      <input
        placeholder="Reviewer Name"
        onChange={(e) => setForm({ ...form, reviewer: e.target.value })}
      />
      <textarea
        placeholder="Enter Comments"
        onChange={(e) => setForm({ ...form, comments: e.target.value })}
      ></textarea>
      <input
        type="number"
        placeholder="Rating (1-5)"
        min="1"
        max="5"
        onChange={(e) => setForm({ ...form, rating: Number(e.target.value) })}
      />
      <button onClick={submitFeedback}>Submit</button>
    </div>
  );
}
