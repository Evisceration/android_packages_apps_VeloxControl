/*
 * Copyright (c) Alexander Martinz @ OpenFire Technologies Pvt Ltd.
 */

package eu.chainfire.libsuperuser;

import net.openfiretechnologies.veloxcontrol.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Thread utility class continuously reading from an InputStream
 */
public class StreamGobbler extends Thread {
    private String shell = null;
    private BufferedReader reader = null;
    private List<String> writer = null;
    private OnLineListener listener = null;

    /**
     * <p>StreamGobbler constructor</p>
     * <p/>
     * <p>We use this class because shell STDOUT and STDERR should be read as quickly as
     * possible to prevent a deadlock from occurring, or Process.waitFor() never
     * returning (as the buffer is full, pausing the native process)</p>
     *
     * @param shell       Name of the shell
     * @param inputStream InputStream to read from
     * @param outputList  List<String> to write to, or null
     */
    public StreamGobbler(String shell, InputStream inputStream, List<String> outputList) {
        this.shell = shell;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        writer = outputList;
    }

    /**
     * <p>StreamGobbler constructor</p>
     * <p/>
     * <p>We use this class because shell STDOUT and STDERR should be read as quickly as
     * possible to prevent a deadlock from occurring, or Process.waitFor() never
     * returning (as the buffer is full, pausing the native process)</p>
     *
     * @param shell          Name of the shell
     * @param inputStream    InputStream to read from
     * @param onLineListener OnLineListener callback
     */
    public StreamGobbler(String shell, InputStream inputStream, OnLineListener onLineListener) {
        this.shell = shell;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        listener = onLineListener;
    }

    @Override
    public void run() {
        // keep reading the InputStream until it ends (or an error occurs)
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (BuildConfig.DEBUG) Debug.log(String.format("[%s] %s", shell, line));
                if (writer != null) writer.add(line);
                if (listener != null) listener.onLine(line);
            }
        } catch (IOException ignored) {
        }

        // make sure our stream is closed and resources will be freed
        try {
            reader.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Line callback interface
     */
    public interface OnLineListener {
        /**
         * <p>Line callback</p>
         * <p/>
         * <p>This callback should process the line as quickly as possible.
         * Delays in this callback may pause the native process or even
         * result in a deadlock</p>
         *
         * @param line String that was gobbled
         */
        public void onLine(String line);
    }
}
