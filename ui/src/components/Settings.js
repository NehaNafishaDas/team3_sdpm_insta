import React, { Component } from 'react';
import axios from 'axios';

class Settings extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }

    componentWillMount(){
        this.isPrivate()
    }

    handleModalClose = ()=>{
        this.props.handleModalClose()
    }

    setPrivate = (e)=>{
        e.preventDefault()
        axios.post(`http://localhost:8081/toggleprivacy`).then(res=>{
            console.log(res)
            this.isPrivate()
        }).catch(error=>{
            console.log(error)
        })
    }

    isPrivate = ()=>{
        axios.get('http://localhost:8081/isprivate').then(res=>{
            console.log(res)
            this.setState({status:res.data.isprivate})
        })
    }

    render() {
        const {status}= this.state

        const  isPrivate = status === "true" ?     <button class="submit" onClick = {this.setPrivate}>Make Private</button> :    <button class="submit" onClick = {this.setPrivate}>Make Public</button>
        return (
            <div class="edit-profile-view active">
            <div class="cancel-icon-white close-view" onClick={this.handleModalClose}></div>
            <form class="edit-profile-form" >
               <p class = "account-privacy"><h4>Acount Privacy</h4></p>
              {isPrivate} <p style = {{marginTop:'-25px',paddingLeft:'130px'}}>Private Account</p>
               <p style = {{color: 'grey',paddingTop:'20px',fontSize:'0.8em'}}>Only people you follow would be able to view your pictures when your account is on private mode.</p>
            </form>
            </div>
        );
    } 
}

export default Settings;