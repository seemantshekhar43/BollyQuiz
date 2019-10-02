package comandydevo.wixsite.seemantshekhar43.bollyquiz;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class Star extends Fragment {

    View myFragment;

    RecyclerView listCategory;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category_model,CategoryViewHolder>adapter;

    //Database Stuffs
    FirebaseDatabase database;
    DatabaseReference category;


    public static Star newInstance(){
        Star star = new Star();
        return star;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        category = database.getReference().child("Groups").child("Star").child("Category");
        category.keepSynced(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_star,container,false);

        listCategory = (RecyclerView) myFragment.findViewById(R.id.star_list);
        listCategory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(container.getContext());
        listCategory.setLayoutManager(layoutManager);

        loadCategories();

        return myFragment;

    }

    private void loadCategories() {
        adapter = new FirebaseRecyclerAdapter<Category_model, CategoryViewHolder>(
                Category_model.class,
                R.layout.activity_category,
                CategoryViewHolder.class,
                category
        ) {
            @Override
            protected void populateViewHolder(final CategoryViewHolder viewHolder, final Category_model model, int position) {
                viewHolder.category_name.setText(model.getName());
                viewHolder.category_level.setText(model.getLevel());
                Picasso.with(getActivity())
                        .load(model.getImage())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(viewHolder.category_image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity())
                                        .load(model.getImage())
                                        .into(viewHolder.category_image);

                            }
                        });

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getContext(), String.format("%s|%s",adapter.getRef(position).getKey(),model.getName()),Toast.LENGTH_LONG).show();

                        Intent startIntent = new Intent(getContext(),Start.class);
                        Common.categoryID = adapter.getRef(position).getKey();
                        Common.classID = "star";
                        startIntent.putExtra("category_name","Star");
                        startIntent.putExtra("id",model.getName());
                        startIntent.putExtra("level",model.getLevel());

                        startActivity(startIntent);

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        listCategory.setAdapter(adapter);

    }
}
