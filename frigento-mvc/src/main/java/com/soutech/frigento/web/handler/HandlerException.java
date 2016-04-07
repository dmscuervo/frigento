package com.soutech.frigento.web.handler;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.soutech.frigento.util.PrinterStack;

@ControllerAdvice
public class HandlerException {

	private static final Logger logger = Logger.getLogger(HandlerException.class);
    
    @ExceptionHandler(Exception.class)
    public String handleSQLException(HttpServletRequest request, Exception ex){
        logger.info("SQLException Occured:: URL="+request.getRequestURL());
        
//        String key = BindingResult.MODEL_KEY_PREFIX + "productoForm";
//        ModelAndView modelAndView = new ModelAndView();
//        
//        BeanPropertyBindingResult br = new BeanPropertyBindingResult(new Producto(), "productoForm");
//        br.rejectValue("codigo", "NotEmpty.productoForm.codigo");
//        Producto producto = new Producto();
//        modelAndView.getModel().put(key, new BeanPropertyBindingResult(producto, "productoForm"));
//        modelAndView.getModel().put("productoForm", producto);
        
        request.setAttribute("msgTitle", "Error Inesperado");
        request.setAttribute("msgResult", "Ocurrio un error no conteplado. Contactese con el administrador.<br>".concat(ex.getMessage()));
        logger.error(PrinterStack.getStackTraceAsString(ex));
        
        return "generic/mensajeException";
    }
     
//    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="IOException occured")
//    @ExceptionHandler(IOException.class)
//    public void handleIOException(){
//        logger.error("IOException handler executed");
//        //returning 404 error code
//    }

}
