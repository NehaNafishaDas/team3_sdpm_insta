import React, { Component } from 'react';
import  io from 'socket.io-client'

const socketURL = "http://localhost:7000"

class Layouts extends Component {
    constructor(props) {
        super(props);
        this.state = { 
            socket:null
         };
    }
    UNSAFE_componentWillMount(){
        this.initSocket()
    }
    initSocket = () =>{
        const socket = io(socketURL)
        socket.on('connect',()=>{
            console.log('connected')
        })
        this.setState({
           socket
        })
      
    }
    render() { 
        return (
            <div class = "container">
                <p>sanya babatunde</p>
            </div>
        );
    }
}

export default Layouts;