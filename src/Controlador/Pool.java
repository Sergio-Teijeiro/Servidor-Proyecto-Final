package Controlador;

import java.io.FileInputStream;
import java.sql.*;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;


public class Pool {

    static Connection Con;
    static BasicDataSource basicdatasource= new BasicDataSource();
    private static String BD = "comics";//*******Indica la BD **************
    private static String IP = "localhost";//*******Indica la IP ***************

    public static Connection IniciaPoolconFichero() throws SQLException {
        try {
            Properties propiedades = new Properties();
            propiedades.load(new FileInputStream("src//Controlador//connect.txt"));
            basicdatasource = (BasicDataSource) BasicDataSourceFactory.createDataSource(propiedades);
      
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Error en la conexi�n a la base de datos (Pool con Fichero)");
        }
        return null;
     }
    public static Connection IniciaPool() throws SQLException {
        try {
        
            
            //basicdatasource.setDriverClassName("org.mariadb.jdbc.Driver");
            basicdatasource.setDriverClassName("com.mysql.jdbc.Driver");
            basicdatasource.setUsername("root");
            basicdatasource.setPassword("root");
            //basicdatasource.setUrl("jdbc:mariadb://" + IP + ":3306/" + BD);
            basicdatasource.setUrl("jdbc:mysql://" + IP + ":3306/" + BD);
            //comprobación de conexion
             basicdatasource.setValidationQuery("Select 1");
            basicdatasource.setMaxTotal(1);//conexiones del pool
            //basicdatasource.setInitialSize(5); //Cuando el pool comienza el número mínimo de conexiones
            basicdatasource.setMinIdle(50); //Numero minimo de conexiones inactivas que queremos que haya
            basicdatasource.setMaxIdle(100); //Numero máximo de conexciones inactivas que queremos que haya
            basicdatasource.setMaxWaitMillis(1000); //Tiempo que espera en las conexiones.
         
            //Si queremos comprobar que funciona bastaría con hacer un getConexion
            //Connection con=basicdatasource.getConnection();
            //System.out.println("Conectado");
            
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Error en la conexi�n a la base de datos (Pool)");
           //e.printStackTrace();
        }
        return null;
     }

   
    public static Connection getConexion() throws SQLException {
    //    System.out.println("Conexiones activas Antes"+basicdatasource.getNumActive());
        Con = basicdatasource.getConnection();
        Con.setAutoCommit(false);
      //  System.out.println("Conexiones activas Despues"+basicdatasource.getNumActive());
        return Con;
        
    }
    public static void Cerrar(){
        try {
            Con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Pool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  

}
