package programa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.math.plot.Plot2DPanel;

public class Graficas {
    // Datos a tratar en la gráfica
    private double[] x;
    private double[] y;
    sim s = new sim();
    private JButton btnGraficar;

    // Constantes
    private final double G = 9.8; // Gravedad

    // Objeto que permite graficar
    private Plot2DPanel plot = new Plot2DPanel();

    public Graficas() {
        // Instancia a los elementos gráficos
        btnGraficar = new JButton("Graficar");
        btnGraficar.setFont(new Font("Monospaced", Font.BOLD, 16));
        btnGraficar.setBackground(Color.GREEN);

        // Generar ventana
        JFrame frame = new JFrame("Gráfica Movimiento Parabólico (X vs Y)");
        frame.setBackground(Color.GREEN);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.add(construirPanelPrincipal());
        frame.setVisible(true);

        // Evento clic
        btnGraficar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                double velocidadInicial = s.vo1;
                double anguloGrados = s.ang1;

                // Validar valores
                if (velocidadInicial <= 0) {
                    System.out.println("La velocidad inicial debe ser mayor que cero.");
                    return; 
                }

                if (anguloGrados < 0 || anguloGrados > 90) {
                    System.out.println("El ángulo debe estar entre 0 y 90 grados.");
                    return; 
                }

                
                double anguloRadianes = Math.toRadians(anguloGrados);

                
                generarTrayectoria(velocidadInicial, anguloRadianes);
                
                plot.removeAllPlots();
                plot.addScatterPlot("Datos", x, y);
                plot.addLinePlot("Trayectoria", x, y);

            }
        });
    }

    // Generar puntos para la trayectoria
    private void generarTrayectoria(double v0, double angulo) {
        boolean r = s.j;
        double tTotalVuelo = (2 * v0 * Math.sin(angulo)) / G; // Tiempo total de vuelo
        int numPuntos = 200; // Número de puntos para la gráfica
        double intervaloTiempo = tTotalVuelo / numPuntos;

        // Crear arreglos temporales para x e y
        double[] xTemp = new double[numPuntos];
        double[] yTemp = new double[numPuntos];
        int contador = 0;

        for (int i = 0; i < numPuntos; i++) {
            double t = i * intervaloTiempo;
            double vx, vy;

            if (r == false) {
                xTemp[i] = v0 * t * Math.cos(angulo); // Posición en x
                yTemp[i] = (v0 * t * Math.sin(angulo)) - (0.5 * G * Math.pow(t, 2)); // Posición en y
            } else {
                vx = v0 * Math.cos(angulo) - 0.47 * t; // Considerar fricción
                vy = v0 * Math.sin(angulo) - G * t; // Considerar fricción
                xTemp[i] = vx * t - ((0.47 * G) / 2) * Math.pow(t, 2);
                yTemp[i] = (vy * t) - (0.5 * G * Math.pow(t, 2)); // Posición en y
            }

            // Solo agregar puntos positivos
            if (xTemp[i] >= 0 && yTemp[i] >= 0) {
                if (contador == 0) {
                    x = new double[numPuntos]; // Inicializar el arreglo x
                    y = new double[numPuntos]; // Inicializar el arreglo y
                }
                x[contador] = xTemp[i];
                y[contador] = yTemp[i]*500;
                contador++;
            }
        }

        x = java.util.Arrays.copyOf(x, contador);
        y = java.util.Arrays.copyOf(y, contador);
    }

    // Panel principal que contiene los otros paneles para generar la interfaz gráfica
    private JPanel construirPanelPrincipal() {
        JPanel pPrincipal = new JPanel();
        pPrincipal.setBackground(Color.YELLOW);
        pPrincipal.setLayout(new BorderLayout());
        pPrincipal.add(plot, BorderLayout.CENTER); // Se posiciona la gráfica en el centro del panel principal
        pPrincipal.add(construirPanelSur(), BorderLayout.SOUTH);
        return pPrincipal;
    }

    // Panel que se localiza en la parte inferior de la interfaz gráfica
    private JPanel construirPanelSur() {
        JPanel pSur = new JPanel();
        pSur.setBackground(Color.LIGHT_GRAY);
        pSur.add(btnGraficar);
        return pSur;
    }

    public static void main(String[] args) {
        Graficas graficas = new Graficas();
    }
}
