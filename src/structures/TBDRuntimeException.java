package structures;

/**
 * Created by Marius on 25/07/2016.
 */
public class TBDRuntimeException extends RuntimeException {

    private int errorCode;

    TBDRuntimeException(int errCode){
        super(TBD.ERROR_CODE.getErrorDescription(errCode));
        errorCode = errCode;
    }

    TBDRuntimeException(int errCode, String message){
        super(TBD.ERROR_CODE.getErrorDescription(errCode)+message);
        errorCode = errCode;
    }

}
