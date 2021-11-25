package wine.com.br.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String message;
	private HttpStatus httpStatus;
}