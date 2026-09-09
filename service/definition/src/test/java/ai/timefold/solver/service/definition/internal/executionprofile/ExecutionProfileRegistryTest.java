package ai.timefold.solver.service.definition.internal.executionprofile;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class ExecutionProfileRegistryTest {

    @Test
    void discoversRegisteredProfilesViaServiceLoader() {
        ExecutionProfileRegistry registry = new ExecutionProfileRegistry();
        // The seed profile is registered as a service, so it must be discovered.
        assertThat(registry.findByName("seed")).isPresent();
        assertThat(registry.all()).extracting(ExecutionProfile::name).contains("seed");
    }

    @Test
    void findByNameReturnsEmptyForUnknownProfile() {
        ExecutionProfileRegistry registry = new ExecutionProfileRegistry();
        assertThat(registry.findByName("does-not-exist")).isEmpty();
    }

    @Test
    void indexesProfilesByName() {
        ExecutionProfile profile = new NamedProfile("custom");
        ExecutionProfileRegistry registry = new ExecutionProfileRegistry(List.of(profile));
        assertThat(registry.findByName("custom")).containsSame(profile);
        assertThat(registry.all()).containsExactly(profile);
    }

    private record NamedProfile(String name) implements ExecutionProfile {
        @Override
        public String description() {
            return name;
        }
    }
}
