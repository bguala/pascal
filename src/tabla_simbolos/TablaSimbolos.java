/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabla_simbolos;

import java.util.HashMap;
import java.util.ArrayList;
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
    //Para debug, permite recorrer en sentido inverso la cadena estatica.
    private ArrayList<TablaSimbolos> inferiores;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public TablaSimbolos (int id){
        this.tabla_simbolos=new HashMap<String, Simbolo>(75, (float)0.8);
        this.id_entorno=id;
        this.superior=null;
        this.inferiores=new ArrayList();
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
    
    public ArrayList<TablaSimbolos> get_ts_inferiores (){
        return this.inferiores;
    }
    
    public TablaSimbolos get_ts_superior (){
        return this.superior;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_ts_superior (TablaSimbolos ts){
        this.superior=ts;
    }
    
    public void set_ts_inferior (TablaSimbolos ts){
        this.inferiores.add(ts);
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
        String encabezado="";
        String separacion="";
        int superior=0;
        int i;
        for(i=0; i<100; i++){
            separacion += "*";
        }
        
        Simbolo s=null;
        Iterator iterator=this.tabla_simbolos.entrySet().iterator();
        
        while(iterator.hasNext()){
            Entry elemento= (Entry) iterator.next();
            s=(Simbolo)elemento.getValue();
            cadena = cadena+elemento.getKey()+" => "+" ( "+s.a_cadena()+" ) \n";
        }
        
        if(this.superior != null)
            superior=this.superior.get_id();
        
        encabezado="ID ENTORNO: "+this.id_entorno+"\n"+" ID ENTORNO SUPERIOR: "+superior+"\n";
        
        return "\n\n"+separacion+"\n\n"+encabezado+"\n"+cadena+"\n"+separacion;
    }
    
}
