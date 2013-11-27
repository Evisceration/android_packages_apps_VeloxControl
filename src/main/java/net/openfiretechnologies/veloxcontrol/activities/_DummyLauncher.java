package net.openfiretechnologies.veloxcontrol.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import net.openfiretechnologies.veloxcontrol.MainMenu;

/**
 * Created by alex on 15.11.13.
 */
public class _DummyLauncher extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(_DummyLauncher.this, MainMenu.class));
        finish();

    }

}
