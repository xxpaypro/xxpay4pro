package org.xxpay.mch.utils.printerUtils.exception;

public class JsonParseException extends ServerErrorException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JsonParseException() {
        super("JSON_PARSE_ERROR", "JSON解析出错");
    }

    public JsonParseException(String message) {
        super("JSON_PARSE_ERROR", message);
    }

}
