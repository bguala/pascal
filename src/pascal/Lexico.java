/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pascal;

import java.util.ArrayList;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import simbolo.Token;

/**
 *
 * @author Bruno
 */
public class Lexico {
    
    private String archivo;
    private ArrayList<Token> tokens_sintacticos;
    private ArrayList<String> palabras_reservadas;
    private String tokens;
    private int linea_programa;
    private boolean comentario_sin_cerrar;
    
    //-----------------------------------------------------------------------------------
    //--- Constructor -------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public Lexico (String archivo){
        this.archivo=archivo;
        this.tokens_sintacticos=new ArrayList();
        this.palabras_reservadas=new ArrayList();
        this.tokens="";
        this.linea_programa=1;
        this.comentario_sin_cerrar=false;
        
        this.cargar_palabras_reservadas();
    }
    
    //-----------------------------------------------------------------------------------
    //--- Observadores ------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public String get_nombre_archivo (){
        return this.archivo;
    }
    
    public ArrayList<Token> get_tokens_sintacticos (){
        return this.tokens_sintacticos;
    }
    
    public ArrayList<String> get_palabras_reservadas (){
        return this.palabras_reservadas;
    }
    
    public String get_tokens (){
        return this.tokens;
    }
    
    public int get_linea_programa (){
        return this.linea_programa;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Modificadores -----------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void set_nombre_archivo (String nombre){
        this.archivo=nombre;
    }
    
    public void set_tokens_sintacticos (ArrayList<Token> t){
        this.tokens_sintacticos=t;
    }
    
    public void set_palabras_reservadas (ArrayList<String> p){
        this.palabras_reservadas=p;
    }
    
    public void set_tokens (String t){
        this.tokens=t;
    }
    
    public void set_linea_programa (int linea_programa){
        this.linea_programa=linea_programa;
    }
    
    //-----------------------------------------------------------------------------------
    //--- Propios -----------------------------------------------------------------------
    //-----------------------------------------------------------------------------------
    
    public void guardar_tokens (){
        try {
            
            String nombre_archivo=this.archivo.substring(0, this.archivo.length()-4);
            PrintWriter salida=new PrintWriter(new FileOutputStream("tokens_sintacticos_"+nombre_archivo+".txt"));
            salida.println(this.tokens);
            salida.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Lexico.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
    
        
    public void analisis_lexico (){
        //File f=new File(path);
        try{
            BufferedReader stream=new BufferedReader(new FileReader(this.archivo));
            String linea_codigo;
            boolean fin=false;
            
            while((linea_codigo = stream.readLine())!=null && !fin){
                examinar_cadena(linea_codigo);
                this.linea_programa++;
            }
            
            if(this.comentario_sin_cerrar){
                System.out.println("\nError Lexico : *** Comentario sin cerrar *** ");
                System.exit(1);
            }
        }catch(FileNotFoundException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
    
    private void cargar_palabras_reservadas (){
        palabras_reservadas.add("if");
        palabras_reservadas.add("then");
        palabras_reservadas.add("else");
        palabras_reservadas.add("case");
        palabras_reservadas.add("of");
        palabras_reservadas.add("begin");
        palabras_reservadas.add("end");
        palabras_reservadas.add("while");
        palabras_reservadas.add("do");
        palabras_reservadas.add("function");
        palabras_reservadas.add("procedure");
        palabras_reservadas.add("var");
        palabras_reservadas.add("const");
        palabras_reservadas.add("type");
        palabras_reservadas.add("program");
        palabras_reservadas.add("read");
        palabras_reservadas.add("write");
        palabras_reservadas.add("succ");
        palabras_reservadas.add("pred");
        palabras_reservadas.add("array");
        palabras_reservadas.add("record");
        palabras_reservadas.add("enum");
        palabras_reservadas.add("integer");
        palabras_reservadas.add("boolean");
        palabras_reservadas.add("true");
        palabras_reservadas.add("false");
    }
    
    private boolean es_palabra_reservada (String lex){
        return this.palabras_reservadas.contains(lex);
    }
    
    private void examinar_cadena (String cadena){
	String lex="";
	char c=' ';
        int estado=(this.comentario_sin_cerrar) ? 5 : 0 ;
        
	int n=cadena.length();
        
        int i;
        for(i=0; i<n; i++){
            c=cadena.charAt(i);
            switch(estado){
                case 0 : switch(c){
                            case ' ' :
                            case '\n':
                            case '\t': //estado=0;
                                       break;
                                       
                            case ',' : tokens=tokens+"\n<coma,"+c+">\n";
                                       //Crear objeto token y guardarlo en tokens_sintacticos
                                       tokens_sintacticos.add(new Token("coma",""+c,this.linea_programa));
                                       break;
                            case ';' : tokens=tokens+"\n<punto_y_coma,"+c+">\n";
                                       //Crear objeto token y guardarlo en tokens_sintacticos
                                       tokens_sintacticos.add(new Token("punto_y_coma",""+c,this.linea_programa));
                                       break;
                            case '.' : lex=""+c;
                                       //Consultamos si es el ultimo caracter de la linea de codigo.
                                       if(i==(n-1)){
                                           tokens=tokens+"\n<punto,"+lex+">\n";
                                           tokens_sintacticos.add(new Token("punto",lex,this.linea_programa));
                                       }else
                                            estado=8;
                                       
                                       break;//Podemos tener un subrango
                            case ':' : lex=""+c;
                                       //Consultamos si es el ultimo caracter de la linea de codigo.
                                       if(i==(n-1)){
                                           tokens=tokens+"\n<dos_puntos,"+lex+">\n";
                                           tokens_sintacticos.add(new Token("dos_puntos",lex,this.linea_programa));
                                       }else
                                            estado=4;
                                       
                                       break;//Podemos tener el operador de asignacion
                            case '(' : tokens=tokens+"\n<apertura_perentesis,"+c+">\n";
                                       //Crear objeto token y guardarlo en tokens_sintacticos
                                       tokens_sintacticos.add(new Token("apertura_parentesis",""+c,this.linea_programa));
                                       break;
                            case ')' : tokens=tokens+"\n<cierre_parentesis,"+c+">\n";
                                       //Crear objeto token y guardarlo en tokens_sintacticos
                                       tokens_sintacticos.add(new Token("cierre_parentesis",""+c,this.linea_programa));
                                       break;
                            case '[' : tokens=tokens+"\n<apertura_corchetes,"+c+">\n";
                                       //Crear objeto token y guardarlo en tokens_sintacticos
                                       tokens_sintacticos.add(new Token("apertura_corchetes",""+c,this.linea_programa));
                                       break;
                            case ']' : tokens=tokens+"\n<cierre_corchetes,"+c+">\n";
                                       //Crear objeto token y guardarlo en tokens_sintacticos
                                       tokens_sintacticos.add(new Token("cierre_corchetes",""+c,this.linea_programa));
                                       break;
                            case '<' : lex=""+c;
                                       //Consultamos si c es el ultimo carcater de la linea de codigo.
                                       if(i==(n-1)){
                                           tokens=tokens+"\n<op_relacional,"+lex+">\n";
                                           tokens_sintacticos.add(new Token("op_relacional",lex,this.linea_programa));
                                       }else
                                            estado=7;
                                       break;//Podemos tener un = o > y formar <= o <>
                            case '>' : lex=""+c;
                                       //Consultamos si c es el ultimo caracter de la linea de codigo.
                                       if(i==(n-1)){
                                           tokens=tokens+"\n<op_relacional,"+lex+">\n";
                                           tokens_sintacticos.add(new Token("op_relacional",lex,this.linea_programa));
                                       }else
                                           estado=9;
                                       
                                       break;//Podemos tener un =, >=
                            case '=' : tokens=tokens+"\n<op_relacional,"+c+">\n";
                                       //Crear objeto token y guardarlo en tokens_sintacticos
                                       tokens_sintacticos.add(new Token("op_relacional",""+c,this.linea_programa));
                                       break;
                            case '+' : 
                            case '-' : 
                            case '*' :
                            case '/' : tokens=tokens+"\n<op_aritmetico,"+c+">\n";
                                       tokens_sintacticos.add(new Token("op_aritmetico",""+c,this.linea_programa));
                                       break;
                            case '{' : estado=5;
                                       this.comentario_sin_cerrar=true;
                                       break;
                            case '@' : 
                            case '!' : 
                            case '#' : 
                            case '$' : 
                            case '%' : 
                            case '&' :
                            case '?' :
                            case '¡' : 
                            case '¿' : 
                            case '_' :
                            case '~' : System.out.println("\nError Lexico: *** El simbolo "+c+" no pertenece al alfabeto del lenguaje Pascal *** Linea "+this.linea_programa);
                                       System.exit(1);
                                       break;
                            default : if ((int)c>=48 && (int)c<=57){
					lex=""+c;
								
					if(i == (n-1)){
					    tokens=tokens+"\n<numero_entero,"+lex+">\n";
                                            //Crear un objeto token y guardarlo en tokens_sintacticos
                                            tokens_sintacticos.add(new Token("numero_entero",lex,this.linea_programa));
                                        }else
                                             estado=2;
				      }else{
                                        if(((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)){
					    lex=""+c;

                                            if(i == (n-1)){
                                            	tokens=tokens+"\n<identificador,"+lex+">\n";
                                                //Crear un objeto token y guardarlo en tokens_sintacticos
                                            	tokens_sintacticos.add(new Token("identificador",lex,this.linea_programa));
                                                
                                            }else
						estado=1;
                                            }
                                      }
                         } break;
                case 1 : //--- Identificador ---
                         switch(c){
                            case '@' : 
                            case '!' : 
                            case '#' : 
                            case '$' : 
                            case '%' : 
                            case '&' :
                            case '?' :
                            case '¡' : 
                            case '¿' : 
                            case '_' :
                            case '~' : System.out.println("\nError Lexico: *** El simbolo "+c+" no pertenece al alfabeto del lenguaje Pascal *** Linea "+this.linea_programa);
                                       System.exit(1);
                                       break;
                            default: 
                                     if(((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122) || ((int)c>=48 && (int)c<=57)){
                                        lex=lex+c;
                                        if(i == (n-1)){
                                            
                                            if(lex.equalsIgnoreCase("or") || lex.equalsIgnoreCase("and") || lex.equalsIgnoreCase("not")){
                                                tokens=tokens+"\n<op_booleano,"+lex+">\n";
                                                tokens_sintacticos.add(new Token("op_booleano",lex,this.linea_programa));
                                            }else{
                                                if(es_palabra_reservada(lex)){
                                                    tokens=tokens+"\n<"+lex+","+lex+">\n";
                                                    tokens_sintacticos.add(new Token(lex,lex,this.linea_programa));

                                                }else{
                                                    tokens=tokens+"\n<identificador,"+lex+">\n";
                                                    tokens_sintacticos.add(new Token("identificador",lex,this.linea_programa));
                                                }
                                            }
                                            
                                        }
                                     }else{
                                         if(lex.equalsIgnoreCase("or") || lex.equalsIgnoreCase("and") || lex.equalsIgnoreCase("not")){
                                             tokens=tokens+"\n<op_booleano,"+lex+">\n";
                                             tokens_sintacticos.add(new Token("op_booleano",lex,this.linea_programa));
                                         }else{
                                             if(es_palabra_reservada(lex)){
                                                 tokens=tokens+"\n<"+lex+","+lex+">\n";
                                                 tokens_sintacticos.add(new Token(lex,lex,this.linea_programa));

                                             }else{
                                                tokens=tokens+"\n<identificador,"+lex+">\n";
                                                tokens_sintacticos.add(new Token("identificador",lex,this.linea_programa));
                                             }
                                         }
                                         
                                         lex="";
                                         i--;
                                         estado=0;
                                     }
                         }
                         break;
                case 2 : //--- Digito ---
                         switch(c){
                            case '@' : 
                            case '!' : 
                            case '#' : 
                            case '$' : 
                            case '%' : 
                            case '&' :
                            case '?' :
                            case '¡' : 
                            case '¿' : 
                            case '_' :
                            case '~' : System.out.println("\nError Lexico: *** El simbolo "+c+" no pertenece al alfabeto del lenguaje Pascal *** Linea "+this.linea_programa);
                                       System.exit(1);
                                       break;
                            default: 
                                     if(((int)c >= 48 && (int)c <= 57)){

                                        lex=lex+c;

                                        if(i == (n-1)){
                                            tokens=tokens+"\n<numero_entero,"+lex+">\n";
                                            //Crear objeto token y guardarlo en tokens_sintacticos
                                            tokens_sintacticos.add(new Token("numero_entero",lex,this.linea_programa));
                                        }

                                     }else{
                                         tokens=tokens+"\n<numero_entero,"+lex+">\n";
                                         //Crear objeto token y guardarlo en tokens_sintacticos
                                         tokens_sintacticos.add(new Token("numero_entero",lex,this.linea_programa));

                                         if(((int)c>=65 && (int)c<=90) || ((int)c>=97 && (int)c<=122)){
                                            lex=""+c;
                                            estado=1;
                                        }else{
                                            i--;
                                            lex="";
                                            estado=0;
                                         }
                                     }
                         }
                         break;
                case 4 : switch(c){
                            case '@' : 
                            case '!' : 
                            case '#' : 
                            case '$' : 
                            case '%' : 
                            case '&' :
                            case '?' :
                            case '¡' : 
                            case '¿' : 
                            case '_' :
                            case '~' : System.out.println("\nError Lexico: *** El simbolo "+c+" no pertenece al alfabeto del lenguaje Pascal *** Linea "+this.linea_programa);
                                       System.exit(1);
                                       break;
                            default : 
                                         if(c == '='){
                                            tokens=tokens+"\n<asignacion,"+lex+c+">\n";
                                            tokens_sintacticos.add(new Token("asignacion",lex+c,this.linea_programa));
                                            lex="";
                                            estado=0;
                                         }else{
                                            tokens=tokens+"\n<dos_puntos,"+lex+">\n";
                                            tokens_sintacticos.add(new Token("dos_puntos",lex,this.linea_programa));
                                            //Si c es un caracter vamos al estado 1.
                                            if(((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)){
                                                lex=""+c;
                                                estado=1;
                                            }else{
                                                if(((int)c >= 48 && (int)c <= 57)){
                                                    lex=""+c;
                                                    estado=2;
                                                }else{
                                                    lex="";
                                                    i--;
                                                    estado=0;
                                                }
                                            }
                                         }
                         }
                         break;
                case 5 : //Eliminamos comentario. Que pasa si el comentario se cierra en otra linea??.
                         //Deberiamos mantener este estado en la proxima linea.
                         switch(c){
                            case '}' : estado=0;
                                       this.comentario_sin_cerrar=false;
                                       break;
                            default :  estado=5;
                         }
                         break;
                case 8 : switch(c){
                            case '@' : 
                            case '!' : 
                            case '#' : 
                            case '$' : 
                            case '%' : 
                            case '&' :
                            case '?' :
                            case '¡' : 
                            case '¿' : 
                            case '_' :
                            case '~' : System.out.println("\nError Lexico: *** El simbolo "+c+" no pertenece al alfabeto del lenguaje Pascal *** Linea "+this.linea_programa);
                                       System.exit(1);
                                       break;
                            default: 
                                     if(c == '.'){
                                        tokens=tokens+"\n<subrango,"+lex+c+">\n";
                                        //Crear objeto token y guardarlo en tokens_sintacticos
                                        tokens_sintacticos.add(new Token("subrango",lex+c,this.linea_programa));
                                        lex="";
                                        estado=0;
                                     }else{
                                        tokens=tokens+"\n<punto,"+lex+">\n";
                                        //Crear objeto token y guardarlos en tokens_sintacticos
                                        tokens_sintacticos.add(new Token("punto",lex,this.linea_programa));
                                        if(((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)){
                                            lex=""+c;
                                            estado=1;
                                        }else{
                                            if(((int)c >= 48 && (int)c <= 57)){
                                                lex=""+c;
                                                estado=2;
                                            }else{
                                                lex="";
                                                i--;
                                                estado=0;
                                            }
                                        }

                                     }
                         }
                         break;
                case 7 : //lex contiene a <. En este estado generamos <, <= o <>.
                         switch(c){
                             case '@' : 
                             case '!' : 
                             case '#' : 
                             case '$' : 
                             case '%' : 
                             case '&' :
                             case '?' :
                             case '¡' : 
                             case '¿' : 
                             case '_' :
                             case '~' : System.out.println("\nError Lexico: *** El simbolo "+c+" no pertenece al alfabeto del lenguaje Pascal *** Linea "+this.linea_programa);
                                        System.exit(1);
                                        break;
                             case '>' :
                             case '=' : tokens=tokens+"\n<op_relacional,"+lex+c+">\n";
                                        tokens_sintacticos.add(new Token("op_relacional",lex+c,this.linea_programa));
                                        lex="";
                                        estado=0;
                                        break;
                             default :  tokens=tokens+"\n<op_relacional,"+lex+">\n"; //<
                                        tokens_sintacticos.add(new Token("op_relacional",lex,this.linea_programa));
                                        if(((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)){
                                            lex=""+c;
                                            estado=1;
                                        }else{
                                            if(((int)c >= 48 && (int)c <= 57)){
                                                lex=""+c;
                                                estado=2;
                                            }else{
                                                lex="";
                                                i--;
                                                estado=0;
                                            }
                                        }
                         }
                         break; 
                case 9 : //lex contiene a >. En este estado podemos generar >, >=.
                         switch(c){
                            //Verificamos si existe error lexico.
                            case '@' : 
                            case '!' : 
                            case '#' : 
                            case '$' : 
                            case '%' : 
                            case '&' :
                            case '?' :
                            case '¡' : 
                            case '¿' : 
                            case '_' :
                            case '~' : System.out.println("\nError Lexico: *** El simbolo "+c+" no pertenece al alfabeto del lenguaje Pascal *** Linea "+this.linea_programa);
                                       System.exit(1);
                                       break;
                            default:   if(c == '='){ //Generamos >=
                                            tokens=tokens+"\n<op_relacional,"+lex+c+">\n";
                                            tokens_sintacticos.add(new Token("op_relacional",lex+c,this.linea_programa));
                                            lex="";
                                            estado=0;
                                       }else{
                                            tokens=tokens+"\n<op_relacional,"+lex+">\n"; //Generamos >
                                            tokens_sintacticos.add(new Token("op_relacional",lex,this.linea_programa));
                                            
                                            if(((int)c >= 65 && (int)c <= 90) || ((int)c >= 97 && (int)c <= 122)){
                                                    lex=""+c;
                                                    estado=1;
                                            }else{
                                                if(((int)c >= 48 && (int)c <= 57)){
                                                     lex=""+c;
                                                     estado=2;
                                                }else{
                                                     lex="";
                                                     i--;
                                                     estado=0;
                                                }
                                             }
                                        }
                         }
                
            }
        }
    }
    
}
