import React from 'react';
import { BrowserRouter, Route } from 'react-router-dom'
import Layout from './components/Layout'
import Login from './components/Auth/Login'
import Home from './components/Home'
import Profile from './components/Profile'
import OtherUserProfiles from './components/OtherUserProfiles'
import ViewAlbum from './components/ViewAlbum'
import ViewPicturesInAlbum from './components/ViewPicturesInAlbum'
import CommentInfo from './components/CommentInfo'

function App() {
  return (
    <div className="container">
      <BrowserRouter>
       <Route exact path = "/" component = {Layout}/>
       <Route path  = "/login" component = {Login}/>
       <Route path  = "/home" component = {Home}/>
       <Route path  = "/profile" component = {Profile}/>
       <Route path  = "/otherusers" component = {OtherUserProfiles}/>
       <Route path = "/album" component  = {ViewAlbum} />
       <Route path = "/picture" component  = {ViewPicturesInAlbum} />
       <Route path = "/commentinfo" component = {CommentInfo} />
       </BrowserRouter>
    </div>
  );
}

export default App;
