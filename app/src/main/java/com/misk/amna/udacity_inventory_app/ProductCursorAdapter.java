
package com.misk.amna.udacity_inventory_app;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.misk.amna.udacity_inventory_app.data.ProductContract;


public class ProductCursorAdapter extends CursorAdapter {


    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        final Context mContext = context;
        final Cursor mCursor = cursor;
        Button mSaleButton = (Button) view.findViewById(R.id.sale_button);
        mSaleButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int quantityIndex = mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
                int quantity = mCursor.getInt(quantityIndex);
                int idIndex = mCursor.getColumnIndex(ProductContract.ProductEntry._ID);
                int id = mCursor.getInt(idIndex);
                if (quantity <= 0)
                    return;
                quantity--;

                ContentValues values = new ContentValues();
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);


                Uri currentUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);

                int rowsAffected = mContext.getContentResolver().update(currentUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(mContext, " Problem in updating", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "successful update", Toast.LENGTH_LONG).show();


                }
            }

        });



        Button mOrderButton = (Button) view.findViewById(R.id.orde_button);
        mOrderButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                int productIndex = mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
                String productName = mCursor.getString(productIndex);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Please Provide us with product :"+productName +"  Regards");
                context.startActivity(Intent.createChooser(intent, "Send Email"));
            }

        });





        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);

        String productName = cursor.getString(nameColumnIndex);
        String ProductQuantity = cursor.getString(quantityColumnIndex);
        String ProductPrice = cursor.getString(priceColumnIndex);


        nameTextView.setText(productName);
        quantityTextView.setText("Available :" + ProductQuantity);
        priceTextView.setText(ProductPrice + " SR");

    }
}
