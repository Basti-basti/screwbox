package io.github.srcimon.screwbox.core.audio;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.utils.Resources;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SoundTest {

    @Test
    void dummyEffect_returnsDummySoundEffect() {
        var sound = Sound.dummyEffect();

        assertThat(sound.content()).hasSizeGreaterThan(10000);
        assertThat(sound.duration()).isEqualTo(Duration.ofMillis(1033));
    }

    @Test
    void fromSoundData_validWav_hasContent() {
        var content = Resources.loadBinary("kill.wav");
        var sound = Sound.fromSoundData(content);

        assertThat(sound.content()).hasSizeGreaterThan(10000);
        assertThat(sound.duration()).isEqualTo(Duration.ofMillis(186));
        assertThat(sound.format()).isEqualTo(Sound.Format.WAV);
    }

    @Test
    void isArtificalStereo_sourceIsMono_isTrue() {
        var sound = Sound.fromFile("kill.wav");

        assertThat(sound.isArtificalStereo()).isTrue();
    }

    @Test
    void isArtificalStereo_sourceIsStereo_isFalse() {
        var sound = Sound.fromFile("stereo.wav");

        assertThat(sound.isArtificalStereo()).isFalse();
    }

    @Test
    void fromSoundData_contentNull_exception() {
        assertThatThrownBy(() -> Sound.fromSoundData(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("content must not be null");
    }

    @Test
    void fromFile_textFile_exception() {
        assertThatThrownBy(() -> Sound.fromFile("not-a-wav.txt"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Audio only supports WAV- and MIDI-Files at the moment.");
    }

    @Test
    void fromFile_existingMidi_hasContent() {
        Sound sound = Sound.fromFile("real.mid");

        assertThat(sound.content()).hasSizeGreaterThan(10);
        assertThat(sound.format()).isEqualTo(Sound.Format.MIDI);
        assertThat(sound.duration()).isEqualTo(Duration.ofMillis(9000));
        assertThat(sound.isArtificalStereo()).isFalse();
    }

    @Test
    void fromFile_invalidMidi_throwsException() {
        assertThatThrownBy(() -> Sound.fromFile("fake.mid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("could not load audio content");
    }

    @Test
    void fromFile_existingWav_hasContent() {
        Sound sound = Sound.fromFile("kill.wav");

        assertThat(sound.content()).hasSizeGreaterThan(10000);
        assertThat(sound.format()).isEqualTo(Sound.Format.WAV);
    }

    @Test
    void fromFile_fileNameNull_exception() {
        assertThatThrownBy(() -> Sound.fromFile(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("fileName must not be null");
    }

    @Test
    void assetFromFile_createsAsset() {
        Asset<Sound> asset = Sound.assetFromFile("kill.wav");

        Sound sound = asset.get();
        assertThat(sound.content()).hasSizeGreaterThan(10000);
    }
}
