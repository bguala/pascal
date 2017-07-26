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
public class Subrango extends Simbolo {
    
    private String limite_inferior;
    private String limite_superior;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Subrango (String lexema, int espacio_asignado, String li, String ls){
        super(lexema,espacio_asignado);
        this.limite_inferior=li;
        this.limite_superior=ls;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String get_limite_inferior (){
        return this.limite_inferior;
    }
    
    public String get_limite_superior (){
        return this.limite_superior;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_limite_inferior (String li){
        this.limite_inferior=li;
    }
    
    public void set_limite_superior (String ls){
        this.limite_superior=ls;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String a_cadena (){
        return super.a_cadena()+" , "+this.limite_inferior+" .. "+this.limite_superior;
    }
    
    
}
