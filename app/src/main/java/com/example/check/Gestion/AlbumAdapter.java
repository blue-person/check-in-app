package com.example.check.Gestion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.check.Entidad.Album;
import com.example.check.Entidad.TravelLocation;
import com.example.check.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.TravelLocationViewHolder>{
    private List<TravelLocation> albums;
    public AlbumAdapter(List<TravelLocation> travelLocations) {
        this.albums = travelLocations;
    }
    @NonNull
    @Override
    public TravelLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TravelLocationViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_album,
                        parent, false
                )
        );
    }
    @Override
    public void onBindViewHolder(@NonNull TravelLocationViewHolder holder, int position) {
        holder.setLocationData(albums.get(position));
    }
    @Override
    public int getItemCount() {
        return albums.size();
    }
    static class TravelLocationViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView kbvLocation;
        private TextView textTitle;

        TravelLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            kbvLocation = itemView.findViewById(R.id.kbvAlbum);
            textTitle = itemView.findViewById(R.id.text_album);
        }
            void setLocationData(TravelLocation album){

            kbvLocation.setTag(album.imagen);
            Picasso.get().load(album.imagen).into(kbvLocation);
            textTitle.setText(album.Nombre);


        }
    }
}