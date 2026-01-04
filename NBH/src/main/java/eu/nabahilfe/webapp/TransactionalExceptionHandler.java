package eu.nabahilfe.webapp;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//@ControllerAdvice
public class TransactionalExceptionHandler {

//    @ExceptionHandler(TransactionSystemException.class)
    public ModelAndView handleTransactionSystemException(
            TransactionSystemException ex,
            WebRequest request,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage",
            "A transaction error occurred. Please try again.");
        redirectAttributes.addFlashAttribute("errorType", "danger");

        return new ModelAndView("redirect:" + getRedirectUrl(request));
    }

//    @ExceptionHandler(UnexpectedRollbackException.class)
    public ModelAndView handleUnexpectedRollbackException(
            UnexpectedRollbackException ex,
            WebRequest request,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage",
            "The operation was rolled back. Please try again.");
        redirectAttributes.addFlashAttribute("errorType", "warning");

        return new ModelAndView("redirect:" + getRedirectUrl(request));
    }

//    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ModelAndView handleOptimisticLockingFailure(
            OptimisticLockingFailureException ex,
            WebRequest request,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage",
            "The data was modified by another user. Please refresh and try again.");
        redirectAttributes.addFlashAttribute("errorType", "warning");

        return new ModelAndView("redirect:" + getRedirectUrl(request));
    }

//    @ExceptionHandler(DataAccessException.class)
    public ModelAndView handleDataAccessException(
            DataAccessException ex,
            WebRequest request,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage",
            "A database error occurred. Please contact support if this persists.");
        redirectAttributes.addFlashAttribute("errorType", "danger");

        return new ModelAndView("redirect:" + getRedirectUrl(request));
    }

//    @ExceptionHandler(PessimisticLockingFailureException.class)
    public ModelAndView handlePessimisticLockingFailure(
            PessimisticLockingFailureException ex,
            WebRequest request,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("errorMessage",
            "Resource is currently locked. Please try again in a moment.");
        redirectAttributes.addFlashAttribute("errorType", "warning");

        return new ModelAndView("redirect:" + getRedirectUrl(request));
    }

    // Helper method to determine redirect URL
    private String getRedirectUrl(WebRequest request) {
        // Get the referer header
        String referer = request.getHeader("Referer");

        if (referer != null && !referer.isEmpty()) {
            try {
                java.net.URI uri = new java.net.URI(referer);
                String path = uri.getPath();

                // Return the path, or home if it's empty
                return (path != null && !path.isEmpty()) ? path : "/";
            } catch (Exception e) {
                // Fall back to home if parsing fails
            }
        }

        // Default fallback to home page
        return "/";
    }
}