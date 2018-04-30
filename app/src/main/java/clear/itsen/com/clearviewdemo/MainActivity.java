package clear.itsen.com.clearviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ClearView clearView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clearView = (ClearView) findViewById(R.id.clear_view);
        textView = (TextView) findViewById(R.id.tv_show_point);
    }

    public void click(View view) {
        if (view.getId()==R.id.btn_start){
            clearView.runningAinm();
        }else if (view.getId()==R.id.btn_finish){
            textView.setText("98");
            clearView.finishAinm();
        }else if (view.getId()==R.id.btn_reset){
            textView.setText("60");
            clearView.initAnim();
        }
    }
}
