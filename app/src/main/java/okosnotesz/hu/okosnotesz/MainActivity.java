package okosnotesz.hu.okosnotesz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import okosnotesz.hu.okosnotesz.model.DBHelper;
import okosnotesz.hu.okosnotesz.model.Experts;
import okosnotesz.hu.okosnotesz.model.Products;
import okosnotesz.hu.okosnotesz.model.Sales;
import okosnotesz.hu.okosnotesz.model.Treatments;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Treatments t = new Treatments("Első", 40, 4000, 0,null);
        Experts e = new Experts("Gabi", "ügyes");
        Products p = new Products("termék1",1000,500,"jó illatú");
        Sales s = new Sales(p,"Emma","fizetve","20117/04/16");
        DBHelper dbh = new DBHelper(this);
        dbh.addTreatment(t);
        dbh.addExpert(e);
        dbh.addProduct(p);
        dbh.addSale(s);
        Cursor c = dbh.getTreatment(t.getName());
        c.moveToFirst();
        Log.d("Treatment név: ",""+(c.getString(c.getColumnIndex("treatmentName"))));
        c.close();
        Cursor c2 = dbh.getExpert(e.getName());
        c2.moveToFirst();
        Log.d("Expert név: ", ""+c2.getString(c2.getColumnIndex("expertName")));
        c2.close();
        Cursor c3 =dbh.getProduct(p.getName());
        c3.moveToFirst();
        Log.d("produt név: ", ""+c3.getString(c3.getColumnIndex("productName")));
        c3.close();
        Cursor c4 =dbh.getSale(s.getProduct());
        c4.moveToFirst();
        Log.d("Sale :" , ""+c4.getString(c4.getColumnIndex("saleNote")) );
        c4.close();
        dbh.close();
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
