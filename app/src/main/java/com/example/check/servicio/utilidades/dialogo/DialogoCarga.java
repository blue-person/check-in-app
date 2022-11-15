package com.example.check.servicio.utilidades.dialogo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.check.R;

import java.util.Formatter;

public class DialogoCarga {
    Context context;
    TextView textinfoView;
    Dialog dialog;

    public DialogoCarga(Context context) {
        this.context = context;
    }

    public void dispararDialogo(View box) {
        dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = box.findViewById(R.id.textoPrincipal);
        textView.setText(R.string.alerta_subir_foto);
        textinfoView = box.findViewById(R.id.info);
        textinfoView.setText(R.string.alerta_actualizar_info);

        dialog.setContentView(box);
        dialog.show();
    }

    public void porsentaje(Formatter progress) {
        textinfoView.setText(String.format("(%s%%) Actualizando... ", progress));
    }

    public void finalizar() {
        dialog.dismiss();
    }
}
