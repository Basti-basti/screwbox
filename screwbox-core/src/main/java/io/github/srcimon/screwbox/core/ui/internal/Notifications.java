package io.github.srcimon.screwbox.core.ui.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.loop.internal.Updatable;

import java.util.ArrayList;
import java.util.List;

import static io.github.srcimon.screwbox.core.graphics.SystemTextDrawOptions.systemFont;

public class Notifications implements Updatable {

    private Duration notificationTimeout = Duration.ofSeconds(4);
    private final Screen screen;

    public Notifications(final Screen screen) {
        this.screen = screen;
    }

    @Override
    public void update() {
        final var updateTime = Time.now();
        final var outdatedNotifications = activeNotifications.stream().
                filter(n -> notificationTimeout.addTo(n.time)
                        .isBefore(updateTime))
                .toList();

        activeNotifications.removeAll(outdatedNotifications);
        int y = 20;
        int maxwidth = 100;
        for (final var notification : activeNotifications) {
            var notificationProgress = notificationTimeout.progress(notification.time, updateTime);
            y += 20;

//            screen.drawLine(
//                    Offset.at(0, y-2),
//                    Offset.at(0, y-2).addX((int)(maxwidth * notificationProgress.invert().value())),
//                    LineDrawOptions.color(Color.YELLOW).strokeWidth(2));

            double inFlowX = notificationProgress.value() < 0.5
                    ? Math.min(0, -2000 * (0.2 - notificationProgress.value()))
                    : Math.min(0, -2000 * (-0.8 + notificationProgress.value()));
            //TODO use Ease for this
            screen.drawText(Offset.at(inFlowX, y), notification.message, systemFont("Arial")
                    .size(12).bold());
        }
    }

    private record ActiveNotification(String message, Time time) {

    }

    private List<ActiveNotification> activeNotifications = new ArrayList<>();


//TODO More performantly remove items from list

    public void add(final String message) {
        activeNotifications.add(new ActiveNotification(message, Time.now()));
    }

}
