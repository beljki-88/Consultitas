package com.example.consultitas;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText clave, titulo, costo;
    Button botaltas, botbajas, botcambios, botconsultas, botVerTabla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clave = findViewById(R.id.eclave);
        titulo = findViewById(R.id.etitulo);
        costo = findViewById(R.id.ecosto);

        botaltas = findViewById(R.id.altasbtn);
        botbajas = findViewById(R.id.bajasbtn);
        botcambios = findViewById(R.id.cambiosbtn);
        botconsultas = findViewById(R.id.consultasbtn);
        botVerTabla = findViewById(R.id.verTablabtn);

        botaltas.setOnClickListener(this);
        botbajas.setOnClickListener(this);
        botcambios.setOnClickListener(this);
        botconsultas.setOnClickListener(this);
        botVerTabla.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String cadenita = ((Button) v).getText().toString();

        // --- VER TABLA (FRAGMENTO) ---
        if (cadenita.equals("ver tabla")) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contenedorTabla, new TablaDisquitosFragment())
                    .commit();
            return;
        }

        // --- ALTAS ---
        if (cadenita.equals("altas")) {
            ayudita();
        } else if (cadenita.equals("agregar")) {
            String clavecita = clave.getText().toString().trim();
            String titulito = titulo.getText().toString().trim();
            String costitoStr = costo.getText().toString().trim();

            if (clavecita.isEmpty() || titulito.isEmpty() || costitoStr.isEmpty()) {
                Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Base admin = new Base(this, "disquitos", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();

            ContentValues registro = new ContentValues();
            registro.put("clave", clavecita);
            registro.put("titulo", titulito);
            registro.put("costo", Integer.parseInt(costitoStr));

            long res = bd.insert("tablita", null, registro);
            bd.close();

            if (res != -1) {
                ayuditaab("agregado");
            } else {
                ayuditaab("no se pudo agregar (revisa la clave)");
            }
        }

        // --- BAJAS ---
        else if (cadenita.equals("bajas")) {
            ayuditabaja();
        } else if (cadenita.equals("eliminar")) {
            String clavecita = clave.getText().toString().trim();

            if (clavecita.isEmpty()) {
                Toast.makeText(this, "Escribe la clave a eliminar", Toast.LENGTH_SHORT).show();
                return;
            }

            Base admin = new Base(this, "disquitos", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();

            int borrados = bd.delete("tablita", "clave = ?", new String[]{clavecita});
            bd.close();

            if (borrados > 0) {
                ayuditaab("eliminado");
            } else {
                ayuditaab("no existe el registro");
            }
        }

        // --- CAMBIOS ---
        else if (cadenita.equals("cambios")) {
            ayuditacambio();
        } else if (cadenita.equals("actualizar")) {
            String clavecita = clave.getText().toString().trim();
            String titulito = titulo.getText().toString().trim();
            String costitoStr = costo.getText().toString().trim();

            if (clavecita.isEmpty() || titulito.isEmpty() || costitoStr.isEmpty()) {
                Toast.makeText(this, "Llena clave, titulo y costo", Toast.LENGTH_SHORT).show();
                return;
            }

            Base admin = new Base(this, "disquitos", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();

            ContentValues registro = new ContentValues();
            registro.put("titulo", titulito);
            registro.put("costo", Integer.parseInt(costitoStr));

            int actualizados = bd.update("tablita", registro, "clave = ?", new String[]{clavecita});
            bd.close();

            if (actualizados > 0) {
                ayuditaab("actualizado");
            } else {
                ayuditaab("no existe el registro");
            }
        }

        // --- CONSULTAS ---
        else if (cadenita.equals("consultas")) {
            ayuditaconsulta();
        } else if (cadenita.equals("buscar")) {
            String clavecita = clave.getText().toString().trim();

            if (clavecita.isEmpty()) {
                Toast.makeText(this, "Escribe una clave", Toast.LENGTH_SHORT).show();
                return;
            }

            Base admin = new Base(this, "disquitos", null, 1);
            SQLiteDatabase bd = admin.getReadableDatabase();

            Cursor fila = bd.rawQuery(
                    "select titulo, costo from tablita where clave = ?",
                    new String[]{clavecita}
            );

            if (fila.moveToFirst()) {
                titulo.setText(fila.getString(0));
                costo.setText(String.valueOf(fila.getInt(1)));
            } else {
                titulo.setText("");
                costo.setText("");
                Toast.makeText(this, "No existe el registro", Toast.LENGTH_SHORT).show();
            }

            fila.close();
            bd.close();

            botaltas.setEnabled(true);
            botbajas.setEnabled(true);
            botcambios.setEnabled(true);
            botconsultas.setEnabled(true);
            botconsultas.setText("consultas");
            clave.setEnabled(false);
        }
    }

    private void ayudita() {
        limpiarcajitas();
        clave.setEnabled(true);
        titulo.setEnabled(true);
        costo.setEnabled(true);
        botbajas.setEnabled(false);
        botcambios.setEnabled(false);
        botconsultas.setEnabled(false);
        botaltas.setText("agregar");
    }

    private void ayuditabaja() {
        limpiarcajitas();
        clave.setEnabled(true);
        titulo.setEnabled(false);
        costo.setEnabled(false);
        botaltas.setEnabled(false);
        botcambios.setEnabled(false);
        botconsultas.setEnabled(false);
        botbajas.setText("eliminar");
    }

    private void ayuditacambio() {
        limpiarcajitas();
        clave.setEnabled(true);
        titulo.setEnabled(true);
        costo.setEnabled(true);
        botaltas.setEnabled(false);
        botbajas.setEnabled(false);
        botconsultas.setEnabled(false);
        botcambios.setText("actualizar");
    }

    private void ayuditaconsulta() {
        limpiarcajitas();
        clave.setEnabled(true);
        titulo.setEnabled(false);
        costo.setEnabled(false);
        botaltas.setEnabled(false);
        botbajas.setEnabled(false);
        botcambios.setEnabled(false);
        botconsultas.setText("buscar");
    }

    private void limpiarcajitas() {
        clave.setText("");
        titulo.setText("");
        costo.setText("");
    }

    private void ayuditaab(String mensajito) {
        limpiarcajitas();
        clave.setEnabled(false);
        titulo.setEnabled(false);
        costo.setEnabled(false);

        botaltas.setText("altas");
        botbajas.setText("bajas");
        botcambios.setText("cambios");
        botconsultas.setText("consultas");

        botaltas.setEnabled(true);
        botbajas.setEnabled(true);
        botcambios.setEnabled(true);
        botconsultas.setEnabled(true);

        Toast.makeText(this, mensajito, Toast.LENGTH_LONG).show();
    }
}