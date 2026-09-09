package ai.timefold.solver.service.definition.impl.executionprofile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import ai.timefold.solver.service.definition.internal.executionprofile.ExecutionProfile;
import ai.timefold.solver.service.definition.internal.executionprofile.ExecutionProfileParameter;

/**
 * Runs the solver with a fixed random seed, making a run reproducible.
 * <p>
 * The seed is an optional {@code seed} parameter; when it is not supplied, a random seed is generated and persisted with
 * the run so the exact seed used can be read back and replayed. The seed is applied by mapping it to the Timefold Quarkus
 * property {@code quarkus.timefold.solver.random-seed} (via its environment-variable form), which the solver pod applies to
 * its {@code SolverConfig} at startup.
 */
public final class SeedExecutionProfile implements ExecutionProfile {

    static final String PARAMETER_SEED = "seed";

    /**
     * Environment-variable form of {@code quarkus.timefold.solver.default.random-seed}, consumed by the Timefold Quarkus
     * extension. The {@code default} segment is the solver name ({@code TimefoldRuntimeConfig.DEFAULT_SOLVER_NAME}); it is
     * correct for the usual single, unnamed solver a model defines. Note this property lives under a solver-name-keyed map,
     * so injecting it purely via an environment variable may not be honored by SmallRye - see the profile's notes.
     */
    static final String ENV_QUARKUS_RANDOM_SEED = "QUARKUS_TIMEFOLD_SOLVER_DEFAULT_RANDOM_SEED";

    @Override
    public String name() {
        return "seed";
    }

    @Override
    public String description() {
        return "Runs the solver with a fixed random seed for reproducible results. "
                + "A random seed is generated and recorded when none is supplied.";
    }

    @Override
    public List<ExecutionProfileParameter> parameters() {
        return List.of(new ExecutionProfileParameter(PARAMETER_SEED,
                "Random seed to solve with. When omitted, a random seed is generated and recorded.",
                ExecutionProfileParameter.Type.LONG, false));
    }

    @Override
    public Map<String, String> resolveParameters(Map<String, String> inputs) {
        String seed = inputs.get(PARAMETER_SEED);
        if (seed == null) {
            seed = Long.toString(ThreadLocalRandom.current().nextLong());
        }
        return Map.of(PARAMETER_SEED, seed);
    }

    @Override
    public Map<String, String> toEnvironment(Map<String, String> resolvedParameters) {
        String seed = resolvedParameters.get(PARAMETER_SEED);
        if (seed == null) {
            return Map.of();
        }
        return Map.of(ENV_QUARKUS_RANDOM_SEED, seed);
    }
}
