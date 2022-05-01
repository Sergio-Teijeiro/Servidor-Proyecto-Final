package Controlador;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

public class generarInformes {

	public static JasperPrint generarInformeColecciones(InputStream is) {
		JasperPrint informe = null;
		
    	try {
			JasperReport plantilla = JasperCompileManager.compileReport(is);
			
			Map<String,Object> params = new HashMap<String,Object>();
			
			informe = JasperFillManager.fillReport(plantilla, params, Pool.getConexion());
			
			JasperViewer.viewReport(informe, false);
			
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Pool.Cerrar();
		}
    	
    	return informe;
	}

}
