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
public class Arreglo extends Simbolo {
    
    private Simbolo tipo_dato;
    private int cantidad_elementos;
    private int limite_inferior;
    private int limite_superior;
    private ArrayList<Arreglo> lista_definiciones_recursivas;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Arreglo (){
        super("", 1);
        this.tipo_dato=null;
        this.cantidad_elementos=0;
        this.limite_inferior=0;
        this.limite_superior=0;
        this.lista_definiciones_recursivas=new ArrayList();
    }
    
    public Arreglo (int c, int li, int ls){
        super("",1);
        this.tipo_dato=null;
        this.cantidad_elementos=c;
        this.limite_inferior=li;
        this.limite_superior=ls;
        this.lista_definiciones_recursivas=new ArrayList();
    }
    
    public Arreglo (Simbolo tipo,int c, int li, int ls){
        super("",1);
        this.tipo_dato=tipo;
        this.cantidad_elementos=c;
        this.limite_inferior=li;
        this.limite_superior=ls;
        this.lista_definiciones_recursivas=new ArrayList();
    }
    
    public Arreglo (String lexema, int espacio_asignado, Simbolo tipo_dato, int c, int li, int ls){
        super(lexema,espacio_asignado);
        this.tipo_dato=tipo_dato;
        this.cantidad_elementos=c;
        this.limite_inferior=li;
        this.limite_superior=ls;
        this.lista_definiciones_recursivas=new ArrayList();
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Simbolo get_tipo_dato (){
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
    
    public ArrayList<Arreglo> get_lista_deficiones_recursivas (){
        return this.lista_definiciones_recursivas;
    }    
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_tipo_dato (Simbolo tipo_dato){
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
    
    public void set_definicion_recursiva (Arreglo a){
        this.lista_definiciones_recursivas.add(a);
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String a_cadena (){
        return super.a_cadena()+" , "+this.limite_inferior+" , "+this.limite_superior+" , "+this.tipo_dato.a_cadena();
    }
    
    public String chequeo_de_tipos (Token id, int indice){
        
        if(!((indice >= this.limite_inferior) && (indice <= this.limite_superior))){
            System.out.println("\nError Semantico : *** Indice fuera de rango, los limites del arreglo \""+id.get_lexema()+"\" son [ "+this.limite_inferior+", "+this.limite_superior+" ] *** Linea "+id.get_linea_programa());
            System.exit(1);
        }
        
        if(this.tipo_dato instanceof TipoDato)
            return ((TipoDato)this.tipo_dato).get_nombre_tipo();
        if(this.tipo_dato instanceof Arreglo){
            System.out.println("\nError Semantico : *** En la definicion de \""+id.get_lexema()+"\" no se admiten arreglos como tipos de datos *** Linea "+id.get_linea_programa());
            System.exit(1);
        }
        if(this.tipo_dato instanceof Registro)
            return "record";
    }
    
}
