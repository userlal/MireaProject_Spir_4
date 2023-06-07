package ru.mirea.elancev.mireaproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.KeyGenerator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class CryptFile extends DialogFragment {

    private EditText text;
    private EditText fileName;
    private static final String ALGORITHM = "AES";
    private static final String KEY = "mysecretkey12345";

    public CryptFile(EditText text, EditText fileName) {
        this.text = text;
        this.fileName = fileName;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        builder.setTitle("Шифр")
                .setMessage("Вы можете зашифровать текст в файле, а также расшифровать. Выберите желаемую опцию")
                .setPositiveButton("Зашифровать", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            String message = text.getText().toString();
                            SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
                            Cipher cipher = Cipher.getInstance(ALGORITHM);
                            cipher.init(Cipher.ENCRYPT_MODE, key);
                            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
                            writeFile(Base64.getEncoder().encodeToString(encryptedBytes));

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .setNegativeButton("Расшифровать", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            String encryptedMessage = readFile();
                            SecretKeySpec key = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
                            Cipher cipher = Cipher.getInstance(ALGORITHM);
                            cipher.init(Cipher.DECRYPT_MODE, key);
                            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);
                            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                            System.out.println("DECTYPTED = " + encryptedMessage);
                            writeFile(new String(decryptedBytes));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        return builder.create();
    }

    public String readFile() {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(path, fileName.getText().toString());
        try {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            for (int i = 0; i < lines.size(); i++) {
                stringBuilder.append(lines.get(i));
            }
        } catch (Exception e) {
            Log.w("ExternalStorage", String.format("Read from file %s failed", e.getMessage()));
        }
        text.setText(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public void writeFile(String text) {
        String string = text;
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName.getText().toString());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
            output.write(string);
            this.text.setText(string);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
