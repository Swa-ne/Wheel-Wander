import express, {Express, Request, Response} from "express"
import bodyParser from "body-parser"

import dotenv from "dotenv"
dotenv.config()

const entryRoutes = require("./routes/entry")


const app = express();
const port = 3000;

app.use(express.json())
app.use(
  bodyParser.urlencoded({
    extended: true,
  }),
);

app.use("/entry/", entryRoutes)

app.get('/', (req, res) => {
  res.send('Hello from your Node.js Express server!');
});

app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
