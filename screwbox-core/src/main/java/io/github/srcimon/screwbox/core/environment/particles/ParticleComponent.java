package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class ParticleComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Integer emitterId;

    public ParticleComponent(final Integer emitterId) {
        this.emitterId = emitterId;
    }
}