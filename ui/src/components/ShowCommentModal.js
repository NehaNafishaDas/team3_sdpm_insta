import React, { Component } from 'react';

class ShowCommentModal extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    render() {
        return (
            <div class="edit-profile-view" ref = {this.editProfile}>
			<div class="cancel-icon-white close-view" onClick={this.handleEditModalClose}></div>
			<form class="edit-profile-form" onSubmit = {this.onSubmitProfile}>
				<img src = {this.props.image}/>
			</form>
		</div>
        );
    }
}

export default ShowCommentModal;