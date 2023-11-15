import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;
import java.util.Set;
import java.util.SortedSet;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.util.Random;

public class ServidorGrafico extends JFrame {
    // DEFINICIÓN DE TODAS LAS VARIABLES
	private static String arrayValidos = "";
	private static String arrayNumeros = "";
	private static String arrayNombres = ""; 
	private static String nombreGanador = "";
	private static String arrayPosiciones = "";
	private static boolean partidaActivada = false;
	private static String partidaIniciada = "0";
	private static String partidaPausada = "0";
	private static String hayLinea = "0";
	private static String hayBingo = "0";
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private ServerSocket serverSocket;
    private static final Random rand = new Random();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                    ServidorGrafico frame = new ServidorGrafico();
                    frame.setVisible(true);
                    new Thread(() -> frame.start(6666)).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
    // ARRAY TRIDIMENSIONAL CON LOS NÚMEROS
    int array[][][] = new int [3][90][100];
    
    // FUNCIÓN -> DETENER LA ESCUCHA DEL SERVIDOR
    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor detenido.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // FUNCIÓN -> COMENZAR LA ESCUCHA DEL SERVIDOR
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado en el puerto " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(" [" + clientSocket.getInetAddress().getHostAddress() + "]");
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }
    
    // FUNCIÓN -> GENERAR LOS NÚMEROS EN EL RANGO VÁLIDO
    private static int generarNumeroEnRango(int inicioRango, Set<Integer> excluidos) {
        int numero;
        do {
            numero = inicioRango + 1 + rand.nextInt(9); 
        } while (excluidos.contains(numero) || numero % 10 == 0);
        excluidos.add(numero);
        return numero;
    }
    
    // FUNCIÓN -> CONVERTIR EL STRING ARRAY A INT ARRAY
    public static int[] setToIntArray(Set<Integer> set) {
        int[] array = new int[set.size()];
        int index = 0;
        for (int num : set) {
            array[index] = num;
            index++;
        }
        return array;
    }
    
    // FUNCIÓN -> REALIZAR COMPROBACIÓN DE LA LÍNEA RECIBIENDO LOS NÚMEROS
    public static boolean comprobarLinea(String numeros_comprobar) {
    	boolean esValido = false;
        String[] numerosArray = numeros_comprobar.split(", ");

        String[] arrayValidosArray = arrayValidos.split(", ");

        for (String numeroStr : numerosArray) {
            if (Arrays.asList(arrayValidosArray).contains(numeroStr)) {
                System.out.println(numeroStr + " está en arrayValidos");
                esValido = true;
            } else {
                System.out.println(numeroStr + " NO está en arrayValidos");
                esValido = false;
            }
        }
        return esValido;
    }
    
    // FUNCIÓN -> GENERAR LAS POSICIONES QUE VAN A ESTAR DESACTIVADAS
    public static String generarPosiciones() {
        Random rand = new Random();
        List<Integer> numeros = new ArrayList<>();

        // Generar y ordenar números en el primer bloque
        for (int i = 0; i < 4; i++) {
            int numero;
            do {
                numero = rand.nextInt(9);
            } while (numeros.contains(numero));
            numeros.add(numero);
        }
        Collections.sort(numeros);

        // Generar y ordenar números en el segundo bloque
        for (int i = 0; i < 4; i++) {
            int numero;
            do {
                numero = rand.nextInt(9) + 9;
            } while (numeros.contains(numero));
            numeros.add(numero);
        }
        Collections.sort(numeros);

        // Generar y ordenar números en el tercer bloque
        for (int i = 0; i < 4; i++) {
            int numero;
            do {
                numero = rand.nextInt(9) + 18;
            } while (numeros.contains(numero));
            numeros.add(numero);
        }
        Collections.sort(numeros);

//        System.out.print(numeros.toString());
        return numeros.toString();
    }

    // FUNCIÓN -> GENERAR NÚMEROS VÁLIDOS NO REPETIDOS
    public static String generarNumerosValidos() {
        List<Integer> todosLosNumeros = new ArrayList<>();

        for (int i = 1; i <= 90; i++) {
            todosLosNumeros.add(i);
        }

        Collections.shuffle(todosLosNumeros);

        return todosLosNumeros.toString();
    }
    
    // FUNCIÓN -> GENERAR NÚMEROS ÚNICOS
    public static String generarNumerosUnicos() {
        Set<Integer> excluidos = new HashSet<>();
        List<Integer> primerConjunto = new ArrayList<>();
        List<Integer> segundoConjunto = new ArrayList<>();
        List<Integer> tercerConjunto = new ArrayList<>();
        List<Integer> todosLosNumeros = new ArrayList<>();
        
        // Primer for generando los números no repetidos en el rango correspondiente
        for (int i = 0; i < 9; i++) {
            primerConjunto.add(generarNumeroEnRango(i * 10 + 1, excluidos));
        }
        
        // Segundo for generando los números no repetidos en el rango correspondiente
        for (int i = 0; i < 9; i++) {
            segundoConjunto.add(generarNumeroEnRango(i * 10 + 1, excluidos));
        }
        
        // Tercer for generando los números no repetidos en el rango correspondiente
        for (int i = 0; i < 9; i++) {
            tercerConjunto.add(generarNumeroEnRango(i * 10 + 1, excluidos));
        }

        // Agregando los tres arrays al array "todoslosNumeros"
        todosLosNumeros.addAll(primerConjunto);
        todosLosNumeros.addAll(segundoConjunto);
        todosLosNumeros.addAll(tercerConjunto);

        // Ordenarlo de menor a mayor
        Collections.sort(todosLosNumeros);

        // Retornarlo en String
        return todosLosNumeros.toString();
    }
    
    // THREAD PARA MANTENER EL SERVIDOR A LA ESCUCHA MIENTRAS SE ENCIENDE DE LA INTERFAZ GRÁFICA
    public class IniciarServidor extends Thread {
        private int port;

        public IniciarServidor(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Servidor iniciado y escuchando en el puerto: " + port);

                while (!interrupted()) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        System.out.println("Servidor interrumpido");
                        break;
                    }
                }
                
                System.out.println("Servidor detenido.");

            } catch (IOException e) {
                System.out.println("Excepción del servidor: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // FUNCIÓN -> DETENER EL SERVIDOR
        public void detenerServidor() {
            this.interrupt();
        }
    }
    
    // SEGUNDO THREAD A LA ESCUCHA DE RECIBIR PETICIONES POR PARTE DE LOS CLIENTES
    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				arrayNumeros = generarNumerosUnicos();

                String clientInput = in.readLine();


                // RECIBIENDO Y FILTRANDO LAS CONSULTAS DE LOS CLIENTES

                // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "2" -> GENERAR LOS NÚMEROS
                if (clientInput.equals("2")) {
                	arrayNumeros = generarNumerosUnicos();
                	out.println(arrayNumeros);
                }

                // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "3"-> GENERAR LAS POSICIONES
                else if (clientInput.equals("3")) {
                	arrayPosiciones = generarPosiciones();
                	out.println(arrayPosiciones);
                }
                
                // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "[%nombre%]="-> RECOGER EL NOMBRE QUE VIENE EN EL MENSAJE
                else if (clientInput.contains("[%nombre%]=")) {
                	String nombre_jugador = clientInput.replace("[%nombre%]=", "");
                	System.out.print("[$] " + nombre_jugador + " se ha unido a la partida!");
                	arrayNombres += nombre_jugador + " ";
                	System.out.print("Array Nombres: " + arrayNombres);
                	if (partidaActivada) {
                    	out.println("Ok");                		
                	}
                	else {
                    	out.println("No");                		
                	}
                }

                // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "[%comprobar_numeros%]="-> RECOGER EL NÚMERO QUE VIENE EN EL MENSAJE
                else if (clientInput.contains("[%comprobar_numeros%]=")) {
                	String comprobar_numeros_str = clientInput.replace("[%comprobar_numeros%]=", "");
                	System.out.print(comprobar_numeros_str);
                	System.out.print("Array Validos: " + arrayValidos);
                	boolean esValido = comprobarLinea(comprobar_numeros_str);
                	if (esValido) {
                		out.println("0");
                	}
                	else {
                		out.println("1");
                	}
                }

                else if (clientInput.contains("[%obtener_nombres%]")) {
                	out.println("Jugadores conectados: " + arrayNombres);
                }

                // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "[%generar_numero%]="-> GENERAR NÚMEROS ALEATORIOS Y ENVIÁRSELOS AL CLIENTE
                else if (clientInput.contains("[%generar_numero%]")) {
                	out.println(arrayValidos);
                }

                // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "[%inicia_partida%]="-> RETORNAR SI LA PARTIDA ESTÁ INICIADA O NO
                else if (clientInput.contains("[%inicia_partida%]")) {
                	out.println(partidaIniciada);
                }

                // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "[%pausar_partida%]="-> ALTERAR LA VARIABLE QUE GUARDA SI LA PARTIDA ESTÁ PAUSADA
                else if (clientInput.contains("[%pausar_partida%]")) {
                	if (partidaPausada == "0") {
                    	partidaPausada = "1";                		
                	}
                	out.println("Ok");
                }

                // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "[%enviar_alteracion%]"-> RECOGER LA LÍNEA Y ALTERAR
                else if (clientInput.contains("[%enviar_alteracion%]")) {
                	clientInput = clientInput.replace("[%enviar_alteracion%]=", "");

                	hayLinea = clientInput;
                	System.out.print("\nNombre: " + hayLinea + "\n");
                	out.println("Ok");
                }
                
                // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "[%alterar_nombre%]="-> ALTERAR LA VARIABLE QUE GUARDA EL NOMBRE DEL JUGADOR QUE HA HECHO LÍNEA
                else if (clientInput.contains("[%alterar_nombre%]")) {
                	clientInput = clientInput.replace("[%alterar_nombre%]=", "");

                	nombreGanador = clientInput;
                	System.out.print("\nNombre: " + nombreGanador + "\n");
                	out.println(nombreGanador);
                }
                
                // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "[%obtener_nombre%]="-> OBTENER EL NOMBRE DEL JUGADOR GANADOR ACCEDIENDO A LA VARIABLE Y RETORNÁRSELO AL CLIENTE
                else if (clientInput.contains("[%obtener_nombre%]")) {
                	out.println(nombreGanador);
                }
                

                // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "[%alterar_bingo%]="-> ALTERAR LA VARIABLE DE BINGO 
                else if (clientInput.contains("[%alterar_bingo%]")) {
                	clientInput = clientInput.replace("[%alterar_bingo%]=", "");

                	hayBingo = clientInput;
                	System.out.print("\nNombre: " + hayBingo + "\n");
                	out.println("Ok");
                }

                 // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "[%hay_linea%]"-> ENVIAR EL CONTENIDO DE LA VARIABLE "hayLinea" 
                else if (clientInput.contains("[%hay_linea%]")) {
                	out.println(hayLinea);
                }
                
                // EN CASO DE QUE EL MENSAJE QUE LE ENVÍE EL CLIENTE SEA "[%hay_bingo%]"-> ENVIAR EL CONTENIDO DE LA VARIABLE "hayBingo" 
                else if (clientInput.contains("[%hay_bingo%]")) {
                	out.println(hayBingo);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    clientSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
	public ServidorGrafico() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 235);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnIniciarPartida = new JButton("Iniciar Partida");
        // Poner a la escucha el botón de "Iniciar Partida"
		btnIniciarPartida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				arrayValidos = generarNumerosValidos();
				partidaActivada = true;
			}
		});

		btnIniciarPartida.setBounds(65, 89, 130, 23);
		contentPane.add(btnIniciarPartida);
		
		JButton btnComenzarPausar = new JButton("Comenzar!");
        // Poner a la escucha el botón de "Comenzar/Pausar"
		btnComenzarPausar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                // Si el estado de la variable "partidaIniciada" es "0", cambiar a "1"  y texto a "Comenzar!"
				if (partidaIniciada == "0") {
					partidaIniciada = "1";
					btnComenzarPausar.setText("Detener!");					
				}

                // Si el estado de la variable "partidaIniciada" es "1", cambiar a "0" y texto a "Comenzar!"
				else if (partidaIniciada == "1") {
					partidaIniciada = "0";
					btnComenzarPausar.setText("Comenzar!");		
				}

			}
		});
        
		btnComenzarPausar.setBounds(248, 89, 130, 23);
		contentPane.add(btnComenzarPausar);
	}
}
