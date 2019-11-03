import React, { Component } from 'react';
import NavBar from './NavBar'
import axios from 'axios'


class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    componentWillMount(){
        this.checkLogin()
     
    }

    checkLogin(){
        console.log(axios)
        axios.get('http://13.82.84.219/loginstatus').then(res=>{
            console.log(res.data.status)
                this.setState({username:res.data.user.username})
            if(res.data.status === "notloggedin"){
                this.props.history.push("/login")
            }
        }).catch(error=>{
            console.log(error)
        })
    }


    render() {
       
        return (
            <div class = "container">
                <NavBar ID = {this.props.ID} username = {this.state.username}/>
                <div id="body">
                    <ul class="post-list">
                        <li class="post">
                            <div class="post-header clearfix">
                                <div class="user-image avatar-def img1"></div>
                                <h4 class="username">muheez_akanni</h4>
                                <h4 class="post-time">2h</h4>
                            </div>
                            <div class="post-image"></div>
                            <h4 class="post-likes">364 likes</h4>
                            <p class="post-caption"><span class="username">muheez_akanni</span> This picture is great!</p>
                            <ul class="post-comments">
                                <li class="comment"><span class="username">lolade_money</span> I know right</li>
                                <li class="comment"><span class="username">oladips</span> Awesome!</li>
                            </ul>
                            <div class="post-actions clearfix">
                                <div class="like-icon like-post"></div>
                                <input type="text" name="comment" class="comment text-field" placeholder="Add a comment..."/>
                            </div>
                        </li>
                        <li class="post">
                            <div class="post-header clearfix">
                                <div class="user-image avatar-def img2"></div>
                                <h4 class="username">blawz_</h4>
                                <h4 class="post-time">3h</h4>
                            </div>
                            <div class="post-image"></div>
                            <h4 class="post-likes">85 likes</h4>
                            <p class="post-caption"><span class="username">blawz_</span> As if you were on fire from within</p>
                            <ul class="post-comments">
                                <li class="comment"><span class="username">lolade_money</span> I know right</li>
                                <li class="comment"><span class="username">oladips</span> Awesome!</li>
                            </ul>
                            <div class="post-actions clearfix">
                                <div class="like-icon like-post"></div>
                                <input type="text" name="comment" class="comment text-field" placeholder="Add a comment..."/>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            
        );
    }
}

export default Home;