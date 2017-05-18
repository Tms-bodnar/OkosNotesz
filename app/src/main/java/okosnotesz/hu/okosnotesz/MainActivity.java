package okosnotesz.hu.okosnotesz;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View tbIcon = findViewById(R.id.tbIcon);
        tbIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoView(v);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btnGues = (Button) findViewById(R.id.btnGues);
        btnGues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    guestsView(v);
            }
        });
    }

    //                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Groups.CONTENT_URI);
//                startActivity(intent);

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_main, menu);
       return true;
    }*/

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();


        if (id == R.id.miInfos) {
          View info =   findViewById(id);
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    infoView(v);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void infoView(View v){
        Intent infoIntent = new Intent(this, Informations.class);
        startActivity(infoIntent);
    }
    public void guestsView(View v){
        Intent guestIntent = new Intent(this, Guests.class);
        /*guestIntent.setAction(Intent.ACTION_VIEW);
        guestIntent.setData(ContactsContract.Contacts.CONTENT_URI);*/
        startActivity(guestIntent);
    }
}
