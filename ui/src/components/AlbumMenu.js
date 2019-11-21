import React, { Component } from 'react';

import CreateAlbum from './CreateAlbum'
import AddUserToAlbum from './AddUserToAlbum'
import AddPictureToAlbum from './AddPictureToAlbum'
import AlbumList from './AlbumList'
import RemoveAlbum from './RemoveAlbum'



class AlbumMenu extends Component {
    constructor(props) {
        super(props);
        this.state = { 
            activeCreateAlbum:false,
            activeAddUserToAlbum:false,
            actiiveAddPictureToAlbum:false,
            activeViewAlbum:false,
            activeRemoveAlbum:false
         };
    }
    handleModalClose = ()=>{
        this.props.handleModalClose()
    }

    componentWillMount(){
        this.onCreateAlbum()
    }

    onCreateAlbum = (e)=>{
        this.setState({activeAddUserToAlbum:false})
        this.setState({activeAddPictureToAlbum:false})
        this.setState({activeViewAlbum:false})
        this.setState({activeRemoveAlbum:false}) 
        this.setState({activeCreateAlbum:'activeCreateAlbum'})

    }

    onAddUserToAlbum = ()=>{
        this.setState({activeCreateAlbum:false})
        this.setState({activeAddPictureToAlbum:false})
        this.setState({activeViewAlbum:false})
        this.setState({activeRemoveAlbum:false})
        this.setState({activeAddUserToAlbum:'activeAddUserToAlbum'})
    }

    onAddPictureToAlbum = ()=>{
        this.setState({activeAddUserToAlbum:false})
        this.setState({activeCreateAlbum:false})
        this.setState({activeViewAlbum:false})
        this.setState({activeRemoveAlbum:false}) 
        this.setState({activeAddPictureToAlbum:'activeAddPictureToAlbum'})
    }

    onViewAlbum = ()=>{
        this.setState({activeAddUserToAlbum:false})
        this.setState({activeCreateAlbum:false})
        this.setState({activeAddPictureToAlbum:false})
        this.setState({activeRemoveAlbum:false})
        this.setState({activeViewAlbum:'activeViewAlbum'})
    }

    onRemoveAlbum = ()=>{
        this.setState({activeAddUserToAlbum:false})
        this.setState({activeCreateAlbum:false})
        this.setState({activeAddPictureToAlbum:false})
        this.setState({activeViewAlbum:false})
        this.setState({activeRemoveAlbum:'activeRemoveAlbum'}) 
    }

    render() {
        const {activeAddPictureToAlbum,activeCreateAlbum,activeAddUserToAlbum,activeRemoveAlbum,activeViewAlbum} = this.state
        return (
            <div class = 'edit-profile-view active'>
              <div class="cancel-icon-white close-view" onClick={this.handleModalClose}></div>
                <div class = "edit-profile-form" style = {{height:'75',width:'75%'}}>
                    <ul class = "album-list">
                        <li onClick = {this.onCreateAlbum} >Create An Album</li>
                        <li onClick = {this.onAddUserToAlbum}>Add Users to Album</li>
                        <li onClick = {this.onAddPictureToAlbum}>Add Pictures to Album</li>
                        <li onClick = {this.onViewAlbum}>View Albums</li>
                        <li onClick = {this.onRemoveAlbum}>Remove User From Album</li>
                    </ul>
               { activeCreateAlbum ==='activeCreateAlbum'? <CreateAlbum handleModalClose = {this.handleModalClose}/>:null}
               {activeAddUserToAlbum ==='activeAddUserToAlbum'? <AddUserToAlbum handleModalClose = {this.handleModalClose}/>:null}
               {activeAddPictureToAlbum ==='activeAddPictureToAlbum'? <AddPictureToAlbum handleModalClose = {this.handleModalClose}/>:null}
               {activeRemoveAlbum === 'activeRemoveAlbum'?<RemoveAlbum/>:null}
               {activeViewAlbum==='activeViewAlbum'?<AlbumList/>:null}
                </div>
            </div>
               
            
        );
    }
}

export default AlbumMenu;