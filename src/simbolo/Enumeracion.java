/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simbolo;

import java.util.ArrayList;

/**
 *
 * @author Bruno
 */
public class Enumeracion extends Simbolo {
    
    private ArrayList<String> lista_identificadores;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Enumeracion (String lexema, int espacio_asignado){
        super(lexema, espacio_asignado);
        this.lista_identificadores=new ArrayList ();
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public ArrayList<String> get_lista_identificadores (){
        return this.lista_identificadores;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_lista_identificadores (ArrayList<String> identificadores){
        this.lista_identificadores=identificadores;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void agregar_identificador (String id){
        this.lista_identificadores.add(id);
    }
    
    public String a_cadena (){
        String str="";
        
        if(this.lista_identificadores.isEmpty()){
            str="enumeracion vacia )";
        }else{
            String coma="";
            int i;
            int n=this.lista_identificadores.size();
            
            for(i=0; i<n; i++){
                coma=(i==(n-1)) ? " ) " : " , " ;
                str=str+this.lista_identificadores.get(i)+coma;
            }
        }
        
        return " ( "+str;
    }
}
