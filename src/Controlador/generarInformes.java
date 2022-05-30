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
	 * Genera el informe de todas las colecciones a través de una plantilla y lo muestra por pantalla
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
	 * Genera el informe de la colección con el nombre especificado a través de una plantilla y lo muestra por pantalla
	 * @param is Fuente de la plantilla del informe (fichero .jrxml)
	 * @param coleccion Colección en la que basa el informe
	 * @return Informe de la colección basado en la plantilla
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
	 * Genera el informe de 100 cómics a partir del rango especificado a través de una plantilla y lo muestra por pantalla
	 * @param is Fuente de la plantilla del informe (fichero .jrxml)
	 * @param offset Rango a partir del cual se buscarán los cómics
	 * @return Informe de los cómics basado en la plantilla
	 */
	public static JasperPrint generarInformeComics(InputStream is, int offset) {
		JasperPrint informe = null;
		
    	try {
			JasperReport plantilla = JasperCompileManager.compileReport(is);
			
			Map<String,Object> params = new HashMap<String,Object>();
			
			params.put("offset", offset);
			
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
	 * Genera el informe de todos los cómics de la colección especificada a través de una plantilla y lo muestra por pantalla
	 * @param is Fuente de la plantilla del informe (fichero .jrxml)
	 * @param coleccion Colección de los cómics a buscar
	 * @return Informe de los cómics basado en la plantilla
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
