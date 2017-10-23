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
    //Contiene el nombre del subprograma duenio de la TS LOCAL, es util para verificar si una variable que se
    //usa en una asignacion o una expresion pertenece a los parametros formales de un subprograma.
    private String propietario;
    
    //Para implementar cadena estatica.
    private TablaSimbolos superior;
    //Para debug, permite recorrer en sentido inverso la cadena estatica.
    private ArrayList<TablaSimbolos> inferiores;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public TablaSimbolos (int id, String propietario){
        this.tabla_simbolos=new HashMap<String, Simbolo>(75, (float)0.8);
        this.id_entorno=id;
        this.propietario=propietario;
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
    
    public String get_propietario (){
        return this.propietario;
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
    
    public void set_propietario (String propietario){
        this.propietario=propietario;
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
    
    /*
    * Devuelve el parametro formal de un subprograma que coincide con alguna variable utilizada en una asignacion
    * o expresion.
    */
    public Simbolo obtener_parametro_formal (Token token){
        //Obtenemos los datos relacionados al identificador propietario de la TS, deberia ser un subprograma.
        Simbolo s=this.get(propietario);
        ArrayList<Parametro> params=null;
        Simbolo simbolo=null;
        int i,n;
        boolean fin;
        
        if(s instanceof Funcion)
            params=((Funcion)s).get_lista_parametros();
        else
            if(s instanceof Procedimiento)
                params=((Procedimiento)s).get_lista_parametros();
        
        if(params != null){
            i=0;
            n=params.size();
            fin=false;
            
            while(i<n && !fin){
                //Obtener parametro devuelve un objeto TipoDato.
                simbolo=params.get(i).obtener_parametro(token.get_lexema());
                
                if(simbolo != null)
                    fin=true;
                
                i++;
            }
        }
        
        return simbolo;
    }
    
    /*
    * Devuelve true si las variables que de declaran coinciden con:
    * a) identificadores definidos previamente que se encuentran dispersos en la TS.
    * b) paramatros formales pertenecientes al subprograma propietario de la TS.
    * c) el nombre del subprograma propietario de la TS.
    * Si ocurren algunas de estas tres condiciones estamos en presencia de un error de unicidad.
    */
    public boolean chequeo_de_unicidad (ArrayList<String> identificadores, Strings conflicto){
        boolean fin=false;
        Simbolo s=null;
        String st="";
        int i=0;
        int n=identificadores.size();
        
        //En primer instancia buscamos identificadores dispersos en la TS.
        while(i<n && !fin){
            st=identificadores.get(i);
            s=this.get(st);
            
            if(s != null){
                fin=true;
                conflicto.set_string(st);
            }else{
                //En segunda instancia buscamos identificadores en los parametros formales del subprograma 
                //propietario de la TS, si no existen identificadores repetidos.
                s=this.obtener_parametro_formal(new Token(st));
                
                if(s != null){
                    fin=true;
                    conflicto.set_string(st);
                }else{
                    //En tercer instancia verificamos si las variables que se declaran no coinciden con el nombre del subprograma
                    //propietario de la TS.
                    if(st.equalsIgnoreCase(this.propietario)){
                        fin=true;
                        conflicto.set_string(st);
                    }
                }
            }
            
            i++;
        }
        
        return fin;
    }
    
}
