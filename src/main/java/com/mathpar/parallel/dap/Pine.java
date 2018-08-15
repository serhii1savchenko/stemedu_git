/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.mathpar.students.ukma17i41.bosa.parallel.engine;
package com.mathpar.parallel.dap;

import java.util.ArrayList;

/**
 *
 * @author alla
 */
public class Pine {
    
    ArrayList<Amin> body;
   
    int state;
    
    public Pine(){
        body = new ArrayList();
    }
    
    public Amin GetCurrentAmin(){
        for(int i = body.size()-1; i<=0; i--){
            if(body.get(i)!=null){
                return body.get(i);    
            }
        }
        return body.get(0);
    } 
    
    public int GetMyAmin(DropTask dp){
        for(int i=0; i<body.size();i++){
            if(body.get(i).branch.contains(dp)){
                return body.get(i).branch.indexOf(dp);
            }
        }
        return 0;
    }
    
    public int CheckState(){
        for(int i=0; i<body.size(); i++){
            if(body.get(i).SetState()!=1){
              return 0;
            }
        }
        return 1;
    }  
    
    public int GetState(){
        return state;
    }
    
    public void SetState(int st){
        state=st;
    }
}
