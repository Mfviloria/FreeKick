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

public class Grafica2 {
    
    private double[] x;
    private double[] velocidad; 
    sim s = new sim();
    private JButton btnGraficar;
    
    private final double G = 9.8; 
    
    private Plot2DPanel plot = new Plot2DPanel();

    public Grafica2() {
        
        btnGraficar = new JButton("Graficar");
        btnGraficar.setFont(new Font("Monospaced", Font.BOLD, 16));
        btnGraficar.setBackground(Color.GREEN);


        JFrame frame = new JFrame("Gráfica Movimiento Parabólico (X vs V)");
        frame.setBackground(Color.GREEN);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.add(construirPanelPrincipal());
        frame.setVisible(true);


        btnGraficar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // Obtener los valores ingresados
                double velocidadInicial = s.vo1;
                double anguloGrados = s.ang1;
                
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
                plot.addScatterPlot("Posición (x)", x, velocidad); // Graficar velocidad
                plot.addLinePlot("Trayectoria", x, velocidad); // Graficar velocidad
                plot.setAxisLabels("Posición (m)", "Velocidad (m/s)"); // Etiquetas de ejes
            }
        });
    }


    private void generarTrayectoria(double v0, double angulo) {
        double tTotalVuelo = (2 * v0 * Math.sin(angulo)) / G; 
        int numPuntos = 500; 
        double intervaloTiempo = tTotalVuelo / numPuntos;
        boolean r = s.j;

        
        x = new double[numPuntos];
        velocidad = new double[numPuntos]; 

        for (int i = 0; i < numPuntos; i++) {
            double t = i * intervaloTiempo;
            x[i] = v0 * t * Math.cos(angulo); 
            double vX = v0 * Math.cos(angulo); 
            double vY = v0 * Math.sin(angulo) - G * t; 
            velocidad[i] = Math.sqrt(vX * vX + vY * vY); 
        }
    }

    
    private JPanel construirPanelPrincipal() {
        JPanel pPrincipal = new JPanel();
        pPrincipal.setLayout(new BorderLayout());
        pPrincipal.add(plot, BorderLayout.CENTER); 
        pPrincipal.add(construirPanelSur(), BorderLayout.SOUTH);
        return pPrincipal;
    }


    private JPanel construirPanelSur() {
        JPanel pSur = new JPanel();
        pSur.add(btnGraficar); 
        return pSur;
    }

    public static void main(String[] args) {
        Grafica2 graficas = new Grafica2();
    }
}
