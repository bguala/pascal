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
public class Arreglo extends Simbolo {
    
    private Tipo tipo_dato;
    private int cantidad_elementos;
    private int limite_inferior;
    private int limite_superior;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Arreglo (String lexema, int espacio_asignado, Tipo tipo_dato, int c, int li, int ls){
        super(lexema,espacio_asignado);
        this.tipo_dato=tipo_dato;
        this.cantidad_elementos=c;
        this.limite_inferior=li;
        this.limite_superior=ls;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Tipo get_tipo_dato (){
        return this.tipo_dato;
    }
    
    public int get_cantidad_elementos (){
        return this.cantidad_elementos;
    }
    
    public int get_limite_inferior (){
        return this.limite_inferior;
    }
    
    public int get_limite_superior (){
        return this.limite_superior;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_tipo_dato (Tipo tipo_dato){
        this.tipo_dato=tipo_dato;
    }
    
    public void set_cantidad_elementos (int cantidad){
        this.cantidad_elementos=cantidad;
    }
    
    public void set_limite_inferior (int li){
        this.limite_inferior=li;
    }
    
    public void set_limite_superior (int ls){
        this.limite_superior=ls;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String a_cadena (){
        return super.a_cadena()+" , "+this.limite_inferior+" , "+this.limite_superior+" , "+this.tipo_dato.a_cadena();
    }
    
}
