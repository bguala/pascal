/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pascal;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import simbolo.Token;
import tabla_simbolos.TablaSimbolos;
import simbolo.*;

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
    
    private void guardar_ts (){
        try{
            
            PrintWriter salida=new PrintWriter(new FileOutputStream("ts_"));
            salida.println("");
            salida.close();
            
        }catch(FileNotFoundException ex){
            
            System.out.println(ex.getMessage());
            System.exit(1);
            
        }
    }
    
    //-----------------------------------------------------------------------------------
    //--- MiPascal ----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void mi_pascal (){
        encabezado();
        //TS local al programa principal.
        definicion(this.tabla_simbolos);
        
        declaracion_subprogramas(this.tabla_simbolos);
        
        bloque(this.tabla_simbolos);
        
        //Fin del programa principal.
        match(".");
    }
    
    //-----------------------------------------------------------------------------------
    //--- Encabezado --------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void encabezado (){
        match("program");
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        identificador(token); //No avanza preanalisis.
        this.preanalisis++;
        match(";");
    }
    
    //-----------------------------------------------------------------------------------
    //--- Definicion --------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void definicion (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            
            case "const" : this.preanalisis++;
                           const_def(ts);
                           token=this.tokens_sintacticos.get(this.preanalisis);
                           switch(token.get_lexema()){
                               
                               case "type" : this.preanalisis++;
                                             type_def(ts);
                                             token=this.tokens_sintacticos.get(this.preanalisis);
                                             if(token.get_lexema().equalsIgnoreCase("var")){
                                                 this.preanalisis++;
                                                 var_def(ts);
                                             }
                                             break;
                               case "var" : this.preanalisis++;
                                            var_def(ts);
                                            token=this.tokens_sintacticos.get(this.preanalisis);
                                            if(token.get_lexema().equalsIgnoreCase("type")){
                                                this.preanalisis++;
                                                type_def(ts);
                                            }
                                            break;
                           }
                           break;
            case "type" : this.preanalisis++;
                          type_def(ts);
                          token=this.tokens_sintacticos.get(this.preanalisis);
                          switch(token.get_lexema()){
                              case "const" : this.preanalisis++;
                                             const_def(ts);
                                             token=this.tokens_sintacticos.get(this.preanalisis);
                                             if(token.get_lexema().equalsIgnoreCase("var")){
                                                 this.preanalisis++;
                                                 var_def(ts);
                                             }
                                             break;
                              case "var" : this.preanalisis++;
                                           var_def(ts);
                                           token=this.tokens_sintacticos.get(this.preanalisis);
                                           if(token.get_lexema().equalsIgnoreCase("const")){
                                               this.preanalisis++;
                                               const_def(ts);
                                           }
                                           break;
                          }
                          break;
            case "var" : this.preanalisis++;
                         var_def(ts);
                         token=this.tokens_sintacticos.get(this.preanalisis);
                         switch(token.get_lexema()){
                             case "const" : this.preanalisis++;
                                            const_def(ts);
                                            token=this.tokens_sintacticos.get(this.preanalisis);
                                            if(token.get_lexema().equalsIgnoreCase("type")){
                                                this.preanalisis++;
                                                type_def(ts);
                                            }
                                            break;
                             case "type" : this.preanalisis++;
                                           type_def(ts);
                                           token=this.tokens_sintacticos.get(this.preanalisis);
                                           if(token.get_lexema().equalsIgnoreCase("const")){
                                               this.preanalisis++;
                                               const_def(ts);
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
        constante(ts);match(";");const_fin(ts);
    }
    
    private void const_fin (TablaSimbolos ts){
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
                             //Ej: tipo1, tipo2 = integer;
                             insertar_tipos(ts,ids,new TipoDato("",1,"integer","definido"));
                             break;
            case "boolean" : this.preanalisis++;
                             //Tenemos una lista de ids, que representan nuevos tipos boolean.
                             //Ej: tipo1, tipo2 = boolean;
                             insertar_tipos(ts,ids,new TipoDato("",1,"boolean","definido"));
                             break;
            //--- Tipos Estructurados ---
            case "("       : this.preanalisis++;
                             //--- Enumeracion ---
                             enum_(ts,ids);
                             break;
            case "array"   : this.preanalisis++;
                             Arreglo a=new Arreglo();
                             //Agregamos datos necesarios para hacer chequeos de tipo.
                             completar_arreglo(a);
                             //Pueden haber definiciones recursivas de arreglos.
                             agregar_tipo(a);
                             //Guardamos los nuevos tipos en la ts.
                             insertar_tipos(ts,ids,a);
                             break;
            case "record"  : this.preanalisis++;
                             reg(ts,ids);                             
                             break;
            default : //--- Subrango ---
                      //No avanzamos preanalisis porque debemos verificar que el token actual sea digito.
                      //Si el token actual es digito posiblemente estamos en presencia de un subrango.
                      sub(ts,ids);
        }
    }
    
    //--- Para reusar en seccion var ---
    
    private void enum_ (TablaSimbolos ts, ArrayList<String> ids){
        ArrayList<String> cuerpo_enum=secuencia_ids();
        match(")");
                             
        insertar_tipos(ts,ids, new Enumeracion("",1,cuerpo_enum));
    }
    
    private void sub (TablaSimbolos ts, ArrayList ids){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        String lis="";
        String lss="";
        //--- Digito ---
        if(digito(token)){
            this.preanalisis++;
            lis=token.get_lexema();
        }else{
            //Podemos tener cualquier otro caracter.
            System.out.println("Error Sintactico : *** Simbolo "+token.get_lexema()+" inesperado *** linea "+token.get_linea_programa());
            System.exit(1);
        }

        match("..");

        //--- Digito ---
        if(digito(token)){
            this.preanalisis++;
            lss=token.get_lexema();
        }else{
            System.out.println("Error Sintactico : *** Simbolo "+token.get_lexema()+" inesperado *** linea "+token.get_linea_programa());
            System.exit(1);
        }

        insertar_tipos(ts,ids,new Subrango("",1,lis,lss));
    }
    
    private void completar_arreglo (Arreglo a){
        match("[");
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        String li="";
        String ls="";
        //Verificamos si el token actual es digito o identificador.
        if(digito(token)){
            this.preanalisis++;//Avanzamos preanalisis porque se produce un match.
            li=token.get_lexema();
        }else{
            //Si el token actual no es digito aun no debemos emitir un mensaje de error. Podriamos tener
            //array [ const1 .. const2 ].
            if(identificador(token)){
                this.preanalisis++;
                li=token.get_lexema();
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
                ls=token.get_lexema();
            }
        }
        match("]");match("of");

        //Completamos parte del arreglo.
        a.set_limite_inferior(Integer.parseInt(li));
        a.set_limite_superior(Integer.parseInt(ls));
        a.set_cantidad_elementos(1);
        
    }
    
    //Agrega el tipo de dato asociado a un arreglo. Antes de conocer el tipo de un arreglo pueden aparecer
    //otras definiciones de arreglo.
    //Ej: array[ 1..10 ] of array[ 1..10 ] of integer.
    private void agregar_tipo (Arreglo a){
        //Ahora analizamos el tipo de dato de los eltos del arreglo.
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        switch(token.get_lexema()){
            //--- Tipos Primitivos ---
            case "integer" : this.preanalisis++;
                             a.set_tipo_dato(new TipoDato("integer",1,"integer","primitivo"));
                             break;
            case "boolean" : this.preanalisis++;
                             a.set_tipo_dato(new TipoDato("boolean",1,"boolean","primitivo"));
                             break;
            //--- Tipos Estructurados ---
            case "("       : this.preanalisis++;
                             //--- Enumeracion ---
                             ArrayList<String> cuerpo_enum=secuencia_ids();
                             match(")");
                             a.set_tipo_dato(new Enumeracion("",1,cuerpo_enum));
                             break;
            case "array"   : this.preanalisis++;
                             //El arreglo posee definiciones recursivas a derecha.
                             match("[");
                             token=this.tokens_sintacticos.get(this.preanalisis);
                             String li="";
                             String ls="";
                             //Verificamos si el token actual es digito o identificador.
                             if(digito(token)){
                                 this.preanalisis++;//Avanzamos preanalisis porque se produce un match.
                                 li=token.get_lexema();
                             }else{
                                 //Si el token actual no es digito aun no debemos emitir un mensaje de error. Podriamos tener
                                 //array [ const1 .. const2 ].
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
                             
                             //Creamos un nuevo arreglo y lo guardamos en la lista de definiciones rec.
                             a.set_definicion_recursiva(new Arreglo(0,Integer.parseInt(li),Integer.parseInt(ls)));
                             
                             //LLamamos recursivamente para saber si existen nuevas definiciones recursivas. Caso
                             //contrario agregamos el tipo de dato al arreglo.
                             agregar_tipo(a);
                             
                             break;
            case "record"  : this.preanalisis++;//En este momento nos debemos encontar con un identificador.
                             Registro registro=new Registro();
                             
                             //r agrega los campos al objeto registro. 
                             r(registro);

                             match("end");
                             
                             //El nuevo registro creado representa el tipo asociado a los eltos. del arreglo.
                             a.set_tipo_dato(registro);
                             break;
            default : //--- Subrango ---
                      //No avanzamos preanalisis porque debemos verificar que el token actual sea digito.
                      token=this.tokens_sintacticos.get(this.preanalisis);
                      if(digito(token)){
                          String lis=token.get_lexema();
                          this.preanalisis++;
                          
                          match("..");
                          
                          token=this.tokens_sintacticos.get(this.preanalisis);
                          if(digito(token)){
                              String lss=token.get_lexema();
                              this.preanalisis++;
                              
                              a.set_tipo_dato(new Subrango("",1,lis,lss));
                          }
                      }else{
                          //--- Tipo definido por el usuario ---
                          if(identificador(token)){
                              this.preanalisis++;
                              a.set_tipo_dato(new TipoDato(token.get_lexema(),1,"","definido"));
                          }
                      }
        }
    }
    
    private void reg (TablaSimbolos ts, ArrayList<String> ids){
        //Creamos un objeto Registro para guardar en el mismo todos sus campos, que pueden ser de tipo
        //integer o boolean.
        Registro reg_principal=new Registro();
        
        r(reg_principal);
        
        match("end");
        
        insertar_tipos(ts,ids,reg_principal);
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
            ; //Presencia de cadena nula si el simbolo actual es una palabra reservada. Despues de la 
        //seccion type pueden aparecer var, const, function, procedure o begind. En caso de error 
        //sintactico puede aparecer cualquier otra palabra. De todas formas debemos finalizar la ejecucion de 
        //type_def. No es responsabilidad de type_def emitir un mensaje de error.
        else
            type_def(ts);
    }
    
    private void r (Registro reg_principal){
        campo(reg_principal);R(reg_principal);
    }
    
    private void campo (Registro reg_principal){
        ArrayList<String> ids=secuencia_ids();
        match(":");
        
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        //Ahora viene el tipo de dato asociado al campo del registro.
        switch(token.get_lexema()){
            case "integer" : this.preanalisis++;
                             reg_principal.agregar_parametro(new Parametro(ids,new TipoDato("integer",1,"integer","primitivo")));
                             break;
            case "boolean" : this.preanalisis++;
                             reg_principal.agregar_parametro(new Parametro(ids,new TipoDato("boolean",1,"boolean","primitivo")));
                             break;
            case "("       : //this.preanalisis++;
                             //ArrayList<String> cuerpo_enum=secuencia_ids();
                             //match(")");
                             //reg_principal.agregar_parametro(new Parametro(ids,new Estructurado("",new Enumeracion("",1,cuerpo_enum))));
                             System.out.println("Error Sintactico : *** No se admiten campos de tipo Enumeracion *** Linea "+token.get_linea_programa());
                             System.exit(1);
                             break;
            case "array"   : 
                             //break;
            case "record"  : //this.preanalisis++;
                             //Registro reg_anidado=new Registro();
                             //r(reg_anidado);
                             //reg_principal.agregar_parametro(new Parametro(ids,new Estructurado("",reg_anidado)));
                             System.out.println("Error Sintactico : *** No se admiten campos de tipo "+token.get_lexema()+" *** Linea "+token.get_linea_programa());
                             System.exit(1);
                             break;
            default : //--- Subrango ---
                      if(digito(token)){
//                          token=this.tokens_sintacticos.get(this.preanalisis);
//                          String li=token.get_lexema();
//                          this.preanalisis++;
//                          match("..");
//                          
//                          if(digito(token)){
//                              token=this.tokens_sintacticos.get(this.preanalisis);
//                              String ls=token.get_lexema();
//                              this.preanalisis++;
//                              reg_principal.agregar_parametro(new Parametro(ids,new Estructurado("",new Subrango("",1,li,ls))));
//                          }else{
//                              System.out.println("Error Sintactico : *** ***");
//                              System.exit(1);
//                          }
                            System.out.println("Error Sintactico : *** No se admiten campos de tipo Subrango *** Linea "+token.get_linea_programa());
                            System.exit(1);
                      }else{
                          //--- Tipo definido por el usuario ---
                          if(identificador(token)){
                              System.out.println("Error Sintactico : *** Tipo de dato "+token.get_lexema()+" inesperado. No se admiten tipos definidos por el usuario *** Linea "+token.get_linea_programa());
                              System.exit(1);
                          }
                      }
        }
    }
    
    private void R (Registro reg_principal){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(token.get_lexema().equalsIgnoreCase(";")){//Es la coma que separa los campos del registro.
            this.preanalisis++;
            
            //Despues de r debe venir un identificador, si no es asi, hacemos uso de la cadena nula.
            token=this.tokens_sintacticos.get(this.preanalisis);
            char c=token.get_lexema().charAt(0);
            //Si el simbolo actual es un identificador y no se corresponde con una palabra reservada
            //significa que estamos en presencia de un nuevo campo del registro.
            if((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(token.get_lexema())){
                //Si avanzamos preanalisis estamos perdiendo un identificador. Todos los ids se obtienen en
                //campo(reg).
                r(reg_principal);
            }else
                ; //Presencia de cadena nula. La proxima palabra reservada debe ser 'end'.
        }else
            ;//Presencia de cadena nula. La proxima palabra reservada debe ser 'end'.
    }
        
    private void var_def (TablaSimbolos ts){
        variable(ts);match(";");var_fin(ts);
    }
    
    private void variable (TablaSimbolos ts){
        ArrayList<String> ids=secuencia_ids();
        match(":");
        
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            //--- Primitivos ---
            case "integer" : this.preanalisis++;
                             insertar_tipos(ts,ids,new Variable(new TipoDato("integer",1,"integer","primitivo")));
                             break;
            case "boolean" : this.preanalisis++;
                             insertar_tipos(ts,ids,new Variable(new TipoDato("boolean",1,"boolean","primitivo")));
                             break;
            //--- Estructurados ---
            case "("       : this.preanalisis++;
                             ArrayList<String> cuerpo_enum=secuencia_ids();
                             match(")");
                             insertar_tipos(ts,ids,new Variable(new Enumeracion(cuerpo_enum)));
                             break;
            case "array"   : this.preanalisis++;
                             Arreglo a=new Arreglo();
                             //Agrega datos necesarios para hacer chequeos de tipo.
                             completar_arreglo(a);
                             //Pueden haber definiciones recursivas de arreglos.
                             agregar_tipo(a);
                             //Guardamos los nuevos tipos en la ts.
                             insertar_tipos(ts,ids,new Variable(a));
                             break;
            case "record"  : this.preanalisis++;
                             Registro registro=new Registro();
                             r(registro);
                             insertar_tipos(ts,ids,new Variable(registro));
                             break;
            default : //--- Subrango ---
                      if(digito(token)){
                          String lis=token.get_lexema();
                          this.preanalisis++;
                          
                          match("..");
                          
                          token=this.tokens_sintacticos.get(this.preanalisis);
                          if(digito(token)){
                              String lss=token.get_lexema();
                              this.preanalisis++;
                              
                              insertar_tipos(ts,ids,new Variable(new Subrango(lis,lss)));
                          }
                          
                      }else{
                          //--- Tipo definido por el usuario ---
                          if(identificador(token)){
                              this.preanalisis++;
                              insertar_tipos(ts,ids,new Variable(new TipoDato(token.get_lexema(),1,"","definido")));
                          }
                      }
        }
    }
    
    private void var_fin (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        //Las palabras reservadas pueden ser const, type, function, procedure o begin.
        if(this.palabras_reservadas.contains(token.get_lexema()))
            ;//Presencia de cadena nula.
        else
            var_def(ts);
    }
    
    //-----------------------------------------------------------------------------------
    //--- Declaracion de Subprogramas ---------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void declaracion_subprogramas (TablaSimbolos ts_superior){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            case "function"   : this.preanalisis++;
                                funcion(ts_superior,null);
                                declaracion_subprogramas(ts_superior);
                                break;
            case "procedure"  : this.preanalisis++;
                                procedimiento(ts_superior,null);
                                declaracion_subprogramas(ts_superior);
                                break;
            default : ; //Presencia de cadena nula. Significa que no hay definicion de subprogramas.
        }
    }
    
    private void funcion (TablaSimbolos ts_superior, TablaSimbolos ts_local){
        Token token;
        String id="";
        //Para guardar el entorno local del subprograma.
        ts_local=new TablaSimbolos(this.id_entorno);
        this.id_entorno++;
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(identificador(token)){
            this.preanalisis++;
            id=token.get_lexema();
        }
        
        match("(");
        
        ArrayList<Parametro> parametro_formal=new ArrayList();
        param(parametro_formal);
        
        match(")");match(":");
        
        String tipo_retorno=this.tokens_sintacticos.get(this.preanalisis).get_lexema();
        switch(tipo_retorno){
            case "integer" :
            case "boolean" : this.preanalisis++;
                             break;
            default : System.out.println("\nError Sintactico : *** Tipo de dato incompatible "+tipo_retorno+" *** Linea "+this.tokens_sintacticos.get(this.preanalisis).get_linea_programa());
                      System.exit(1);
        }
                
        match(";");
        
        ts_local.insertar(id, new Funcion(id,1,new TipoDato(tipo_retorno,1,"",""),parametro_formal));
        
        definicion(ts_local);
        
        //Construimos la cadena estatica para recorrerla en dos direccionnes.
        ts_local.set_ts_superior(ts_superior);
        ts_superior.set_ts_inferior(ts_local);
        
        //Para subprogramas anidados.
        declaracion_subprogramas(ts_local);
        
        bloque(ts_local);
        
        match(";");
    }
    
    private void param (ArrayList<Parametro> parametro_formal){
        TipoDato tipo=null;
        ArrayList<String> ids=secuencia_ids();
        
        match(":");
        
        String tipo_dato=this.tokens_sintacticos.get(this.preanalisis).get_lexema();
        this.preanalisis++;
        
        switch(tipo_dato){
            case "integer" : 
                             
            case "boolean" : tipo=new TipoDato(tipo_dato,1,tipo_dato,"primitivo");
                             break;
            default : tipo=new TipoDato(tipo_dato,1,"","definido");
        }
        
        parametro_formal.add(new Parametro(ids,tipo));
        
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase(",")){
            this.preanalisis++;
            param(parametro_formal);
        }else
            ; //Presencia de cadena nula. Representa caso base.
    }
    
    private void procedimiento (TablaSimbolos ts_superior, TablaSimbolos ts_local){
        Token token;
        String id="";
        
        ts_local=new TablaSimbolos(this.id_entorno);
        this.id_entorno++;
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(identificador(token)){
            this.preanalisis++;
            id=token.get_lexema();
        }
        
        match("(");//En funciones y procedimientos pueden omitirse. En este caso tampoco hay parametros formales.
        
        ArrayList<Parametro> parametro_formal=new ArrayList();
        param(parametro_formal);
        
        match(")");
        
        match(";");
        
        ts_local.insertar(id, new Procedimiento(id,1,parametro_formal));
        
        definicion(ts_local);
        
        //Construimos la cadena estatica.
        ts_local.set_ts_superior(ts_superior);
        
        //Para subprogramas anidados.
        declaracion_subprogramas(ts_local);
        
        bloque(ts_local);
        
        match(";");
    }
    
    //-----------------------------------------------------------------------------------
    //--- Bloque ------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    private void bloque (TablaSimbolos ts){
        match("begin");//Se puede sacar.
        S(ts);
        match("end");
    }
    
    private void S (TablaSimbolos ts){
        sentencia(ts);S1(ts);
    }
    
    private void S1 (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase(";")){
            this.preanalisis++;
            token=this.tokens_sintacticos.get(this.preanalisis);
            
            if(token.get_lexema().equalsIgnoreCase("end"))
                ; //Presencia de cadena nula. S11 deriva en landa.
            else
                S(ts);
        }else
            ; //Presencia de cadena nula. S1 deriva en landa.
    }
    
    private void sentencia (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            case "if"     : this.preanalisis++;
                            alternativa(ts);
                            break;
            case "while"  : this.preanalisis++;
                            repetitiva(ts);
                            break;
            case "case"   : this.preanalisis++;
                            seleccion_multiple(ts);
                            break;
            case "read"   : this.preanalisis++;
                            read();
                            break;
            case "write"  : this.preanalisis++;
                            write();
                            break;
            case "succ"   : this.preanalisis++;
                            succ();
                            break;
            case "pred"   : this.preanalisis++;
                            pred();
                            break;
            //--- Identificador ---
            default : if(identificador(token)){
                this.preanalisis++;
                token=this.tokens_sintacticos.get(this.preanalisis);
                
                switch(token.get_lexema()){
                    case ":=" : //--- Asignacion ---
                                this.preanalisis++;
                                argumento();
                                break;
                    case "("  : //--- Llamada a Subprograma ---
                                this.preanalisis++;
                                args();
                                match(")");
                                break;
                }
            }
                
        }
    }
    
    private void alternativa (TablaSimbolos ts){
        expresion();match("then");
        
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("begin")){ //Presencia de un bloque.
            this.preanalisis++;
            bloque(ts);
        }else
            sentencia(ts);
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("else")){
            this.preanalisis++;
            token=this.tokens_sintacticos.get(this.preanalisis);
            
            if(token.get_lexema().equalsIgnoreCase("begin")){
                this.preanalisis++;
                bloque(ts);
            }else
                sentencia(ts);
        }else
            ; //Presencia de cadena nula. Significa que no hay bloque else.
    }
    
    private void repetitiva (TablaSimbolos ts){
        expresion();match("do");
        
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("begin")){
            this.preanalisis++;
            bloque(ts);
        }else
            sentencia(ts);
    }
    
    private void seleccion_multiple (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(digito(token))
            this.preanalisis++;
        else
            if(identificador(token))
                this.preanalisis++;
            
        match("of");
        
        opciones(ts);
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("else")){
            this.preanalisis++;
            token=this.tokens_sintacticos.get(this.preanalisis);
            
            if(token.get_lexema().equalsIgnoreCase("begin")){
                this.preanalisis++;
                bloque(ts);
            }else
                sentencia(ts);
        }else
            ; //Presencia de cadena nula. No hay bloque else.
    }
    
    private void opciones (TablaSimbolos ts){
        opcion(ts);op(ts);
    }
    
    private void opcion (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        if(digito(token) || identificador(token))
            this.preanalisis++;
                
        match(":");
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("begin")){
            this.preanalisis++;
            bloque(ts);
        }else
            sentencia(ts);
    }
    
    private void op (TablaSimbolos ts){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase(";")){
            this.preanalisis++;
            token=this.tokens_sintacticos.get(this.preanalisis);
            char c=token.get_lexema().charAt(0);
            if(digito(token) || ((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(token.get_lexema()))){
                opciones(ts);
            }else              
                ; //Presencia de cadena nula. Determina el fin de las opciones del case con punto y coma.
        }else
            ; //Presencia de cadena nula. Determina el fin de las opciones del case sin punto y coma.
    }
    
    private void read (){
        match("(");
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(identificador(token))
            this.preanalisis++;
        
        match(")");
    }
    
    private void write (){
        match("(");
        
        exp();
        
        match(")");
    }
    
    private void exp (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        char c;
        
        //--- Posible expresion ---
        switch(token.get_lexema()){
            case "-"   : //--- Expresion con - unario ---
            case "not" : //--- Expresion booleana que empieza con not ---
            case "("   : //--- Expresion parentizada ---
                         expresion();
                         break;
            default :
                        c=token.get_lexema().charAt(0);
                        //--- Identificador ---
                        if((((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)) && !this.palabras_reservadas.contains(token.get_lexema())){
                            this.preanalisis++;
                            token=this.tokens_sintacticos.get(this.preanalisis);

                            switch(token.get_lexema()){
                                case "(" : //--- Llamada a Subprograma ---
                                           this.preanalisis++;
                                           //L1();
                                           break;
                                case "." : //--- Acceso a registro ---
                                           this.preanalisis++;
                                           token=this.tokens_sintacticos.get(this.preanalisis);
                                           if(identificador(token))
                                               this.preanalisis++;
                                           break;
                                case "[" : //--- Acceso a arreglo ---
                                           this.preanalisis++;
                                           token=this.tokens_sintacticos.get(this.preanalisis);

                                           if(digito(token) || identificador(token))
                                               this.preanalisis++;
                                           
                                           //Si el arreglo posee dos dimensiones :
                                           //nombre[ fila, columna ]

                                           match("]");
                                           break;

                                default :  //--- Posible expresion que empieza con un identificador ---
                                           switch(token.get_lexema()){
                                               case "+"    : 
                                               case "-"    : 
                                               case "*"    :
                                               case "/"    :
                                               case "<"    :
                                               case ">"    :
                                               case "<="   :
                                               case ">="   :
                                               case "<>"   :
                                               case "and"  :
                                               case "or"   : //Disminuimos preanalisis para no alterar la ejecucion de expresion
                                                             //porque antes de consumir el proximo simbolo se ejecutan varias 
                                                             //llamadas a funciones.
                                                             this.preanalisis--;
                                                             expresion();
                                                             break;
                                               default : ; //Presencia de cadena nula. Solamente tenemos un identificador.
                                           }
                            }
                        }else{
                            if(digito(token)){
                                this.preanalisis++;
                                token=this.tokens_sintacticos.get(this.preanalisis);

                                //--- Posible expresion que empieza con un digito ---
                                switch(token.get_lexema()){
                                    case "+"    : 
                                    case "-"    : 
                                    case "*"    :
                                    case "/"    :
                                    case "<"    :
                                    case ">"    :
                                    case "<="   :
                                    case ">="   :
                                    case "<>"   :
                                    case "and"  :
                                    case "or"   : //Disminuimos preanalisis para no alterar la ejecucion de expresion
                                                  //porque antes de consumir el proximo simbolo se ejecutan varias 
                                                  //llamadas a funciones.
                                                  this.preanalisis--;
                                                  expresion();
                                                  break;
                                    default : ; //Presencia de cadena nula. Solamente tenemos un digito.
                                }

                            }else
                                ; //Presencia de cadena nula. No hay expresiones.
                        }
        }
    }
        
    private void succ (){
        match("(");
        
        exp();
        
        match(")");
    }
    
    private void pred (){
        match("(");
        
        exp();
        
        match(")");
    }
    
    private void args (){
        Token token;
        argumento();
        
        token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase(",")){
            this.preanalisis++;
            args();
        }else
            ;
    }
    
    private void argumento (){
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        switch(token.get_lexema()){
            //--- Constante Booleana ---
            case "true"    :
            case "false"   : this.preanalisis++;
                             break;
            
            default : exp();
        }
    }
    
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
        Token token=this.tokens_sintacticos.get(this.preanalisis);
        
        if(token.get_lexema().equalsIgnoreCase("(")){
            expresion();
            match(")");
        }else{
            if(digito(token)){
                this.preanalisis++;
            }else{
                if(identificador(token)){
                    token=this.tokens_sintacticos.get(this.preanalisis);
                    
                    //--- Llamada a Subprograma ---
                    if(token.get_lexema().equalsIgnoreCase("(")){
                        args();
                        match(")");
                    }else
                        ; //Presencia de cadena nula. Solamente tenemos un identificador.
                }
            }
        }
    }
    
}
