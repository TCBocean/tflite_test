package com.stars.tflite_test1;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String MODEL_FILE = "converted_model.tflite";
        Interpreter tfLite = null;
        try {
            Interpreter.Options options = (new Interpreter.Options());
            GpuDelegate delegate = new GpuDelegate();
            options.addDelegate(delegate);
            tfLite = new Interpreter(loadModelFile(getAssets(), MODEL_FILE), options);
        }catch(IOException e){
            e.printStackTrace();
        }

        // for siamfcpp
//        int net_input_sz_1 = 127;
//        int net_input_sz_2 = 303;
//        Log.e("1111","input");
//        ByteBuffer inputData1;
//        inputData1 = ByteBuffer.allocateDirect(net_input_sz_1 * net_input_sz_1 * 3 * 4);//4表示一个浮点占4byte
//        inputData1.order(ByteOrder.nativeOrder());
//        inputData1.rewind();
//        for(int i=0; i<net_input_sz_1 * net_input_sz_1 * 3; i++){
//            inputData1.putFloat(1.0f);
//        }
//
//        ByteBuffer inputData2;
//        inputData2 = ByteBuffer.allocateDirect(net_input_sz_2 * net_input_sz_2 * 3 * 4);//4表示一个浮点占4byte
//        inputData2.order(ByteOrder.nativeOrder());
//        inputData2.rewind();
//        for(int i=0; i<net_input_sz_2 * net_input_sz_2 * 3; i++){
//            inputData2.putFloat(1.0f);
//        }
//        Object[] inputArray = {inputData1, inputData2};

        int net_input_sz = 6;
        Log.e("1111","input");
        ByteBuffer inputData1;
        inputData1 = ByteBuffer.allocateDirect(net_input_sz * net_input_sz * 4);//4表示一个浮点占4byte
        inputData1.order(ByteOrder.nativeOrder());
        inputData1.rewind();
        for(int i=0; i<net_input_sz * net_input_sz; i++){
            inputData1.putFloat(1.0f);
        }
//        ByteBuffer inputData2;
//        inputData2 = ByteBuffer.allocateDirect(net_input_sz * net_input_sz * 4);//4表示一个浮点占4byte
//        inputData2.order(ByteOrder.nativeOrder());
//        inputData2.rewind();
//        for(int i=0; i<net_input_sz * net_input_sz; i++){
//            inputData2.putFloat(1.0f);
//        }
        Object[] inputArray = {inputData1};

        Log.e("1111","finish input");


        // for siamfcpp
//        float[][][][] output1;
//        float[][][][] output2;
//        float[][][][] output3;
//        output1 = new float[1][19][19][1];
//        output2 = new float[1][19][19][1];
//        output3 = new float[1][19][19][4];
//        Map<Integer, Object> outputMap = new HashMap<>();
//        outputMap.put(0, output1);
//        outputMap.put(1, output2);
//        outputMap.put(2, output3);
        for(int i=0; i<10; i++){
            float[][][][] output1;
            float[][][][] output2;
            output1 = new float[1][6][6][33];
            output2 = new float[1][6][6][14];
            Map<Integer, Object> outputMap = new HashMap<>();
            outputMap.put(0, output1);
            outputMap.put(1, output2);
            Log.e("1111","start");
            tfLite.runForMultipleInputsOutputs(inputArray, outputMap);  // 即便是单输入也可以使用这个函数
            Log.e("1111","output1:" + output1[0][5][0][30]);
            Log.e("1111","output2:" + output2[0][0][5][11]);
        }


    }

    private static MappedByteBuffer loadModelFile(AssetManager assets, String modelFilename)
            throws IOException {
        AssetFileDescriptor fileDescriptor = assets.openFd(modelFilename);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
