package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ecosphere.Component;

public class LetsGoComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double modifier = 0;
    public Percent visibility = Percent.max();
}
