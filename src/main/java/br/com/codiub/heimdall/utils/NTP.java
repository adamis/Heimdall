package br.com.codiub.heimdall.utils;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

/**
* @author adamis.rocha
*
* @since  08 nov 2018
*         Updates: Inserção de servidores da América do Sul. Sugestão de padronização de código.
*         Sugestão de acesso a host por laço for ao invés de while. Inserção de funcionalidade de
*         formatação do retorno Date para String.
*/
public class NTP {
	
	private static final Integer FIVE_THOUSAND = 5000;
	private static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss.SSS";
	private static final String DD_MM_YYYY = "dd/MM/yyyy";
	private static final String HH_MM_SS = "HH:mm:ss";
	private static final String[] hosts = new String[] { 
		"0.south-america.pool.ntp.org",	
		"1.south-america.pool.ntp.org",
		"2.south-america.pool.ntp.org",
		"3.south-america.pool.ntp.org",
		"2.br.pool.ntp.org",
		"0.pool.ntp.org",
		"1.pool.ntp.org",
		"2.pool.ntp.org",
		"3.pool.ntp.org",   
		"time.windows.com",
		"time.windows.org",
		"time-a-g.nist.gov",
		"a.st1.ntp.br",
		"ntp02.oal.ul.pt", 
		"ntp04.oal.ul.pt",
		"ntp.xs4all.nl",
		"time.foo.com",
		"time.nist.gov",
	};

	/**
	 * Retorna a data e hora atual via NTP Servers.
	 * @return O objeto Date
	 * @throws Exception	 
	 */
	public static Date getDateTime() throws Exception {
		Date currentDate = currentDate(); 		
		SimpleDateFormat formatDateTime = new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS);
		currentDate = formatDateTime.parse(formatDateTime.format(currentDate));		
		return currentDate;
	}
	
	/**
	 * Retorna a data e hora atual formatada via NTP Servers.
	 * @return O objeto String
	 * @throws Exception	 
	 */
	public static String getDateTimeToString() throws Exception {
		Date currentDate = currentDate(); 		
		SimpleDateFormat formatDateTime = new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS);
		return formatDateTime.format(currentDate);
	}

	/**
	 * Retorna somente a data atual via NTP Servers.
	 * @return O objeto Date
	 * @throws Exception
	 */
	public static Date getDate() throws Exception {
		Date currentDate = currentDate();		
		SimpleDateFormat formatDate = new SimpleDateFormat(DD_MM_YYYY);
		currentDate = formatDate.parse(formatDate.format(currentDate));
		return currentDate;
	}

	/**
	 * Retorna somente a data atual formatada via NTP Servers.
	 * @return O objeto String
	 * @throws Exception
	 */
	public static String getDateToString() throws Exception {
		Date currentDate = currentDate();
		SimpleDateFormat formatDate = new SimpleDateFormat(DD_MM_YYYY);
		return formatDate.format(currentDate);
	}	

	/**
	 * Retorna somente a hora atual via NTP Servers.
	 * @return O objeto Date
	 * @throws Exception
	 */
	public static Date getTime() throws Exception {
		Date currentDate = currentDate();		
		SimpleDateFormat formatTime = new SimpleDateFormat(HH_MM_SS);
		currentDate = formatTime.parse(formatTime.format(currentDate));
		return currentDate;
	}
	
	/**
	 * Retorna somente a hora atual formatada via NTP Servers.
	 * @return O objeto String
	 * @throws Exception
	 */
	public static String getTimeToString() throws Exception {
		SimpleDateFormat formatTime = new SimpleDateFormat(HH_MM_SS);
		Date currentDate = currentDate();
		return formatTime.format(currentDate);
	}	

	/**
	 * Retorna a data atual de acordo com o padrão de formatação via NTP Servers.
	 * @param pattern O padrão para formatação
	 * @return O objeto String
	 * @throws Exception
	 */
	public static String getDatePattern(String pattern) throws Exception {
		Date currentDate = currentDate();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);    
		return sdf.format(currentDate);
	}

	/**
	 * Retorna a data atual após 5 segundos a partir de um host.
	 * @return O objeto Date
	 */
	private static Date currentDate() {
		Date currentDate = null;
		for (String host : hosts) {
			currentDate = currentDateByHost(host);
			if (currentDate != null) {
				break;
			}		
		}
		return currentDate;
	}
	
	/**
	 * Retorna a data atual de um host.
	 * @param host
	 * @return O objeto de Date
	 */
	private static Date currentDateByHost(String host) {
		try {       
			NTPUDPClient timeClient = new NTPUDPClient();
			timeClient.setDefaultTimeout(FIVE_THOUSAND);		
			InetAddress inetAddress = InetAddress.getByName(host);
			TimeInfo timeInfo = timeClient.getTime(inetAddress);
			Long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
			return new Date(returnTime);
		} catch (Exception e) {
			return null;
		}
	}

}