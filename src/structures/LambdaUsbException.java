package structures;

/**
 * Created by Marius on 24/07/2016.
 */
public class LambdaUsbException extends Exception {

    private LambdaUsb.Error error;

    LambdaUsbException(LambdaUsb.Error err){
        super(err.toString());
        error = err;
    }

    LambdaUsbException(LambdaUsb.Error err, String message){
        super(err.toString() + message);
        error = err;
    }

    LambdaUsbException(int errCode){
        super(LambdaUsb.Error.getFromCode(errCode).toString());
        error = LambdaUsb.Error.getFromCode(errCode);
    }

    LambdaUsbException(int errCode, String message){
        super(LambdaUsb.Error.getFromCode(errCode).toString()+message);
        error = LambdaUsb.Error.getFromCode(errCode);
    }

    public LambdaUsb.Error getError(){
        return error;
    }


}
