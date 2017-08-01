/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simbolo;

import tipos.*;

/**
 *
 * @author Bruno
 */
public class Variable extends Simbolo {
    
    private Tipo tipo_dato;
    private String dato;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Variable (Tipo tipo_dato){
        super("",1);
        this.tipo_dato=tipo_dato;
        this.dato="";
    }
    
    public Variable(String lexema, int espacio_asignado, Tipo tipo_dato){
        super(lexema,espacio_asignado);
        this.tipo_dato=tipo_dato;
        this.dato="";
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Tipo get_tipo_dato (){
        return this.tipo_dato;
    }
    
    public String get_dato (){
        return this.dato;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_tipo_dato (Tipo tipo_dato){
        this.tipo_dato=tipo_dato;
    }
    
    public void set_dato (String dato){
        this.dato=dato;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String a_cadena (){
        return super.a_cadena()+" , "+this.tipo_dato.a_cadena();
    }
    
}
