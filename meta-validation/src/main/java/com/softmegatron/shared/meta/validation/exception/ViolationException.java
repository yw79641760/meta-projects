package com.softmegatron.shared.meta.validation.exception;

import com.softmegatron.shared.meta.validation.model.Violation;

import jakarta.validation.ConstraintViolation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * ViolationException
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/6/20 1:17 AM
 */
public class ViolationException extends RuntimeException {

    private static final long serialVersionUID = 6723569501337198389L;

    private Set<Violation> violations;

    public ViolationException(Set<Violation> violations) {
        this.violations = violations;
    }

    public Set<Violation> getViolations() {
        return violations;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        violations.forEach(violation -> {
            sb.append(String.format("[message=%s][propertyPath=%s][invalidValue=%s]%n",
                                    violation.getMessage(),
                                    violation.getPropertyPath(),
                                    violation.getInvalidValue()));
        });
        return sb.toString();
    }

    /**
     * 构造异常信息
     *
     * @param cvs
     * @param <T>
     * @return
     */
    public static <T> ViolationException build(Set<ConstraintViolation<T>> cvs) {
        Set<Violation> result = new HashSet<>();
        if (cvs != null && !cvs.isEmpty()) {
            for (ConstraintViolation<T> cv : cvs) {
                result.add(new Violation(cv.getMessage(),
                                         cv.getRootBean() == null ? null : cv.getRootBean().toString(),
                                         cv.getPropertyPath() == null ? null : cv.getPropertyPath().toString(),
                                         cv.getInvalidValue()));
            }
        }
        return new ViolationException(result);
    }

    /**
     *
     * @param regex
     * @param value
     * @return
     */
    public static ViolationException build(String regex, String value) {
        return new ViolationException(Collections.singleton(new Violation(String.format("Value %s not matching the regex %s.",
                                                                                        value,
                                                                                        regex),
                                                                          value,
                                                                          null,
                                                                          regex)));
    }
}
