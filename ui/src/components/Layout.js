import React, { Component } from 'react';
import SignUp from './Auth/SignUp'
class Layout extends Component {
    constructor(props) {
        super(props);
        this.state = {  };
    }
    render() {
        return (
            <SignUp/>
        );
    }
}

export default Layout;