const express = require('express');
const app = express();
const mysql = require('mysql');

// Configure your MySQL database connection
const db = mysql.createConnection({
  host: 'your_database_host',
  user: 'your_username',
  password: 'your_password',
  database: 'your_database_name'
});

// Connect to the database
db.connect(err => {
  if (err) {
    console.error('Database connection error:', err);
  } else {
    console.log('Connected to the database');
  }
});

// Create a route to fetch and serve the PDF
app.get('/pdf/:pdfId', (req, res) => {
  const pdfId = req.params.pdfId;

  // Retrieve the PDF data from the database
  db.query('SELECT pdf_data FROM pdf_table WHERE id = ?', [pdfId], (err, results) => {
    if (err) {
      console.error('Error fetching PDF data:', err);
      res.status(500).send('Internal Server Error');
    } else {
      // Set the appropriate headers and send the PDF data
      res.setHeader('Content-Type', 'application/pdf');
      res.send(results[0].pdf_data);
    }
  });
});

// Start the server
app.listen(3000, () => {
  console.log('Server listening on port 3000');
});
