// TODOS LOS MÓDULOS QUE UTILIZA EL PROGRAMA
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;

public class JuegoBingo extends JFrame {

	// DEFINICIÓN GLOBAL DE VARIABLES
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
	private static JButton arrayBotones[];
	private static final long serialVersionUID = 1L;
	private static String respuestaNumeros;
	private static String respuestaPosiciones;
    private static String ip = "192.168.0.79";
//    private static String ip = "54.175.182.62"; // IP PÚBLICA (AMAZON AWS)
	private boolean Pausa = false;
    private static int port = 6666;
    private static int lineaHecha = 0;
    private static int bingoHecho = 0;
	static ArrayList<Integer> arraySeleccionados = new ArrayList<>();
	static ArrayList<Integer> arrayValidos = new ArrayList<>();
    private static List<Integer> arrayTodosNums = new ArrayList<>();
	static String arrayServidorValidos;
	private static String jugador_nombre = "";
	private static Random random = new Random();
	private static String[] preguntas = {
           "Días de permiso retribuidos por traslado del domicilio habitual\na) 1  \nb) 2\n",
           "Las empresas con 25 o más trabajadores deben tener un plan de igualdad\na) 1 Verdadero\nb) 2 Falso  \n",
           "En general, las horas extraordinarias son obligatorias\na) Si\nb) No \n",
           "Es legal trabajar 80h semanales en la misma empresa\na) Si\nb) No\nc) Depende de la comunidad autónoma\n",
           "¿Es obligatorio que un empleador proporcione un contrato laboral por escrito a sus empleados?\na) Si\nb) No siempre \n",
           "¿Puede el empresario hacer un registro de la taquilla del trabajador sin motivo alguno?\na) Si\nb) No \n",
           "Es deber de un empleado notificar con antelación a su empleador sobre una ausencia por enfermedad?\na) Sí \nb) No\n",
           "Durante las vacaciones se tiene derecho a salario\na) Si \nb) No\n",
           "¿Tiempo mínimo entre jornadas de trabajo?\na) 12 horas \nb) 15 horas en caso de jornada completa\nc) 10 horas\n",
           "Organizar y dirigir la empresa es una de las potestades del empresario\na) Falso\nb) Verdadero \n",
           "¿Un empleador puede despedir a un empleado sin motivo alguno?\na) Sí\nb) No \n",
           "¿Los trabajadores tienen también unas obligaciones ?\na) Sí \nb) No",
           "¿Es un derecho de los empleados recibir capacitación y formación adecuadas para realizar su trabajo de manera segura?\na) Sí \nb) No\n",
           "¿Un empleado tiene derecho a igualdad de salario por igual trabajo, independientemente de su género?\na) Sí \nb) No\n",
           "¿Están los trabajadores obligados a observar las medidas de seguridad o higiene?\na) Sí \nb) No\nc) Solo las de seguridad\n",
           "El estatuto de los trabajadores regula las condiciones laborales de los trabajadores por cuenta ajena\na) Verdadero\nb) Falso \n",
           "En caso de hospitalización de tu padre cuántos días corresponden\na) 5 días \nb) 2 días\n",
           "Las empresas con 25 o más trabajadores deben tener un plan de igualdad\na) Verdadero\nb) Falso \n",
           "¿Los empleadores tienen el deber de respetar los derechos sindicales de los empleados?\na) Sí \nb) No\n",
           "Si uno de tus padres fallece tienes un permiso retribuido de 1 semana\na) Verdadero\nb) Falso \n",
           "¿Cuál de estos días es festivo?\na) 1 mayo \nb) 31 de diciembre\nc) Ambas son correctas\n",
           "¿Duración mínima de las vacaciones anuales?\na) 30 días naturales \nb) 20 días naturales\nc) 45 días naturales\n",
           "Los empleados, en general, ¿pueden negarse a realizar tareas que consideren peligrosas, sin represalias, si se cumplen las medidas de seguridad necesarias?\na) Sí\nb) No \n",
           "¿Los empleadores pueden despedir a un empleado debido a su orientación sexual?\na) Sí\nb) No \n",
           "Los empleados tienen el deber de ser puntuales y cumplir con su horario laboral\na) Sí \nb) No\n",
           "Los días naturales son de lunes a sábado\na) Si\nb) No \n",
           "¿Cuál de estos no es un derecho derivado de la relación laboral?\na) Derecho a la intimidad\nb) Derecho a la ocupación efectiva\nc) Derecho al trabajo \n",
		   "¿Puede un empresario vigilar con cámaras provistas de audio e imagen los lugares de trabajo?\na) Sí\nb) No",
		   "¿Los policías tienen contrato de trabajo?\na) Si\nb) No",
		   "¿Los empleadores tienen el deber de proporcionar equipos de seguridad adecuados?\na) Sí\nb) No",
		   "¿Los empleados tienen el deber de proteger la confidencialidad de la información de la empresa?\na) Si\nb) No",
		   "¿La fecha de vacaciones será conocida por el trabajador como mínimo 1 mes antes?\na) Si\nb) No",
		   "¿Cuál de estos empleos no tiene relaciones laborales especiales?",
		   "En general, horas extraordinarias al año máximas\na) 80\nb) 200\nc) 365",
		   "En general, jornada diaria máxima mayor de edad\na) 9\nb) 8\nc) 10",
		   "El descanso mínimo semanal es de 2 días ininterrumpido para un mayor de edad\na) Verdadero\nb) Falso",
		   "El trabajo nocturno es aquel que se realiza entre las 24 AM y las 6 AM\na) Sí\nb) No",
		   "Tiempo mínimo de descanso tras 6 horas seguidas de trabajo para los mayores de edad\na) Mínimo 15 minutos\nb) Mínimo 20 minutos",
		   "Las relaciones laborales pueden ser ordinarias o extraordinarias\na) Verdadero\nb) Falso",
		   "¿Cuál de estas características no define la relación laboral ordinaria?\na) Personalísima\nb) Independiente",
		   "Se considera que los autónomos tienen una relación laboral\na) No\nb) Sí",
		   "¿Un dependiente de una tienda tiene una relación laboral especial?\na) Si\nb) No",
		   "¿El personal de alta dirección tiene una relación laboral especial?\na) Sí\nb) No",
		   "¿Es cierto que hay máximo 14 fiestas laborales al año?\na) Sí\nb) No",
		   "¿Pueden los menores realizar horas extraordinarias si no son de fuerza mayor?\na) Sí\n b) No",
		   "¿Y los trabajadores nocturnos pueden realizar horas extraordinarias?\na) Sí\nb) No, salvo si es de fuerza mayor",
		   "¿A cuánto se quiere reducir la jornada laboral en España?\na) 35\nb) 37,5",
		   "¿Promedio jornada laboral máxima en cómputo anual en España?\na) 45\nb) 40",
		   "¿El trabajador puede extinguir contrato sin motivo alguno?\na) Sí\nb) No",
        };

	private static char[] respuestasCorrectas = {
		'a', 
		'b', 
		'b', 
		'b', 
		'b', 
		'b', 
		'b', 
		'a', 
		'a', 
		'b', 
		'b', 
		'a', 
		'a', 
		'a', 
		'a', 
		'b', 
		'a', 
		'b', 
		'a', 
		'b', 
		'a', 
		'a', 
		'b', 
		'b', 
		'a', 
		'b', 
		'c', 
		'b', 
		'b', 
		'a', 
		'a', 
		'b', 
		'a', 
		'a', 
		'a', 
		'b', 
		'b', 
		'a', 
		'b', 
		'b', 
		'a', 
		'b', 
		'a', 
		'a', 
		'b', 
		'b', 
		'b', 
		'b', 
		'a', 
	};
	private JPanel contentPane;
	private JButton btn1Fila1;
	private JButton btn2Fila1;
	private JButton btn3Fila1;
	private JButton btn4Fila1;
	private JButton btn5Fila1;
	private JButton btn6Fila1;
	private JButton btn7Fila1;
	private JButton btn8Fila1;
	private JButton btn9Fila1;
	private JButton btn1Fila2;
	private JButton btn2Fila2;
	private JButton btn3Fila2;
	private JButton btn4Fila2;
	private JButton btn5Fila2;
	private JButton btn6Fila2;
	private JButton btn7Fila2;
	private JButton btn8Fila2;
	private JButton btn9Fila2;
	private JButton btn1Fila3;
	private JButton btn2Fila3;
	private JButton btn3Fila3;
	private JButton btn4Fila3;
	private JButton btn5Fila3;
	private JButton btn6Fila3;
	private JButton btn7Fila3;
	private JButton btn8Fila3;
	private JButton btn9Fila3;
	private static JButton btnLinea;
	private static JButton btnBingo;
	private JLabel lblNumeros;
	private JLabel lblNombres;

	// FUNCIÓN -> INICIAR CONEXIÓN CON EL SERVIDOR
    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	// FUNCIÓN -> ENVIAR MENSAJE AL SERVIDOR
    public void sendMessage(String msg) {
        out.println(msg);
    }

	// FUNCIÓN -> OBTENER EL MENSAJE DE RESPUESTA DEL SERVIDOR
    public String receiveMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

	// FUNCIÓN -> FINALIZAR LA CONEXIÓN CON EL SERVIDOR
    public void finalizarConexion() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	// FUNCIÓN -> OBTENER UNA PREGUNTA ALEATORIA
    private static String obtenerPreguntaAleatoria(String[] preguntas) {
        if (preguntas == null || preguntas.length == 0) {
            throw new IllegalArgumentException("La lista de preguntas no puede estar vacía o ser nula.");
        }

		// OBTENER UN NÚMERO DE PREGUNTA ALEATORIO
        int indiceAleatorio = random.nextInt(preguntas.length);

		// RETORNAR LA PREGUNTA ALEATORIA
        return preguntas[indiceAleatorio];
    }

	// FUNCIÓN -> COMPROBAR SI LA RESPUESTA DE LA PREGUNTA ES CORRECTA
    private static boolean esRespuestaCorrecta(String respuestaUsuario, char respuestaCorrecta) {
        return respuestaUsuario.length() > 0 && respuestaUsuario.trim().equalsIgnoreCase(String.valueOf(respuestaCorrecta));
    }
    
	// FUNCIÓN -> ENVIAR ACCIÓN AL SERVIDOR (EN CASO DE QUE ALGUIEN HAGA LÍNEA O BINGO)
	public static void enviarAccionServidor(int tipo) {
		// SI EL TIPO ES 1, PROCEDE A COMPRAR LA LÍNEA
		if (tipo == 1) {
			// ENVIAR COMPROBACIÓN DE LA LÍNEA Y GUARDAR LA RESPUESTA EN BOOLEAN
	        boolean res = comprobarSiHayLinea();
			if (res) {
				// PAUSAR LA PARTIDA MIENTRAS EL JUGADOR HACE LA PREGUNTA
				pausarPartidaLinea();
				// OBTENER PREGUNTA ALEATORIA Y GUARDARLA EN LA VARIABLE "preguntaAleatoria"
		        String preguntaAleatoria = obtenerPreguntaAleatoria(preguntas);
		        char respuestaCorrecta = respuestasCorrectas[Arrays.asList(preguntas).indexOf(preguntaAleatoria)];

				// MOSTRAR DIÁLOGO DE ENTRADA
		        String respuesta_pregunta = JOptionPane.showInputDialog((preguntaAleatoria), JOptionPane.YES_OPTION);

				// SU LA PREGUNTA NO ESTÁ VACÍA
		        if (respuesta_pregunta != null) {
		            respuesta_pregunta = respuesta_pregunta.toLowerCase();

					// SI LA RESPUESTA DE LA PREGUNTA ES CORRECTA
		            if (esRespuestaCorrecta(respuesta_pregunta, respuestaCorrecta)) {
						// INSTANCIANDO EL CLIENTE
		                Cliente client = new Cliente();

						// ENVIAR ALTERACIÓN DE LA LÍNEA AL SERVIDOR
		                client.startConnection(ip, port);
		                client.sendMessage("[%enviar_alteracion%]=1");
		                client.receiveMessage();
		                client.finalizarConexion();

						// ENVIAR NOMBRE DEL GANADOR
		                client.startConnection(ip, port);
		                client.sendMessage("[%alterar_nombre%]=" + jugador_nombre);
		                client.receiveMessage();
		                client.finalizarConexion();
		                
						// ENVIAR MENSAJE DE DESPAUSAR AL SERVIDOR PARA REANUDAR LA PARTIDA
		                enviarDespausaLinea();

		            } else {
						// EN CASO DE QUE FALLE LA PREGUNTA, ENVIAR DESPAUSA AL SERVIDOR
						JOptionPane.showConfirmDialog(null, "Has fallado la pregunta!", "Aviso", JOptionPane.DEFAULT_OPTION);
						btnLinea.setEnabled(false);
		            	enviarDespausaLineaNoAcertada();
		            }
		        }
			}
			// EN CASO DE QUE LA LINEA NO SEA CORRECTA
			else {
				JOptionPane.showConfirmDialog(null, "La linea no es correcta!", "Aviso", JOptionPane.DEFAULT_OPTION);
			}		
		}

		// SI EL TIPO ES DOS, PROCEDE A COMPROBAR EL BINGO
		else if (tipo == 2) {
			boolean res = todosEnBingo();
			if (res) {
				// PAUSAR LA PARTIDA MIENTRAS EL JUGADOR HACE LA PREGUNTA
				pausarPartidaLinea();

				// OBTENER PREGUNTA ALEATORIA Y GUARDARLA EN LA VARIABLE "preguntaAleatoria"
				String preguntaAleatoria = obtenerPreguntaAleatoria(preguntas);

				// OBTENER LA RESPUESTA CORRECTA DEL ARRAY DE PREGUNTAS CORRESPONDIENTE A LA MISMA POSICIÓN DE RESPUESTA
				char respuestaCorrecta = respuestasCorrectas[Arrays.asList(preguntas).indexOf(preguntaAleatoria)];

				// MOSTRAR DIÁLOGO DE ENTRADA
				String respuesta_pregunta = JOptionPane.showInputDialog((preguntaAleatoria), JOptionPane.YES_OPTION);

				// SI LA PREGUNTA NO ESTÁ VACÍA
				if (respuesta_pregunta != null) {
					respuesta_pregunta = respuesta_pregunta.toLowerCase();

					// SI LA RESPUESTA ES CORRECTAS
					if (esRespuestaCorrecta(respuesta_pregunta, respuestaCorrecta)) {
						Cliente client = new Cliente();

						// ENVIAR ALTERACIÓN DE TIPO "2" (ALTERAR LINEA)
						client.startConnection(ip, port);
						client.sendMessage("[%enviar_alteracion%]=2");
						client.receiveMessage();
						client.finalizarConexion();

						client.startConnection(ip, port);
						client.sendMessage("[%alterar_nombre%]=" + jugador_nombre);
						client.receiveMessage();
						client.finalizarConexion();
						
						enviarDespausaBingo();

					} else {
						// SINÓ, ENVIAR DESPAUSA DE TIPO BINGO
						JOptionPane.showConfirmDialog(null, "Has fallado la pregunta!", "Aviso", JOptionPane.DEFAULT_OPTION);
						enviarDespausaBingoNoAcertado();
					}
				}
			}
			// SINÓ, EL BINGO NO ES CORRECTO
			else { 
				JOptionPane.showConfirmDialog(null, "El bingo no es correcto!", "Aviso", JOptionPane.DEFAULT_OPTION);
			}
		}
	}
    
	// FUNCIÓN -> ESCUCHAR TODOS LOS BOTONES, MEDIANTE EL USO DE FOR RECORRIENDO EL ARRAY DE LOS BOTONES
    public static void EscucharBotones() {
    	for (int i = 0; i < arrayBotones.length; i++) {
    	    final JButton boton = arrayBotones[i];
    	    final String numBoton = boton.getText();
    	    
    	    boton.addActionListener(new ActionListener() {
    	        public void actionPerformed(ActionEvent e) {
    	            int numBoton_int = Integer.parseInt(numBoton);
    	            if (arraySeleccionados.contains(numBoton_int)) {
    	                boton.setBackground(Color.YELLOW);
    	                arraySeleccionados.remove(Integer.valueOf(numBoton_int));
    	            } else {
    	            	boton.setBackground(Color.MAGENTA);    	                
						arraySeleccionados.add(numBoton_int);
    	            }
    	        }
    	    });
    	}

		// FUNCIÓN -> PONER BOTÓN LÍNEA A LA ESCUCHA
		btnLinea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviarAccionServidor(1);
			}
		});
		
		// FUNCIÓN -> PONER BOTÓN BINGO A LA ESCUCHA
		btnBingo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enviarAccionServidor(2);
			}
		});
    }
    
	// FUNCIÓN -> ENVIAR PAUSA DE QUE SE HA HECHO LINEA AL SERVIDOR
	// (EL "3" HACE REFERENCIA A QUE SE ESTÁ ENVIANDO UNA PAUSA DEBIDO A QUE ALGUIEN HA HECHO LINEA)
    public static void pausarPartidaLinea() {
    	Cliente client = new Cliente();
        client.startConnection(ip, port);
        client.sendMessage("[%enviar_alteracion%]=3");
        client.receiveMessage();
        client.finalizarConexion();
    }
    
	// FUNCIÓN -> ENVIAR PAUSA DE QUE SE HA HECHO BINGO AL SERVIDOR
	// (EL "5" HACE REFERENCIA A QUE SE ESTÁ ENVIANDO UNA PAUSA DEBIDO A QUE ALGUIEN HA HECHO BINGO)
    public static void pausarPartidaBingo() {
    	Cliente client = new Cliente();
        client.startConnection(ip, port);
        client.sendMessage("[%enviar_alteracion%]=5");
        client.receiveMessage();
        client.finalizarConexion();
    }
    
	// FUNCIÓN -> ENVIAR LA DESPAUSA DE LA LÍNEA
	// (EL "4" HACE REFERENCIA A QUE LA DESPAUSA ES DE TIPO "LINEA")
    public static void enviarDespausaLinea() {
    	Cliente client = new Cliente();
        client.startConnection(ip, port);
        client.sendMessage("[%enviar_alteracion%]=4");
        client.receiveMessage();
        client.finalizarConexion();
    }

		// FUNCIÓN -> ENVIAR LA DESPAUSA DE LA LÍNEA
	// (EL "4" HACE REFERENCIA A QUE LA DESPAUSA ES DE TIPO "LINEA")
    public static void enviarDespausaLineaNoAcertada() {
    	Cliente client = new Cliente();
        client.startConnection(ip, port);
        client.sendMessage("[%enviar_alteracion%]=7");
        client.receiveMessage();
        client.finalizarConexion();
    }
    
	// FUNCIÓN -> ENVIAR AL SERVIDOR QUE EL PROGRAMA HA SIDO DESPAUSADO 
	// (EL "6" HACE REFERENCIA A QUE LA DESPAUSA ES DE TIPO "BINGO")
    public static void enviarDespausaBingo() {
    	Cliente client = new Cliente();
        client.startConnection(ip, port);
        client.sendMessage("[%enviar_alteracion%]=6");
        client.receiveMessage();
        client.finalizarConexion();
    }

	public static void enviarDespausaBingoNoAcertado() {
    	Cliente client = new Cliente();
        client.startConnection(ip, port);
        client.sendMessage("[%enviar_alteracion%]=8");
        client.receiveMessage();
        client.finalizarConexion();
    }
    
	// FUNCIÓN -> LLENAR EL ARRAY CON NÚMEROS VÁLIDOS PARSEÁNDOLO DE STRING A INTEGER
    public static void llenarArrayNumsValidos() {
    	for (int x=0;x<27;x++) {
    		try {
    			int num_arrayBtnTemp = Integer.parseInt(arrayBotones[x].getText());
                arrayTodosNums.add(num_arrayBtnTemp);
    		} catch (Exception e) {}    		
    	}
    }
    
	// FUNCIÓN -> COMPROBAR SI TODOS LOS NÚMEROS ESTÁN EN EL BINGO
    public static boolean todosEnBingo() {
    	if (arraySeleccionados.size() == 0) {
    		return false;
    	}
    	else {
            boolean resTodosBingo = false;

            HashSet<Integer> conjuntoSeleccionados = new HashSet<>();
            for (int num : arraySeleccionados) {
                conjuntoSeleccionados.add(num);
            }

            HashSet<Integer> conjuntoValidos = new HashSet<>();
            for (int num : arrayValidos) {
                conjuntoValidos.add(num);
            }

            resTodosBingo = conjuntoValidos.containsAll(conjuntoSeleccionados);

            return resTodosBingo;	
    	}
    }
    
	// FUNCIÓN -> COMPROBAR SI TODOS LOS NÚMEROS SELECCIONADOS SON LOS QUE YA SE HAN GENERADO
    public static boolean todosEnArrayValidos(List<Integer> arraySeleccionados, List<Integer> arrayValidos) {
        return arrayValidos.containsAll(arraySeleccionados);
    }

    // FUNCIÓN -> COMPROBAR POR CADA ARRAY CORRESPONDIENTE A CADA FILA, SI TODOS LOS NÚMEROS SON LOS CORRESPONDIENTES
    public static boolean verificarArrays(int[] array1, int[] array2, int[] array3) {
    	return contieneAlguno(array1, arraySeleccionados) ||
               contieneAlguno(array2, arraySeleccionados) ||
               contieneAlguno(array3, arraySeleccionados);
    }

	// FUNCIÓN -> COMPROBAR SI CONTIENE TODOS LOS DE LA LINEA
	private static boolean contieneAlguno(int[] array, List<Integer> arrayValidos) {
		for (int num : array) {
			if (arrayValidos.contains(num)) {
				return true;
			}
		}
		return false;
	}

	// FUNCIÓN -> COMPROBAR SI LA LINEA SELECCIONA ES VÁLIDA
    public static boolean comprobarSiHayLinea() {
        boolean todosEnArrayValidos = todosEnArrayValidos(arraySeleccionados, arrayValidos);

        int cantidad = arraySeleccionados.size();
        if (cantidad >= 5) {
			if (todosEnArrayValidos) {
				// CONTADOR DE LAS FILAS DE ARRAYS
				int arrayFila1[] = new int[5];
				int arrayFila2[] = new int[5];
				int arrayFila3[] = new int[5];

				// CONTADOR DE LOS INTEGERS
				int contInteger1 = 0, contInteger2 = 0, contInteger3 = 0;

				// OBTENER LOS NÚMEROS DEL PRIMER ARRAY
				for (int z = 0; z < 9; z++) {
					String botonarray = arrayBotones[z].getText();
					try {
						int temp_num = Integer.parseInt(botonarray);
						if (contInteger1 < 5) {
							arrayFila1[contInteger1] = temp_num;
						}
						contInteger1++;
					} catch (Exception e) {}
				}

				// OBTENER LOS NÚMEROS DEL SEGUNDO ARRAY
				for (int z = 9; z < 18; z++) {
					String botonarray = arrayBotones[z].getText();
					try {
						int temp_num = Integer.parseInt(botonarray);
						if (contInteger2 < 5) {
							arrayFila2[contInteger2] = temp_num;
						}
						contInteger2++;
					} catch (Exception e) {}
				}

				// OBTENER LOS NÚMEROS DEL TERCER ARRAY
				for (int z = 18; z < 27; z++) {
					String botonarray = arrayBotones[z].getText();
					try {
						int temp_num = Integer.parseInt(botonarray);
						if (contInteger3 < 5) {
							arrayFila3[contInteger3] = temp_num;
						}
						contInteger3++;
					} catch (Exception e) {}
				}

				// OBTENER LA RESPUESTA DE LA FUNCIÓN "VERIFICARARRAYS"
				todosEnArrayValidos = verificarArrays(arrayFila1, arrayFila2, arrayFila3);


			}
        } else {
			// EN CASO DE QUE NO SEAN 5 NÚMEROS
        	todosEnArrayValidos = false;
			JOptionPane.showConfirmDialog(null, "Deben ser 5 números!", "Aviso", JOptionPane.DEFAULT_OPTION);
        }

        return todosEnArrayValidos;
    }
    
	// FUNCIÓN -> PASAR EL ARRAY DE INTEGER A STRING PARA PONERLO EN LOS BOTONES
    public static String pasarArrayString() {
    	StringBuilder sb = new StringBuilder();

    	for (int elemento : arraySeleccionados) {
    	    sb.append(elemento).append(", ");
    	}

    	if (sb.length() > 0) {
    	    sb.setLength(sb.length() - 2);
    	}

    	String arrayString = sb.toString();
    	return arrayString;
    }
    
	// FUNCIÓN -> POR CADA FILA, PONER LOS BOTONES DE COLOR AMARILLO
    public void colorBotones() {
    	Color ColorActivo = Color.YELLOW;
    	// PRIMERA FILA
    	for (int x=0;x<9;x++) {
    		arrayBotones[x].setBackground(ColorActivo);
    	}
    	
    	// SEGUNDA FILA
    	for (int x=9;x<18;x++) {
    		arrayBotones[x].setBackground(ColorActivo);
    	}
    	
    	// TERCERA FILA
    	for (int x=18;x<27;x++) {
    		arrayBotones[x].setBackground(ColorActivo);
    	}
    }
    
	// FUNCIÓN -> ACTIVAR TODOS LOS BOTONES
    public void activarBotones() {
    	for (int x=0;x<27;x++) {
    		arrayBotones[x].setEnabled(true);
    	}
    }
    
    
	// FUNCIÓN -> PONER LOS NÚMEROS DE LOS BOTONES POR CADA FILA DE LA FORMA CORRESPONDIENTE
    public static void ponerNumeroBoton(String numeros_str, String posiciones_str) {
        String[] numeros = numeros_str.replace(",", "").replace("[", "").replace("]", "").split(" ");
        
		// DEFINICIÓN DE LOS ARRAYS DE CADA FILA
        int[] fila1 = new int[9];
        int[] fila2 = new int[9];
        int[] fila3 = new int[9];
        
		// DEFINICIÓN DEL CONTADOR TEMPORAL DE CADA FILA
        int cont1 = 0;
        int cont2 = 0;
        int cont3 = 0;
        
		// RECORRER LOS TRES ARRAYS
        for (int i = 0; i < numeros.length && i < 27; i++) {
            int valor = Integer.parseInt(numeros[i]);
            
            if (i % 3 == 0) {
                fila1[cont1] = valor;
                cont1++;
            } else if (i % 3 == 1) {
                fila2[cont2] = valor;
                cont2++;
            } else {
                fila3[cont3] = valor;
                cont3++;
            }
        }
        
		// ASIGNAR EL TEXTO DEL NÚMERO A CADA FILA
        for (int i = 0; i < 9; i++) {
            arrayBotones[i].setText(String.valueOf(fila1[i]));
            arrayBotones[i + 9].setText(String.valueOf(fila2[i]));
            arrayBotones[i + 18].setText(String.valueOf(fila3[i]));
        }
        
        int rand_pos;

        rand_pos = random.nextInt(1,4);
        if (rand_pos == 1) {
          for (int x=0;x<27;x++) {
        	if (x==1 || x==3 || x==5 || x==7 || x==10 || x==12 || x==15 || x==17 || x==18 || x==20 || x==22 || x==24) {
        		arrayBotones[x].setEnabled(false);
        		arrayBotones[x].setText("");
        	}
          }		
        }
        else if (rand_pos == 2) {
          for (int x=0;x<27;x++) {
        	if (x==2 || x==4 || x==6 || x==7 || x==9 || x==14 || x==12 || x==17 || x==19 || x==22 || x==23 || x==25) {
        		arrayBotones[x].setEnabled(false);
        		arrayBotones[x].setText("");
        	}
          }	
        }
        else if (rand_pos == 3) {
          for (int x=0;x<27;x++) {
        	if (x==3 || x==5 || x==2 || x==8 || x==10 || x==11 || x==13 || x==16 || x==18 || x==21 || x==23 || x==24) {
        		arrayBotones[x].setEnabled(false);
        		arrayBotones[x].setText("");
        	}
          }        	
        }
        else if (rand_pos == 4) {
          for (int x=0;x<27;x++) {
        	if (x==1 || x==4 || x==5 || x==7 || x==10 || x==11 || x==15 || x==16 || x==18 || x==20 || x==23 || x==24) {
        		arrayBotones[x].setEnabled(false);
        		arrayBotones[x].setText("");
        	}
          }	
        }
        
        EscucharBotones();
    }
    
	// FUNCIÓN -> CREANDO EL ARRAY DE TIPO JBUTTON QUE CONTIENE TODOS LOS BOTONES
    private void crearArrayBotones() {
    	arrayBotones = new JButton[27];
    	arrayBotones[0] = btn1Fila1;
    	arrayBotones[1] = btn2Fila1;
    	arrayBotones[2] = btn3Fila1;
    	arrayBotones[3] = btn4Fila1;
    	arrayBotones[4] = btn5Fila1;
    	arrayBotones[5] = btn6Fila1;
    	arrayBotones[6] = btn7Fila1;
    	arrayBotones[7] = btn8Fila1;
    	arrayBotones[8] = btn9Fila1;

    	arrayBotones[9] = btn1Fila2;
    	arrayBotones[10] = btn2Fila2;
    	arrayBotones[11] = btn3Fila2;
    	arrayBotones[12] = btn4Fila2;
    	arrayBotones[13] = btn5Fila2;
    	arrayBotones[14] = btn6Fila2;
    	arrayBotones[15] = btn7Fila2;
    	arrayBotones[16] = btn8Fila2;
    	arrayBotones[17] = btn9Fila2;
    	
    	arrayBotones[18] = btn1Fila3;
    	arrayBotones[19] = btn2Fila3;
    	arrayBotones[20] = btn3Fila3;
    	arrayBotones[21] = btn4Fila3;
    	arrayBotones[22] = btn5Fila3;
    	arrayBotones[23] = btn6Fila3;
    	arrayBotones[24] = btn7Fila3;
    	arrayBotones[25] = btn8Fila3;
    	arrayBotones[26] = btn9Fila3;

    }

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JuegoBingo frame = new JuegoBingo();
					frame.setVisible(true);
			        
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// FUNCIÓN -> GENERAR NÚMEROS ALEATORIOS
	private void generarNumerosAleatorios() {
	    SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
	        @Override
	        protected Void doInBackground() throws Exception {
	            StringTokenizer tokenizer = new StringTokenizer(arrayServidorValidos, " [],");
	            while (tokenizer.hasMoreTokens()) {
	                String token = tokenizer.nextToken();
	                try {
	                    int numero = Integer.parseInt(token);
	                    lblNumeros.setText(Integer.toString(numero));
	                    publish(numero);
	                    Thread.sleep(10);
	                    while (Pausa) {
	                    	Thread.sleep(100);
	                    	if (Pausa == false) {
	                    	}
	                    }

	                } catch (NumberFormatException e) {

	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	            return null;
	        }

			// MIENTRAS EL THREAD SE ESTÁ EJECUTANDO, POR CADA NÚMERO GENERADO, GUARDARLO EN EL ARRAY DE "ARRAYVALIDOS"
	        @Override
	        protected void process(java.util.List<Integer> chunks) {
	            for (int numero : chunks) {
	                arrayValidos.add(numero);
	            }
	        }

			// CUANDO TERMINE ESTE THREAD SIGNIFICA QUE YA SE HAN GENERADO TODOS LOS NÚMEROS
			@Override
	        protected void done() {
//				System.exit(0);
	        }
	    };

		// EJECUTAR SECUENCIA DEL THREAD
	    worker.execute();
	}
	
	// FUNCIÓN PARA INICIAR NUEVA PARTIDA
	public void iniciarNuevaPartida() {
		// SOLICITAR EL NOMBRE
		jugador_nombre = JOptionPane.showInputDialog("Introduce tu nombre: ");
		lblNombres.setText("Identificado como: " + jugador_nombre);

        Cliente client = new Cliente();
        client.startConnection(ip, port);
        client.sendMessage("[%nombre%]=" + jugador_nombre);
        String srv_respuesta = client.receiveMessage();

		// SI EL SERVIDOR LE DEVUELVE "OK", ENVIAR EL NOMBRE DE USUARIO
        if (srv_respuesta.equals("Ok")) {
	        client.startConnection(ip, port);
	        client.sendMessage("[%inicia_partida%]" + jugador_nombre);
	        String srv_estaIniciada = client.receiveMessage();
	        client.finalizarConexion();
	        
			// SI LA RESPUESTA DEL SERVIDOR ES "1", LA PARTIDA SE ENCUENTRA EN CURSO
	        if (srv_estaIniciada.equals("1")) {
				int res_partidaEnCurso = JOptionPane.showConfirmDialog(null, ("La partida ya se encuentra en curso! ¿Reintentar?"), "Aviso", JOptionPane.YES_OPTION);
				if (res_partidaEnCurso == 0) {
					iniciarNuevaPartida();
				}
				else if (res_partidaEnCurso == 1) {
					System.exit(0);
				}
	        }
	        
			// EN CUALQUIER CASO, CONECTARSE DIRECTAMENTE AL SERVIDOR
	        else {
	        	client.startConnection(ip, port);
		        client.sendMessage("2");
		        
		        respuestaNumeros = client.receiveMessage();

		        String[] stringNumbers = respuestaNumeros.replaceAll("[\\[\\]]", "").split(", ");

		        int[] numbers = new int[stringNumbers.length];

		        for (int i = 0; i < stringNumbers.length; i++) {
		            numbers[i] = Integer.parseInt(stringNumbers[i].trim());
		        }
		        
		        int arrayColumna1[] = new int [9]; 
		        int arrayColumna2[] = new int [9]; 
		        int arrayColumna3[] = new int [9]; 
		        
		        int cont_pos = 0, cont_array1 = 0, cont_array2 = 0, cont_array3 = 0;
		        
		        for (int number : numbers) {
		        	if (cont_pos == 0) {
		        		arrayColumna1[cont_array1] = number;
		        		cont_array1++;
		        		cont_pos++;
		        	}
		        	
		        	else if (cont_pos == 1) {
		        		arrayColumna2[cont_array2] = number;
		        		cont_array2++;
		        		cont_pos++;
		        	}
		        	else if (cont_pos == 2) {
		        		arrayColumna3[cont_array3] = number;
		        		cont_array3++;
		        		cont_pos = 0;
		        	}
		        }
		        
		      
		        client.finalizarConexion();
		        
		        client.startConnection(ip, port);
		        client.sendMessage("3");
		        respuestaPosiciones = client.receiveMessage();
		        cont_pos = 0;
		        client.finalizarConexion();
		        
				// PREGUNTAR AL SERVIDOR LA GENERACIÓN DE NÚMEROS ALEATORIOS
		        client.startConnection(ip, port);
		        client.sendMessage("[%generar_numero%]");
		        arrayServidorValidos = client.receiveMessage();
		        client.finalizarConexion();
		        
		        crearArrayBotones();
	        	activarBotones();
		        ponerNumeroBoton(respuestaNumeros, respuestaPosiciones);	
				colorBotones();
            	llenarArrayNumsValidos();
            	
		        Runnable testRunnable2 = new Runnable() {
		            @Override
		            public void run() {	
		            	boolean iniciada = true;
		                while (iniciada) {
					        client.startConnection(ip, port);
					        client.sendMessage("[%hay_linea%]");
					        String respuestaIniciarPartida = client.receiveMessage();
					        client.startConnection(ip, port);
					        client.sendMessage("[%obtener_nombre%]");
					        String respuestaNombreObtenido = client.receiveMessage();
					        if (respuestaIniciarPartida.equals("1")) {
					        	if (lineaHecha == 0) {
					        		lineaHecha = 1;
									JOptionPane.showConfirmDialog(null, ("La linea es correcta! Completado por " + respuestaNombreObtenido), "Aviso", JOptionPane.DEFAULT_OPTION);
						        	btnLinea.setEnabled(false);
						        	Pausa = false;
					        	}
					        }

					        if (respuestaIniciarPartida.equals("2")) {
					        	if (bingoHecho == 0) {
					        		bingoHecho = 1;
									JOptionPane.showConfirmDialog(null, ("El bingo es correcto! Completado por " + respuestaNombreObtenido), "Aviso", JOptionPane.DEFAULT_OPTION);
						        	btnBingo.setEnabled(false);
						        	btnLinea.setEnabled(false);	
					        	}
					        }
							
					        if (respuestaIniciarPartida.equals("3")) {
					        	Pausa = true;
					        }

					        if (respuestaIniciarPartida.equals("4")) {
					        	if (lineaHecha == 0) {
					        		lineaHecha = 1;
									JOptionPane.showConfirmDialog(null, ("La linea es correcta! Completado por " + respuestaNombreObtenido), "Aviso", JOptionPane.DEFAULT_OPTION);
						        	btnLinea.setEnabled(false);
						        	Pausa = false;
					        	}
					        }
					        
					        if (respuestaIniciarPartida.equals("5")) {
					        	if (bingoHecho == 0) {
					        		bingoHecho = 1;
									JOptionPane.showConfirmDialog(null, ("El bingo es correcto! Completado por " + respuestaNombreObtenido), "Aviso", JOptionPane.DEFAULT_OPTION);
						        	btnBingo.setEnabled(false);
						        	btnLinea.setEnabled(false);	
					        	}
					        }
					        
					        if (respuestaIniciarPartida.equals("7")) {
					        	Pausa = false;
					        }

							if (respuestaIniciarPartida.equals("8")) {
					        	Pausa = false;
					        }
					        
					        if (respuestaIniciarPartida.equals("6")) {
					        	Pausa = false;
					        	btnLinea.setEnabled(false);
					        	btnBingo.setEnabled(false);
								JOptionPane.showConfirmDialog(null, ("El bingo es correcto! Completado por " + respuestaNombreObtenido), "Aviso", JOptionPane.YES_OPTION);
					        }
							
		                    try {
		                        TimeUnit.MILLISECONDS.sleep(1000);
		                    } catch (InterruptedException e) {
		                        e.printStackTrace();
		                    }
		                }
		            }
		        };
		        Runnable testRunnable = new Runnable() {
		            @Override
		            public void run() {	
		            	boolean iniciada = true;
		                while (iniciada) {
					        client.startConnection(ip, port);
					        client.sendMessage("[%inicia_partida%]");
					        String respuestaIniciarPartida = client.receiveMessage();
					        if (respuestaIniciarPartida.equals("1")) {
								generarNumerosAleatorios();
								client.startConnection(ip, port);
								client.sendMessage("[%obtener_nombres%]");
								String res_nombres = client.receiveMessage();
								lblNombres.setText("Tú eres: " + jugador_nombre + " - " + res_nombres);

						        Thread testThread2 = new Thread(testRunnable2);
						        testThread2.start();
								break;
					        }
		                    try {
		                        TimeUnit.MILLISECONDS.sleep(100);
		                    } catch (InterruptedException e) {
		                        e.printStackTrace();
		                    }
		                }
		            }
		        };
		        Thread testThread = new Thread(testRunnable);
		        testThread.start();
	        }
	        
        }
        else if (srv_respuesta.equals("No")) {
			JOptionPane.showConfirmDialog(JuegoBingo.this, "No se puede acceder, la partida se encuentra en curso!", "Aviso", JOptionPane.YES_OPTION);
        }
        else {

        }
        client.finalizarConexion();
	}

	
	public JuegoBingo() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 370);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][::50px,grow][grow]", "[grow][grow,fill]"));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "cell 0 0,grow");
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		lblNombres = new JLabel("");
		lblNombres.setVerticalAlignment(SwingConstants.CENTER);
		lblNombres.setHorizontalAlignment(SwingConstants.CENTER);
		lblNombres.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.add(lblNombres);
		
		JPanel numeros = new JPanel();
		contentPane.add(numeros, "cell 2 0,grow");
		numeros.setLayout(new BorderLayout(0, 0));
		
		lblNumeros = new JLabel("Esperando anfitrion");
		lblNumeros.setHorizontalAlignment(SwingConstants.CENTER);
		lblNumeros.setBorder(BorderFactory.createLineBorder(Color.black));
		numeros.add(lblNumeros, BorderLayout.CENTER);
		
		JPanel bingo = new JPanel();
		contentPane.add(bingo, "cell 0 1,grow");
		bingo.setLayout(new GridLayout(3, 9, 0, 0));
		
		btn1Fila1 = new JButton("");
		bingo.add(btn1Fila1);
		
		btn2Fila1 = new JButton("");
		bingo.add(btn2Fila1);
		
		btn3Fila1 = new JButton("");
		bingo.add(btn3Fila1);
		
		btn4Fila1 = new JButton("");
		bingo.add(btn4Fila1);
		
		btn5Fila1 = new JButton("");
		bingo.add(btn5Fila1);
		
		btn6Fila1 = new JButton("");
		bingo.add(btn6Fila1);
		
		btn7Fila1 = new JButton("");
		bingo.add(btn7Fila1);
		
		btn8Fila1 = new JButton("");
		bingo.add(btn8Fila1);
		
		btn9Fila1 = new JButton("");
		bingo.add(btn9Fila1);
		
		btn1Fila2 = new JButton("");
		bingo.add(btn1Fila2);
		
		btn2Fila2 = new JButton("");
		bingo.add(btn2Fila2);
		
		btn3Fila2 = new JButton("");
		bingo.add(btn3Fila2);
		
		btn4Fila2 = new JButton("");
		bingo.add(btn4Fila2);
		
		btn5Fila2 = new JButton("");
		bingo.add(btn5Fila2);
		
		btn6Fila2 = new JButton("");
		bingo.add(btn6Fila2);
		
		btn7Fila2 = new JButton("");
		bingo.add(btn7Fila2);
		
		btn8Fila2 = new JButton("");
		bingo.add(btn8Fila2);
		
		btn9Fila2 = new JButton("");
		bingo.add(btn9Fila2);
		
		btn1Fila3 = new JButton("");
		bingo.add(btn1Fila3);
		
		btn2Fila3 = new JButton("");
		bingo.add(btn2Fila3);
		
		btn3Fila3 = new JButton("");
		bingo.add(btn3Fila3);
		
		btn4Fila3 = new JButton("");
		bingo.add(btn4Fila3);
		
		btn5Fila3 = new JButton("");
		bingo.add(btn5Fila3);
		
		btn6Fila3 = new JButton("");
		bingo.add(btn6Fila3);
		
		btn7Fila3 = new JButton("");
		bingo.add(btn7Fila3);
		
		btn8Fila3 = new JButton("");
		bingo.add(btn8Fila3);
		
		btn9Fila3 = new JButton("");
		bingo.add(btn9Fila3);
		
		JPanel botones = new JPanel();
		botones.setBorder(null);
		contentPane.add(botones, "flowx,cell 2 1,growx,aligny center");
		botones.setLayout(new MigLayout("", "[grow][grow]", "[grow][grow]"));
		
		btnLinea = new JButton("Linea");
		
		botones.add(btnLinea, "cell 0 0,grow");
				
		JButton btnNuevaPartida = new JButton("Nueva partida");
		btnNuevaPartida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iniciarNuevaPartida();
			}
			});
		
		botones.add(btnNuevaPartida, "cell 1 0 1 2,grow");
		
		btnBingo = new JButton("Bingo");
		botones.add(btnBingo, "cell 0 1,grow");

	}

}
