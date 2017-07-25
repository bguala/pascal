/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabla_simbolos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import simbolo.*;

/**
 *
 * @author Bruno
 */
public class TablaSimbolos {
    
    private HashMap<String,Simbolo> tabla_simbolos;
    private int id_entorno;
    
    //Para implementar cadena estatica.
    private TablaSimbolos superior;
    //Para fines de debug, permite recorrer en sentido inverso la cadena estatica.
    private TablaSimbolos inferior;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public TablaSimbolos (int id){
        this.tabla_simbolos=new HashMap<String, Simbolo>(75, (float)0.8);
        this.id_entorno=id;
        this.superior=null;
        this.inferior=null;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Simbolo get (String lexema){
        Simbolo s=null;
        
        if(this.tabla_simbolos.containsKey(lexema)){
            s=this.tabla_simbolos.get(lexema);
        }
        
        return s;
    }
    
    public int get_id (){
        return this.id_entorno;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_ts_superior (TablaSimbolos ts){
        this.superior=ts;
    }
    
    public void set_ts_inferior (TablaSimbolos ts){
        this.inferior=ts;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String insertar (String lexema, Simbolo simbolo){
        String r="";
        
        if(!this.tabla_simbolos.containsKey(lexema)){
            this.tabla_simbolos.put(lexema, simbolo);
        }else
            r="Clave repetida en un mismo ambiente";
        
        return r;
    }
    
    public Simbolo eliminar (String lexema){
        Simbolo s=null;
        
        if(this.tabla_simbolos.containsKey(lexema)){
            s=this.tabla_simbolos.remove(lexema);
        }
        
        return s;
    }
    
    public String mostrar_contenido (){
        String cadena="";
        Simbolo s=null;
        Iterator iterator=this.tabla_simbolos.entrySet().iterator();
        
        while(iterator.hasNext()){
            Entry elemento= (Entry) iterator.next();
            s=(Simbolo)elemento.getValue();
            cadena = cadena+elemento.getKey()+" => "+" ( "+s.a_cadena()+" ) \n";
        }
        
        return cadena;
    }
    
}
