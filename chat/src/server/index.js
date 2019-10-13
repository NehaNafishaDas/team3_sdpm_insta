
var app = require('http').createServer()


const io = module.exports.io = require('socket.io')(app)

const PORT = 7000

const SocketManager = require('./SocketManager')

io.on('connection',SocketManager)


app.listen(PORT,()=>{
    console.log('youre are now connected')
})