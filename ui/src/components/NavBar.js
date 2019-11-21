import React, { Component } from 'react';
import {Link} from 'react-router-dom'
import image from '../img/album.png'
import AlbumMenu from './AlbumMenu'

import Post from './Post'

class NavBar extends Component {
    constructor(props) {
        super(props);
        this.state = { activePostModal:false, activeNotificationModal:false,activeAlbumModal:false };
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
        this.setState({activePostModal:false,activeAlbumModal:false})
    }

    onClickAlbumMenu = ()=>{
        this.setState({activeAlbumModal:'activeAlbumModal'})
    }

    render() {
        const {activePostModal,activeAlbumModal} = this.state
        return (
            <div id="top">
            <div class="topbar clearfix">
                <Link to  = {{pathname: '/home'}}>
                    <ul class="brands clearfix">
                        <li class="insta-logo"></li>
                        <li class="insta-logo-type"></li>
                    </ul>
                </Link>
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
                    <li class = "link" onClick = {this.onClickAlbumMenu}><img src = {image} style = {{maxWidth:26}} onClick = {this.onClickAlbumMenu} alt = "..." /></li>
                </ul>

            </div>
            {activePostModal === "activePostModal"? <Post ID={this.props.ID} avatarMedium = {this.avatarMedium} username = {this.props.username} getFollowersInfo = {this.props.getFollowersInfo} getAccountPicture = {this.props.getAccountPicture} handleModalClose={this.handleModalClose.bind(this)} /> : null}

            {activeAlbumModal==='activeAlbumModal'?<AlbumMenu handleModalClose = {this.handleModalClose}/>:null}
        </div>
        );
    }
}

export default NavBar;