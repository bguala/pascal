/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pascal;

import java.util.ArrayList;
import simbolo.Token;
import tabla_simbolos.TablaSimbolos;
import simbolo.*;
import tipos.*;
/**
 *
 * @author Bruno
 */
public class Sintactico {
    
    private int preanalisis;
    private int id_entorno;
    private ArrayList<Token> tokens_sintacticos;
    private ArrayList<String> palabras_reservadas;
    private TablaSimbolos tabla_simbolos; //Contiene a la TS relacionada al programa principal.
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Sintactico (ArrayList<Token> tokens_sintacticos, ArrayList<String> palabras){
        this.preanalisis=0;
        this.id_entorno=1;
        this.tokens_sintacticos=tokens_sintacticos;
        this.palabras_reservadas=palabras;
        
        //Agregamos nuevas palabras reservadas.
        this.palabras_reservadas.add("and");
        this.palabras_reservadas.add("or");
        this.palabras_reservadas.add("not");
        
        this.tabla_simbolos=new TablaSimbolos(0);
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public ArrayList<Token> get_tokens_sintacticos (){
        return this.tokens_sintacticos;
    }
    
    public int get_id_entorno (){
        return this.id_entorno;
    }
    
    public TablaSimbolos get_tabla_simbolos (){
        return this.tabla_simbolos;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_tokens_sintacticos (ArrayList<Token> ts){
        this.tokens_sintacticos=ts;
    }
    
    public void set_id_entorno (int id){
        this.id_entorno=id;
    }
    
    public void set_tabla_simbolos (TablaSimbolos ts){
        this.tabla_simbolos=ts;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void analisis_sintactico (){
        mi_pascal();
    }
    
    private void match (String terminal){
        Token tk_actual=this.tokens_sintacticos.get(this.preanalisis);
        
        if(terminal.equalsIgnoreCase(tk_actual.get_lexema())){
            this.preanalisis++;
        }else{
            System.out.println("\nError Sintactico : *** Se esperaba el simbolo "+terminal+" *** Linea "+tk_actual.get_linea_programa());
            System.exit(1);
        }
    }
    
    //-----------------------------------------------------------------------------------
    //--- MiPascal ----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void mi_pascal (){
        encabezado();definicion(this.tabla_simbolos);
        //declaracion_subprogramas();bloque(this.tabla_simbolos);
    }
    
    //-----------------------------------------------------------------------------------
    //--- Encabezado --------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void encabezado (){
        match("program");
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        identificador(token);match(";");
    }
    
    //-----------------------------------------------------------------------------------
    //--- Definicion --------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void definicion (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            
            case "const" : this.preanalisis++;//C();
                           token=this.tokens_sintacticos.get(this.preanalisis);
                           switch(token.get_lexema()){
                               
                               case "type" : this.preanalisis++;//type_def();
                                             token=this.tokens_sintacticos.get(this.preanalisis);
                                             if(token.get_lexema().equalsIgnoreCase("var")){
                                                 this.preanalisis++;//var_def();
                                             }
                                             break;
                               case "var" : this.preanalisis++;//var_def();
                                            token=this.tokens_sintacticos.get(this.preanalisis);
                                            if(token.get_lexema().equalsIgnoreCase("type")){
                                                this.preanalisis++;//type_def();
                                            }
                                            break;
                           }
                           break;
            case "type" : this.preanalisis++;//type_def();
                          token=this.tokens_sintacticos.get(this.preanalisis);
                          switch(token.get_lexema()){
                              case "const" : this.preanalisis++;//const_def();
                                             token=this.tokens_sintacticos.get(this.preanalisis);
                                             if(token.get_lexema().equalsIgnoreCase("var")){
                                                 this.preanalisis++;//var_def();
                                             }
                                             break;
                              case "var" : this.preanalisis++;//var_def();
                                           token=this.tokens_sintacticos.get(this.preanalisis);
                                           if(token.get_lexema().equalsIgnoreCase("const")){
                                               this.preanalisis++;//const_def();
                                           }
                                           break;
                          }
                          break;
            case "var" : this.preanalisis++;//var_def();
                         token=this.tokens_sintacticos.get(this.preanalisis);
                         switch(token.get_lexema()){
                             case "const" : this.preanalisis++;//const_def();
                                            token=this.tokens_sintacticos.get(this.preanalisis);
                                            if(token.get_lexema().equalsIgnoreCase("type")){
                                                this.preanalisis++;//type_def();
                                            }
                                            break;
                             case "type" : this.preanalisis++;//type_def();
                                           token=this.tokens_sintacticos.get(this.preanalisis);
                                           if(token.get_lexema().equalsIgnoreCase("const")){
                                               this.preanalisis++;//type_def();
                                           }
                                           break;
                         }
                         break;
            default : //Segmento de definicion vacio.
            
        }
    }
    
    private boolean identificador (Token token){
        boolean r=false;
        String id=token.get_lexema();
        char c=id.charAt(0);
        
        if((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(id))
            r=true;
        else{
            System.out.println("Error Sintactico : *** Simbolo "+id+" inesperado *** Linea "+token.get_linea_programa());
            System.exit(1);
        }
        
        return r;
    }
    
    private ArrayList<String> secuencia_ids (){
        boolean fin=false;
        ArrayList<String> ids=new ArrayList();
        
        while(!fin && identificador(this.tokens_sintacticos.get(this.preanalisis))){
            ids.add(this.tokens_sintacticos.get(this.preanalisis).get_lexema());
            this.preanalisis++;
            
            if(this.tokens_sintacticos.get(this.preanalisis).get_lexema().equalsIgnoreCase(","))
                this.preanalisis++;
            else
                fin=true;
        }
        
        return ids;
    }
    
    private void const_def (TablaSimbolos ts){
        constante(ts);match(";");c1(ts);
    }
    
    private void c1 (TablaSimbolos ts){
        if(this.palabras_reservadas.contains(this.tokens_sintacticos.get(this.preanalisis).get_lexema()))
            ; //Presencia de cadena nula.
        else
            const_def(ts);
    }
    
    private void constante (TablaSimbolos ts){
        ArrayList ids=secuencia_ids();
        match("=");
        String dato=this.tokens_sintacticos.get(this.preanalisis).get_lexema();
        
        //Verifiamos que dato sea un numero entero, true o false mediante un esquema de traduccion.
        
        int i;
        int n=ids.size();
        String id="";
        for(i=0; i<n; i++){
            id=(String)ids.get(i);
            
            ts.insertar(id, new Constante(id,1,dato));
        }
    }
    
    private void type_def (TablaSimbolos ts){
        tipo(ts);match(";");type_fin(ts);
    }
    
    private void tipo (TablaSimbolos ts){
        ArrayList<String> ids=secuencia_ids();
        match("=");
        
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            //--- Tipos Primitivos ---
            case "integer" : this.preanalisis++;
                             //Tenemos una lista de ids, que representan nuevos tipos integer.
                             //tipo1, tipo2 = integer;
                             insertar_tipos(ts,ids,new TipoDato("",1,"integer","definido"));
                             break;
            case "boolean" : this.preanalisis++;
                             //Tenemos una lista de ids, que representan nuevos tipos boolean.
                             //tipo1, tipo2 = boolean;
                             insertar_tipos(ts,ids,new TipoDato("",1,"boolean","definido"));
                             break;
            //--- Tipos Estructurados ---
            case "("       : this.preanalisis++;
                             //--- Enumeracion ---
                             enum_(ts,ids);
                             break;
            case "array"   : this.preanalisis++;
                             arr(ts,ids);
                             break;
            case "record"  : this.preanalisis++;
                             reg(ts,ids);                             
                             break;
            default : //--- Subrango ---
                      //No avanzamos preanalisis porque debemos verificar que el token actual sea digito.
                      sub(ts,ids); //Para poder reusar en la seccion var, si es necesario.
        }
    }
    
    //--- Para reusar en seccion var ---
    
    private void enum_ (TablaSimbolos ts, ArrayList<String> ids){
        ArrayList cuerpo_enum=secuencia_ids();
        match(")");
                             
        insertar_tipos(ts,ids, new Enumeracion("",1,cuerpo_enum));
    }
    
    private void sub (TablaSimbolos ts, ArrayList ids){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        char c=token.get_lexema().charAt(0);
        String lis="";
        String lss="";
        //--- Digito o Identificador ---
        if(digito(token)){
            this.preanalisis++;
            lis=token.get_lexema();
        }else{
            //Podemos tener cualquier otro caracter.
            System.out.println("Error Sintactico : *** Simbolo "+token.get_lexema()+" inesperado *** linea "+token.get_linea_programa());
            System.exit(1);
        }

        match("..");

        if(digito(token)){
            this.preanalisis++;
            lss=token.get_lexema();
        }else{
            System.out.println("Error Sintactico : *** Simbolo "+token.get_lexema()+" inesperado *** linea "+token.get_linea_programa());
            System.exit(1);
        }

        insertar_tipos(ts,ids,new Subrango("",1,lis,lss));
    }
    
    //Aqui consideramos el caso de recursion a derecha arr->'array' '[' ']' 'of' resto_arr
    private void arr (TablaSimbolos ts, ArrayList<String> ids){
        match("[");
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        //Verificamos si el token actual es digito o identificador.
        String li="";
        String ls="";
        if(digito(token)){
            this.preanalisis++;//Porque se produce un match.
            li=token.get_lexema();
        }else{
            //Si el token actual no es digito aun no debemos emitir un mensaje de error. 
            if(identificador(token)){
                this.preanalisis++;
            }
        }
        match("..");
        //Nuevamente verificamos si el token actual es digito o identificador.
        if(digito(token)){
            this.preanalisis++;
            ls=token.get_lexema();
        }else{
            if(identificador(token)){
                this.preanalisis++;
            }
        }
        match("]");match("of");

        //Si el arreglo posee recursion a derecha podemos enganchar las diferentes definiciones del
        //arreglo como tipos estructurados usando la clase Estructurado, de esta manera generamos 
        //una lista enlazada de definiciones. El ultimo elto de esta lista contiene al tipo raiz.
        
        //c : cantidad de elementos del arreglo.
        //Tipo puede ser estructurado o simple, ambos se determinan arriba.
        //insertar_tipos(ts,ids,new Arreglo("",1,new Tipo(),c,li,ls));
    }
    
    private void reg (TablaSimbolos ts, ArrayList<String> ids){
        ArrayList<Parametro> lista_campos=new ArrayList();
        //r(ts,lista_campos); deberia devolver la lista de campos del registro.
        
        match("end");
        
        //Evitamos problemas relacionados a la recursion del cuerpo del registro.
        //insertar_tipos(ts,ids,new Regitro());
    }
    
    //--- Para reusar en seccion var ---
    
    private void insertar_tipos (TablaSimbolos ts, ArrayList<String> ids, Simbolo s){
        int i;
        int n=ids.size();
        String lex="";
        for(i=0; i<n; i++){
            lex=ids.get(i);
            //s.set_lexema(lex);
            ts.insertar(lex, s);
        }
    }
    
    private boolean digito (Token token){
        String lex=token.get_lexema();
        char c=lex.charAt(0);
        return ((int)c>=48 && (int)c<=57);
    }
    
    private void type_fin (TablaSimbolos ts){
        if(this.palabras_reservadas.contains(this.tokens_sintacticos.get(this.preanalisis).get_lexema()))
            ; //Presencia de cadena nula.
        else
            type_def(ts);
    }
    
    private void r (TablaSimbolos ts, ArrayList<Parametro> lista_campos){
        //En campo no hay que agregar sus eltos a la ts, porque forman parte de la lista de campos del
        //registro. Solamente debemos agregar el registro.
        campo(lista_campos);R(ts,lista_campos);
    }
    
    private void campo (ArrayList<Parametro> lista_campos){
        ArrayList<String> ids=secuencia_ids();
        match(":");
        
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        //Ahora viene el tipo de dato asociado al campo del registro.
        switch(token.get_lexema()){
            case "integer" : lista_campos.add(new Parametro(ids,new Simple("integer","integer","primitivo")));
                             break;
            case "boolean" : break;
            case "("       : break;
            case "array"   : break;
            case "record"  : break;
            default : //--- Subrango ---
                      if(digito(token)){
                          
                      }else{
                          //--- Tipo definido por el usuario ---
                          if(identificador(token)){
                              
                          }
                      }
        }
    }
    
    private void R (TablaSimbolos ts, ArrayList<Parametro> lista_campos){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase(";")){
            this.preanalisis++;RP(ts,lista_campos);
        }else
            ;//Presencia de cadena nula.
    }
    
    private void RP (TablaSimbolos ts, ArrayList<Parametro> lista_campos){
        //Despues de r debe venir un identificador, si no es asi hacemos uso de la cadena nula.
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        char c=token.get_lexema().charAt(0);
        if((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(token.get_lexema())){
            //Si avanzamos preanalisis estamos perdiendo un identificador. Todos los ids se obtienen en
            //campo.
            r(ts,lista_campos);
        }else
            ; //Presencia de cadena nula.
    }
    
    //-----------------------------------------------------------------------------------
    //--- Declaracion de Subprogramas ---------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    //-----------------------------------------------------------------------------------
    //--- Bloque ------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    //-----------------------------------------------------------------------------------
    //--- Expresiones -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void expresion (){
        T();E1();
    }
    
    private void T (){
        F();T1();
    }
    
    private void F (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase("not")){
            this.preanalisis++;G();
        }else
            G();
    }
    
    private void G (){
        H();G1();
    }
    
    private void H (){
        I();H1();
    }
    
    private void I (){
        J();I1();
    }
    
    private void J (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase("-")){
            this.preanalisis++;K();
        }else
            K();
    }
    
    private void I1 (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        switch(token.get_lexema()){
            case "+" :
            case "-" : this.preanalisis++;
                       J();
                       I1();
                       break;
            default : ; //Presencia de cadenanula.
        }
    }
    
    private void H1 (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        switch(token.get_lexema()){
            case "*" :
            case "/" : this.preanalisis++;
                       I();
                       H1();
                       break;
            default : ; //Presencia de cadena nula.
        }
    }
    
    private void G1 (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        switch(token.get_lexema()){
            case ">"  :
            case "<"  :
            case ">=" :
            case "<=" :
            case "="  :
            case "<>" : this.preanalisis++;
                        H();
                        G1();
            default : ; //Presencia de cadena nula.
        }
    }
    
    private void T1 (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase("and")){
            this.preanalisis++;
            F();
            T1();
        }else
            ; //Presencia de cadena nula.
    }
    
    private void E1 (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase("or")){
            this.preanalisis++;
            T();
            E1();
        }else
            ; //Presencia de cadena nula.
    }
    
    private void K (){
        
    }
    
}
