import React, { Component } from 'react';
import axios from 'axios';
import {Redirect} from 'react-router-dom'

class Settings extends Component {
    constructor(props) {
        super(props);
        this.state = { activeSellImageModal:false };
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

    sellImage = () =>{
        this.setState({activeSellImageModal:"activeSellImageModal"})
       // this.handleModalClose()
    }

    render() {
        const {status,activeSellImageModal}= this.state

        const  isPrivate = status === "true" ?     <button class="submit" onClick = {this.setPrivate}>Make Public</button> :    <button class="submit" onClick = {this.setPrivate}>Make Private</button>
        return (
            <div class="edit-profile-view active">
            <div class="cancel-icon-white close-view" onClick={this.handleModalClose}></div>
            <form class="edit-profile-form" >
               <p class = "account-privacy"><h4>Acount Privacy</h4></p>
               {isPrivate} <p style = {{marginTop:'-25px',paddingLeft:'130px'}}>Private Account</p>
               <p style = {{color: 'grey',paddingTop:'20px',fontSize:'0.8em'}}>Only people you follow would be able to view your pictures when your account is on private mode.</p>
               <br/>
               <hr/>
               {/* <p class = "account-privacy"><h4>Image Selling</h4></p>
               <button class="submit" onClick = {this.sellImage}>Sell</button> <p style = {{marginTop:'-25px',paddingLeft:'85px'}}>Sell Image</p>
               <p style = {{color: 'grey',paddingTop:'20px',fontSize:'0.8em'}}>Select which of your images you would like to sell and put on the market </p>
                */}
            </form>
            {activeSellImageModal === "activeSellImageModal"?<Redirect to = {{pathname:"/sellimage"}}/>:null}
            </div>
        );
    } 
}

export default Settings;