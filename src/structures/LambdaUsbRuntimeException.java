package structures;

/**
 * Created by Marius on 25/07/2016.
 */
public class LambdaUsbRuntimeException extends RuntimeException {

    private LambdaUsb.Error error;

    LambdaUsbRuntimeException(LambdaUsb.Error err){
        super(err.toString());
        error = err;
    }

    LambdaUsbRuntimeException(LambdaUsb.Error err, String message){
        super(err.toString() + message);
        error = err;
    }

    LambdaUsbRuntimeException(int errCode){
        super(LambdaUsb.Error.getFromCode(errCode).toString());
        error = LambdaUsb.Error.getFromCode(errCode);
    }

    LambdaUsbRuntimeException(int errCode, String message){
        super(LambdaUsb.Error.getFromCode(errCode).toString()+message);
        error = LambdaUsb.Error.getFromCode(errCode);
    }

}
