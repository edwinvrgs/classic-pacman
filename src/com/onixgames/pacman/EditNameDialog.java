package com.onixgames.pacman;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class EditNameDialog extends DialogFragment implements OnEditorActionListener {

    public interface EditNameDialogListener {
    	void onFinishEditDialog(String inputText);
    }
    
    private EditText campo_texto;
    
    public EditNameDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View view = inflater.inflate(R.layout.dialog, container);
        
        getDialog().setTitle("Ingrese su nombre: ");
        getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        
        campo_texto = (EditText) view.findViewById(R.id.campo_texto);
        
        campo_texto.setTextColor(Color.BLACK);
        campo_texto.setBackgroundColor(Color.WHITE);
        campo_texto.requestFocus();
     
        campo_texto.setOnEditorActionListener(this);
        
        return view;
    }

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        
		if (EditorInfo.IME_ACTION_DONE == actionId) {
			
            EditNameDialogListener activity = (EditNameDialogListener) getActivity();
            
            String username = campo_texto.getText().toString();
            username = username.split(":")[0];
            
            if(username.length() >= 3 && username.length() <= 7)
            {
            	activity.onFinishEditDialog(campo_texto.getText().toString());
            	this.dismiss();
            }
            else if(username.length() > 7)
            	Toast.makeText(getActivity().getApplicationContext(), "Ingrese menos de 8 caracteres", Toast.LENGTH_SHORT).show();
            else if(username.length() < 3)
            	Toast.makeText(getActivity().getApplicationContext(), "Ingrese mas de 2 caracteres", Toast.LENGTH_SHORT).show();
            
            return true;
        }

		return false;
	}
}