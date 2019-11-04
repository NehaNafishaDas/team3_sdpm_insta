import React, { Component } from 'react';
import axios from 'axios';
//import { NavLink } from "react-router-dom";
import '../../styles/index.css'

import InputField from '../InputField'

class SignUp extends Component {
    constructor(props) {
        super(props);
        this.state = { username : "",password : "", errors:[] };
    }


    onSubmit = (e)=>{
        e.persist()
        e.preventDefault()
       
        axios.post(`http://13.82.84.219/signup?password=${this.state.password}&username=${this.state.username}`,{headers:{'Content-Type':'Application/json'}}).then(res=>{
            this.setState({errors:[{username:res.data.username,password:res.data.password}]})

        }).catch(error=>{
            console.log(error)
        })

        this.setState({
            username:'',
            password:'',
            errors:{}
        })
    }

    onChange = (e)=>{
        this.setState({ [e.target.name] : e.target.value})   
    }

    render() {
        const {errors} = this.state
        console.log(this.state)
     
       
        return (
            <div id="body">
                <form class="signup-form" onSubmit = {this.onSubmit}>
                    <div class="insta-logo-type"></div>
                    <InputField errors = {this.state.errors} name="firstname">
                        <input type="text" value = {this.state.username} name="username" class="text-field username" placeholder="Username" onChange={this.onChange}/>
                    </InputField>
                    <InputField errors = {this.state.errors} name="password">
                    <input type="password" value= {this.state.password} name="password" class="text-field password" placeholder="Password" onChange={this.onChange}/>
                    </InputField>
                    <input type="submit" name="signup" class="def-button signup" value="Sign up"/>
                </form>
                <div class="form-pointer">
                    <p class="msg">Have an account?<a href ="/login" class="link">Log in</a></p>
                </div>
	        </div>
        );
    }
}

export default SignUp;