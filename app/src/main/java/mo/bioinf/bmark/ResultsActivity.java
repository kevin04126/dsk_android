package mo.bioinf.bmark;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Context context = this;

        final Button done_button = (Button) findViewById(R.id.finished_button);
        final Button read_dna_button = (Button) findViewById(R.id.dna_button);
        final TextView tv = (TextView) findViewById(R.id.time_text);
        final TextView results_view = (TextView) findViewById(R.id.histogram);

        //set results text
        final String results = getIntent().getStringExtra("runtime");
        final String filename = getIntent().getStringExtra("filename");
        tv.setText(results);




        read_dna_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                final String base_path = context.getFilesDir().getAbsolutePath().toString() + "/";
                /*** making dna output ***/
                DnaOutput dna_output = null;
                try{
                    dna_output = new DnaOutput(filename, base_path);



                }catch(java.io.FileNotFoundException e){
                    System.out.println(e.getMessage());
                }




                /************************/



                /*** determines what the name of the histogram should be, opens it, and reads it into the view ***/

                Map<String,String> dna_map = dna_output.getDna_map();

                for(Map.Entry<String,String> entry : dna_map.entrySet())
                {
                    results_view.append(entry.getKey() + " " + entry.getValue() + "\n");
                }
                /****************************************************************************************************/
            }
        });

        done_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
    }

}
