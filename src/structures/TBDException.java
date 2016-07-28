package structures;

/**
 * Created by Marius on 24/07/2016.
 */
public class TBDException extends Exception {

    private int errorCode;

    TBDException(int errCode){
        super(TBD.ERROR_CODE.getErrorDescription(errCode));
        errorCode = errCode;
    }

    TBDException(int errCode, String message){
        super(TBD.ERROR_CODE.getErrorDescription(errCode)+message);
        errorCode = errCode;
    }

    public int getErrorCode(){
        return errorCode;
    }


}
