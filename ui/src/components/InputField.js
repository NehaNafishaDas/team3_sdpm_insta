import React, {Component} from 'react'

import '../styles/index.css'

class InputField extends Component {

 
 render(){

      var error = false;
      var errors = {
          fieldName : {

          }
      }

 
            return(
                    <div className="col s6" >

                    {this.props.children}

                    {
                        error ? <p className="custom-input-error">{error}</p> : null
                    }
                    </div>
                

            )
  }

}
  
 export default InputField