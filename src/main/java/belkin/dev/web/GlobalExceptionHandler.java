package belkin.dev.web;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {

    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ServerErrorDto> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Запрос с невалидными данными", e);
        String detailMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .collect(Collectors.joining(","));
        var errorDto = new ServerErrorDto("Ошибка валидации запроса", detailMessage,
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDto> handleNotFoundException(EntityNotFoundException e) {
        log.error("Сущность не найдена", e);
        var errorDto = new ServerErrorDto("сущность не найдена", e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorDto);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ServerErrorDto> HandlerMethodValidationException(HandlerMethodValidationException e) {
        log.error("Некорректный id", e);
        var errorDto = new ServerErrorDto("Некорректный id", e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }



    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ServerErrorDto> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("Некорректный запрос", e);
        var errorDto = new ServerErrorDto("Некорректный запрос",
                e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServerErrorDto> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("ошибка валидации запроса", e);
        var errorDto = new ServerErrorDto("ошибка валидации запроса", e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ServerErrorDto> handleGenericException(Exception e) {
        log.error("Ошибка сервера", e);
        var errorDto = new ServerErrorDto("Ошибка сервера", e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ServerErrorDto> handleAuthorizationException(Exception e) {
        log.error("Ошибка авторизации", e);
        var errorDto = new ServerErrorDto("Ошибка авторизации", e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(errorDto);
    }

}

