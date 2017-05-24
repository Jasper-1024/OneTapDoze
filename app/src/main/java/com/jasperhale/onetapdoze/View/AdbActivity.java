package com.jasperhale.onetapdoze.View;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jasperhale.onetapdoze.R;
import com.jasperhale.onetapdoze.lib.ShellUtils;

public class AdbActivity extends AppCompatActivity {

    TextView adbresult;
    EditText adbuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adb);
        Toolbar toolbar = (Toolbar) findViewById(R.id.adb_toolbar);
        toolbar.setTitle("ADB");
        setSupportActionBar(toolbar);
        //返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        adbresult = (TextView)findViewById(R.id.adbresulet);
        adbuser = (EditText)findViewById(R.id.adb_user);
    }

    //返回按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void enable(View view){
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand("dumpsys deviceidle enable", true, true);
        if (commandResult.result == 0){
            adbresult.setText(commandResult.successMsg);
        }else {
            adbresult.setText("error code"+String.valueOf(commandResult.result)+"\n"+commandResult.errorMsg);
        }
    }
    public void disable(View view){
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand("dumpsys deviceidle disable", true, true);
        if (commandResult.result == 0){
            adbresult.setText(commandResult.successMsg);
        }else {
            adbresult.setText("error code"+String.valueOf(commandResult.result)+"\n"+commandResult.errorMsg);
        }
    }
    public void whitelist(View view){
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand("dumpsys deviceidle whitelist", true, true);
        if (commandResult.result == 0){
            adbresult.setText(commandResult.successMsg);
        }else {
            adbresult.setText("error code"+String.valueOf(commandResult.result)+"\n"+commandResult.errorMsg);
        }
    }
    public void user(View view){
        String s = adbuser.getText().toString();
        if (s.equals("")){
            Toast.makeText(this, "请输入命令！", Toast.LENGTH_SHORT).show();
        }else {
            ShellUtils.CommandResult commandResult = ShellUtils.execCommand(s, true, true);
            if (commandResult.result == 0) {
                adbresult.setText(commandResult.successMsg);
            } else {
                adbresult.setText("error code" + String.valueOf(commandResult.result) + "\n" + commandResult.errorMsg);
            }
        }
    }



}
