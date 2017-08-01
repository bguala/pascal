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
public abstract class Simbolo {
    
    protected String lexema;
    protected int espacio_asignado;
    
    //-----------------------------------------------------------------------------------
    //--- Constructores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Simbolo (){
        this.lexema="";
        this.espacio_asignado=1;
    }
    
    public Simbolo (String lexema, int espacio_asignado){
        this.lexema=lexema;
        this.espacio_asignado=espacio_asignado;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String get_lexema (){
        return this.lexema;
    }
    
    public int get_espacio_asignado (){
        return this.espacio_asignado;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_lexema (String lex){
        this.lexema=lex;
    }
    
    public void set_espacio_asignado (int espacio){
        this.espacio_asignado=espacio;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String a_cadena (){
        return this.lexema+" , "+this.espacio_asignado;
    }
    
}
