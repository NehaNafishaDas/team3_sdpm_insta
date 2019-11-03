import React, { Component } from 'react';
import {Link} from 'react-router-dom'

import Post from './Post'
class NavBar extends Component {
    constructor(props) {
        super(props);
        this.state = { activePostModal:false, activeNotificationModal:false };
        this.notificationList = React.createRef();
        this.onclickNotification = this.onclickNotification.bind(this)
    }

    onClickPost = ()=>{
        this.setState({activePostModal:"activePostModal"})
    }

    onclickNotification(){
        this.notificationList.current.classList.toggle("notif-list-active");
    }

    handleModalClose = ()=>{
        this.setState({activePostModal:false})
    }

    render() {
        const {activePostModal} = this.state
        return (
            <div id="top">
            <div class="topbar clearfix">
                <a href="index.html">
                    <ul class="brands clearfix">
                        <li class="insta-logo"></li>
                        <li class="insta-logo-type"></li>
                    </ul>
                </a>
                <input type="text" class="text-field search" placeholder="Search"/>
                <ul class="links">
                    <li class="link explore-icon explore" onClick = {this.onClickPost}></li>
                    <li class="link notifications" >
                        <div class="like-icon" onClick = {this.onclickNotification}></div>
                        <ul class="notif-list" ref = {this.notificationList}>
                            <li class="notif clearfix">
                                <div class="avatar-def user-image img2"></div>
                                <div class="notif-info">
                                    <p class="notif-msg"><span class="username">shades_of_lin</span> liked your photo. <span class="notif-time">1h</span></p>
                                    <div class="notif-image"></div>
                                </div>
                            </li>
                           
                            <li class="notif clearfix">
                                <div class="avatar-def user-image img1"></div>
                                <div class="notif-info">
                                    <p class="notif-msg"><span class="username">don_prince</span> started following you. <span class="notif-time">3h</span></p>
                                    <button class="def-button follow">follow</button>
                                </div>
                            </li>
                           
                        
                        </ul>
                    </li>
                 

                    <Link to={{pathname: '/profile',state: {username:this.props.username}}} username = {this.props.username} activeClassName="current"><li class="link user-icon profile"></li></Link>
                </ul>

            </div>
            {activePostModal === "activePostModal"? <Post ID={this.props.ID} getAccountPicture = {this.props.getAccountPicture} handleModalClose={this.handleModalClose.bind(this)} /> : null}
        </div>
        );
    }
}

export default NavBar;