/**
 * 
 */
package br.com.codiub.heimdall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ReportType {
	CSV("CSV"),
	PDF("PDF"),
	HTML("HTML"),
	XLSX("XLSX"),
	DOCX("DOCX"),
	RTF("RTF")
	;
	@Getter private String value;
	
}
