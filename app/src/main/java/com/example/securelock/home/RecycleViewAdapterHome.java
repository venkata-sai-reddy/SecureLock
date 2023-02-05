package com.example.securelock.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.securelock.R;

import java.time.LocalDate;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class RecycleViewAdapterHome  extends RecyclerView.Adapter<RecycleViewAdapterHome.MyViewHolder> {
    private final PasswordsViwerInterface viwerInterface;

    Context context;
    List<PasswordVO> passwords;

    public RecycleViewAdapterHome(PasswordsViwerInterface viwerInterface, Context context, List<PasswordVO> passwords) {
        this.viwerInterface = viwerInterface;
        this.context = context;
        this.passwords = passwords;
    }

    @NonNull
    @Override
    public RecycleViewAdapterHome.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view, parent, false);
        return new RecycleViewAdapterHome.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterHome.MyViewHolder holder, int position) {

        holder.passName.setText(passwords.get(position).getName());
        holder.passExpiry.setText(passwords.get(position).getExpireDate());
        if(LocalDate.parse(passwords.get(position).getExpireDate()).compareTo(LocalDate.now()) <= 0) {
            holder.passExpiry.setTextColor(Color.parseColor("#FF9494"));
        }
        if(holder.password.getText().toString().equalsIgnoreCase("********")){
            holder.password.setOnClickListener(v ->
                    holder.password.setText(passwords.get(position).getPassword())
            );
        } else {
            holder.password.setText(passwords.get(position).getPassword());
        }
        holder.delPass.setOnClickListener(v -> {
            if(viwerInterface != null){
                viwerInterface.deletePassword(position);
            }
        });
        holder.newPass.setOnClickListener(v -> {
            if(viwerInterface != null){
                viwerInterface.restPassword(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button newPass, delPass;
        TextView passName, passExpiry, password;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            passName = itemView.findViewById(R.id.passwordName);
            passExpiry = itemView.findViewById(R.id.expiryDate);
            password = itemView.findViewById(R.id.password);
            newPass = itemView.findViewById(R.id.genPass);
            delPass = itemView.findViewById(R.id.deletePass);

        }
    }
}
