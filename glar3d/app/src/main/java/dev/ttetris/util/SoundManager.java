package dev.ttetris.util;

import android.app.Activity;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private AudioManager audioManager;
    private SoundPool soundPool;
    private Map<String, Integer> sounds = new HashMap();

    public SoundManager(Activity paramActivity, String paramString) {
        this(paramActivity, paramString, 3); // originally 8
    }

    public SoundManager(Activity paramActivity, String paramString, int paramInt) {
        this.audioManager = ((AudioManager)paramActivity.getSystemService("audio"));
        paramActivity.setVolumeControlStream(3);
        this.soundPool = new SoundPool(paramInt, 3, 0);
        int i = 0;
        int j = 0;
        do {
            try {
                String[] arrayOfString = paramActivity.getAssets().list(paramString);
                i = arrayOfString.length; // 3
                String str1 = arrayOfString[j];
                int m = str1.lastIndexOf('.');
                if (m == -1) continue;
                int k = this.soundPool.load(paramActivity.getAssets().openFd(paramString + "/" + str1), 1);
                for (String str2 = str1.substring(0, m); j < i; str2 = str1) {
                    this.sounds.put(str2, Integer.valueOf(k));
                    j++;
                }
            }
            catch (IOException localIOException) {
                throw new RuntimeException("No sounds directory \"" + paramString + "\"");
            }
        } while (j < i);
    }

    public void play(String paramString) {
        play(paramString, 0);
    }

    public void play(String paramString, int paramInt) {
        Integer localInteger = (Integer)this.sounds.get(paramString);
        if (localInteger != null) {
            float f = this.audioManager.getStreamVolume(3) / this.audioManager.getStreamMaxVolume(3);
            this.soundPool.play(localInteger.intValue(), f, f, 1, paramInt, 1.0F);
        }
    }

    public void playForever(String paramString) {
        play(paramString, -1);
    }

    public void stop(String paramString) {
        Integer localInteger = (Integer)this.sounds.get(paramString);
        if (localInteger != null)
            this.soundPool.stop(localInteger.intValue());
    }
}
