import React, { Component } from 'react';

import Post from './Post'
class NavBar extends Component {
    constructor(props) {
        super(props);
        this.state = { activePostModal:false };
    }

    onClickPost = ()=>{
        this.setState({activePostModal:"activePostModal"})
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
                    <li class="link notifications">
                        <div class="like-icon"></div>
                        <ul class="notif-list">
                            <li class="notif clearfix">
                                <div class="avatar-def user-image img1"></div>
                                <div class="notif-info">
                                    <p class="notif-msg"><span class="username">don_prince</span> started following you. <span class="notif-time">1h</span></p>
                                    <button class="def-button follow">follow</button>
                                </div>
                            </li>
                            <li class="notif clearfix">
                                <div class="avatar-def user-image img2"></div>
                                <div class="notif-info">
                                    <p class="notif-msg"><span class="username">shades_of_lin</span> liked your photo. <span class="notif-time">1h</span></p>
                                    <div class="notif-image"></div>
                                </div>
                            </li>
                            <li class="notif clearfix">
                                <div class="avatar-def user-image"></div>
                                <div class="notif-info">
                                    <p class="notif-msg"><span class="username">i_am_2e</span> liked your photo. <span class="notif-time">2h</span></p>
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
                            <li class="notif clearfix">
                                <div class="avatar-def user-image img2"></div>
                                <div class="notif-info">
                                    <p class="notif-msg"><span class="username">shades_of_lin</span> liked your photo. <span class="notif-time">3h</span></p>
                                    <div class="notif-image"></div>
                                </div>
                            </li>
                            <li class="notif clearfix">
                                <div class="avatar-def user-image"></div>
                                <div class="notif-info">
                                    <p class="notif-msg"><span class="username">i_am_2e</span> liked your photo. <span class="notif-time">3h</span></p>
                                    <div class="notif-image"></div>
                                </div>
                            </li>
                        </ul>
                    </li>
                    <a href="/profile"><li class="link user-icon profile"></li></a>
                </ul>
            </div>
            {activePostModal === "activePostModal"? <Post ID={this.props.ID} handleModalClose={this.handleModalClose.bind(this)} /> : null}
        </div>
        );
    }
}

export default NavBar;