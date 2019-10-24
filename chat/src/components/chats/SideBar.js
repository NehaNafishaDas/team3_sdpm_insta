import React, {Component} from 'react';
import {Icon} from 'react-icons-kit'
import {arrowDown} from 'react-icons-kit/fa/arrowDown'
import {listUl} from 'react-icons-kit/fa/listUl'
import {search} from 'react-icons-kit/fa/search'
import {ic_exit_to_app} from 'react-icons-kit/md/ic_exit_to_app'
import '../../index.css';


export default class SideBar extends Component {

    constructor(props){
		super(props)
		this.state = {
			receiver:""
		}
    }
    
	handleSubmit = (e) => {
		e.preventDefault()
		const { receiver } = this.state
		const { onSendPrivateMessage } = this.props
		

		onSendPrivateMessage(receiver)
		
	}

    render() {
		const {chats, activeChat, user, setActiveChat, logout} = this.props
	
		const { receiver } = this.state
		console.log(receiver)
        return(
            <div id="side-bar">
                <div className="heading">
                <div className="app-name"> Our Cool Chat <Icon icon={arrowDown}/></div>
                <div className="menu">
                <Icon icon={listUl}/>
                </div>
                </div>
                <div className="search">
                <form onSubmit={this.handleSubmit} className="search">
                    <i className="search-icon"><Icon icon={search}/></i>
                
                <input placeholder="Search"
                         type="text"
                         value={receiver}
						 onChange={(e)=>{ this.setState({receiver:e.target.value}) }} 
                         
                         />
                <div className="plus"></div>
                </form>
                </div>
                <div
						className="users"
						ref='users'
						onClick={(e)=>{ (e.target === this.refs.user) && setActiveChat(null) }}>

						{
						chats.map((chat)=>{
							if(chat.name){
								if(chat.users){
									const usersArray = Object.values(chat.users)
									for (const userArray of usersArray) {
										console.log(userArray)
											const keys = Object.keys(userArray)
											for (const key of keys) {
												console.log(key)
											const chatSideName =  key !== user.name ? key : "Community"
										
								const lastMessage = chat.messages[chat.messages.length - 1];	
									
									const classNames = (activeChat && activeChat.id === chat.id) ? 'active' : ''
								return(
								<div
									key={chat.id}
									className={`user ${classNames}`}
									onClick={ ()=>{ setActiveChat(chat) } }
									>
									<div className="user-photo">{chatSideName[0].toUpperCase()}</div>
									<div className="user-info">
										<div className="name">{chatSideName}</div>
										{lastMessage && <div className="last-message">{lastMessage.message}</div>}
									</div>

								</div>
							)
								
						} 
					}	
				}
					return null			
							}

							return null
						})
						}

					</div>
               <div className="current-user">
               <span>{user.name}</span>
               <div onClick={()=>{logout()}} title="Logout" className="logout">
                        <Icon icon={ic_exit_to_app} />
               </div>
                
                </div>
                </div>
        )}
    };