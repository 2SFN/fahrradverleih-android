package de.fhswf.fahrradverleih.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import de.fhswf.fahrradverleih.android.R;

public class IconInputView extends ConstraintLayout {
    private static final String STATE_SUPER = "super";
    private static final String STATE_INPUT = "input";

    private final EditText input;

    public IconInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // Layout erstellen
        inflate(context, R.layout.view_icon_input, this);

        // Views
        TextView label = findViewById(R.id.label);
        this.input = findViewById(R.id.input);
        ImageView icon = findViewById(R.id.icon);

        // Attribute lesen
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.IconInputView);

        if (attributes.hasValue(R.styleable.IconInputView_icon)) {
            icon.setImageResource(attributes.getResourceId(
                    R.styleable.IconInputView_icon, 0));
        } else {
            icon.setImageDrawable(null);
        }

        if (attributes.hasValue(R.styleable.IconInputView_label)) {
            label.setText(attributes.getString(R.styleable.IconInputView_label));
        }

        input.setText("");

        if (attributes.hasValue(R.styleable.IconInputView_inputType)) {
            input.setInputType(attributes.getInt(
                    R.styleable.IconInputView_inputType, 0x60));
        }

        // Recyclen der gelesenen Attribute
        attributes.recycle();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle instanceState = new Bundle();

        instanceState.putParcelable(STATE_SUPER, super.onSaveInstanceState());
        instanceState.putString(STATE_INPUT, input.getText().toString());

        return instanceState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle instanceState = (Bundle) state;
        super.onRestoreInstanceState(instanceState.getParcelable(STATE_SUPER));
        this.input.setText(instanceState.getString(STATE_INPUT, ""));
    }

    public void addTextWatcher(@NonNull TextWatcher textWatcher) {
        this.input.addTextChangedListener(textWatcher);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        input.setEnabled(enabled);
    }

    public void setText(@Nullable String text) {
        input.setText(text == null ? "" : text);
    }
}
