package org.testots;

public class PositiveIntValidator implements ArgsValidator {
    @Override
    public boolean isValid(Object[] args) {
        if (args == null || args.length == 0) {
            return false;
        }

        for (Object arg : args) {
            if (!(arg instanceof Integer)) {
                return false;
            }
            Integer i = (Integer) arg;
            if (i <= 0) {
                return false;
            }
        }
        return true;
    }
}
