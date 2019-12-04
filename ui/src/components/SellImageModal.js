import React, { Component } from 'react';


class SellImageModal extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    handleModalClose = ()=>{
        this.props.handleModalClose()
    }
    render() {
        return (
            <div class="edit-profile-view active">
            <div class="cancel-icon-white close-view" onClick={this.handleModalClose}></div>
            <form class="edit-profile-form"onSubmit = {this.onSubmit}>
             
      
               
                <label for="name"> Amount</label>
                <input type="text" name="tags" class="name text-field" placeholder="enter your amount" onChange = {this.onChange} />
               
                <input type="submit" name="submit" class="def-button submit" value="Submit"/>
                
            </form>
            </div>
        );
    }
}

export default SellImageModal;