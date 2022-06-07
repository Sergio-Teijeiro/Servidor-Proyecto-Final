package Controlador;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import Modelo.Coleccion;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Clase encargada de generar los diferentes informes del programa
 * @author admin
 *
 */
public class generarInformes {

	/**
	 * Genera el informe de todas las colecciones a trav�s de una plantilla y lo muestra por pantalla
	 * @param is Fuente de la plantilla del informe (fichero .jrxml)
	 * @return Informe de todas las colecciones basado en la plantilla
	 */
	public static JasperPrint generarInformeColecciones(InputStream is) {
		JasperPrint informe = null;
		
    	try {
			JasperReport plantilla = JasperCompileManager.compileReport(is);
			
			Map<String,Object> params = new HashMap<String,Object>();
			
			informe = JasperFillManager.fillReport(plantilla, params, Pool.getConexion());
			
			JasperViewer.viewReport(informe, false);
			
		} catch (JRException e) {
			//e.printStackTrace();
		} catch (SQLException e) {
			//e.printStackTrace();
		} finally {
			Pool.Cerrar();
		}
    	
    	return informe;
	}

	/**
	 * Genera el informe de la colecci�n con el nombre especificado a trav�s de una plantilla y lo muestra por pantalla
	 * @param is Fuente de la plantilla del informe (fichero .jrxml)
	 * @param coleccion Colecci�n en la que basa el informe
	 * @return Informe de la colecci�n basado en la plantilla
	 */
	public static JasperPrint generarInformeColPorNombre(InputStream is, Coleccion coleccion) {
		JasperPrint informe = null;
		
    	try {
			JasperReport plantilla = JasperCompileManager.compileReport(is);
			
			Map<String,Object> params = new HashMap<String,Object>();
			
			params.put("nombreCol", coleccion.getNombre());
			params.put("idCol", coleccion.getId());
			
			informe = JasperFillManager.fillReport(plantilla, params, Pool.getConexion());
			
			JasperViewer.viewReport(informe, false);
			
		} catch (JRException e) {
			//e.printStackTrace();
		} catch (SQLException e) {
			//e.printStackTrace();
		} finally {
			Pool.Cerrar();
		}
    	
    	return informe;
	}

	/**
	 * Genera el informe de 100 c�mics a partir del rango especificado a trav�s de una plantilla y lo muestra por pantalla
	 * @param is Fuente de la plantilla del informe (fichero .jrxml)
	 * @param offset Rango a partir del cual se buscar�n los c�mics
	 * @return Informe de los c�mics basado en la plantilla
	 */
	public static JasperPrint generarInformeComics(InputStream is) {
		JasperPrint informe = null;

    	try {
			JasperReport plantilla = JasperCompileManager.compileReport(is);
			
			Map<String,Object> params = new HashMap<String,Object>();
			
			informe = JasperFillManager.fillReport(plantilla, params, Pool.getConexion());
			
			JasperViewer.viewReport(informe, false);
			
		} catch (JRException e) {
			//e.printStackTrace();
		} catch (SQLException e) {
			//e.printStackTrace();
		} finally {
			Pool.Cerrar();
		}
    	
    	return informe;
	}

	/**
	 * Genera el informe de todos los c�mics de la colecci�n especificada a trav�s de una plantilla y lo muestra por pantalla
	 * @param is Fuente de la plantilla del informe (fichero .jrxml)
	 * @param coleccion Colecci�n de los c�mics a buscar
	 * @return Informe de los c�mics basado en la plantilla
	 */
	public static JasperPrint generarInformeComicsPorCol(InputStream is, Coleccion coleccion) {
		JasperPrint informe = null;
		
    	try {
			JasperReport plantilla = JasperCompileManager.compileReport(is);
			
			Map<String,Object> params = new HashMap<String,Object>();
			
			params.put("nombre_col", coleccion.getNombre());
			params.put("id_col", coleccion.getId());
			
			informe = JasperFillManager.fillReport(plantilla, params, Pool.getConexion());
			
			JasperViewer.viewReport(informe, false);
			
		} catch (JRException e) {
			//e.printStackTrace();
		} catch (SQLException e) {
			//e.printStackTrace();
		} finally {
			Pool.Cerrar();
		}
    	
    	return informe;
	}

}
