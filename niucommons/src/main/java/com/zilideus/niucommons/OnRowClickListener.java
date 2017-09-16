package com.zilideus.niucommons;

import android.view.View;

import com.zilideus.niucommons.api.models.Record;


/**
 * Created by sandeeprana on 14/09/17.
 * License is only applicable to individuals and non-profits
 * and that any for-profit company must
 * purchase a different license, and create
 * a second commercial license of your
 * choosing for companies
 */

public interface OnRowClickListener {

    void onRowClicked(View view, Record record, int adapterPosition);
}
