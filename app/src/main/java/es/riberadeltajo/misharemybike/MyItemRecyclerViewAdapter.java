package es.riberadeltajo.misharemybike;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import es.riberadeltajo.misharemybike.bikes.BikesContent;
import es.riberadeltajo.misharemybike.placeholder.PlaceholderContent.PlaceholderItem;
import es.riberadeltajo.misharemybike.databinding.FragmentItemBinding;
import es.riberadeltajo.misharemybike.pojos.Bike;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Bike> mValues;

    public MyItemRecyclerViewAdapter(List<Bike> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mOwner.setText(mValues.get(position).getOwner());
        holder.mDescripcion.setText(mValues.get(position).getDescription());
        holder.mImagen.setImageBitmap(mValues.get(position).getPhoto());
        holder.mCiudad.setText(mValues.get(position).getCity());
        holder.mUbicacion.setText(mValues.get(position).getLocation());
        //AL PULSAR EL BOTON EMAIL, ACCEDEMOS AL CORREO Y ESCRIBIMOS UN CORREO PREDETERMINADO
        holder.mBtnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String owner=holder.mOwner.getText().toString();
                String ciudad=holder.mCiudad.getText().toString();
                String ubicacion=holder.mUbicacion.getText().toString();



            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Bike mItem;

        public final TextView mDescripcion;
        public final TextView mOwner;
        public final TextView mCiudad;
        public final TextView mUbicacion;
        public final ImageView mImagen;
        public final ImageButton mBtnEmail;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mDescripcion = binding.txtDescription;
            mOwner = binding.txtOwner;
            mCiudad = binding.txtCity;
            mUbicacion = binding.txtLocation;
            mImagen = binding.imageView;
            mBtnEmail = binding.btnMail;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mOwner.getText() + "'";
        }
    }
}