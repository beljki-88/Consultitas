package com.example.consultitas;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TablaDisquitosFragment extends Fragment {

    TableLayout tablita;

    public TablaDisquitosFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabla_disquitos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tablita = view.findViewById(R.id.tablita);
    }

    @Override
    public void onResume() {
        super.onResume();
        mostrar();
    }

    private void mostrar() {
        int totalVistas = tablita.getChildCount();
        if (totalVistas > 1) {
            tablita.removeViews(1, totalVistas - 1);
        }

        Base admin = new Base(requireContext(), "disquitos", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT clave, titulo, costo FROM tablita", null);

        while (c.moveToNext()) {
            TableRow fila = new TableRow(requireContext());
            fila.setPadding(5, 5, 5, 5);

            TextView txt1 = new TextView(requireContext());
            TextView txt2 = new TextView(requireContext());
            TextView txt3 = new TextView(requireContext());

            txt1.setText(c.getString(0));
            txt2.setText(c.getString(1));
            txt3.setText(String.valueOf(c.getInt(2)));

            configurarClickCelda(txt1);
            configurarClickCelda(txt2);
            configurarClickCelda(txt3);

            fila.addView(txt1);
            fila.addView(txt2);
            fila.addView(txt3);
            tablita.addView(fila);
        }

        c.close();
        bd.close();
    }

    private void configurarClickCelda(TextView celda) {
        celda.setOnClickListener(v -> {
            String valor = ((TextView) v).getText().toString();
            Toast.makeText(requireContext(), valor + " hola", Toast.LENGTH_SHORT).show();
        });
    }
}