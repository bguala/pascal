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
    
    private int limite_inferior;
    private int limite_superior;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Subrango (int li, int ls){
        super("",1);
        this.limite_inferior=li;
        this.limite_superior=ls;
    }
    
    public Subrango (String lexema, int espacio_asignado, int li, int ls){
        super(lexema,espacio_asignado);
        this.limite_inferior=li;
        this.limite_superior=ls;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public int get_limite_inferior (){
        return this.limite_inferior;
    }
    
    public int get_limite_superior (){
        return this.limite_superior;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
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
        return super.a_cadena()+" , "+this.limite_inferior+" .. "+this.limite_superior;
    }
    
    
}
