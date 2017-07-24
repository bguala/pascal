/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simbolo;

import java.util.Vector;

/**
 *
 * @author Bruno
 */
public class Procedimiento extends Simbolo {
    
    private Vector<Parametro> lista_parametros;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Procedimiento (String lexema, int espacio_asignado){
        super(lexema,espacio_asignado);
        this.lista_parametros=new Vector<Parametro> ();
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Vector<Parametro> get_lista_parametros (){
        return this.lista_parametros;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_lista_parametros (Vector<Parametro> lista_parametros){
        this.lista_parametros=lista_parametros;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void agregar_parametro (Parametro p){
        this.lista_parametros.add(p);
    }
    
    public String a_cadena (){
        String lista="";
        
        if(this.lista_parametros.isEmpty()){
            lista=" [ lista de parametros vacia ] ";
        }else{
            String str="", fin="";
            Parametro parametro=null;
            int i;
            int n=this.lista_parametros.size();

            for(i=0; i<n; i++){
                parametro=(Parametro)this.lista_parametros.elementAt(i);
                fin=(i==(n-1)) ? ""  : " , " ;
                str=str + parametro.a_cadena() + fin;
            }
            
            lista=" [ " + str + " ] ";
        }
        
        return super.a_cadena() + lista;
    }
    
}
