import React, { Component } from 'react';
import io from 'socket.io-client'
import { USER_CONNECTED,USERS_CONNECTED, LOGOUT } from '../Events'
import LoginForm from './LoginForm'
import ChatContainer from './chats/ChatContainer'

const socketUrl = "http://localhost:7000"
export default class Layout extends Component {
	
	constructor(props) {
	  super(props);
	
	  this.state = {
	  	socket:null,
		  user:null,
		  users:null
	  };
	}

	componentWillMount() {
		this.initSocket()
	}

	initSocket = ()=>{
		const socket = io(socketUrl)

		socket.on('connect', ()=>{
			console.log("Connected");
		})
		
		this.setState({socket})
	}
	
	setUser = (user)=>{
		const { socket } = this.state
		socket.emit(USER_CONNECTED, user);
		
		this.setState({user})
		
	}

	setUsers = ()=>{
		const { socket } = this.state
		socket.on(USER_CONNECTED,(users)=>{
			this.setState({users})
		})
	}

	/*
	*	Sets the user property in state to null.
	*/
	logout = ()=>{
		const { socket } = this.state
		socket.emit(LOGOUT)
		this.setState({user:null})

	}


	render() {
		const { socket, user ,users} = this.state
		return (
			<div className="container">
				{
					!user ?	
					<LoginForm socket={socket} setUser={this.setUser} setUsers={this.setUsers}/>
					:
					<ChatContainer socket={socket} users = {users} user={user} logout={this.logout}/>
				}
			</div>
		);
	}
}
