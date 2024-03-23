package io.github.srcimon.screwbox.core.audio.internal;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.audio.AudioConfiguration;
import io.github.srcimon.screwbox.core.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import javax.sound.sampled.TargetDataLine;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.test.TestUtil.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VolumeMonitorTest {

    @Mock
    AudioAdapter audioAdapter;

    @Mock
    TargetDataLine targetDataLine;

    ExecutorService executor;

    VolumeMonitor volumeMonitor;

    AudioConfiguration configuration;


    @BeforeEach
    void setUp() {
        executor = Executors.newSingleThreadExecutor();
        configuration = new AudioConfiguration();
        volumeMonitor = new VolumeMonitor(executor, audioAdapter, configuration);
    }

    @Test
    void isActive_afterCallingLevel_isTrue() {
        volumeMonitor.level();

        assertThat(volumeMonitor.isActive()).isTrue();
    }

    @Test
    void isActive_noCallToLevel_isFalse() {
        assertThat(volumeMonitor.isActive()).isFalse();
    }

    @Test
    void isActive_afertIdleTimeoutReached_isFalse() {
        when(audioAdapter.getStartedTargetDataLine(any())).thenReturn(targetDataLine);
        configuration.setMicrophoneTimeout(Duration.ofMillis(40));
        volumeMonitor.level();

        await(() -> !volumeMonitor.isActive(), ofSeconds(1));
    }

    @Test
    void xxxx() {
        when(audioAdapter.getStartedTargetDataLine(any())).thenReturn(targetDataLine);
        when(targetDataLine.getBufferSize()).thenReturn(8);
        byte[] buffer = new byte[8];
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                buffer[0] = 20;
                buffer[1] = 80;
                buffer[2] = 20;
                System.out.println("x");
                return 1;
            }
        }).when(targetDataLine).read(buffer, 0, 8);

        await(() -> {
            double value = volumeMonitor.level().value();
            System.out.println(value);
            return value > 0;
        }, ofSeconds(1));
    }

    @AfterEach
    void tearDown() {
        TestUtil.shutdown(executor);
    }
}
