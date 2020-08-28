package com.webank.blockchain.acct.gov.tool;

public class JacksonException extends RuntimeException {

    /** @Fields serialVersionUID : TODO */
    private static final long serialVersionUID = -2562202775306914220L;

    public JacksonException() {
        super();
    }

    public JacksonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JacksonException(String message) {
        super(message);
    }

    public JacksonException(Throwable cause) {
        super(cause);
    }
}
