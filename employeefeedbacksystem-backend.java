const express = require("express");
const bodyParser = require("body-parser");
const { Sequelize, DataTypes } = require("sequelize");
const app = express();
app.use(bodyParser.json());

// Database connection
const sequelize = new Sequelize("feedbackdb", "user", "password", {
  host: "localhost",
  dialect: "postgres",
});

// Model definitions
const Employee = sequelize.define("Employee", {
  name: DataTypes.STRING,
  avgRating: { type: DataTypes.FLOAT, defaultValue: 0 },
});

const Feedback = sequelize.define("Feedback", {
  employeeId: DataTypes.INTEGER,
  reviewer: DataTypes.STRING,
  comments: DataTypes.TEXT,
  rating: DataTypes.FLOAT,
});

// Feedback submission API
app.post("/api/feedback", async (req, res) => {
  const { employeeId, reviewer, comments, rating } = req.body;
  try {
    await Feedback.create({ employeeId, reviewer, comments, rating });

    // Calculate average rating
    const allFeedback = await Feedback.findAll({ where: { employeeId } });
    const avg =
      allFeedback.reduce((sum, fb) => sum + fb.rating, 0) /
      allFeedback.length;

    await Employee.update({ avgRating: avg }, { where: { id: employeeId } });

    res.json({ message: "Feedback stored successfully", avgRating: avg });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: "Server error" });
  }
});

// Start server
app.listen(3000, async () => {
  await sequelize.sync();
  console.log("Employee Feedback System running on port 3000");
});
