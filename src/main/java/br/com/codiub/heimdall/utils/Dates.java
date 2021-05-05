/**
 *
 */
package br.com.codiub.heimdall.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Controller;

/**
 * @author otavio.folador
 *
 */
@Controller
public class Dates {

	private SimpleDateFormat formatEntrada = new SimpleDateFormat("dd/MM/yyyy");

	public Date ultimoDiaMesAno(Integer mes, Integer ano) throws ParseException {
		
		String mesTemp = arredondar("" + mes, 2, "0", true, false);
		
		// String mesTemp = mes > 9 ? "" + mes : "0" + mes;

		String fim = "01/" + mesTemp + "/" + ano;
		
		Date parse = formatEntrada.parse(fim);
		Calendar cal = Calendar.getInstance();
		cal.setTime(parse);
		cal.add(Calendar.MONTH, +1);
		cal.add(Calendar.DAY_OF_MONTH, -1);

		return cal.getTime();
	}

	public String arredondar(String text, Integer casas, String caracter, Boolean esqueda, Boolean direita) {

		if (esqueda) {
			if (text.length() != casas) {
				for (int i = text.length(); i < casas; i++) {
					text = caracter + text;
				}
			}

		} else if (direita) {
			if (text.length() != casas) {
				for (int i = text.length(); i < casas; i++) {
					text += caracter;
				}
			}

		}

		return text;
	}
	
	public boolean verificaData(Date data) {
		if (data != null) {
			
			boolean after = data.after(new Date());
			int equal = data.compareTo(new Date());

			if (after || (equal == 0)) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

}
