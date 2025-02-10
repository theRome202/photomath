package com.example.aiphotomathematic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
//import com.rmtheis.tess.two.TessBaseAPI;
import com.rmtheis.tess.two.TessBaseAPI;
//import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private TextView tvResult;
    private Button btnScan;
    private TessBaseAPI tessBaseAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        btnScan = findViewById(R.id.btnScan);

        // Инициализация Tesseract
        /*tessBaseAPI = new TessBaseAPI();
        try {
            tessBaseAPI = new TessBaseAPI();
            tessBaseAPI.init("file:///android_asset/tessdata", "eng"); // путь к tessdata и язык
            Log.d("Tesseract", "Инициализация выполнена успешно.");
        } catch (Exception e) {
            Log.e("Tesseract", "Ошибка инициализации: " + e.getMessage());
        }*/
        // Инициализация Tesseract
        tessBaseAPI = new TessBaseAPI();
        try {
            String path = "file:///android_asset/tessdata";
            Log.d("Tesseract", "Path to tessdata: " + path); // Логирование пути
            tessBaseAPI.init(path, "eng");
            Log.d("Tesseract", "Инициализация выполнена успешно.");
        } catch (Exception e) {
            Log.e("Tesseract", "Ошибка инициализации: " + e.getMessage());
        }


        // Проверка разрешения на камеру
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        btnScan.setOnClickListener(v -> {
            // Захват изображения с камеры
            Bitmap imageBitmap = captureImageFromCamera(); // метод захвата изображения
            recognizeText(imageBitmap);
        });
    }

    // Метод для распознавания текста
    private void recognizeText(Bitmap bitmap) {
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init("/mnt/sdcard/tesseract", "eng"); // путь к языковым данным
        tessBaseAPI.setImage(bitmap);
        String recognizedText = tessBaseAPI.getUTF8Text();
        tessBaseAPI.end();

        // Вывод результата
        tvResult.setText("Распознано: " + recognizedText);

        // Решение уравнения
        solveEquation(recognizedText);
    }

    // Метод для решения уравнения
    private void solveEquation(String equation) {
        try {
            double result = evaluateExpression(equation); // Реализуйте метод оценки выражения
            tvResult.append("\nРезультат: " + result);
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Метод для оценки математического выражения
    private double evaluateExpression(String expression) {
        // Пример использования MathParser.org-mXparser
        mXparser.Expression exp = new mXparser.Expression(expression);
        return exp.calculate();
    }

    // Метод для захвата изображения с камеры
    private Bitmap captureImageFromCamera() {
        // Реализуйте логику захвата изображения
        return null; // Верните Bitmap изображения
    }

    // Обработка разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Разрешение на камеру получено", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Разрешение на камеру отклонено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String extractText(Bitmap bitmap) throws Exception
    {
        TessBaseAPI tessBaseApi = new TessBaseAPI();
        tessBaseApi.init(DATA_PATH, "eng");
        tessBaseApi.setImage(bitmap);
        String extractedText = tessBaseApi.getUTF8Text();
        tessBaseApi.end();
        return extractedText;
    }
}