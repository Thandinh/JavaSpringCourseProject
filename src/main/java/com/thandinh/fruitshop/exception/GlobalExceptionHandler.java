package com.thandinh.fruitshop.exception;

import com.thandinh.fruitshop.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("ResourceNotFoundException: {}", ex.getMessage());
        
        if (isApiRequest(request)) {
            return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
            );
        }
        
        return createErrorView("404", ex.getMessage(), request);
    }

    // 400 - Bad Request
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        log.error("BadRequestException: {}", ex.getMessage());
        
        if (isApiRequest(request)) {
            return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
            );
        }
        
        return createErrorView("400", ex.getMessage(), request);
    }

    // 401 - Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Object handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        log.error("UnauthorizedException: {}", ex.getMessage());
        
        if (isApiRequest(request)) {
            return new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                request.getRequestURI()
            );
        }
        
        ModelAndView mav = new ModelAndView("redirect:/login");
        mav.addObject("error", ex.getMessage());
        return mav;
    }

    // 403 - Forbidden
    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Object handleForbiddenException(Exception ex, HttpServletRequest request) {
        log.error("ForbiddenException: {}", ex.getMessage());
        
        if (isApiRequest(request)) {
            return new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                ex.getMessage(),
                request.getRequestURI()
            );
        }
        
        return createErrorView("403", ex.getMessage(), request);
    }

    // 409 - Duplicate Resource
    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Object handleDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest request) {
        log.error("DuplicateResourceException: {}", ex.getMessage());
        
        if (isApiRequest(request)) {
            return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
            );
        }
        
        return createErrorView("409", ex.getMessage(), request);
    }

    // Data Integrity Violation (Database Constraint)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Object handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("DataIntegrityViolationException: {}", ex.getMessage());
        
        String message = "Dữ liệu bị trùng lặp hoặc vi phạm ràng buộc cơ sở dữ liệu";
        
        if (isApiRequest(request)) {
            return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                message,
                request.getRequestURI()
            );
        }
        
        return createErrorView("409", message, request);
    }

    // Validation Errors (Bean Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("MethodArgumentNotValidException: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        if (isApiRequest(request)) {
            ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Dữ liệu không hợp lệ",
                request.getRequestURI()
            );
            return ResponseEntity.badRequest().body(Map.of(
                "error", errorResponse,
                "validationErrors", errors
            ));
        }
        
        return createErrorView("400", "Dữ liệu không hợp lệ: " + errors.toString(), request);
    }

    // Constraint Violation
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        log.error("ConstraintViolationException: {}", ex.getMessage());
        
        if (isApiRequest(request)) {
            return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                ex.getMessage(),
                request.getRequestURI()
            );
        }
        
        return createErrorView("400", ex.getMessage(), request);
    }

    // Illegal Argument
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.error("IllegalArgumentException: {}", ex.getMessage());
        
        if (isApiRequest(request)) {
            return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
            );
        }
        
        return createErrorView("400", ex.getMessage(), request);
    }

    // Bad Credentials (Authentication Failed)
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Object handleBadCredentialsException(Exception ex, HttpServletRequest request) {
        log.error("BadCredentialsException: {}", ex.getMessage());
        
        String message = "Email hoặc mật khẩu không đúng!";
        
        if (isApiRequest(request)) {
            return new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                message,
                request.getRequestURI()
            );
        }
        
        ModelAndView mav = new ModelAndView("redirect:/login");
        mav.addObject("error", message);
        return mav;
    }

    // File Upload Size Exceeded
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public Object handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        log.error("MaxUploadSizeExceededException: {}", ex.getMessage());
        
        String message = "Kích thước file vượt quá giới hạn cho phép";
        
        if (isApiRequest(request)) {
            return new ErrorResponse(
                HttpStatus.PAYLOAD_TOO_LARGE.value(),
                "Payload Too Large",
                message,
                request.getRequestURI()
            );
        }
        
        return createErrorView("413", message, request);
    }

    // No Handler Found (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        log.error("NoHandlerFoundException: {}", ex.getMessage());
        
        String message = "Không tìm thấy trang yêu cầu";
        
        if (isApiRequest(request)) {
            return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                message,
                request.getRequestURI()
            );
        }
        
        return createErrorView("404", message, request);
    }

    // Generic Exception (500)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Exception: ", ex);
        
        String message = ex.getMessage() != null ? ex.getMessage() : "Đã xảy ra lỗi không mong muốn";
        
        if (isApiRequest(request)) {
            return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                message,
                request.getRequestURI()
            );
        }
        
        return createErrorView("500", message, request);
    }

    // Helper method to check if request is API request
    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String acceptHeader = request.getHeader("Accept");
        
        return uri.startsWith("/api/") || 
               (acceptHeader != null && acceptHeader.contains("application/json"));
    }

    // Helper method to create error view for MVC
    private ModelAndView createErrorView(String errorCode, String message, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorCode", errorCode);
        mav.addObject("errorMessage", message);
        mav.addObject("path", request.getRequestURI());
        return mav;
    }
}
