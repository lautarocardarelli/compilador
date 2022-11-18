package lyc.compiler.model;

import java.io.Serial;

public class TokenNotFound extends CompilerException {
    public TokenNotFound(String message) {
        super(message);
    }
}