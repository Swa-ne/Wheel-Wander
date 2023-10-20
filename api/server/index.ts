import express, {Express, Request, Response} from "express"
import bodyParser from "body-parser"

import dotenv from "dotenv"
import path from "path"
dotenv.config()

const socket = require("socket.io")
const entryRoutes = require("./routes/entry")
const messageRoutes = require("./routes/message")
const marketRoutes = require("./routes/market")


const app = express();
const port = 3000;

app.use(express.json())
app.use(
  bodyParser.urlencoded({
    extended: true,
  }),
);

app.use("/images", express.static(path.join(__dirname, '../Images')));

app.use("/message/", messageRoutes)
app.use("/entry/", entryRoutes)
app.use("/market/", marketRoutes)

app.get('/', (req, res) => {
  res.send('Hello from your Node.js Express server!');
});

const server = app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});


const io = socket(server, {
  cors: {
    origin: "*",
    credentials: true,
  },
});

io.on("connection", (socket : any) => {
  socket.on("send-msg", (data : any) => {
    data = JSON.parse(data)
    console.log(data, data.senderID)
    socket.to(`CHATID-${data.senderID}`).emit("msg-recieve", data.msg)
  })
})