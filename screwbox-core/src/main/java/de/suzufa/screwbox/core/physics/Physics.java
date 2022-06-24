package de.suzufa.screwbox.core.physics;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;

public interface Physics {

    RaycastBuilder raycastFrom(Vector position);

    SelectEntityBuilder searchAtPosition(Vector position);

    SelectEntityBuilder searchInRange(Bounds range);

}
