package ai.timefold.solver.service.definition.impl.executionprofile;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import ai.timefold.solver.service.definition.internal.executionprofile.ExecutionProfileParameter;

import org.junit.jupiter.api.Test;

class SeedExecutionProfileTest {

    private final SeedExecutionProfile profile = new SeedExecutionProfile();

    @Test
    void declaresOptionalLongSeedParameter() {
        assertThat(profile.parameters()).singleElement()
                .satisfies(parameter -> {
                    assertThat(parameter.name()).isEqualTo(SeedExecutionProfile.PARAMETER_SEED);
                    assertThat(parameter.type()).isEqualTo(ExecutionProfileParameter.Type.LONG);
                    assertThat(parameter.required()).isFalse();
                });
    }

    @Test
    void keepsSuppliedSeed() {
        Map<String, String> resolved = profile.resolveParameters(Map.of(SeedExecutionProfile.PARAMETER_SEED, "42"));
        assertThat(resolved).containsExactly(Map.entry(SeedExecutionProfile.PARAMETER_SEED, "42"));
        assertThat(profile.toEnvironment(resolved))
                .containsExactly(Map.entry(SeedExecutionProfile.ENV_QUARKUS_RANDOM_SEED, "42"));
    }

    @Test
    void generatesSeedWhenAbsent() {
        Map<String, String> resolved = profile.resolveParameters(Map.of());
        assertThat(resolved).containsOnlyKeys(SeedExecutionProfile.PARAMETER_SEED);
        // The generated seed is recorded and is a valid long.
        assertThat(resolved.get(SeedExecutionProfile.PARAMETER_SEED)).isNotNull();
        assertThat(Long.parseLong(resolved.get(SeedExecutionProfile.PARAMETER_SEED))).isNotNull();
        assertThat(profile.toEnvironment(resolved))
                .containsOnlyKeys(SeedExecutionProfile.ENV_QUARKUS_RANDOM_SEED);
    }

    @Test
    void emitsNoEnvironmentWithoutSeed() {
        assertThat(profile.toEnvironment(Map.of())).isEmpty();
    }
}
