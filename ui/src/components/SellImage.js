import React, { Component } from 'react';
import images from '../img/img1.jpg';
import NavBar from './NavBar'
import SellImageModal from './SellImageModal'

class SellImage extends Component {
    constructor(props) {
        super(props);
        this.state = { activeSellImage:false };
    }

    onClickSellImage = () =>{
        this.setState({activeSellImage:"activeSellImage"})
    }

    handleModalClose = ()=>{
        this.setState({activeSellImage:false})
    }

    render() {
        const {activeSellImage} = this.state
        return (
            <div >  
            <NavBar/>
            <ul class="user-post-list">    
            <li class="user-post" onClick = {this.onClickSellImage} style={{backgroundImage : "url(" + images + ")",backgroundSize : "cover",backgroundPosition : 'center', marginTop:'150px', marginLeft:'50px'}}></li>        
            </ul> 
            {activeSellImage === "activeSellImage"?<SellImageModal handleModalClose = {this.handleModalClose}/>:null}
            </div> 

        );
    }
}

export default SellImage;