package br.com.codiub.heimdall.response;

import lombok.Data;

@Data
public class Response<T> {
	private String message;
	private T result;
	private int code;
}
