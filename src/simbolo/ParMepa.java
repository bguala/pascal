/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simbolo;

/**
 *
 * @author Bruno
 */
public class ParMepa {
    
    private String etiqueta;
    private String inst;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public ParMepa (String etiqueta, String inst){
        this.etiqueta=etiqueta;
        this.inst=inst;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores-------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String get_etiqueta (){
        return this.etiqueta;
    }
    
    public String get_inst (){
        return this.inst;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_etiqueta (String etiqueta){
        this.etiqueta=etiqueta;
    }
    
    public void set_inst (String inst){
        this.inst=inst;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String a_cadena (){
        return this.etiqueta+this.inst;
    }
    
}
