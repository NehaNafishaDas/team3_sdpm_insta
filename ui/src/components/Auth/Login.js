import React, { Component } from 'react';
import axios from 'axios';


import {Redirect} from 'react-router-dom'
axios.defaults.withCredentials = true;

class Login extends Component {
    constructor(props) {
        super(props);
        this.state = { success: "",loggedInAs: "" };
    }

    onSubmit = (e)=>{
        e.preventDefault()

        axios.post(`http://13.82.84.219/signin?password=${this.state.password}&username=${this.state.username}`,{
            withCredentials: true}).then(res=>{
            console.log(res)
            this.setState({success:res.data.status, error:res.data.error, ID :res.data.loggedInAs,username:res.data.username})
        }).catch(error=>{

        })

    }

    onChange = (e)=>{
        this.setState({[e.target.name]:e.target.value})
    }

    render() {
        const {error} = this.state
        console.log(error)
        return (
        <div id="body">
            <form class="login-form" onSubmit = {this.onSubmit}>
           
                <div class="insta-logo-type"></div>
                {  this.state.error ? <p className="custom-input-error">{error}</p> : null}
                <input type="text" name="username" class="text-field username" placeholder="Username" onChange = {this.onChange}/>
                <input type="password" name="password" class="text-field password" placeholder="Password" onChange = {this.onChange}/>
                <input type="submit" name="login" class="def-button login" value="Login"/>
            </form>
            <div class="form-pointer">
                <p class="msg">Don't have an account? <a href="/" class="link">Sign up</a></p>
            </div>
            {this.state.success === "success"?<Redirect to={{pathname: '/home',state: {ID:this.state.ID,username:this.state.username}}} /> : null}
    	</div>
        );
    }
}

export default Login;