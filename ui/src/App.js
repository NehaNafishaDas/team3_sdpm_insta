import React from 'react';
import { BrowserRouter, Route } from 'react-router-dom'
import Layout from './components/Layout'
import Login from './components/Auth/Login'
import Home from './components/Home'
import Profile from './components/Profile'
import OtherUserProfiles from './components/OtherUserProfiles'

function App() {
  return (
    <div className="container">
      <BrowserRouter>
       <Route exact path = "/" component = {Layout}/>
       <Route path  = "/login" component = {Login}/>
       <Route path  = "/home" component = {Home}/>
       <Route path  = "/profile" component = {Profile}/>
       <Route path  = "/otherusers" component = {OtherUserProfiles}/>
       </BrowserRouter>
    </div>
  );
}

export default App;
