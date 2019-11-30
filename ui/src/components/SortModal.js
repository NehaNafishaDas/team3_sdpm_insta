import React, { Component } from 'react';

class SortModal extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    handleModalClose=()=>{
        this.props.handleModalClose()
    }

   onSubmit = (e)=>{
        this.props.onSubmit(e)
   }

    onChange = (e)=>{
        this.props.onChange(e)
    }
    render() {
        return (
            <div class="edit-profile-view active">
            <div class="cancel-icon-white close-view" onClick={this.handleModalClose}></div>
            <form class="edit-profile-form" onSubmit = {this.onSubmit}>
           
            <select name = "sort" onChange = {this.onChange}  class="name text-field">
                <option selected>how do you want to sort the images?</option>
                <option option name = "sort" onChange = {this.onChange}>likes</option>
                <option option name = "sort" onChange = {this.onChange}>sort</option>
                <option option name = "sort" onChange = {this.onChange}> ascending</option>
                <option option name = "sort" onChange = {this.onChange}>descending</option>
              
            </select>
            <input type="submit" name="submit" class="def-button submit" value="Submit"/>
            
            </form>
            </div>
        );
    }
}

export default SortModal;