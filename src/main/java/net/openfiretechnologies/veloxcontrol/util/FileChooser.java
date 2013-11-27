package net.openfiretechnologies.veloxcontrol.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import net.openfiretechnologies.veloxcontrol.R;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileChooser extends ListActivity implements VeloxConstants {
    final Context context = this;
    private File currentDir;
    SharedPreferences mPreferences;
    private FileArrayAdapter adapter;
    private ProgressDialog progressDialog;
    private String tip;
    private String nFile;
    private int nbk = 1;
    private String dtitlu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent1 = getIntent();
        tip = intent1.getStringExtra("mod");
        if (tip.equalsIgnoreCase("robotoregular")) {
            dtitlu = getString(R.string.tools_fonts_robotoregular);
        } else if (tip.equalsIgnoreCase("robotobold")) {
            dtitlu = getString(R.string.tools_fonts_robotobold);
        }
        currentDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        fill(currentDir);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filechooser_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.close) {
            finish();
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fill(currentDir);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void fill(File f) {
        File[] dirs = f.listFiles();
        List<Item> dir = new ArrayList<>();
        List<Item> fls = new ArrayList<>();
        try {
            assert dirs != null;
            for (File ff : dirs) {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if (ff.isDirectory()) {
                    dir.add(new Item(ff.getName(), getString(R.string.dir), date_modify, ff.getAbsolutePath(), "dir"));
                } else {
                    if (((tip.equalsIgnoreCase("robotoregular"))
                            || (tip.equalsIgnoreCase("robotobold")))
                            && ff.getName().toLowerCase().endsWith(".ttf"))
                        fls.add(new Item(ff.getName(), Helpers.ReadableByteCount(ff.length()), date_modify, ff.getAbsolutePath(), "file"));
                }
            }
        } catch (Exception e) {
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!f.getName().equalsIgnoreCase(""))
            dir.add(0, new Item("..", getString(R.string.dir_parent), "", f.getParent(), "dir"));
        adapter = new FileArrayAdapter(this, R.layout.util_file_view, dir);
        this.setListAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        if (adapter.getItem(0).getName().equalsIgnoreCase("..")) {
            currentDir = currentDir.getParentFile();
            fill(currentDir);
            nbk = 1;
        } else {
            if (nbk == 2) {
                finish();
            } else {
                nbk++;
                Toast.makeText(getApplicationContext(), getString(R.string.action_press_again), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Item o = adapter.getItem(position);
        if (o.getImage().equalsIgnoreCase("dir")) {
            currentDir = new File(o.getPath());
            fill(currentDir);
        } else {
            nFile = currentDir + "/" + o.getName();
            makedialog();
        }
    }

    private class FlashOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            final List<String> s = new ArrayList<>();

            s.add("busybox mount -o rw,remount /system");

            if (tip.equalsIgnoreCase("robotoregular")) {
                s.add("rm -rf " + TOOLS_FONT_ROBOTOREGULAR);
                s.add("dd if=" + nFile + " of=" + TOOLS_FONT_ROBOTOREGULAR);
                s.add("chmod 644 " + TOOLS_FONT_ROBOTOREGULAR);
            } else if (tip.equalsIgnoreCase("robotobold")) {
                s.add("rm -rf " + TOOLS_FONT_ROBOTOBOLD);
                s.add("dd if=" + nFile + " of=" + TOOLS_FONT_ROBOTOBOLD);
                s.add("chmod 644 " + TOOLS_FONT_ROBOTOBOLD);
            }

            String result = Helpers.shExec(s, true);
            VeloxMethods.logDebug("FlashFonts Result: " + result, true);
            return tip;
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            makedialogdone();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(FileChooser.this, dtitlu, getString(R.string.wait));
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            dialog.cancel();
            new FlashOperation().execute();
        }
    }

    private void makedialog() {

        String fontName = "";
        if (tip.equalsIgnoreCase("robotoregular")) {
            fontName = getString(R.string.tools_fonts_robotoregular);
        } else if (tip.equalsIgnoreCase("robotobold")) {
            fontName = getString(R.string.tools_fonts_robotobold);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dtitlu)
                .setMessage(nFile + " " + getString(R.string.flash_font_info, fontName))
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                //finish();
                            }
                        })
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //alertDialog.setCancelable(false);
        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (theButton != null) {
            theButton.setOnClickListener(new CustomListener(alertDialog));
        }
    }

    private void makedialogdone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dtitlu)
                .setMessage(getString(R.string.flash_font_info_done))
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
        //alertDialog.setCancelable(false);
    }

}
