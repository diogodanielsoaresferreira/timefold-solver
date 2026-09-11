package ai.timefold.solver.service.definition.internal.executionprofile;

/**
 * Declares a single input an {@link ExecutionProfile} accepts. The declared parameters form the whitelist of inputs a
 * caller may supply: the platform validates submitted values against them and rejects anything not declared, so a profile
 * fully controls which values can reach the run.
 *
 * @param name stable identifier of the parameter, used in APIs
 * @param description human readable description of the parameter
 * @param type expected value type, used to validate submitted values
 * @param required whether a value must be supplied by the caller (a profile may still generate one in
 *        {@link ExecutionProfile#resolveParameters}, in which case this should be {@code false})
 */
public record ExecutionProfileParameter(String name, String description, Type type, boolean required) {

    public enum Type {
        STRING,
        INTEGER,
        LONG,
        BOOLEAN;

        /**
         * Whether the given raw value is a valid representation of this type.
         */
        public boolean isValid(String rawValue) {
            if (rawValue == null) {
                return false;
            }
            switch (this) {
                case STRING:
                    return true;
                case INTEGER:
                    try {
                        Integer.parseInt(rawValue);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                case LONG:
                    try {
                        Long.parseLong(rawValue);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                case BOOLEAN:
                    return "true".equalsIgnoreCase(rawValue) || "false".equalsIgnoreCase(rawValue);
                default:
                    return false;
            }
        }
    }
}
