package com.example.async_thread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected TextView statusText;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Button loadButton;
    private MainAsyncTask mainAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.am_image_iv);
        progressBar = findViewById(R.id.am_progress);
        loadButton = findViewById(R.id.am_load_btn);
        statusText = findViewById(R.id.am_status_tv);

        loadButton.setOnClickListener(view -> {
            if (mainAsyncTask == null) {
                mainAsyncTask = new MainAsyncTask();
                mainAsyncTask.execute();
            } else {
                if (mainAsyncTask.getStatus() != AsyncTask.Status.FINISHED) {
                    mainAsyncTask.cancel(true);
                    mainAsyncTask = new MainAsyncTask();
                    mainAsyncTask.execute();
                } else {
                    mainAsyncTask = new MainAsyncTask();
                    mainAsyncTask.execute();
                }
            }
        });


    }

    class MainAsyncTask extends AsyncTask<Void, Void, String> {

        //выолняется в главном потоке перед выполнением задачи рабочего потока
        @Override
        protected void onPreExecute() {
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            statusText.setText("DOWNLOADING");
        }

        //Срабаатывают когда фоновая задача в рабочем потоке была отменена
        //отменить задачу рабочего потока можно из любого другого потока
        @Override
        protected void onCancelled() {
            statusText.setText("STOPPED");
        }

        @Override
        protected void onCancelled(String s) {
            statusText.setText("Загружена половина");
        }


        //Можно вызвать из метода doInBackground чтобы отобразить некий прогресс операции
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        //в этом методе выполняется операция в рабочем потоке(рабочий - не главный)
        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(5000);
                publishProgress();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        //выполняется в главном потоке после выполнения операции в рабочем потоке
        //как правило использует входное значение некоторого типа
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.mipmap.ic_launcher);
            imageView.setVisibility(View.VISIBLE);
            statusText.setText("READY");

        }
    }

}
