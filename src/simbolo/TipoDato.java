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
public class TipoDato extends Simbolo {
    
    private String nombre_tipo; //Guarda el nombre de tipo, ej: entero, integer, boolean, racional etc.
    private String tipo_base; //Integer o boolean.
    private String caracteristica; //Primitivo o definido por el usuario.
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public TipoDato (String lexema, int espacio_asignado,String tipo_base, String caracteristica){
        super(lexema,espacio_asignado); //El nombre del nuevo tipo se guarda en lexema (Simbolo)
        this.tipo_base=tipo_base;
        this.caracteristica=caracteristica;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
        
    public String get_caracteristica (){
        return this.caracteristica;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
       
    public void set_caracteristica (String caracteristica){
        this.caracteristica=caracteristica;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String a_cadena (){
        return super.a_cadena()+" , "+this.caracteristica;
    }
    
}
