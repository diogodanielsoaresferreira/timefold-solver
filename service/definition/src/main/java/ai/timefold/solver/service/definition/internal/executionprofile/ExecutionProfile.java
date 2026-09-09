package ai.timefold.solver.service.definition.internal.executionprofile;

import java.util.List;
import java.util.Map;

/**
 * A named, predefined runtime configuration a run can be started with.
 */
public interface ExecutionProfile {

    /**
     * Stable identifier of the profile, used in APIs and permissions. Must be unique across all implementations.
     */
    String name();

    /**
     * Human readable description of the profile.
     */
    String description();

    /**
     * The inputs this profile accepts. Acts as a whitelist: the platform validates submitted values against these and
     * rejects anything not declared. Defaults to no parameters (a fixed profile that takes no input).
     */
    default List<ExecutionProfileParameter> parameters() {
        return List.of();
    }

    /**
     * Turns the caller-supplied inputs (already validated against {@link #parameters()}) into the concrete parameter values
     * to use for this run - applying defaults and generating values for absent parameters where applicable. Called once at
     * submit time; the result is persisted with the run so it stays reproducible. Defaults to returning the inputs
     * unchanged.
     */
    default Map<String, String> resolveParameters(Map<String, String> inputs) {
        return inputs;
    }

    /**
     * Maps the resolved parameter values (from {@link #resolveParameters(Map)}) to environment variables injected into the
     * solver pod. Keys must be valid environment-variable names. When multiple profiles are activated and define the same
     * key, the resulting value is unspecified. Defaults to no environment variables.
     */
    default Map<String, String> toEnvironment(Map<String, String> resolvedParameters) {
        return Map.of();
    }
}
